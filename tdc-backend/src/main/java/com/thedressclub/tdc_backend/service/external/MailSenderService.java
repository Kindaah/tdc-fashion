/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service.external;

import static com.thedressclub.tdc_backend.controller.CompanyController.COMPANY_URL;
import static com.thedressclub.tdc_backend.controller.OrderController.ORDERS_URL;
import static com.thedressclub.tdc_backend.controller.UserController.USERS_URL;
import static com.thedressclub.tdc_backend.model.Payment.FAILED_STATE;
import static com.thedressclub.tdc_backend.util.Utils.getMessage;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.springframework.ui.freemarker.FreeMarkerTemplateUtils.processTemplateIntoString;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.google.common.base.Charsets;
import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.Payment;
import com.thedressclub.tdc_backend.model.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for send mail.
 *
 * @author Daniel Mejia.
 */
@Service("mailSenderService")
@Transactional
public class MailSenderService implements IMailSenderService
{
    private static final Logger LOGGER = Logger.getLogger(MailSenderService.class.getName());
    private static final String EMAIL_SENT_KEY = "email_sent";

    static final String PAYMENT_EMAIL_TEMPLATE = "email-payment.ftl";
    static final String REGISTER_EMAIL_TEMPLATE = "email-user-complete.ftl";

    private static final String PAYMENT_STATE_TEMPLATE = "paymentState";
    private static final String PAYMENT_FAILED_TEMPLATE = "isPaymentFailed";
    private static final String USERNAME_TEMPLATE = "userName";
    private static final String LAST_NAME_TEMPLATE = "userLastName";
    private static final String AMOUNT_TEMPLATE = "paymentAmount";
    private static final String ORDER_TEMPLATE = "paymentOrderId";
    private static final String INVOICE_TEMPLATE = "paymentInvoice";
    private static final String REDIRECT_TO_TEMPLATE = "redirectTo";

    private static final String NAME_TEMPLATE = "name";
    private static final String EMAIL_TEMPLATE = "email";
    private static final String PHONE_NUMBER_TEMPLATE = "phoneNumber";
    private static final String SENDING_EMAIL_MESSAGE = "sending_email";
    private static final String PAYMENTS_MESSAGE = "payments";
    private static final String COMPLETE_USER_MESSAGE = "complete_user";
    static final String REGISTER_SUBJECT_EMAIL_KEY = "register_subject_email";
    private static final String SLASH = "/";
    private static final String BLANK_SPACE = " ";

    @Autowired
    private Configuration freemarkerConfig;

    private String sourceEmail;
    private String supportEmail;
    private String awsAccessKey;
    private String awsSecretKey;
    private String appDomain;

    /**
     * Constructor with parameters for aws email sender.
     *
     * @param sourceEmail the source email sender.
     * @param supportEmail the support email.
     * @param awsAccessKey the aws access key.
     * @param awsSecretKey the aws secret key.
     */
    public MailSenderService(
        @Value("${aws.email.sourceEmail}") String sourceEmail,
        @Value("${aws.email.supportEmail}") String supportEmail,
        @Value("${aws.email.accessKey}") String awsAccessKey,
        @Value("${aws.email.secretKey}") String awsSecretKey,
        @Value("${app.domain}") String appDomain)
    {
        this.sourceEmail = sourceEmail;
        this.supportEmail = supportEmail;
        this.awsAccessKey = awsAccessKey;
        this.awsSecretKey = awsSecretKey;
        this.appDomain = appDomain;
    }

    /** (non-Javadoc) @see {@link IMailSenderService} */
    @Override
    public void sendEmail(Collection<String> recipients, Collection<String> ccAddresses, String subject, String bodyHtml)
    {
        BasicAWSCredentials credentials =
            new BasicAWSCredentials(awsAccessKey, awsSecretKey);

        AmazonSimpleEmailService amazonSimpleEmailService =
            AmazonSimpleEmailServiceClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();

        Body bodyMessage =
            new Body()
                .withHtml(new Content()
                    .withCharset(Charsets.UTF_8.name())
                    .withData(bodyHtml));

        Content content =
            new Content()
                .withCharset(Charsets.UTF_8.name())
                .withData(subject);

        Destination destination =
            new Destination()
                .withToAddresses(recipients)
                .withCcAddresses(ccAddresses);

        Message message =
            new Message()
                .withBody(bodyMessage)
                .withSubject(content);

        SendEmailRequest emailRequest =
            new SendEmailRequest()
                .withDestination(destination)
                .withMessage(message)
                .withSource(sourceEmail);

        amazonSimpleEmailService
            .sendEmail(emailRequest);

        Object[] params = { recipients, ccAddresses, subject};
        LOGGER.log(Level.INFO, getMessage(EMAIL_SENT_KEY), params);
    }

    /** (non-Javadoc) @see {@link IMailSenderService} */
    @Override
    public String getHtmlBody(String templateName, Object templateModel)
    throws IOException, TemplateException
    {
        Template emailTemplate = freemarkerConfig.getTemplate(templateName);

        return processTemplateIntoString(emailTemplate, templateModel);
    }

    /** (non-Javadoc) @see {@link IMailSenderService} */
    @Override
    public void sendPaymentEmail(String recipient, String subject, Payment payment)
    throws IOException, TemplateException
    {
        Object[] params = { getMessage(PAYMENTS_MESSAGE), payment.getPaymentKey()};
        LOGGER.log(Level.INFO, getMessage(SENDING_EMAIL_MESSAGE), params);

        Order order = payment.getOrder();
        User user =  order.getUser();
        String paymentStatus = payment.getStatus();

        String redirectTo = appDomain + ORDERS_URL + SLASH + order.getId();

        Map<String, Object> emailParams = new HashMap<>();
        emailParams.put(PAYMENT_STATE_TEMPLATE, paymentStatus);
        emailParams.put(PAYMENT_FAILED_TEMPLATE, FAILED_STATE.equals(paymentStatus));
        emailParams.put(USERNAME_TEMPLATE, user.getFirstName());
        emailParams.put(LAST_NAME_TEMPLATE, user.getLastName());
        emailParams.put(AMOUNT_TEMPLATE, payment.getAmount());
        emailParams.put(ORDER_TEMPLATE, order.getId());
        emailParams.put(INVOICE_TEMPLATE, payment.getPaymentKey());
        emailParams.put(REDIRECT_TO_TEMPLATE, redirectTo);

        String bodyHtmlClient = getHtmlBody(PAYMENT_EMAIL_TEMPLATE, emailParams);
        sendEmail(singletonList(recipient), singletonList(supportEmail), subject, bodyHtmlClient);
    }

    /** (non-Javadoc) @see {@link IMailSenderService} */
    @Override
    public void sendCompleteUserEmail(User user)
    throws IOException, TemplateException
    {
        Object[] params = { getMessage(COMPLETE_USER_MESSAGE), user.getCompanyEmail()};
        LOGGER.log(Level.INFO, getMessage(SENDING_EMAIL_MESSAGE), params);

        String redirectTo = appDomain + USERS_URL + SLASH +  user.getId() + COMPANY_URL;

        Map<String, Object> registerParams = new HashMap<>();
        registerParams.put(REDIRECT_TO_TEMPLATE, redirectTo);
        registerParams.put(NAME_TEMPLATE, user.getFirstName() + BLANK_SPACE + user.getLastName());
        registerParams.put(EMAIL_TEMPLATE, user.getCompanyEmail());
        registerParams.put(PHONE_NUMBER_TEMPLATE, user.getPhoneNumber());

        String bodyHtmlRegister = getHtmlBody(REGISTER_EMAIL_TEMPLATE, registerParams);
        sendEmail(singletonList(supportEmail), emptyList(), getMessage(REGISTER_SUBJECT_EMAIL_KEY), bodyHtmlRegister);
    }
}
