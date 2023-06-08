/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service.external;

import static com.thedressclub.tdc_backend.model.Payment.SUCCEEDED_STATE;
import static com.thedressclub.tdc_backend.service.external.MailSenderService.PAYMENT_EMAIL_TEMPLATE;
import static com.thedressclub.tdc_backend.service.external.MailSenderService.REGISTER_EMAIL_TEMPLATE;
import static com.thedressclub.tdc_backend.service.external.MailSenderService.REGISTER_SUBJECT_EMAIL_KEY;
import static com.thedressclub.tdc_backend.util.Utils.getMessage;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.Payment;
import com.thedressclub.tdc_backend.model.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;


/**
 * Test class for {@link MailSenderService}.
 *
 * @author Daniel Mejia.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FreeMarkerTemplateUtils.class, AmazonSimpleEmailServiceClientBuilder.class})
public class MailSenderServiceTest
{
    private static final String TEST_SOURCE_EMAIL = "test@tdc.fashion";
    private static final String TEST_SUPPORT_EMAIL = "test_support@tdc.fashion";
    private static final String TEST_ACCESS_KEY = "test key";
    private static final String TEST_SECRET_KEY = "test secret key";
    private static final String TEST_APP_DOMAIN = "test domain";
    private static final Long OBJECT_ID = 1L;
    private static final String TEST_HTML_BODY = "html test body";
    private static final String TEST_ADDRESS = "recipient@tdc.fashion";
    private static final String TEST_SUBJECT = "subject";
    private static final String TEST_USER_NAME = "test user";
    private static final String TEST_USER_LAST_NAME = "test last name";
    private static final String TEST_PHONE_NUMBER = "test phone";

    @Mock
    private Payment mockPayment;

    @Mock
    private Order mockOrder;

    @Mock
    private User mockUser;

    @Mock
    private Template mockTemplate;

    @Mock
    private AmazonSimpleEmailServiceClientBuilder mockAmazonSimpleEmailServiceClientBuilder;

    @Mock
    private AmazonSimpleEmailService mockAmazonSimpleEmailService;

    @Mock
    private Configuration mockFreemarkerConfig;

    @InjectMocks
    @Spy
    private MailSenderService instance = new MailSenderService(
        TEST_SOURCE_EMAIL,
        TEST_SUPPORT_EMAIL,
        TEST_ACCESS_KEY,
        TEST_SECRET_KEY,
        TEST_APP_DOMAIN);

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        mockStatic(AmazonSimpleEmailServiceClientBuilder.class);
        mockStatic(FreeMarkerTemplateUtils.class);
    }

    @Test
    public void testSendEmail()
    {
        when(AmazonSimpleEmailServiceClientBuilder.standard())
            .thenReturn(mockAmazonSimpleEmailServiceClientBuilder);
        when(mockAmazonSimpleEmailServiceClientBuilder.withCredentials(any()))
            .thenReturn(mockAmazonSimpleEmailServiceClientBuilder);
        when(mockAmazonSimpleEmailServiceClientBuilder.withRegion(Regions.US_EAST_1))
            .thenReturn(mockAmazonSimpleEmailServiceClientBuilder);
        when(mockAmazonSimpleEmailServiceClientBuilder.build())
            .thenReturn(mockAmazonSimpleEmailService);

        instance.sendEmail(
            singletonList(TEST_ADDRESS),
            singletonList(TEST_SUPPORT_EMAIL),
            TEST_SUBJECT,
            TEST_HTML_BODY);

        verify(mockAmazonSimpleEmailService).sendEmail(any());
    }

    @Test
    public void testGetHtmlBody()
    throws IOException, TemplateException
    {
        when(mockFreemarkerConfig.getTemplate(PAYMENT_EMAIL_TEMPLATE)).thenReturn(mockTemplate);
        when(FreeMarkerTemplateUtils.processTemplateIntoString(eq(mockTemplate), any(Map.class)))
            .thenReturn(TEST_HTML_BODY);

        String response = instance.getHtmlBody(PAYMENT_EMAIL_TEMPLATE, Collections.emptyMap());

        assertThat(response).isEqualTo(TEST_HTML_BODY);
    }

    @Test
    public void testSendPaymentEmail()
    throws IOException, TemplateException
    {
        when(mockPayment.getOrder()).thenReturn(mockOrder);
        when(mockPayment.getStatus()).thenReturn(SUCCEEDED_STATE);
        when(mockOrder.getId()).thenReturn(OBJECT_ID);
        when(mockOrder.getUser()).thenReturn(mockUser);
        doReturn(TEST_HTML_BODY).when(instance).getHtmlBody(eq(PAYMENT_EMAIL_TEMPLATE), any(Map.class));
        doNothing()
            .when(instance)
            .sendEmail(singletonList(TEST_ADDRESS), singletonList(TEST_SUPPORT_EMAIL), TEST_SUBJECT, TEST_HTML_BODY);
        doNothing()
            .when(instance)
            .sendEmail(singletonList(TEST_SUPPORT_EMAIL), singletonList(TEST_SUPPORT_EMAIL), TEST_SUBJECT, TEST_HTML_BODY);

        instance.sendPaymentEmail(TEST_ADDRESS, TEST_SUBJECT, mockPayment);

        verify(instance).sendEmail(singletonList(TEST_ADDRESS), singletonList(TEST_SUPPORT_EMAIL), TEST_SUBJECT, TEST_HTML_BODY);
    }

    @Test
    public void testSendCompleteUserEmail()
    throws IOException, TemplateException
    {
        when(mockUser.getId()).thenReturn(OBJECT_ID);
        when(mockUser.getFirstName()).thenReturn(TEST_USER_NAME);
        when(mockUser.getLastName()).thenReturn(TEST_USER_LAST_NAME);
        when(mockUser.getPhoneNumber()).thenReturn(TEST_PHONE_NUMBER);
        doReturn(TEST_HTML_BODY).when(instance).getHtmlBody(eq(REGISTER_EMAIL_TEMPLATE), any(Map.class));
        doNothing()
            .when(instance)
            .sendEmail(singletonList(TEST_SUPPORT_EMAIL), emptyList(), getMessage(REGISTER_SUBJECT_EMAIL_KEY), TEST_HTML_BODY);

        instance.sendCompleteUserEmail(mockUser);

        verify(instance).sendEmail(singletonList(TEST_SUPPORT_EMAIL), emptyList(), getMessage(REGISTER_SUBJECT_EMAIL_KEY), TEST_HTML_BODY);
    }
}