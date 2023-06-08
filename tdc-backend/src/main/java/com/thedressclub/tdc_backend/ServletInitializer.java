/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Class to initialize the web servlet application.
 *
 * @author Daniel Mejia
 */
public class ServletInitializer extends SpringBootServletInitializer
{
    /** (non-Javadoc) @see {@link SpringBootServletInitializer} */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(TdcApplication.class);
    }
}
