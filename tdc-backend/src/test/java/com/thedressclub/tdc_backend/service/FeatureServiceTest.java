/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.thedressclub.tdc_backend.model.Feature;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;

/**
 * Test class for {@link FeatureService}.
 *
 * @author Daniel Mejia.
 */
public class FeatureServiceTest extends GenericServiceTest<Feature>
{
    @Test
    public void testConstructor()
    throws NoSuchMethodException
    {
        Constructor<FeatureService> constructor = FeatureService.class.getDeclaredConstructor();
        boolean isPublic = Modifier.isPublic(constructor.getModifiers());
        FeatureService featureService = new FeatureService();

        assertThat(isPublic).as("The constructor is not public: ").isTrue();
        assertThat(featureService.getGenericDAO()).as("The productDao is not null").isNull();
    }
}