/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static com.thedressclub.tdc_backend.controller.WebHookController.BLANK_SPACE;
import static com.thedressclub.tdc_backend.controller.WebHookController.CHARGE_BANK_KEY;
import static com.thedressclub.tdc_backend.controller.WebHookController.STRIPE_SIGNATURE_KEY;
import static com.thedressclub.tdc_backend.model.Order.SAMPLES_STATE_ID;
import static com.thedressclub.tdc_backend.model.Payment.FAILED_STATE;
import static com.thedressclub.tdc_backend.model.PaymentType.SAMPLES_ID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.EventData;
import com.stripe.net.Webhook;
import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.Payment;
import com.thedressclub.tdc_backend.model.PaymentType;
import com.thedressclub.tdc_backend.model.State;
import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.OrderService;
import com.thedressclub.tdc_backend.service.PaymentService;
import com.thedressclub.tdc_backend.service.StateService;
import com.thedressclub.tdc_backend.service.external.IMailSenderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for {@link WebHookController}.
 *
 * @author Daniel Mejia.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Webhook.class)
public class WebHookControllerTest
{
    private static final String WEB_HOOKS_URL = "/webhooks";
    private static final String PAYMENT_BANK = "/payment/chargeBank";
    private static final String WEB_HOOK_SECRET = "whsec_12345";
    private static final String PAYLOAD_TEST = "{}";
    private static final String TEST_SIGNATURE = "signature";
    private static final String CHARGE_STATUS = "charge_status";
    private static final String CHARGE_ID = "cha_1234";
    private static final Long OBJECT_ID = 1L;
    private static final String TEST_STATE = "state name";
    private static final String TEST_RECIPIENT_EMAIL = "test@tdc.fashion";
    private static final String TEST_PAYMENT_TYPE = "test payment type";

    private MockMvc mockMvc;

    @Mock
    private Event mockEvent;

    @Mock
    private EventData mockEventData;

    @Mock
    private Charge mockCharge;

    @Mock
    private PaymentService mockPaymentService;

    @Mock
    private Payment mockPayment;

    @Mock
    private Environment mockEnvironment;

    @Mock
    private Order mockOrder;

    @Mock
    private State mockState;

    @Mock
    private StateService mockStateService;

    @Mock
    private OrderService mockOrderService;

    @Mock
    private PaymentType mockPaymentType;

    @Mock
    private User mockUser;

    @Mock
    private IMailSenderService mockMailSenderService;

    @InjectMocks
    private WebHookController instance;

    @Before
    public void setUp()
    throws SignatureVerificationException
    {
        instance = new WebHookController();
        mockMvc = MockMvcBuilders.standaloneSetup(instance).build();
        MockitoAnnotations.initMocks(this);
        mockStatic(Webhook.class);
        when(Webhook.constructEvent(PAYLOAD_TEST, TEST_SIGNATURE, WEB_HOOK_SECRET))
            .thenReturn(mockEvent);
        when(mockEvent.getData()).thenReturn(mockEventData);
        when(mockEventData.getObject()).thenReturn(mockCharge);
        when(mockCharge.getStatus()).thenReturn(CHARGE_STATUS);
        when(mockCharge.getId()).thenReturn(CHARGE_ID);
        when(mockPaymentService.findByKey(CHARGE_ID)).thenReturn(mockPayment);
        when(mockPaymentService.update(mockPayment)).thenReturn(mockPayment);
        when(mockEnvironment.getProperty(CHARGE_BANK_KEY)).thenReturn(WEB_HOOK_SECRET);
        when(mockOrderService.update(mockOrder)).thenReturn(mockOrder);
        when(mockPayment.getOrder()).thenReturn(mockOrder);
        when(mockOrder.getId()).thenReturn(OBJECT_ID);
        when(mockOrder.getUser()).thenReturn(mockUser);
        when(mockUser.getCompanyEmail()).thenReturn(TEST_RECIPIENT_EMAIL);
        when(mockPayment.getPaymentType()).thenReturn(mockPaymentType);
        when(mockPaymentType.getId()).thenReturn(SAMPLES_ID);
        when(mockPaymentType.getDescription()).thenReturn(TEST_PAYMENT_TYPE);
    }

    @Test
    public void testPaymentChargeBankWebHookSuccess()
    throws Exception
    {
        ResultActions result = getPaymentRequest();

        result
            .andExpect(status().isOk());

        String expectedSubject = TEST_PAYMENT_TYPE + BLANK_SPACE + CHARGE_STATUS;
        verify(mockMailSenderService).sendPaymentEmail(TEST_RECIPIENT_EMAIL, expectedSubject, mockPayment);
    }

    @Test
    public void testPaymentChargeBankWebHookFailed()
    throws Exception
    {
        when(mockCharge.getStatus()).thenReturn(FAILED_STATE);
        when(mockStateService.findById(SAMPLES_STATE_ID)).thenReturn(mockState);
        when(mockState.getName()).thenReturn(TEST_STATE);

        ResultActions result = getPaymentRequest();

        result
            .andExpect(status().isOk());

        String expectedSubject = TEST_PAYMENT_TYPE + BLANK_SPACE + FAILED_STATE;
        verify(mockMailSenderService).sendPaymentEmail(TEST_RECIPIENT_EMAIL, expectedSubject, mockPayment);
    }

    @Test
    public void testPaymentChargeBankWebHookBadRequest()
    throws Exception
    {
        when(Webhook.constructEvent(PAYLOAD_TEST, TEST_SIGNATURE, WEB_HOOK_SECRET))
            .thenThrow(new JsonSyntaxException(""));

        ResultActions result = getPaymentRequest();

        result
            .andExpect(status().isBadRequest());
    }

    private ResultActions getPaymentRequest()
    throws Exception
    {
        return mockMvc
            .perform(post(WEB_HOOKS_URL + PAYMENT_BANK)
                .contentType(MediaType.APPLICATION_JSON)
                .content(PAYLOAD_TEST)
                .header(STRIPE_SIGNATURE_KEY, TEST_SIGNATURE));
    }
}
