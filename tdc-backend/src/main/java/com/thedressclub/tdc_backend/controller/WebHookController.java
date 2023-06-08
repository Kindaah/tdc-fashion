/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static com.thedressclub.tdc_backend.model.Order.FIRST_PAYMENT_STATE_ID;
import static com.thedressclub.tdc_backend.model.Order.LAST_PAYMENT_STATE_ID;
import static com.thedressclub.tdc_backend.model.Order.SAMPLES_STATE_ID;
import static com.thedressclub.tdc_backend.model.Payment.FAILED_STATE;
import static com.thedressclub.tdc_backend.model.PaymentType.FIRST_PAYMENT_ID;
import static com.thedressclub.tdc_backend.model.PaymentType.LAST_PAYMENT_ID;
import static com.thedressclub.tdc_backend.model.PaymentType.SAMPLES_ID;
import static com.thedressclub.tdc_backend.util.Utils.getMessage;
import static java.util.Objects.requireNonNull;

import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.EventData;
import com.stripe.net.Webhook;
import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.Payment;
import com.thedressclub.tdc_backend.model.PaymentType;
import com.thedressclub.tdc_backend.model.State;
import com.thedressclub.tdc_backend.service.OrderService;
import com.thedressclub.tdc_backend.service.PaymentService;
import com.thedressclub.tdc_backend.service.StateService;
import com.thedressclub.tdc_backend.service.external.IMailSenderService;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest API controller for manage webhooks.
 *
 * @author Daniel Mejia.
 */
@RestController
@RequestMapping("webhooks")
public class WebHookController
{
    private static final Logger LOGGER = Logger.getLogger(WebHookController.class.getName());

    private static final String EVENT_WEB_HOOK_KEY = "event_webhook";
    private static final String FAILED_WEB_HOOKS = "event_webhook_failed";

    private static final String PAYMENT_FAILED_KEY = "payment_failed";
    private static final String ORDER_GIVE_BACK_KEY = "order_state_changed";
    private static final String PAYMENT_SUCCESS_KEY = "payment_success";

    static final String BLANK_SPACE = " ";
    static final String STRIPE_SIGNATURE_KEY = "Stripe-Signature";
    static final String CHARGE_BANK_KEY = "payment.webhook.chargeBank";

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private Environment environment;

    @Autowired
    private StateService stateService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private IMailSenderService mailSenderService;

    private Map<Long, Long> paymentBeforeStates;

    /**
     * Constructor.
     */
    public WebHookController()
    {
        paymentBeforeStates = new HashMap<>();
        paymentBeforeStates.put(SAMPLES_ID, SAMPLES_STATE_ID);
        paymentBeforeStates.put(FIRST_PAYMENT_ID, FIRST_PAYMENT_STATE_ID);
        paymentBeforeStates.put(LAST_PAYMENT_ID, LAST_PAYMENT_STATE_ID);
    }

    /**
     * @api {post} /webhooks/payment WebHooks for stripe payment system
     * @apiName WebHooks
     * @apiGroup WebHooks
     * @apiVersion 1.0.0
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     */
    @PostMapping(value = "/payment/chargeBank")
    public ResponseEntity paymentChargeBankWebHook(
        @RequestBody String payload, @RequestHeader(value = STRIPE_SIGNATURE_KEY) String signature)
    {
        try
        {
            String webHookKey = requireNonNull(environment.getProperty(CHARGE_BANK_KEY));
            Event event = Webhook.constructEvent(payload, signature, webHookKey);
            updatePayment(event);

            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, getMessage(FAILED_WEB_HOOKS), e.getMessage());

            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    private void updatePayment(Event event)
    throws IOException, TemplateException
    {
        LOGGER.log(Level.INFO, getMessage(EVENT_WEB_HOOK_KEY), event.toString());

        EventData eventData = event.getData();
        Charge charge = (Charge) eventData.getObject();
        String chargeStatus = charge.getStatus();

        Payment storedPayment = paymentService.findByKey(charge.getId());
        Order storedOrder = storedPayment.getOrder();
        PaymentType storedType = storedPayment.getPaymentType();

        storedPayment.setStatus(chargeStatus);
        paymentService.update(storedPayment);

        if (FAILED_STATE.equals(chargeStatus))
        {

            LOGGER.log(Level.INFO, getMessage(PAYMENT_FAILED_KEY), storedOrder.getId());

            State beforeState = stateService.findById(paymentBeforeStates.get(storedType.getId()));
            storedOrder.setState(beforeState);
            orderService.update(storedOrder);

            LOGGER.log(Level.INFO, getMessage(ORDER_GIVE_BACK_KEY), beforeState.getName());
        }
        else
        {
            LOGGER.log(Level.INFO, getMessage(PAYMENT_SUCCESS_KEY), storedPayment.getPaymentKey());
        }

        String clientEmail = storedOrder.getUser().getCompanyEmail();
        String subjectEmail = storedType.getDescription() + BLANK_SPACE + chargeStatus;
        mailSenderService.sendPaymentEmail(clientEmail, subjectEmail, storedPayment);
    }
}
