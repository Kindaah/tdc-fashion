/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.thedressclub.tdc_backend.model.Feature;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;

/**
 * Test class for {@link FeatureDAO}.
 *
 * @author Daniel Mejia.
 */
public class FeatureDAOTest extends GenericDAOTest<Feature>
{
    @Test
    public void testConstructor()
    throws NoSuchMethodException
    {
        Constructor<FeatureDAO> constructor = FeatureDAO.class.getDeclaredConstructor();
        boolean isPublic = Modifier.isPublic(constructor.getModifiers());
        FeatureDAO featureDAO = new FeatureDAO();

        assertThat(isPublic).as("The constructor is not public: ").isTrue();
        assertThat(featureDAO.entityClass).as("The entity class is null").isNotNull();
    }
}