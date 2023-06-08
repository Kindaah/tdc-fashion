/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club
 */

package com.thedressclub.tdc_backend;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Test class for {@link ServletInitializer}.
 *
 * @author Daniel Mejia.
 */
public class ServletInitializerTest
{
    @Mock
    private SpringApplicationBuilder mockSpringApplicationBuilder;

    private ServletInitializer instance;

    @Before
    public void setUp()
    {
        instance = new ServletInitializer();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConfigure()
    {
        when(mockSpringApplicationBuilder.sources(TdcApplication.class))
            .thenReturn(mockSpringApplicationBuilder);

        SpringApplicationBuilder response = instance.configure(mockSpringApplicationBuilder);

        assertEquals("The Spring Builder is wrong: ", mockSpringApplicationBuilder, response);
    }
}