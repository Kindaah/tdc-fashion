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
import static org.junit.Assert.assertTrue;

import com.thedressclub.tdc_backend.model.ShippingAddress;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;

/**
 * Test class for {@link ShippingAddressService}.
 *
 * @author Edson Ruiz Ramirez.
 */
public class ShippingAddressServiceTest extends GenericServiceTest<ShippingAddress>
{
    @Test
    public void testConstructor()
    throws NoSuchMethodException
    {
        Constructor<ShippingAddressService> constructor =
            ShippingAddressService.class.getDeclaredConstructor();
        boolean isPublic = Modifier.isPublic(constructor.getModifiers());
        ShippingAddressService shippingAddressService = new ShippingAddressService();

        assertTrue("The constructor is not public: ", isPublic);
        assertNull("The userDao is not null", shippingAddressService.getGenericDAO());
    }
}