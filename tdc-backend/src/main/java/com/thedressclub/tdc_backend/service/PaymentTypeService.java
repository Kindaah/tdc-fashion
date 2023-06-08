/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.PaymentType;
import java.util.HashMap;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * Contains the service methods for {@link PaymentType}.
 *
 * @author Daniel Mejia.
 */
@Service("paymentTypeService")
@Transactional
public class PaymentTypeService extends GenericService<PaymentType>
{
    /**
     * Gets an payment type according to the order state.
     *
     * @param order the order to search.
     *
     * @return a payment type according to the order state.
     */
    public PaymentType findByOrderState(Order order)
    {
        Map<Long, Long> paymentTypes = new HashMap<>();
        paymentTypes.put(Order.SAMPLES_STATE_ID, PaymentType.SAMPLES_ID);
        paymentTypes.put(Order.FIRST_PAYMENT_STATE_ID, PaymentType.FIRST_PAYMENT_ID);
        paymentTypes.put(Order.LAST_PAYMENT_STATE_ID, PaymentType.LAST_PAYMENT_ID);

        Long paymentTypeId = paymentTypes.get(order.getState().getId());

        return paymentTypeId == null ? null : findById(paymentTypeId);
    }
}
