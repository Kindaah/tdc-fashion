/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static com.thedressclub.tdc_backend.util.Utils.getMessage;
import static java.util.Collections.singletonMap;

import com.stripe.exception.StripeException;
import com.stripe.model.BankAccount;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.thedressclub.tdc_backend.controller.config.GenericController;
import com.thedressclub.tdc_backend.controller.config.RestException;
import com.thedressclub.tdc_backend.dto.TokenResponse;
import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.Payment;
import com.thedressclub.tdc_backend.model.PaymentType;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest API controller for manage payments.
 *
 * @author Daniel Mejia.
 */
@RestController
@RequestMapping("payments")
public class PaymentController extends GenericController<Payment>
{
    private static final Logger LOGGER = Logger.getLogger(PaymentController.class.getName());
    private static final String ADD_PAYMENT_CHARGE = "add_payment_charge";
    private static final String GET_BANK_TOKEN = "get_bank_token";

    static final String PAYMENT_UNAVAILABLE = "payment_unavailable";
    static final String ORDER_NOT_EXIST = "order_not_found";
    static final String AMOUNT_INCORRECT = "amount_incorrect";

    static final String ACCOUNT_ID_PARAM = "accountId";
    static final String PUBLIC_TOKEN_PARAM = "publicToken";

    static final String BANK_TOKEN_PARAM = "bankToken";
    static final String AMOUNT_PARAM = "amount";
    static final String ORDER_KEY = "order";

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private IStripeService stripeService;

    @Autowired
    private PaymentTypeService paymentTypeService;

    /**
     * @api {post} /payments/bankToken Gets a bank token according to the public key and account id.
     * @apiName GetsBankToken
     * @apiGroup Payments
     * @apiVersion 1.0.0
     * @apiHeaderExample {json} Header-Example:
     *     {
     *       "Content-Type": "application/x-www-form-urlencoded"
     *     }
     *
     * @apiParam {String} publicToken The public token generated in the client side.
     * @apiParam {String} accountId The account id generated in the client side.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "stripe_bank_account_token": "btok_5oEetfLzPklE1fwJZ7SG",
     *          "request_id": "[Unique request ID]"
     *     }
     *
     * @apiError BAD_REQUEST If there an error generating the bank token.
     */
    @PostMapping(value = "/bankToken", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<TokenResponse> getBankToken(
        @RequestParam(PUBLIC_TOKEN_PARAM) String publicToken, @RequestParam(ACCOUNT_ID_PARAM) String accountId)
    throws IOException
    {
        TokenResponse bankToken = stripeService.getBankToken(publicToken, accountId);

        if (bankToken.getAccountToken() == null)
        {
            throw new RestException(HttpStatus.BAD_REQUEST, bankToken.getErrorMessage());
        }

        LOGGER.log(Level.INFO, getMessage(GET_BANK_TOKEN), bankToken);

        return new ResponseEntity<>(bankToken, HttpStatus.OK);
    }

    /**
     * @api {post} /payments/orders/{id}/charge Adds a payment charge to the current user.
     * @apiName AddPaymentCharge
     * @apiGroup Payments
     * @apiVersion 1.0.0
     * @apiHeaderExample {json} Header-Example:
     *     {
     *       "Content-Type": "application/x-www-form-urlencoded"
     *     }
     *
     * @apiParam {Number} id The id of the order that will be charge.
     * @apiParam {Number} amount The amount to charge
     * @apiParam {String} method The payment method that will be charged
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 6,
     *          "createdAt": "2018-10-10@20:45:33",
     *          "updatedAt": "2018-10-10@20:45:33",
     *          "status": "pending",
     *          "paymentKey": "char_12345",
     *          "paymentType": {
     *              "id": 1,
     *              "createdAt": "2018-10-09@18:42:24",
     *              "updatedAt": "2018-10-09@18:42:24",
     *              "name": "samples",
     *              "description": "Payment for samples"
     *          }
     *     }
     *
     * @apiError BAD_REQUEST The payment is not available in this order state
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "The payment is not available in this order state"
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Thursday, October 11, 2018 4:19:24 PM COT"
     *      }
     */
    @PostMapping(value = "/orders/{id}/bankCharge", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Payment> addBankPaymentCharge(
        @PathVariable(ID_KEY) long idOrder,
        @RequestParam(BANK_TOKEN_PARAM) String bankToken,
        @RequestParam(AMOUNT_PARAM) double amountDollars)
    throws StripeException
    {
        User currentUser = UserUtils.getCurrentUser(userService);

        if (currentUser.getPaymentKey() == null)
        {
            Customer customer = stripeService.addPaymentCustomer(currentUser);
            currentUser.setPaymentKey(customer.getId());
            currentUser = userService.update(currentUser);
        }

        BankAccount storedBankAccount = stripeService.addBankAccount(currentUser, bankToken);

        Order order = orderService.findById(idOrder);

        if (order == null)
        {
            throw new RestException(ORDER_NOT_EXIST, HttpStatus.BAD_REQUEST);
        }

        PaymentType paymentType = paymentTypeService.findByOrderState(order);

        if (paymentType == null)
        {
            throw new RestException(PAYMENT_UNAVAILABLE, HttpStatus.BAD_REQUEST);
        }

        if (!amountCorrect(order).test(amountDollars))
        {
            throw new RestException(AMOUNT_INCORRECT, HttpStatus.BAD_REQUEST);
        }

        Charge charge =
            stripeService.addPaymentCharge(
                currentUser,
                storedBankAccount.getId(),
                amountDollars,
                paymentType.getDescription(),
                singletonMap(ORDER_KEY, order.getId()));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentType(paymentType);
        payment.setStatus(charge.getStatus());
        payment.setPaymentKey(charge.getId());
        payment.setAmount(amountDollars);

        LOGGER.log(Level.INFO, getMessage(ADD_PAYMENT_CHARGE), charge);

        return add(payment);
    }

    Predicate<Double> amountCorrect(Order order)
    {
        Map<Long, Predicate<Double>> amountFunction = new HashMap<>();
        amountFunction.put(Order.FIRST_PAYMENT_STATE_ID,
            amountDollars -> amountDollars == Math.ceil(order.getFinalQuote() / 2));
        amountFunction.put(Order.LAST_PAYMENT_STATE_ID, amountDollars -> {
            double payment = order.getFinalQuote() - Math.ceil(order.getFinalQuote() / 2);
            return amountDollars == (Math.round(payment * 100.0) / 100.0);
        });

        return amountFunction.getOrDefault(order.getState().getId(), amountDollars -> true);
    }
}
