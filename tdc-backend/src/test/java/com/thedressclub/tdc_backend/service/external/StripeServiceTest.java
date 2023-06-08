/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service.external;

import static com.plaid.client.PlaidClient.Builder.DEFAULT_SANDBOX_BASE_URL;
import static com.thedressclub.tdc_backend.service.external.StripeService.BANK_ACCOUNT_EXISTS;
import static com.thedressclub.tdc_backend.service.external.StripeService.BANK_ACCOUNT_TYPE;
import static com.thedressclub.tdc_backend.service.external.StripeService.DEVELOP_ENVIRONMENT;
import static com.thedressclub.tdc_backend.service.external.StripeService.ERROR_MESSAGE_KEY;
import static com.thedressclub.tdc_backend.service.external.StripeService.METHOD_KEY;
import static java.util.Collections.singletonMap;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import com.plaid.client.PlaidApiService;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.request.ItemStripeTokenCreateRequest;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import com.plaid.client.response.ItemStripeTokenCreateResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.BankAccount;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccount;
import com.stripe.model.ExternalAccountCollection;
import com.stripe.model.Token;
import com.thedressclub.tdc_backend.dto.TokenResponse;
import com.thedressclub.tdc_backend.model.Company;
import com.thedressclub.tdc_backend.model.User;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import okhttp3.ResponseBody;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Test class for {@link StripeService}.
 *
 * @author Daniel Mejia.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Customer.class, Charge.class, PlaidClient.class, Token.class})
public class StripeServiceTest
{
    private static final String PRODUCTION_ENVIRONMENT = "production";
    private static final String TEST_KEY = "sk_test_only_test";
    private static final String TEST_PAYMENT_KEY = "cus_12345";
    private static final String METHOD_TEST = "method_test";
    private static final String TEST_DESCRIPTION = "test_description";
    private static final int TEST_AMOUNT = 40000;
    private static final String PLAID_CLIENT = "plaid_client";
    private static final String PLAID_PUBLIC = "plaid_public";
    private static final String PLAID_SECRET = "plaid_secret";
    private static final String TEST_METADATA_KEY = "test_metadata_key";
    private static final Object TEST_METADATA_VALUE = 1;
    private static final String PUBLIC_TOKEN = "public token";
    private static final String ACCOUNT_ID = "account id";
    private static final String TEST_ACCESS_TOKEN = "access_token";
    private static final String STRIPE_TOKEN = "stripe token";
    private static final String ERROR_MESSAGE = "Message";
    private static final String BANK_TOKEN = "bank_token";
    private static final String ANOTHER_CODE = "another";
    private static final String ANOTHER_TYPE = "card";
    private static final String ROUTING_NUMBER_TEST = "routing";
    private static final String BANK_NAME_TEST = "bank name";
    private static final String LAST_4_TEST = "1234";
    private static final String ANOTHER_ROUTING_TEST = "another";
    private static final String ANOTHER_LAST_4 = "another 4";
    private static final String ANOTHER_BANK_NAME = "another bank";

    @Mock
    private User mockUser;

    @Mock
    private Customer mockCustomer;

    @Mock
    private ExternalAccountCollection mockExternalAccountCollection;

    @Mock
    private Charge mockCharge;

    @Mock
    private PlaidClient.Builder mockBuilder;

    @Mock
    private PlaidClient mockPlaidClient;

    @Mock
    private PlaidApiService mockPlaidApiService;

    @Mock
    private Call<ItemPublicTokenExchangeResponse> mockCallExchangeToken;

    @Mock
    private Response<ItemPublicTokenExchangeResponse> mockResponseExchangeToken;

    @Mock
    private ItemPublicTokenExchangeResponse mockTokenResponse;

    @Mock
    private Call<ItemStripeTokenCreateResponse> mockCallStripeToken;

    @Mock
    private Response<ItemStripeTokenCreateResponse> mockResponseStripeToken;

    @Mock
    private ItemStripeTokenCreateResponse mockStripeToken;

    @Mock
    private ResponseBody mockResponse;

    private Map<String, String> responseMap;

    @Mock
    private BankAccount mockBankAccount;

    @Mock
    private BankAccount mockAnotherAccount;

    @Mock
    private StripeException mockStripeException;

    @Mock
    private Token mockToken;

    @Mock
    private Card mockCard;

    @Mock
    private Company mockCompany;

    @Spy
    private IStripeService instance =
        new StripeService(TEST_KEY, PLAID_CLIENT, PLAID_PUBLIC, PLAID_SECRET, DEVELOP_ENVIRONMENT);

    @Before
    public void setUp()
    throws StripeException
    {
        MockitoAnnotations.initMocks(this);
        mockStatic(Customer.class);
        mockStatic(PlaidClient.class);
        when(Customer.retrieve(TEST_PAYMENT_KEY)).thenReturn(mockCustomer);
        when(mockUser.getPaymentKey()).thenReturn(TEST_PAYMENT_KEY);
        when(mockUser.getCompany()).thenReturn(mockCompany);
        when(mockCustomer.getSources()).thenReturn(mockExternalAccountCollection);
    }

    @Test
    public void testConstructorProduction()
    {
        IStripeService service =
            new StripeService(TEST_KEY, PLAID_CLIENT, PLAID_PUBLIC, PLAID_SECRET, PRODUCTION_ENVIRONMENT);

        assertNotNull("The service is null", service);
    }

    @Test
    public void testGetBankTokenSuccess()
    throws IOException
    {
        initBankTokenMocks();

        TokenResponse response = instance.getBankToken(PUBLIC_TOKEN, ACCOUNT_ID);

        assertEquals("The stripe token is wrong:", STRIPE_TOKEN , response.getAccountToken());
    }

    @Test
    public void testGetBankTokenFailedExchangeToken()
    throws IOException
    {
        initBankTokenMocks();

        when(mockResponseExchangeToken.isSuccessful()).thenReturn(false);
        when(mockResponseExchangeToken.errorBody()).thenReturn(mockResponse);
        when(mockResponse.string()).thenReturn(responseMap.toString());

        TokenResponse response = instance.getBankToken(PUBLIC_TOKEN, ACCOUNT_ID);

        assertEquals("The error is wrong:", ERROR_MESSAGE , response.getErrorMessage());
    }

    @Test
    public void testGetBankTokenFailedStripeToken()
    throws IOException
    {
        initBankTokenMocks();

        when(mockResponseStripeToken.isSuccessful()).thenReturn(false);
        when(mockResponseStripeToken.errorBody()).thenReturn(mockResponse);
        when(mockResponse.string()).thenReturn(responseMap.toString());

        TokenResponse response = instance.getBankToken(PUBLIC_TOKEN, ACCOUNT_ID);

        assertEquals("The error is wrong:", ERROR_MESSAGE , response.getErrorMessage());
    }

    private void initBankTokenMocks()
    throws IOException
    {
        responseMap = singletonMap(ERROR_MESSAGE_KEY, ERROR_MESSAGE);

        when(PlaidClient.newBuilder()).thenReturn(mockBuilder);
        when(mockBuilder.clientIdAndSecret(PLAID_CLIENT, PLAID_SECRET)).thenReturn(mockBuilder);
        when(mockBuilder.publicKey(PLAID_PUBLIC)).thenReturn(mockBuilder);
        when(mockBuilder.baseUrl(DEFAULT_SANDBOX_BASE_URL)).thenReturn(mockBuilder);
        when(mockBuilder.build()).thenReturn(mockPlaidClient);
        when(mockPlaidClient.service()).thenReturn(mockPlaidApiService);

        when(mockPlaidApiService.itemPublicTokenExchange(any(ItemPublicTokenExchangeRequest.class)))
            .thenReturn(mockCallExchangeToken);
        when(mockCallExchangeToken.execute()).thenReturn(mockResponseExchangeToken);

        when(mockPlaidApiService.itemStripeTokenCreate(any(ItemStripeTokenCreateRequest.class)))
            .thenReturn(mockCallStripeToken);

        when(mockCallStripeToken.execute()).thenReturn(mockResponseStripeToken);
        when(mockResponseExchangeToken.isSuccessful()).thenReturn(true);
        when(mockResponseExchangeToken.body()).thenReturn(mockTokenResponse);
        when(mockTokenResponse.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);

        when(mockResponseStripeToken.isSuccessful()).thenReturn(true);
        when(mockResponseStripeToken.body()).thenReturn(mockStripeToken);
        when(mockStripeToken.getStripeBankAccountToken()).thenReturn(STRIPE_TOKEN);
    }

    @Test
    public void testGetPaymentMethods()
    throws StripeException
    {
        ExternalAccountCollection response = instance.getPaymentMethods(mockUser);
        assertEquals("The payment methods are wrong: ", mockExternalAccountCollection, response);
    }

    @Test
    public void testAddPaymentCustomer()
    throws StripeException
    {
        when(Customer.create(anyMap())).thenReturn(mockCustomer);

        Customer response = instance.addPaymentCustomer(mockUser);

        assertEquals("The customer is wrong: ", mockCustomer, response);
    }

    @Test
    public void testAddBankAccount()
    throws StripeException
    {
        doReturn(mockBankAccount).when(instance).addPaymentMethod(mockUser, BANK_TOKEN);

        BankAccount response = instance.addBankAccount(mockUser, BANK_TOKEN);

        assertEquals("The account is wrong:", mockBankAccount, response);
    }

    @Test
    public void testAddBankAccountAlreadyExists()
    throws StripeException
    {
        doThrow(mockStripeException).when(instance).addPaymentMethod(mockUser, BANK_TOKEN);
        doReturn(mockBankAccount).when(instance).getAccountBy(BANK_TOKEN);
        doReturn(mockBankAccount).when(instance).findBankAccountBy(mockUser, mockBankAccount);
        when(mockStripeException.getCode()).thenReturn(BANK_ACCOUNT_EXISTS);

        BankAccount response = instance.addBankAccount(mockUser, BANK_TOKEN);

        assertEquals("The account is wrong:", mockBankAccount, response);
    }

    @Test(expected = Exception.class)
    public void testAddBankAccountException()
    throws StripeException
    {
        doThrow(mockStripeException).when(instance).addPaymentMethod(mockUser, BANK_TOKEN);
        doReturn(mockBankAccount).when(instance).getAccountBy(BANK_TOKEN);
        doReturn(mockBankAccount).when(instance).findBankAccountBy(mockUser, mockBankAccount);
        when(mockStripeException.getCode()).thenReturn(ANOTHER_CODE);

        instance.addBankAccount(mockUser, BANK_TOKEN);
    }

    @Test
    public void testGetAccountByBankAccount()
    throws StripeException
    {
        mockStatic(Token.class);
        when(Token.retrieve(BANK_TOKEN)).thenReturn(mockToken);
        when(mockToken.getType()).thenReturn(BANK_ACCOUNT_TYPE);
        when(mockToken.getBankAccount()).thenReturn(mockBankAccount);

        ExternalAccount response = instance.getAccountBy(BANK_TOKEN);

        assertEquals("The bank account is wrong: ", mockBankAccount, response);
    }

    @Test
    public void testGetAccountByCard()
    throws StripeException
    {
        mockStatic(Token.class);
        when(Token.retrieve(BANK_TOKEN)).thenReturn(mockToken);
        when(mockToken.getType()).thenReturn(ANOTHER_TYPE);
        when(mockToken.getCard()).thenReturn(mockCard);

        ExternalAccount response = instance.getAccountBy(BANK_TOKEN);

        assertEquals("The card is wrong: ", mockCard, response);
    }

    @Test
    public void testFindBankAccountBy()
    throws StripeException
    {
        doReturn(mockExternalAccountCollection).when(instance).getPaymentMethods(mockUser);
        doReturn(true).when(instance).areSameAccount(mockBankAccount, mockBankAccount);
        when(mockExternalAccountCollection.list(anyMap())).thenReturn(mockExternalAccountCollection);
        when(mockExternalAccountCollection.getData()).thenReturn(getBankAccountList());

        BankAccount response = instance.findBankAccountBy(mockUser, mockBankAccount);

        assertEquals("The bank account is wrong:", mockBankAccount, response);
    }

    @Test
    public void testAreSameAccountEqual()
    {
        when(mockBankAccount.getRoutingNumber()).thenReturn(ROUTING_NUMBER_TEST);
        when(mockBankAccount.getBankName()).thenReturn(BANK_NAME_TEST);
        when(mockBankAccount.getLast4()).thenReturn(LAST_4_TEST);

        when(mockAnotherAccount.getRoutingNumber())
            .thenReturn(ROUTING_NUMBER_TEST, ROUTING_NUMBER_TEST, ROUTING_NUMBER_TEST, ANOTHER_ROUTING_TEST);
        when(mockAnotherAccount.getBankName())
            .thenReturn(BANK_NAME_TEST, BANK_NAME_TEST, ANOTHER_BANK_NAME);
        when(mockAnotherAccount.getLast4())
            .thenReturn(LAST_4_TEST, ANOTHER_LAST_4);

        List<Boolean> responses =
            Stream
                .iterate(0, index -> index + 1)
                .limit(4)
                .map(index -> instance.areSameAccount(mockBankAccount, mockAnotherAccount))
                .collect(Collectors.toList());

        assertTrue("The account are not the same", responses.get(0));
        assertFalse("The account are the same because the last4", responses.get(1));
        assertFalse("The account are the because the bank account", responses.get(2));
        assertFalse("The account are the same because routing number", responses.get(3));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddPaymentMethod()
    throws StripeException
    {
        Matcher matcher = hasEntry(METHOD_KEY, METHOD_TEST);
        when(mockCustomer.getSources()).thenReturn(mockExternalAccountCollection);
        when(mockExternalAccountCollection.create((Map<String, Object>) argThat(matcher)))
            .thenReturn(mockBankAccount);

        ExternalAccount response = instance.addPaymentMethod(mockUser, METHOD_TEST);

        assertEquals("The account added is wrong: ", mockBankAccount , response);
    }

    @Test
    public void addPaymentCharge()
    throws StripeException
    {
        mockStatic(Charge.class);
        when(Charge.create(anyMap())).thenReturn(mockCharge);

        Charge response =
            instance.addPaymentCharge(
                mockUser,
                METHOD_TEST,
                TEST_AMOUNT,
                TEST_DESCRIPTION,
                singletonMap(TEST_METADATA_KEY, TEST_METADATA_VALUE));

        assertEquals("The charge is wrong: ", mockCharge, response);
    }

    private List<ExternalAccount> getBankAccountList()
    {
        return Collections.singletonList(mockBankAccount);
    }
}