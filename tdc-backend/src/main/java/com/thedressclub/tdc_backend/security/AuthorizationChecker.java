/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.security;

import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.UserService;
import com.thedressclub.tdc_backend.util.UserUtils;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Contains the implementation to check the access for an user.
 *
 * @author Edson Ruiz Ramirez
 */
@Component
public class AuthorizationChecker
{
    @Autowired
    private UserService userService;

    /**
     * Check the access for an user.
     *
     * @param authentication The user authentication.
     * @param rolesAccess The allowed roles.
     * @return true If the user has access.
     */
    public boolean checkAccess(Authentication authentication, String... rolesAccess)
    {
        boolean hasAccess = false;
        User currentUser = UserUtils.getCurrentUser(authentication, userService);

        if (currentUser != null)
        {
            String userRole = currentUser.getRole().getName();
            hasAccess = Arrays.asList(rolesAccess).contains(userRole);
        }

        return hasAccess;
    }
}
