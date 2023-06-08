/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend;

import com.thedressclub.tdc_backend.model.GenericModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application to initialize the application.
 *
 * @author Daniel Mejia.
 */
@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@EnableJpaAuditing
@EntityScan(basePackageClasses= GenericModel.class)
public class TdcApplication
{
    /**
     * Method to start the application.
     *
     * @param args the arguments list to execute the application.
     */
    public static void main(String[] args)
    {
        SpringApplication.run(TdcApplication.class, args);
    }
}
