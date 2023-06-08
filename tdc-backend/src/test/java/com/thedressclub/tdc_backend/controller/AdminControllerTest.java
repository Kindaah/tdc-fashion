/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static com.thedressclub.tdc_backend.controller.AdminController.ADMIN_URL;
import static com.thedressclub.tdc_backend.controller.AdminController.ROLE_WRONG_REQUEST;
import static com.thedressclub.tdc_backend.controller.AdminController.TEAM_URL;
import static com.thedressclub.tdc_backend.controller.UserController.USER_ROLE_PARAM;
import static com.thedressclub.tdc_backend.controller.config.GenericController.NOT_CREATED_KEY;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thedressclub.tdc_backend.controller.config.RestException;
import com.thedressclub.tdc_backend.model.Role;
import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.RoleService;
import com.thedressclub.tdc_backend.service.UserService;
import com.thedressclub.tdc_backend.service.external.IAuthService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for {@link AdminController}.
 *
 * @author Daniel Mejia.
 */
public class AdminControllerTest
{
    private static final String ROLE_TEST = "role";
    private static final String EMAIL_TEST = "email";
    private static final String TEST_AUTH_KEY = "auth";

    private MockMvc mockMvc;

    @Mock
    private RoleService mockRoleService;

    @Mock
    private UserService mockUserService;

    @Mock
    private IAuthService mockAuthService;
    
    @InjectMocks
    private AdminController instance;

    @Mock
    private Role mockRole;

    private User user;

    @Before
    public void setUp()
    {
        user = new User();
        user.setCompanyEmail(EMAIL_TEST);
        instance = new AdminController();
        mockMvc = MockMvcBuilders.standaloneSetup(instance).build();
        MockitoAnnotations.initMocks(this);
        when(mockRoleService.findBy(ROLE_TEST.toUpperCase())).thenReturn(mockRole);
        when(mockAuthService.addUser(EMAIL_TEST)).thenReturn(TEST_AUTH_KEY);
        when(mockUserService.add(user)).thenReturn(user);
    }

    @Test
    public void addUserTeamSuccess()
    throws Exception
    {
        ResultActions result = getAddMemberRequest();

        result
            .andExpect(status().isOk());
    }

    @Test
    public void addUserTeamRoleWrong()
    {
        when(mockRoleService.findBy(ROLE_TEST.toUpperCase())).thenReturn(null);
        assertThatThrownBy(this::getAddMemberRequest)
            .hasCause(new RestException(ROLE_WRONG_REQUEST, BAD_REQUEST));
    }

    @Test
    public void addUserTeamAuth0Error()
    {
        when(mockAuthService.addUser(EMAIL_TEST)).thenReturn(null);
        assertThatThrownBy(this::getAddMemberRequest)
            .hasCause(new RestException(NOT_CREATED_KEY, BAD_REQUEST));
    }

    @Test
    public void addUserTeamBackendError()
    {
        when(mockUserService.add(user)).thenReturn(null);
        assertThatThrownBy(this::getAddMemberRequest)
            .hasCause(new RestException(NOT_CREATED_KEY, BAD_REQUEST));
    }

    private ResultActions getAddMemberRequest()
    throws Exception
    {
        return mockMvc
            .perform(post(ADMIN_URL + TEAM_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param(USER_ROLE_PARAM, ROLE_TEST)
                .content(new ObjectMapper().writeValueAsString(user)));
    }
}