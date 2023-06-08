/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller.config;


import org.springframework.http.HttpStatus;

/**
 * Custom exception to manage error and send to error handler.
 *
 * @author Edson Ruiz Ramirez.
 */
public class RestException extends RuntimeException
{
    private final String errorKey;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    /**
     * Constructor.
     *
     * @param errorKey the error key in messages file.
     * @param httpStatus the status code to return.
     */
    public RestException(String errorKey, HttpStatus httpStatus)
    {
        super();
        this.errorKey = errorKey;
        this.errorMessage = null;
        this.httpStatus = httpStatus;
    }

    /**
     * Constructor.
     *
     * @param errorKey the error key in messages file.
     */
    public RestException(String errorKey)
    {
        super();
        this.errorKey = errorKey;
        this.httpStatus = null;
        this.errorMessage = null;
    }

    /**
     * Constructor.
     *
     * @param errorMessage the error message.
     * @param httpStatus the status code to return.
     */
    public RestException(HttpStatus httpStatus, String errorMessage)
    {
        super();
        this.errorKey = null;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    /**
     * Gets the httpStatus.
     *
     * @return Value of httpStatus.
     */
    public HttpStatus getHttpStatus()
    {
        return httpStatus;
    }

    /**
     * Gets the errorKey.
     *
     * @return Value of errorKey.
     */
    public String getErrorKey()
    {
        return errorKey;
    }

    /**
     * Gets the error message.
     *
     * @return Value of error message.
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }
}
