/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service.external;

import com.thedressclub.tdc_backend.model.Payment;
import com.thedressclub.tdc_backend.model.User;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Collection;

/**
 * Interface for the mail sender service.
 *
 * @author Daniel Mejia.
 */
public interface IMailSenderService
{
    /**
     * Sends an email with the parameters.
     *
     * @param recipients The emails for the recipients.
     * @param ccAddresses The emails for the copy recipients.
     * @param subject The email subject.
     * @param bodyHtml The html body for the email.
     */
    void sendEmail(Collection<String> recipients, Collection<String> ccAddresses, String subject, String bodyHtml);

    /**
     * Gets the html body for an email reading a template and passing a parameters for model.
     *
     * @param templateName The template name.
     * @param templateModel The params for complete the variables in the template.
     *
     * @return The html body representation for the email.
     */
    String getHtmlBody(String templateName, Object templateModel)
    throws IOException, TemplateException;

    /**
     * Sends an email with the parameters for notify a payment..
     *
     * @param recipient The email for the recipient that will be notified.
     * @param subject The email subject.
     * @param payment The payment for the notification.
     */
    void sendPaymentEmail(String recipient, String subject, Payment payment)
    throws IOException, TemplateException;

    /**
     * Sends an email with the parameters for notify a new user created.
     *
     * @param user The new user registered.
     */
    void sendCompleteUserEmail(User user)
    throws IOException, TemplateException;
}
