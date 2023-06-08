/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.security;

import static org.mockito.Mockito.when;

import com.thedressclub.tdc_backend.model.Role;
import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

/**
 * Test class for {@link AuthorizationChecker}.
 *
 * @author Edson Ruiz Ramirez.
 */
public class AuthorizationCheckerTest
{
    private static final String ROLE_CUSTOMER = "CUSTOMER";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String PRINCIPAL = "auth|123545678";
    private static final String PRINCIPAL_SPLIT = "123545678";

    @Mock
    private User mockUser;

    @Mock
    private Role mockRole;

    @Mock
    private UserService mockUserService;

    @Mock
    private Authentication mockAuthentication;

    @InjectMocks
    private AuthorizationChecker authorizationChecker;

    @Before
    public void setup()
    {
        authorizationChecker = new AuthorizationChecker();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckAccessSuccess()
    throws Exception
    {
        when(mockAuthentication.getPrincipal()).thenReturn(PRINCIPAL);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockUser.getRole()).thenReturn(mockRole);
        when(mockRole.getName()).thenReturn(ROLE_CUSTOMER);
        when(mockUserService.findByAuthKey(PRINCIPAL_SPLIT)).thenReturn(mockUser);
        Assert.assertTrue("Check access success wrong",
            authorizationChecker.checkAccess(mockAuthentication, ROLE_CUSTOMER));
    }

    @Test
    public void testCheckAccessFail()
    throws Exception
    {
        when(mockAuthentication.getPrincipal()).thenReturn(PRINCIPAL);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockUser.getRole()).thenReturn(mockRole);
        when(mockRole.getName()).thenReturn(ROLE_CUSTOMER);
        when(mockUserService.findByAuthKey(PRINCIPAL_SPLIT)).thenReturn(mockUser);
        Assert.assertFalse("Check access success",
            authorizationChecker.checkAccess(mockAuthentication, ROLE_ADMIN));
    }

    @Test
    public void testCheckAccessNoUser()
    throws Exception
    {
        when(mockAuthentication.getPrincipal()).thenReturn(PRINCIPAL);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockUserService.findByAuthKey(PRINCIPAL_SPLIT)).thenReturn(null);
        Assert.assertFalse("Check access success",
            authorizationChecker.checkAccess(mockAuthentication));
    }
}