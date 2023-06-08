/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

/**
 * Test class for {@link ErrorResponse}.
 *
 * @author Edson Ruiz Ramirez.
 */
public class ErrorResponseTest
{
    private static final String MESSAGE = "message";

    private HttpStatus httpStatus;
    private ErrorResponse instance;

    @Before
    public void setUp()
    {
        List<String> errorMessages = getMessages();
        MockitoAnnotations.initMocks(this);
        httpStatus = HttpStatus.BAD_REQUEST;
        instance = new ErrorResponse(errorMessages, httpStatus, Locale.US);
    }

    @Test
    public void getErrorMessages()
    {
        List<String> response = instance.getErrorMessages();
        response
            .forEach(errorMessage -> assertEquals("The message is wrong: ", MESSAGE, errorMessage));
    }

    @Test
    public void getStatus()
    {
        assertEquals("The status is wrong:", httpStatus, instance.getStatus());
    }

    @Test
    public void getTimestamp()
    {
        assertNotNull("The timestamp is null", instance.getTimestamp());
    }

    @Test
    public void getStatusCode()
    {
        assertEquals("The status code is wrong:", httpStatus.value(), instance.getStatusCode
            ());
    }

    private List<String> getMessages()
    {
        return Arrays.asList(MESSAGE, MESSAGE);
    }
}