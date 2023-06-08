/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller.config;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;

/**
 * Response DTO for return a error in the request.
 *
 * @author Edson Ruiz Ramirez.
 */
public class ErrorResponse
{
    private List<String> errorMessages;
    private HttpStatus status;
    private int statusCode;
    private String timestamp;

    /**
     * Constructor.
     *
     * @param errorMessages a message list.
     */
    ErrorResponse(List<String> errorMessages, HttpStatus status, Locale locale)
    {
        this.errorMessages = errorMessages;
        this.status = status;
        this.statusCode = status.value();
        DateFormat dateFormat =
            DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, locale);
        this.timestamp = dateFormat.format(new Date());
    }

    /**
     * Gets the error messages list.
     *
     * @return List of messages.
     */
    public List<String> getErrorMessages()
    {
        return errorMessages;
    }

    /**
     * Gets the status.
     *
     * @return Value of status.
     */
    public HttpStatus getStatus()
    {
        return status;
    }

    /**
     * Gets the timeStamp.
     *
     * @return Value of timeStamp.
     */
    public String getTimestamp()
    {
        return timestamp;
    }

    /**
     * Gets the statusCode.
     *
     * @return Value of statusCode.
     */
    public int getStatusCode()
    {
        return statusCode;
    }
}