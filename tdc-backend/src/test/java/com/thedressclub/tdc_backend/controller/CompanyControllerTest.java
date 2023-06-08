/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static com.thedressclub.tdc_backend.controller.CompanyController.COMPANY_URL;
import static com.thedressclub.tdc_backend.controller.CompanyController.COMPLETE_USER_EMAIL_SENT;
import static com.thedressclub.tdc_backend.controller.CompanyController.FAILED_TO_SEND_EMAIL;
import static com.thedressclub.tdc_backend.controller.CompanyController.MESSAGE_KEY;
import static com.thedressclub.tdc_backend.controller.CompanyController.REQUEST;
import static com.thedressclub.tdc_backend.controller.UserController.USERS_URL;
import static com.thedressclub.tdc_backend.util.UserUtils.getCurrentUser;
import static com.thedressclub.tdc_backend.util.Utils.getMessage;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.thedressclub.tdc_backend.controller.config.GenericControllerTest;
import com.thedressclub.tdc_backend.controller.config.RestException;
import com.thedressclub.tdc_backend.model.Company;
import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.UserService;
import com.thedressclub.tdc_backend.service.external.IMailSenderService;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Test class for {@link CompanyController}.
 *
 * @author Daniel Mejia.
 */
public class CompanyControllerTest extends GenericControllerTest<Company>
{
    private static final String ADD_COMPANY_URL = USERS_URL + SLASH + OBJECT_ID + COMPANY_URL;
    private static final String UPDATE_COMPANY_URL = USERS_URL + COMPANY_URL + SLASH + OBJECT_ID;
    private static final String COMPANY_COMPLETE_URL = USERS_URL + COMPANY_URL + REQUEST;
    private static final String TEST_NAME = "name";
    private static final String TEST_COUNTRY = "country";
    private static final String TEST_STATE = "state";
    private static final String TEST_CITY = "city";
    private static final String TEST_ADDRESS = "address";

    private Company company;

    @Mock
    private UserService mockUserService;

    @Mock
    private IMailSenderService mockIEmailService;
    
    @Mock
    private User mockUser;

    @InjectMocks
    private CompanyController companyController;

    @Before
    public void setUp()
    {
        company = new Company();
        company.setId(OBJECT_ID);
        company.setName(TEST_NAME);
        company.setCountry(TEST_COUNTRY);
        company.setState(TEST_STATE);
        company.setCity(TEST_CITY);
        company.setStreetAddress(TEST_ADDRESS);

        MockitoAnnotations.initMocks(this);
        companyController.setGenericService(mockGenericService);
        init(ADD_COMPANY_URL, company, companyController);

        when(mockUserService.findById(OBJECT_ID)).thenReturn(mockUser);
        when(getCurrentUser(mockUserService)).thenReturn(mockUser);
    }

    @Test
    public void testAddCompany()
    throws Exception
    {
        ResultActions result = performAddRequest(company);

        result
            .andExpect(status().isCreated());

        verify(mockGenericService).add(company);
    }

    @Test
    public void testUpdateCompany()
    throws Exception
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(company);
        when(mockGenericService.update(company)).thenReturn(company);

        ResultActions result = performPutRequest(UPDATE_COMPANY_URL, company);

        result
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(company)));
    }

    @Test
    public void testGetRequestUserCompany()
    throws Exception
    {
        ResultActions result = mockMvc.perform(get(COMPANY_COMPLETE_URL)
            .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status()
            .isOk())
            .andExpect(content()
                .string(asJsonString(Collections.singletonMap(MESSAGE_KEY ,getMessage(COMPLETE_USER_EMAIL_SENT)))));

        verify(mockIEmailService).sendCompleteUserEmail(mockUser);
    }

    @Test
    public void testGetRequestUserCompanyError()
    throws IOException, TemplateException
    {
        doThrow(new IOException()).when(mockIEmailService).sendCompleteUserEmail(mockUser);

        assertThatThrownBy(() -> mockMvc.perform(get(COMPANY_COMPLETE_URL)
            .contentType(MediaType.APPLICATION_JSON)))
            .hasCause(new RestException(FAILED_TO_SEND_EMAIL));
    }
}