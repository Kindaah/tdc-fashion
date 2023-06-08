/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import com.thedressclub.tdc_backend.model.Payment;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * Contains the service methods for {@link Payment}.
 *
 * @author Daniel Mejia.
 */
@Service("paymentService")
@Transactional
public class PaymentService extends GenericService<Payment>
{
    /**
     * Find an payment by the paymentKey.
     *
     * @param paymentKey The user paymentKey.
     *
     * @return The payment found.
     */
    public Payment findByKey(String paymentKey)
    {
        Map<String, Object> filterMap = Collections.singletonMap("paymentKey", paymentKey);
        List<Payment> paymentList = filterBy(filterMap);

        return paymentList.isEmpty() ? null : paymentList.get(0);
    }
}
