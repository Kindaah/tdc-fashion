/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.dto;

/**
 * DTO object for response token.
 *
 * @author Daniel Mejia.
 */
public class TokenResponse
{
    private String accountToken;
    private String errorMessage;

    /**
     * Gets errorMessage.
     *
     * @return Value of errorMessage.
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }

    /**
     * Sets new accountToken.
     *
     * @param accountToken New value of accountToken.
     */
    public void setAccountToken(String accountToken)
    {
        this.accountToken = accountToken;
    }

    /**
     * Gets accountToken.
     *
     * @return Value of accountToken.
     */
    public String getAccountToken()
    {
        return accountToken;
    }

    /**
     * Sets new errorMessage.
     *
     * @param errorMessage New value of errorMessage.
     */
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
}
