/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.thedressclub.tdc_backend.model.Payment;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;

/**
 * Test class for {@link PaymentDAO}.
 *
 * @author Daniel Mejia.
 */
public class PaymentDAOTest extends GenericDAOTest<Payment>
{
    @Test
    public void testConstructor()
    throws NoSuchMethodException
    {
        Constructor<PaymentDAO> constructor = PaymentDAO.class.getDeclaredConstructor();
        boolean isPublic = Modifier.isPublic(constructor.getModifiers());
        PaymentDAO paymentDAO = new PaymentDAO();

        assertTrue("The constructor is not public: ", isPublic);
        assertNotNull("The entity class is null", paymentDAO.entityClass);
    }
}