/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import static org.assertj.core.api.Java6Assertions.assertThat;

import com.thedressclub.tdc_backend.model.Company;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;

/**
 * Test class for {@link CompanyService}.
 *
 * @author Daniel Mejia.
 */
public class CompanyServiceTest extends GenericServiceTest<Company>
{
    @Test
    public void testConstructor()
    throws NoSuchMethodException
    {
        Constructor<CompanyService> constructor = CompanyService.class.getDeclaredConstructor();
        boolean isPublic = Modifier.isPublic(constructor.getModifiers());
        CompanyService companyService = new CompanyService();

        assertThat(isPublic).isTrue();
        assertThat(companyService.getGenericDAO()).isNull();
    }
}