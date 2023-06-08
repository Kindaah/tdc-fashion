/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service.external;

import static com.thedressclub.tdc_backend.util.Utils.getMessage;
import static java.lang.String.format;
import static org.apache.commons.lang3.RandomStringUtils.random;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.AuthRequest;
import com.auth0.net.Request;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Services for the Ath0 Service.
 *
 * @author Edson Ruiz Ramirez.
 */
@Service("authService")
@Transactional
public class AuthService implements IAuthService
{
    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());
    private static final String PASS_CHANGE_KEY = "password_change";
    private static final String PASS_CHANGE_ERROR_KEY = "password_change_error";
    private static final String AUTH_KEY_SPLIT_REGEX = "\\|";
    private static final int PASSWORD_LENGTH = 6;
    static final String USER_ID_FORMAT = "auth0|%s";
    static final String REQUEST_FORMAT = "https://%s/api/v2/";

    private ManagementAPI managementAPI;
    private AuthAPI authAPI;
    private String domain;
    private String connection;

    /**
     * Constructor.
     *
     * @param domain the auth0 domain.
     */
    public AuthService(
        @Value("${auth0.management.domain}") String domain,
        @Value("${auth0.management.client.id}") String clientId,
        @Value("${auth0.management.client.secret}") String clientSecret,
        @Value("${auth0.management.connection}") String connection)
    {
        managementAPI = new ManagementAPI(domain, "");
        authAPI = new AuthAPI(domain, clientId, clientSecret);
        this.domain = domain;
        this.connection = connection;
    }

    /** (non-Javadoc) @see {@link IAuthService} */
    @Override
    public String addUser(String memberEmail)
    {
        try
        {
            LOGGER.log(Level.INFO, getMessage(PASS_CHANGE_KEY));

            AuthRequest authRequest = authAPI.requestToken(format(REQUEST_FORMAT, domain));
            TokenHolder holder = authRequest.execute();
            managementAPI.setApiToken(holder.getAccessToken());

            User data = new User();
            data.setEmail(memberEmail);
            data.setName(memberEmail);
            data.setConnection(connection);
            data.setPassword(random(PASSWORD_LENGTH));
            data.setEmailVerified(true);
            User memberCreated = managementAPI.users().create(data).execute();
            authAPI.resetPassword(memberEmail, connection).execute();

            return memberCreated.getId().split(AUTH_KEY_SPLIT_REGEX)[1];

        } catch (Auth0Exception e)
        {
            LOGGER.log(Level.SEVERE, getMessage(PASS_CHANGE_ERROR_KEY), e.getMessage());

            return null;
        }
    }

    /** (non-Javadoc) @see {@link IAuthService} */
    @Override
    public boolean changePassword(String authKey, String newPassword)
    {
        try
        {
            LOGGER.log(Level.INFO, getMessage(PASS_CHANGE_KEY));

            AuthRequest authRequest = authAPI.requestToken(format(REQUEST_FORMAT, domain));
            TokenHolder holder = authRequest.execute();
            managementAPI.setApiToken(holder.getAccessToken());

            User data = new User();
            data.setPassword(newPassword);
            String userId = format(USER_ID_FORMAT, authKey);
            Request<User> request = managementAPI.users().update(userId, data);
            request.execute();

            return true;

        } catch (Auth0Exception e)
        {
            LOGGER.log(Level.SEVERE, getMessage(PASS_CHANGE_ERROR_KEY), e.getMessage());

            return false;
        }
    }


    /** (non-Javadoc) @see {@link IAuthService} */
    @Override
    public void setAuthAPI(AuthAPI authAPI)
    {
        this.authAPI = authAPI;
    }

    /** (non-Javadoc) @see {@link IAuthService} */
    @Override
    public void setManagementAPI(ManagementAPI managementAPI)
    {
        this.managementAPI = managementAPI;
    }
}
