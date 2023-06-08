/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.thedressclub.tdc_backend.model.Payment;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Test class for {@link PaymentService}.
 *
 * @author Daniel Mejia.
 */
public class PaymentServiceTest extends GenericServiceTest<Payment>
{
    private static final String PAYMENT_KEY = "paymentKey";
    private static final String AUTH_VALUE = "value";

    @InjectMocks
    private PaymentService instance;
    
    @Test
    public void testConstructor()
    throws NoSuchMethodException
    {
        Constructor<PaymentService> constructor = PaymentService.class.getDeclaredConstructor();
        boolean isPublic = Modifier.isPublic(constructor.getModifiers());
        PaymentService paymentService = new PaymentService();

        assertTrue("The constructor is not public: ", isPublic);
        assertNull("The paymentDAO is not null", paymentService.getGenericDAO());
    }

    @Test
    public void testFindByAuthKey()
    {
        Map<String, Object> filterMap = Collections.singletonMap(PAYMENT_KEY, AUTH_VALUE);
        List<Payment> payments = Collections.singletonList(new Payment());
        when(mockGenericDAO.list(filterMap)).thenReturn(payments);

        Payment payment = instance.findByKey(AUTH_VALUE);

        assertSame("Payment retrieved doesn't match: ", payments.get(0), payment);
    }

    @Test
    public void testFindByAuthKeyEmpty()
    {
        Map<String, Object> filterMap = Collections.singletonMap(PAYMENT_KEY, AUTH_VALUE);
        List<Payment> payments = new ArrayList<>();
        when(mockGenericDAO.list(filterMap)).thenReturn(payments);

        Payment payment = instance.findByKey(AUTH_VALUE);

        assertNull("Payment data should be empty: ", payment);
    }
}