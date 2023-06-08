/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import static com.thedressclub.tdc_backend.util.Utils.getMessage;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toList;

import com.thedressclub.tdc_backend.model.User;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.bytecode.stackmap.TypeData;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

/**
 * Contains the service methods for {@link User}.
 *
 * @author Edson Ruiz Ramirez.
 */
@Service("userService")
@Transactional
public class UserService extends GenericService<User>
{
    private static final Logger LOGGER = Logger.getLogger(TypeData.ClassName.class.getName());
    private static final String METHOD_FIND_BY_AUTH_KEY = "service_find_by_auth_key";
    private static final String FILTERS_BY_ROLES_KEY = "user_service_find_by_roles";
    static final String ROLE_NAME_FILTER_KEY = "role.name";
    static final String AUTH_KEY_FILTER = "authKey";

    /**
     * Find an user by the authKey.
     *
     * @param authKey The user authKey.
     *
     * @return The user found.
     */
    public User findByAuthKey(String authKey)
    {
        LOGGER.log(Level.INFO, getMessage(METHOD_FIND_BY_AUTH_KEY));

        Map<String, Object> filterMap = singletonMap(AUTH_KEY_FILTER, authKey);
        List<User> usersFiltered = filterBy(filterMap);

        return usersFiltered.isEmpty() ? null : usersFiltered.get(0);
    }

    /**
     * Filter a users by role.
     *
     * @param roles The list of roles to filter the users.
     *
     * @return The users list filtered by roles.
     */
    public List<User> filterBy(@NotNull List<String> roles)
    {
        LOGGER.log(Level.INFO, getMessage(FILTERS_BY_ROLES_KEY), roles);

        List<String> rolesUpperCase =
            roles
                .stream()
                .map(String::toUpperCase)
                .collect(toList());

        Map<String, Object> filters =
            roles.isEmpty() ? emptyMap() : singletonMap(ROLE_NAME_FILTER_KEY, rolesUpperCase);

        return filterBy(filters);
    }
}