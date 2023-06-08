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
import static org.mockito.Mockito.when;

import com.stripe.exception.StripeException;
import io.jsonwebtoken.JwtException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * Test class for {@link ErrorHandler}.
 *
 * @author Edson Ruiz Ramirez.
 */
public class ErrorHandlerTest
{
    private static final String MESSAGE = "error";
    private static final String INTERNAL_ERROR = "internal error";
    private static final String ERROR_KEY = "error_key";
    private static final String REST_MESSAGE = "rest message";
    private static final String SIZE_EXCEPTION = "Maximum size";
    private static final String JWT_EXCEPTION = "Signature wrong";
    private static final String STRIPE_EXCEPTION = "stripe error";

    @Mock
    private ObjectError mockObjectError;

    @Mock
    private MethodArgumentNotValidException mockArgumentNotValidException;

    @Mock
    private Exception mockException;

    @Mock
    private JwtException mockJwtException;

    @Mock
    private MaxUploadSizeExceededException mockMaxUploadSizeExceededException;

    @Mock
    private RestException mockRestException;

    @Mock
    private BindingResult mockBindingResult;

    @Mock
    private MessageSource mockMessageSource;

    @Mock
    private StripeException mockStripeException;

    @InjectMocks
    private ErrorHandler instance;

    @Before
    public void setUp()
    {
        instance = new ErrorHandler();
        MockitoAnnotations.initMocks(this);

        when(mockArgumentNotValidException.getBindingResult()).thenReturn(mockBindingResult);
        when(mockBindingResult.getAllErrors()).thenReturn(getErrorList());
        when(mockMessageSource.getMessage(mockObjectError, Locale.US)).thenReturn(MESSAGE);
        when(mockMessageSource.getMessage(ErrorHandler.UNEXPECTED_ERROR, null, Locale.US))
            .thenReturn(INTERNAL_ERROR);
        when(mockException.getMessage()).thenReturn(INTERNAL_ERROR);
        when(mockJwtException.getMessage()).thenReturn(JWT_EXCEPTION);
    }

    private List<ObjectError> getErrorList()
    {
        return Arrays.asList(mockObjectError, mockObjectError);
    }

    @Test
    public void testHandleArgumentNotValidException()
    {
        ResponseEntity<ErrorResponse> response =
            instance.handleArgumentNotValidException(mockArgumentNotValidException, Locale.US);
        ErrorResponse errorResponse = response.getBody();

        assertEquals("The status is wrong: ", HttpStatus.BAD_REQUEST, response.getStatusCode());
        assert errorResponse != null;
        errorResponse.getErrorMessages()
            .forEach(errorMessage ->
                assertEquals("The error response message is wrong: ", MESSAGE, errorMessage));
    }

    @Test
    public void testHandleExceptions()
    {
        ResponseEntity<ErrorResponse> response = instance.handleExceptions(mockException, Locale.US);
        ErrorResponse errorResponse = response.getBody();

        assertEquals(
            "The status is wrong: ",
            HttpStatus.INTERNAL_SERVER_ERROR,
            response.getStatusCode());
        assert errorResponse != null;
        errorResponse.getErrorMessages()
            .forEach(errorMessage ->
                assertEquals("The error message is wrong: ", INTERNAL_ERROR, errorMessage));
    }

    @Test
    public void testHandleJwtException()
    {
        ResponseEntity<ErrorResponse> response = instance.handleJwtException(mockJwtException, Locale.US);
        ErrorResponse errorResponse = response.getBody();

        assertEquals(
            "The status is wrong: ",
            HttpStatus.BAD_REQUEST,
            response.getStatusCode());
        assert errorResponse != null;
        errorResponse.getErrorMessages()
            .forEach(errorMessage ->
                assertEquals("The error message is wrong: ", JWT_EXCEPTION, errorMessage));
    }

    @Test
    public void testHandleMaxSizeException()
    {
        when(mockMaxUploadSizeExceededException.getMessage()).thenReturn(SIZE_EXCEPTION);
        ResponseEntity<ErrorResponse> response = instance.handleMaxSizeException(mockMaxUploadSizeExceededException, Locale.US);
        ErrorResponse errorResponse = response.getBody();

        assertEquals(
            "The status is wrong: ",
            HttpStatus.BAD_REQUEST,
            response.getStatusCode());
        assert errorResponse != null;
        errorResponse.getErrorMessages()
            .forEach(errorMessage ->
                assertEquals("The error message is wrong: ", SIZE_EXCEPTION, errorMessage));
    }

    @Test
    public void testHandleRestExceptionWithKey()
    {
        when(mockMessageSource.getMessage(ERROR_KEY, null, Locale.US)).thenReturn(REST_MESSAGE);
        when(mockRestException.getErrorKey()).thenReturn(ERROR_KEY);
        when(mockRestException.getHttpStatus()).thenReturn(HttpStatus.BAD_REQUEST);

        ResponseEntity<ErrorResponse> response =
            instance.handleRestException(mockRestException, Locale.US);
        ErrorResponse errorResponse = response.getBody();

        assertEquals(
            "The status is wrong: ",
            HttpStatus.BAD_REQUEST,
            response.getStatusCode());
        assert errorResponse != null;
        errorResponse.getErrorMessages()
            .forEach(errorMessage ->
                assertEquals("The error message is wrong: ", REST_MESSAGE, errorMessage));
    }

    @Test
    public void testHandleRestExceptionWithMessage()
    {
        RestException restException = new RestException(HttpStatus.BAD_REQUEST, REST_MESSAGE);

        ResponseEntity<ErrorResponse> response =
            instance.handleRestException(restException, Locale.US);
        ErrorResponse errorResponse = response.getBody();

        assertEquals(
            "The status is wrong: ",
            HttpStatus.BAD_REQUEST,
            response.getStatusCode());
        assert errorResponse != null;
        errorResponse.getErrorMessages()
            .forEach(errorMessage ->
                assertEquals("The error message is wrong: ", REST_MESSAGE, errorMessage));
    }

    @Test
    public void testHandleStripeException()
    {
        when(mockStripeException.getMessage()).thenReturn(STRIPE_EXCEPTION);

        ResponseEntity<ErrorResponse> response =
            instance.handleStripeException(mockStripeException, Locale.US);
        ErrorResponse errorResponse = response.getBody();

        assertEquals(
            "The status is wrong: ",
            HttpStatus.BAD_REQUEST,
            response.getStatusCode());
        assert errorResponse != null;
        errorResponse.getErrorMessages()
            .forEach(errorMessage ->
                assertEquals("The error message is wrong: ", STRIPE_EXCEPTION, errorMessage));
    }
}