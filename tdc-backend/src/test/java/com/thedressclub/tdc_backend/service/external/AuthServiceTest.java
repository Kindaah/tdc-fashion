/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service.external;

import static com.thedressclub.tdc_backend.service.external.AuthService.REQUEST_FORMAT;
import static com.thedressclub.tdc_backend.service.external.AuthService.USER_ID_FORMAT;
import static java.lang.String.format;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.UsersEntity;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.AuthRequest;
import com.auth0.net.Request;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for {@link IAuthService}.
 *
 * @author Daniel Mejia.
 */
public class AuthServiceTest
{
    private static final String TEST_DOMAIN = "test.tdc.fashion";
    private static final String TEST_CLIENT_ID = "client id";
    private static final String TEST_CLIENT_SECRET = "secret id";
    private static final String TEST_AUTH_KEY = "4043";
    private static final String TEST_NEW_PASSWORD = "pass";
    private static final String AUTH_EXCEPTION_MESSAGE = "an error";
    private static final String TEST_ACCESS_TOKEN = "token";
    private static final String TEST_CONNECTION = "oonnection";
    private static final String TEST_EMAIL = "email";
    private static final String AUTH_KEY_COMPLEX = "auth|" + TEST_AUTH_KEY;

    @Mock
    private ManagementAPI mockManagementAPI;

    @Mock
    private AuthAPI mockAuthAPI;

    @Mock
    private AuthRequest mockAuthRequest;

    @Mock
    private TokenHolder mockTokenHolder;

    @Mock
    private UsersEntity mockUserEntity;

    @Mock
    private Request<User> mockUserRequest;

    @Mock
    private User mockUser;

    @Mock
    private Request mockAPIRequest;

    @InjectMocks
    private IAuthService instance;

    @Before
    public void setUp()
    throws Auth0Exception
    {
        instance = new AuthService(TEST_DOMAIN, TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_CONNECTION);
        MockitoAnnotations.initMocks(this);
        instance.setAuthAPI(mockAuthAPI);
        instance.setManagementAPI(mockManagementAPI);
        when(mockAuthAPI.requestToken(format(REQUEST_FORMAT, TEST_DOMAIN))).thenReturn(mockAuthRequest);
        when(mockAuthRequest.execute()).thenReturn(mockTokenHolder);
        when(mockTokenHolder.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mockManagementAPI.users()).thenReturn(mockUserEntity);
    }

    @Test
    public void testAddUserSuccess()
    throws Auth0Exception
    {
        when(mockAuthAPI.resetPassword(TEST_EMAIL, TEST_CONNECTION)).thenReturn(mockAPIRequest);
        when(mockUserEntity.create(any(User.class)))
            .thenReturn(mockUserRequest);
        when(mockUserRequest.execute()).thenReturn(mockUser);
        when(mockUser.getId()).thenReturn(AUTH_KEY_COMPLEX);

        String response = instance.addUser(TEST_EMAIL);

        assertThat(response).isEqualTo(TEST_AUTH_KEY);
        verify(mockAPIRequest).execute();
    }

    @Test
    public void testAddUserFail()
    throws Auth0Exception
    {
        when(mockAuthRequest.execute())
            .thenThrow(new Auth0Exception(AUTH_EXCEPTION_MESSAGE));

        String response = instance.addUser(TEST_EMAIL);

        assertThat(response).isNull();
        verify(mockAuthAPI, times(0)).resetPassword(TEST_EMAIL, TEST_CONNECTION);
    }

    @Test
    public void testChangePasswordSuccess()
    throws Auth0Exception
    {
        when(mockUserEntity.update(eq(format(USER_ID_FORMAT, TEST_AUTH_KEY)), any(User.class)))
            .thenReturn(mockUserRequest);

        boolean response = instance.changePassword(TEST_AUTH_KEY, TEST_NEW_PASSWORD);

        assertThat(response).isTrue();
        verify(mockUserRequest).execute();
    }

    @Test
    public void testChangePasswordFail()
    throws Auth0Exception
    {
        when(mockAuthRequest.execute())
            .thenThrow(new Auth0Exception(AUTH_EXCEPTION_MESSAGE));

        boolean response = instance.changePassword(TEST_AUTH_KEY, TEST_NEW_PASSWORD);

        assertThat(response).isFalse();
        verify(mockUserRequest, times(0)).execute();
    }
}