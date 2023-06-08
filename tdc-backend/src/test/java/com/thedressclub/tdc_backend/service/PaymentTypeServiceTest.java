/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.PaymentType;
import com.thedressclub.tdc_backend.model.State;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for {@link PaymentTypeService}.
 *
 * @author Daniel Mejia.
 */
public class PaymentTypeServiceTest extends GenericServiceTest<PaymentType>
{
    private static final Long STATE_ID = 1L;

    @Mock
    private Order mockOrder;

    @Mock
    private State mockState;

    @Mock
    private PaymentType mockPaymentType;

    @InjectMocks
    private PaymentTypeService instance;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        when(mockOrder.getState()).thenReturn(mockState);
        when(mockState.getId()).thenReturn(Order.SAMPLES_STATE_ID);
        when(mockGenericDAO.findBy(PaymentType.SAMPLES_ID)).thenReturn(mockPaymentType);
    }

    @Test
    public void testConstructor()
    throws NoSuchMethodException
    {
        Constructor<PaymentTypeService> constructor = PaymentTypeService.class.getDeclaredConstructor();
        boolean isPublic = Modifier.isPublic(constructor.getModifiers());
        PaymentTypeService paymentTypeService = new PaymentTypeService();

        assertTrue("The constructor is not public: ", isPublic);
        assertNull("The paymentTypeDAO is not null", paymentTypeService.getGenericDAO());
    }

    @Test
    public void testFindByOrderState()
    {
        PaymentType response = instance.findByOrderState(mockOrder);

        assertEquals("The response is wrong: ", mockPaymentType, response);
    }

    @Test
    public void testFindByOrderStateNotFound()
    {
        when(mockState.getId()).thenReturn(STATE_ID);
        PaymentType response = instance.findByOrderState(mockOrder);

        assertNull("The response is not null: ", response);
    }
}