/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.util;

import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.UserService;
import java.util.regex.Pattern;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utils for the user process.
 *
 * @author Daniel Mejia.
 */
public final class UserUtils
{
    private static final String SPLIT_CRITERIA = Pattern.quote("|");

    /**
     * Constructor
     */
    private UserUtils()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Split the user authentication to get the authKey.
     *
     * @param authentication The user authentication.
     * @return The authKey.
     */
    private static String splitPrincipal(Authentication authentication)
    {
        String authKey = "";
        if (authentication != null)
        {
            String principal = authentication.getPrincipal().toString();
            String[] splitPrincipal = principal.split(SPLIT_CRITERIA);
            authKey = splitPrincipal.length == 2 ? splitPrincipal[1] : authKey;
        }

        return authKey;
    }

    /**
     * Gets an Object by {@link User}.
     *
     * @param authentication the current user authentication.
     * @param service The user service.
     * @return the current user in the application.
     */
    public static User getCurrentUser(Authentication authentication, UserService service)
    {
        String authKey = splitPrincipal(authentication);
        return service.findByAuthKey(authKey);
    }

    /**
     * Gets an Object by {@link User}.
     *
     * @param service The user service.
     * @return the current user in the application.
     */
    public static User getCurrentUser(UserService service)
    {
        Authentication currentAuthenticationUser =
            SecurityContextHolder.getContext().getAuthentication();

        String authKey = splitPrincipal(currentAuthenticationUser);
        return service.findByAuthKey(authKey);
    }
}
