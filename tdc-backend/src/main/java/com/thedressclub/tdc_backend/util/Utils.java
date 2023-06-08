/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.util;

import java.util.Locale;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Utils for the application.
 *
 * @author Daniel Mejia.
 */
public final class Utils
{
    private static final String BASENAME = "messages";

    /**
     * Constructor
     */
    private Utils()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets message from the application messages.
     *
     * @param messageKey the message key.
     *
     * @return the message.
     */
    public static String getMessage(String messageKey)
    {
        ResourceBundleMessageSource bean = new ResourceBundleMessageSource();
        bean.setBasename(BASENAME);

        return bean.getMessage(messageKey, null, Locale.getDefault());
    }
}
