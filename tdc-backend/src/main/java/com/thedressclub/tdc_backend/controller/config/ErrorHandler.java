/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller.config;

import com.stripe.exception.StripeException;
import io.jsonwebtoken.JwtException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javassist.bytecode.stackmap.TypeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * Response error handler for the Rest API.
 *
 * @author Edson Ruiz Ramirez.
 */
@ControllerAdvice
public class ErrorHandler
{
    private static final Logger LOGGER = Logger.getLogger(TypeData.ClassName.class.getName());
    static final String UNEXPECTED_ERROR = "Exception.unexpected";

    @Autowired
    private MessageSource messageSource;

    private List<String> errorMessages;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentNotValidException(
        MethodArgumentNotValidException ex, Locale locale)
    {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        BindingResult result = ex.getBindingResult();
        errorMessages = result.getAllErrors()
            .stream()
            .map(objectError -> messageSource.getMessage(objectError, locale))
            .collect(Collectors.toList());

        LOGGER.log(Level.SEVERE, ex.getMessage());
        ErrorResponse restError = new ErrorResponse(errorMessages, status, locale);
        return new ResponseEntity<>(restError, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleExceptions(Exception ex, Locale locale)
    {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorMessage = messageSource.getMessage(UNEXPECTED_ERROR, null, locale);
        errorMessages = Arrays.asList(ex.getMessage(), errorMessage);

        LOGGER.log(Level.SEVERE, ex.getMessage());
        ErrorResponse restError = new ErrorResponse(errorMessages, status, locale);
        return new ResponseEntity<>(restError, status);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex, Locale locale)
    {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        errorMessages = Collections.singletonList(ex.getMessage());

        LOGGER.log(Level.SEVERE, ex.getMessage());
        ErrorResponse restError = new ErrorResponse(errorMessages, status, locale);
        return new ResponseEntity<>(restError, status);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(
        MaxUploadSizeExceededException ex, Locale locale)
    {
        errorMessages = Collections.singletonList(ex.getMessage());

        LOGGER.log(Level.SEVERE, ex.getMessage());
        ErrorResponse restError = new ErrorResponse(errorMessages, HttpStatus.BAD_REQUEST, locale);
        return new ResponseEntity<>(restError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<ErrorResponse> handleRestException(RestException ex, Locale locale)
    {
        String errorKey = ex.getErrorKey();
        String errorMessage =
            errorKey == null ? ex.getErrorMessage() : messageSource.getMessage(errorKey, null, locale);
        errorMessages = Collections.singletonList(errorMessage);

        LOGGER.log(Level.SEVERE, errorMessage);
        ErrorResponse restError = new ErrorResponse(errorMessages, ex.getHttpStatus(), locale);
        return new ResponseEntity<>(restError, ex.getHttpStatus());
    }

    @ExceptionHandler(StripeException .class)
    public ResponseEntity<ErrorResponse> handleStripeException(StripeException ex, Locale locale)
    {
        errorMessages = Collections.singletonList(ex.getMessage());

        LOGGER.log(Level.SEVERE, ex.getMessage());
        ErrorResponse restError = new ErrorResponse(errorMessages, HttpStatus.BAD_REQUEST, locale);
        return new ResponseEntity<>(restError, HttpStatus.BAD_REQUEST);
    }
}