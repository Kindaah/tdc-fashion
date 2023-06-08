/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service.external;

import com.stripe.exception.StripeException;
import com.stripe.model.BankAccount;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccount;
import com.stripe.model.ExternalAccountCollection;
import com.thedressclub.tdc_backend.dto.TokenResponse;
import com.thedressclub.tdc_backend.model.User;
import java.io.IOException;
import java.util.Map;

/**
 * Interface for the Stripe payment Service.
 *
 * @author Daniel Mejia.
 */
public interface IStripeService
{
    /**
     * Gets the bank token using plaid system.
     *
     * @param publicToken the public token generated in the client side.
     * @param accountId the account id for the user.
     *
     * @return the stripe bank account token.
     */
    TokenResponse getBankToken(String publicToken, String accountId)
    throws IOException;

    /**
     * Gets the payment methods for an user.
     *
     * @param user the user to search his payment methods.
     *
     * @return the payment methods.
     * @throws StripeException If an error is thrown by the stripe service.
     */
    ExternalAccountCollection getPaymentMethods(User user)
    throws StripeException;

    /**
     * Add a payment customer to stripe and associated with the local user.
     *
     * @param user the local user.
     *
     * @return the customer object created.
     * @throws StripeException If an error is thrown by the stripe service.
     */
    Customer addPaymentCustomer(User user)
    throws StripeException;

    /**
     * Adds a bank account using the token.
     *
     * @param user the user to assign the bank account.
     * @param bankToken the bank account token.
     *
     * @return the bank account.
     * @throws StripeException If an error is thrown by the stripe service.
     */
    BankAccount addBankAccount(User user, String bankToken)
    throws StripeException;

    /**
     * Gets the account by token.
     *
     * @param token the token to search the account.
     *
     * @return the account if exists, null otherwise.
     * @throws StripeException If an error is thrown by the stripe service.
     */
    ExternalAccount getAccountBy(String token)
    throws StripeException;

    /**
     * Find the bank account comparing with according to the account.
     *
     * @param user the user owner for the account.
     * @param bankAccount the bank account to get search parameters.
     *
     * @return The bank account if exists, null otherwise.
     * @throws StripeException If an error is thrown by the stripe service.
     */
    BankAccount findBankAccountBy(User user, BankAccount bankAccount)
    throws StripeException;

    /**
     * Compare if two bank accounts are the same base on some parameters.
     *
     * @param bankAccount one of the bank accounts.
     * @param anotherAccount the another bank account.
     *
     * @return true if the accounts are the same, false otherwise.
     */
    boolean areSameAccount(BankAccount bankAccount, BankAccount anotherAccount);

    /**
     * Add a payment source to a customer in stripe.
     *
     * @param user the local user.
     * @param bankToken the payment tokenMethod token to add.
     *
     * @throws StripeException If an error is thrown by the stripe service.
     */
    ExternalAccount addPaymentMethod(User user, String bankToken)
    throws StripeException;

    /**
     * Adds a payment charge to a payment methodId for an user.
     *
     * @param user the user to charge.
     * @param methodId the payment methodId to the user.
     * @param amountDollars the amount in dollars to charge.
     * @param description the payment description.
     * @param metadata the payment metadata.
     *
     * @return The charge done for the user.
     * @throws StripeException If an error is thrown by the stripe service.
     */
    Charge addPaymentCharge(
        User user, String methodId, double amountDollars, String description, Map<String, Object> metadata)
    throws StripeException;
}
