/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Test class for {@link AuthenticationFilter}.
 *
 * @author Daniel Mejia.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthenticationFilter.class)
@AutoConfigureMockMvc
@EnableWebMvc
public class AuthenticationFilterTest
{
    private static final String ANY_ENDPOINT = "/";

    @Autowired
    private WebApplicationContext webAppContextSetup;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @Before
    public void setup()
    {
        mockMvc =
            MockMvcBuilders.webAppContextSetup(webAppContextSetup)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void testRequiresAuthentication()
    throws Exception
    {
        mockMvc.perform(get(ANY_ENDPOINT))
            .andExpect(status().isUnauthorized());
    }
}