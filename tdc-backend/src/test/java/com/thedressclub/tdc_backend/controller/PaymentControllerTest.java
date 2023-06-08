/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static com.thedressclub.tdc_backend.controller.PaymentController.ACCOUNT_ID_PARAM;
import static com.thedressclub.tdc_backend.controller.PaymentController.AMOUNT_INCORRECT;
import static com.thedressclub.tdc_backend.controller.PaymentController.AMOUNT_PARAM;
import static com.thedressclub.tdc_backend.controller.PaymentController.BANK_TOKEN_PARAM;
import static com.thedressclub.tdc_backend.controller.PaymentController.ORDER_KEY;
import static com.thedressclub.tdc_backend.controller.PaymentController.ORDER_NOT_EXIST;
import static com.thedressclub.tdc_backend.controller.PaymentController.PAYMENT_UNAVAILABLE;
import static com.thedressclub.tdc_backend.controller.PaymentController.PUBLIC_TOKEN_PARAM;
import static java.lang.Integer.valueOf;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.stripe.model.BankAccount;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.thedressclub.tdc_backend.controller.config.GenericControllerTest;
import com.thedressclub.tdc_backend.controller.config.RestException;
import com.thedressclub.tdc_backend.dto.TokenResponse;
import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.Payment;
import com.thedressclub.tdc_backend.model.PaymentType;
import com.thedressclub.tdc_backend.model.State;
import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.OrderService;
import com.thedressclub.tdc_backend.service.PaymentTypeService;
import com.thedressclub.tdc_backend.service.UserService;
import com.thedressclub.tdc_backend.service.external.IStripeService;
import com.thedressclub.tdc_backend.util.UserUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Test class for {@link PaymentController}.
 *
 * @author Daniel Mejia.
 */
public class PaymentControllerTest extends GenericControllerTest<Payment>
{
    private static final String PAYMENT_URL = "/payments";
    private static final String BANK_TOKEN_URL = PAYMENT_URL  + "/bankToken";
    private static final String CHARGE_URL = PAYMENT_URL + "/orders/" + OBJECT_ID + "/bankCharge";

    private static final String PUBLIC_TOKEN_TEST = "public token";
    private static final String ACCOUNT_ID_TEST = "account id";
    private static final String ACCOUNT_TOKEN_TEST = "account token";
    private static final String ERROR_TOKEN_TEST = "error token";

    private static final String AMOUNT_TEST = "1200";
    private static final String BANK_TOKEN_TEST = "token bank test";
    private static final String STRIPE_CUSTOMER_KEY = "cus_123";
    private static final String BANK_ACCOUNT_ID = "bank_test1123";
    private static final String PAYMENT_DESCRIPTION = "testPayment";
    private static final String PENDING_STATUS = "pending";
    private static final String CHARGE_ID = "char_1234";

    private static final double AMOUNT = 1000.0;
    private static final double FINAL_QUOTE = 2000.0;
    private static final double FINAL_QUOTE_ERROR = 1000.0;

    private User user;

    private Payment payment;

    private PaymentType paymentType;

    private TokenResponse tokenResponse;

    @Mock
    private UserService mockUserService;

    @Mock
    private OrderService mockOrderService;

    @Mock
    private IStripeService mockStripeService;

    @Mock
    private PaymentTypeService mockPaymentTypeService;

    @Mock
    private Customer mockCustomer;

    @Mock
    private BankAccount mockBankToken;

    @Mock
    private Order mockOrder;

    @Mock
    private Charge mockCharge;

    @InjectMocks
    @Spy
    private PaymentController instance;

    @Mock
    private State mockState;

    @Before
    public void setUp()
    {
        user = new User();
        user.setId(OBJECT_ID);

        payment = new Payment();
        payment.setId(OBJECT_ID);

        paymentType = new PaymentType();
        paymentType.setId(OBJECT_ID);
        paymentType.setDescription(PAYMENT_DESCRIPTION);

        tokenResponse = new TokenResponse();

        MockitoAnnotations.initMocks(this);
        init(PAYMENT_URL, payment, instance);
        instance.setGenericService(mockGenericService);
    }

    @Test
    public void testGetBankTokenSuccess()
    throws Exception
    {
        when(mockStripeService.getBankToken(PUBLIC_TOKEN_TEST, ACCOUNT_ID_TEST)).thenReturn(tokenResponse);
        tokenResponse.setAccountToken(ACCOUNT_TOKEN_TEST);
        ResultActions resultSuccess = performFormRequestPost(BANK_TOKEN_URL, getTokenParams());

        resultSuccess
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(tokenResponse)));
    }

    @Test
    public void testGetBankTokenFailed()
    throws IOException
    {
        when(mockStripeService.getBankToken(PUBLIC_TOKEN_TEST, ACCOUNT_ID_TEST)).thenReturn(tokenResponse);
        tokenResponse.setErrorMessage(ERROR_TOKEN_TEST);
        assertThatThrownBy(() -> performFormRequestPost(BANK_TOKEN_URL, getTokenParams()))
            .hasCause(new RestException(HttpStatus.BAD_REQUEST, ERROR_TOKEN_TEST));
    }

    @Test
    public void testAddPaymentChargeFirstTime()
    throws Exception
    {
        initChargeMocks();

        ResultActions result = performFormRequestPost(CHARGE_URL, getChargeParams());

        result
            .andExpect(status().isCreated())
            .andExpect(content().string(asJsonString(payment)));
    }

    @Test
    public void testAddPaymentChargeSecondTime()
    throws Exception
    {
        initChargeMocks();
        user.setPaymentKey(STRIPE_CUSTOMER_KEY);

        ResultActions result = performFormRequestPost(CHARGE_URL, getChargeParams());

        result
            .andExpect(status().isCreated())
            .andExpect(content().string(asJsonString(payment)));

        verify(mockStripeService, never()).addPaymentCustomer(user);
        verify(mockUserService, never()).update(user);
    }

    @Test
    public void testAddPaymentChargeOrderNotExist()
    throws Exception
    {
        initChargeMocks();

        when(mockOrderService.findById(OBJECT_ID)).thenReturn(null);

        assertThatThrownBy(() -> performFormRequestPost(CHARGE_URL, getChargeParams()))
            .hasCause(new RestException(ORDER_NOT_EXIST));
    }

    @Test
    public void testAddPaymentChargeUnavailable()
    throws Exception
    {
        initChargeMocks();

        when(mockPaymentTypeService.findByOrderState(mockOrder)).thenReturn(null);

        assertThatThrownBy(() -> performFormRequestPost(CHARGE_URL, getChargeParams()))
            .hasCause(new RestException(PAYMENT_UNAVAILABLE));
    }

    @Test
    public void testAddPaymentChargeAmountIncorrect()
    throws Exception
    {
        initChargeMocks();
        doReturn((Predicate<Double>) amount -> false).when(instance).amountCorrect(mockOrder);

        assertThatThrownBy(() -> performFormRequestPost(CHARGE_URL, getChargeParams()))
            .hasCause(new RestException(HttpStatus.BAD_REQUEST, AMOUNT_INCORRECT));
    }

    @Test
    public void testAmountValidatorFirstPaymentSuccess()
    {
        when(mockOrder.getState()).thenReturn(mockState);
        when(mockOrder.getFinalQuote()).thenReturn(FINAL_QUOTE);
        when(mockState.getId()).thenReturn(Order.FIRST_PAYMENT_STATE_ID);
        Predicate<Double> response = instance.amountCorrect(mockOrder);

        assertThat(response.test(AMOUNT)).isTrue();
    }

    @Test
    public void testAmountValidatorFirstPaymentError()
    {
        when(mockOrder.getState()).thenReturn(mockState);
        when(mockOrder.getFinalQuote()).thenReturn(FINAL_QUOTE_ERROR);
        when(mockState.getId()).thenReturn(Order.FIRST_PAYMENT_STATE_ID);
        Predicate<Double> response = instance.amountCorrect(mockOrder);

        assertThat(response.test(AMOUNT)).isFalse();
    }

    @Test
    public void testAmountValidatorLastPaymentSuccess()
    {
        when(mockOrder.getState()).thenReturn(mockState);
        when(mockOrder.getFinalQuote()).thenReturn(FINAL_QUOTE);
        when(mockState.getId()).thenReturn(Order.LAST_PAYMENT_STATE_ID);
        Predicate<Double> response = instance.amountCorrect(mockOrder);

        assertThat(response.test(AMOUNT)).isTrue();
    }

    @Test
    public void testAmountValidatorLastPaymentError()
    {
        when(mockOrder.getState()).thenReturn(mockState);
        when(mockOrder.getFinalQuote()).thenReturn(FINAL_QUOTE_ERROR);
        when(mockState.getId()).thenReturn(Order.LAST_PAYMENT_STATE_ID);
        Predicate<Double> response = instance.amountCorrect(mockOrder);

        assertThat(response.test(AMOUNT)).isFalse();
    }

    private void initChargeMocks()
    throws Exception
    {
        User userWIthCustomer = SerializationUtils.clone(user);
        userWIthCustomer.setPaymentKey(STRIPE_CUSTOMER_KEY);

        when(UserUtils.getCurrentUser(mockUserService)).thenReturn(user);
        when(mockStripeService.addPaymentCustomer(user)).thenReturn(mockCustomer);
        when(mockCustomer.getId()).thenReturn(STRIPE_CUSTOMER_KEY);
        when(mockUserService.update(user)).thenReturn(userWIthCustomer);

        when(mockStripeService.addBankAccount(user, BANK_TOKEN_TEST)).thenReturn(mockBankToken);
        when(mockBankToken.getId()).thenReturn(BANK_ACCOUNT_ID);
        when(mockOrderService.findById(OBJECT_ID)).thenReturn(mockOrder);
        when(mockOrder.getId()).thenReturn(OBJECT_ID);
        when(mockOrder.getState()).thenReturn(mockState);
        when(mockState.getId()).thenReturn(Order.FIRST_PAYMENT_STATE_ID);
        when(mockPaymentTypeService.findByOrderState(mockOrder)).thenReturn(paymentType);
        doReturn((Predicate<Double>) amount -> true).when(instance).amountCorrect(mockOrder);

        when(mockStripeService.addPaymentCharge(
            userWIthCustomer,
            BANK_ACCOUNT_ID,
            valueOf(AMOUNT_TEST),
            PAYMENT_DESCRIPTION,
            singletonMap(ORDER_KEY, OBJECT_ID)))
                .thenReturn(mockCharge);
        when(mockCharge.getId()).thenReturn(CHARGE_ID);
        when(mockCharge.getStatus()).thenReturn(PENDING_STATUS);
        when(mockGenericService.add(any())).thenReturn(payment);
    }

    private Map<String, String> getTokenParams()
    {
        Map<String, String> params = new HashMap<>();
        params.put(PUBLIC_TOKEN_PARAM, PUBLIC_TOKEN_TEST);
        params.put(ACCOUNT_ID_PARAM, ACCOUNT_ID_TEST);

        return params;
    }

    private Map<String, String> getChargeParams()
    {
        Map<String, String> chargeParams = new HashMap<>();
        chargeParams.put(AMOUNT_PARAM, AMOUNT_TEST);
        chargeParams.put(BANK_TOKEN_PARAM, BANK_TOKEN_TEST);

        return chargeParams;
    }
}
