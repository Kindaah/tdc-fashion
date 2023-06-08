/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.util;

import static java.lang.reflect.Modifier.isPrivate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.UserService;
import java.lang.reflect.Constructor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Test class for {@link UserUtils}.
 *
 * @author Edson Ruiz Ramirez.
 */
public class UserUtilsTest
{
    private static final String AUTH_KEY = "123545678";
    private static final String PRINCIPAL = "auth|" + AUTH_KEY;

    @Mock
    private User mockUser;

    @Mock
    private Authentication mockAuthentication;

    @Mock
    private SecurityContext mockSecurityContext;

    @Mock
    private UserService mockUserService;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        when(mockAuthentication.getPrincipal()).thenReturn(PRINCIPAL);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);
    }

    @After
    public void tearDown()
    {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testPrivateConstructor()
    throws NoSuchMethodException
    {
        Constructor<UserUtils> constructor = UserUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        boolean isPrivate = isPrivate(constructor.getModifiers());

        assertThat(isPrivate).isTrue();
        assertThatThrownBy(constructor::newInstance)
            .hasCauseInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void  testGetCurrentUserSuccess()
    {
        when(mockUserService.findByAuthKey(AUTH_KEY)).thenReturn(mockUser);

        assertThat(UserUtils.getCurrentUser(mockUserService))
            .as("The users are not the same")
            .isEqualTo(mockUser);
    }

    @Test
    public void testGetCurrentUserAnonymous()
    {
        when(mockAuthentication.getPrincipal()).thenReturn(AUTH_KEY);
        when(mockUserService.findByAuthKey(AUTH_KEY)).thenReturn(null);

        assertThat(UserUtils.getCurrentUser(mockUserService)).isNull();
    }

    @Test
    public void testGetCurrentUserAuthentication()
    {
        when(mockUserService.findByAuthKey(AUTH_KEY)).thenReturn(mockUser);

        assertThat(UserUtils.getCurrentUser(mockAuthentication, mockUserService))
            .as("The users are not the same")
            .isEqualTo(mockUser);
    }

    @Test
    public void testGetCurrentUserAuthenticationNull()
    {
        assertThat(UserUtils.getCurrentUser(null, mockUserService))
            .as("The user is not null")
            .isNull();
    }

    @Test
    public void testGetCurrentUserNull()
    {
        assertThat(UserUtils.getCurrentUser(mockAuthentication, mockUserService))
            .as("The user is not null")
            .isNull();
    }
}
