/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service.external;

import static com.plaid.client.PlaidClient.Builder.DEFAULT_DEVELOPMENT_BASE_URL;
import static com.plaid.client.PlaidClient.Builder.DEFAULT_SANDBOX_BASE_URL;
import static com.thedressclub.tdc_backend.util.Utils.getMessage;

import com.plaid.client.PlaidApiService;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.request.ItemStripeTokenCreateRequest;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import com.plaid.client.response.ItemStripeTokenCreateResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.BankAccount;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccount;
import com.stripe.model.ExternalAccountCollection;
import com.stripe.model.Token;
import com.thedressclub.tdc_backend.dto.TokenResponse;
import com.thedressclub.tdc_backend.model.User;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Response;


/**
 * Services for the Stripe payment.
 *
 * @author Daniel Mejia.
 */
@Service("stripeService")
@Transactional
public class StripeService implements IStripeService
{
    private static final Logger LOGGER = Logger.getLogger(StripeService.class.getName());
    private static final String EXCHANGE_TOKEN_SUCCESS_KEY = "exchange_token_success";
    private static final String EXCHANGE_TOKEN_FAILED_KEY = "exchange_token_error";
    private static final String STRIPE_TOKEN_SUCCESS_KEY = "stripe_token_success";
    private static final String STRIPE_TOKEN_ERROR_KEY = "stripe_token_error";
    private static final String GET_PAYMENTS_METHODS_KEY = "get_payments_methods";
    private static final String CREATING_CUSTOMER = "creating_customer";
    private static final String ADD_BANK_ACCOUNT = "add_bank_account";
    private static final String DECODING_TOKEN = "get_account";
    private static final String ADD_PAYMENT_METHOD = "add_payment_method";
    private static final String FIND_BANK_ACCOUNT = "find_bank_account";
    private static final String CHARGE_PAYMENT_KEY = "charge_payment";

    private static final String EMAIL_KEY = "email";
    private static final String DESCRIPTION_KEY = "description";
    private static final String CUSTOMER_KEY = "customer";
    private static final String AMOUNT_KEY = "amount";
    private static final String CURRENCY_KEY = "currency";
    private static final Object DOLLAR_CURRENCY = "usd";
    private static final String METADATA_KEY = "metadata";
    private static final String LIMIT_KEY = "limit";
    private static final String TYPE_OBJECT_KEY = "object";

    static final String BANK_ACCOUNT_TYPE = "bank_account";
    static final String BANK_ACCOUNT_EXISTS = "bank_account_exists";
    static final String ERROR_MESSAGE_KEY = "error_message";
    static final String METHOD_KEY = "source";
    static final String DEVELOP_ENVIRONMENT = "develop";

    private String plaidClientId;
    private String plaidPublic;
    private String plaidSecret;
    private String plaidEnvironmentUrl;

    /**
     * Constructor.
     *
     * @param apiKey the stripe api key.
     */
    public StripeService(
        @Value("${payment.key}") String apiKey,
        @Value("${plaid.client_id}") String plaidClientId,
        @Value("${plaid.public_key}") String plaidPublic,
        @Value("${plaid.secret_key}") String plaidSecret,
        @Value("${plaid.environment}") String plaidEnvironment)
    {
        Stripe.apiKey = apiKey;
        this.plaidClientId = plaidClientId;
        this.plaidPublic = plaidPublic;
        this.plaidSecret = plaidSecret;
        this.plaidEnvironmentUrl =
            DEVELOP_ENVIRONMENT.equals(plaidEnvironment) ? DEFAULT_SANDBOX_BASE_URL : DEFAULT_DEVELOPMENT_BASE_URL;
    }

    /** (non-Javadoc) @see {@link IStripeService} */
    @Override
    public TokenResponse getBankToken(String publicToken, String accountId)
    throws IOException
    {
        TokenResponse bankToken = new TokenResponse();

        PlaidClient plaidClient =
            PlaidClient
                .newBuilder()
                .clientIdAndSecret(plaidClientId, plaidSecret)
                .publicKey(plaidPublic)
                .baseUrl(plaidEnvironmentUrl)
                .build();

        PlaidApiService plaidApiService = plaidClient.service();

        Response<ItemPublicTokenExchangeResponse> exchangeResponse =
            plaidApiService
                .itemPublicTokenExchange(new ItemPublicTokenExchangeRequest(publicToken))
                .execute();

        if (exchangeResponse.isSuccessful())
        {
            LOGGER.log(Level.INFO, getMessage(EXCHANGE_TOKEN_SUCCESS_KEY));

            String accessToken = exchangeResponse.body().getAccessToken();
            Response<ItemStripeTokenCreateResponse> stripeResponse =
                plaidApiService
                    .itemStripeTokenCreate(new ItemStripeTokenCreateRequest(accessToken, accountId))
                    .execute();

            if (stripeResponse.isSuccessful())
            {
                LOGGER.log(Level.INFO, getMessage(STRIPE_TOKEN_SUCCESS_KEY));

                bankToken.setAccountToken(stripeResponse.body().getStripeBankAccountToken());
            }
            else
            {
                String errorMessage = getErrorMessage(stripeResponse.errorBody().string());
                LOGGER.log(Level.INFO, getMessage(STRIPE_TOKEN_ERROR_KEY), errorMessage);

                bankToken.setErrorMessage(errorMessage);
            }
        }
        else
        {
            String errorMessage = getErrorMessage(exchangeResponse.errorBody().string());
            LOGGER.log(Level.INFO, getMessage(EXCHANGE_TOKEN_FAILED_KEY), errorMessage);

            bankToken.setErrorMessage(errorMessage);
        }

        return bankToken;
    }

    /** (non-Javadoc) @see {@link IStripeService} */
    @Override
    public ExternalAccountCollection getPaymentMethods(User user)
    throws StripeException
    {
        LOGGER.log(Level.INFO, getMessage(GET_PAYMENTS_METHODS_KEY));

        return Customer.retrieve(user.getPaymentKey()).getSources();
    }

    /** (non-Javadoc) @see {@link IStripeService} */
    @Override
    public Customer addPaymentCustomer(User user)
    throws StripeException
    {
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put(EMAIL_KEY, user.getCompanyEmail());
        customerParams.put(DESCRIPTION_KEY, user.getCompany().getName());

        LOGGER.log(Level.INFO, getMessage(CREATING_CUSTOMER), user.getCompanyEmail());

        return Customer.create(customerParams);
    }

    /** (non-Javadoc) @see {@link IStripeService} */
    @Override
    public BankAccount addBankAccount(User user, String bankToken)
    throws StripeException
    {
        LOGGER.log(Level.INFO, getMessage(ADD_BANK_ACCOUNT), user.getCompanyEmail());

        try
        {
            return (BankAccount) addPaymentMethod(user, bankToken);
        }
        catch (StripeException stripeException)
        {
            if (BANK_ACCOUNT_EXISTS.equals(stripeException.getCode()))
            {
                BankAccount bankAccount = (BankAccount) getAccountBy(bankToken);
                return findBankAccountBy(user, bankAccount);
            }

            throw stripeException;
        }
    }

    /** (non-Javadoc) @see {@link IStripeService} */
    @Override
    public ExternalAccount getAccountBy(String token)
    throws StripeException
    {
        Token tokenObject = Token.retrieve(token);

        LOGGER.log(Level.INFO, getMessage(DECODING_TOKEN));

        return BANK_ACCOUNT_TYPE.equals(tokenObject.getType()) ? tokenObject.getBankAccount() : tokenObject.getCard();
    }

    /** (non-Javadoc) @see {@link IStripeService} */
    @Override
    public BankAccount findBankAccountBy(User user, BankAccount bankAccount)
    throws StripeException
    {
        ExternalAccountCollection sources = getPaymentMethods(user);

        Map<String, Object> bankAccountParams = new HashMap<>();
        bankAccountParams.put(LIMIT_KEY, sources.getTotalCount());
        bankAccountParams.put(TYPE_OBJECT_KEY, BANK_ACCOUNT_TYPE);

        List<ExternalAccount> bankAccounts = sources.list(bankAccountParams).getData();

        Optional<ExternalAccount> bankAccountOptional =
            bankAccounts
                .stream()
                .filter(account -> areSameAccount((BankAccount) account, bankAccount))
                .findFirst();

        LOGGER.log(Level.INFO, getMessage(FIND_BANK_ACCOUNT), user.getCompanyEmail());

        return (BankAccount) bankAccountOptional.orElse(null);
    }

    /** (non-Javadoc) @see {@link IStripeService} */
    @Override
    public boolean areSameAccount(BankAccount bankAccount, BankAccount anotherAccount)
    {
        return bankAccount.getRoutingNumber().equals(anotherAccount.getRoutingNumber()) &&
            bankAccount.getBankName().equals(anotherAccount.getBankName()) &&
            bankAccount.getLast4().equals(anotherAccount.getLast4());
    }

    /** (non-Javadoc) @see {@link IStripeService} */
    @Override
    public ExternalAccount addPaymentMethod(User user, String bankToken)
    throws StripeException
    {
        Customer customer = Customer.retrieve(user.getPaymentKey());

        Map<String, Object> params = new HashMap<>();
        params.put(METHOD_KEY, bankToken);

        LOGGER.log(Level.INFO, getMessage(ADD_PAYMENT_METHOD), user.getCompanyEmail());

        return customer.getSources().create(params);
    }

    /** (non-Javadoc) @see {@link IStripeService} */
    @Override
    public Charge addPaymentCharge(
        User user, String methodId, double amountDollars, String description, Map<String, Object> metadata)
    throws StripeException
    {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put(CUSTOMER_KEY, user.getPaymentKey());
        chargeParams.put(METHOD_KEY, methodId);
        chargeParams.put(AMOUNT_KEY, (int) (amountDollars * 100));
        chargeParams.put(DESCRIPTION_KEY, description);
        chargeParams.put(METADATA_KEY, metadata);
        chargeParams.put(CURRENCY_KEY, DOLLAR_CURRENCY);

        Object[] params = { user.getCompanyEmail(), amountDollars};
        LOGGER.log(Level.INFO, getMessage(CHARGE_PAYMENT_KEY), params);

        return Charge.create(chargeParams);
    }

    private String getErrorMessage(String object)
    {
        JSONObject error = new JSONObject(object);
        return error.getString(ERROR_MESSAGE_KEY);
    }
}
