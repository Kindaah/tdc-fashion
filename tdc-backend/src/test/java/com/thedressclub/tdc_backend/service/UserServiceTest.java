/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import static com.thedressclub.tdc_backend.service.UserService.AUTH_KEY_FILTER;
import static com.thedressclub.tdc_backend.service.UserService.ROLE_NAME_FILTER_KEY;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.thedressclub.tdc_backend.model.User;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Test class for {@link UserService}.
 *
 * @author Edson Ruiz Ramirez.
 */
public class UserServiceTest extends GenericServiceTest<User>
{
    private static final String AUTH_VALUE = "value";

    @InjectMocks
    private UserService instance;

    @Test
    public void testConstructor()
    throws NoSuchMethodException
    {
        Constructor<UserService> constructor = UserService.class.getDeclaredConstructor();
        boolean isPublic = Modifier.isPublic(constructor.getModifiers());
        UserService userService = new UserService();

        assertThat(isPublic)
            .as("The constructor is not public: ")
            .isTrue();
        assertThat(userService.getGenericDAO())
            .as("The userDao is not null")
            .isNull();
    }

    @Test
    public void testFindByAuthKey()
    {
        Map<String, Object> filterMap = singletonMap(AUTH_KEY_FILTER, AUTH_VALUE);
        List<User> users = singletonList(new User());
        when(mockGenericDAO.list(filterMap)).thenReturn(users);

        User user = instance.findByAuthKey(AUTH_VALUE);

        assertThat(user)
            .as("User retrieved doesn't match: ")
            .isEqualTo(users.get(0));
    }

    @Test
    public void testFindByAuthKeyEmpty()
    {
        Map<String, Object> filterMap = singletonMap(AUTH_KEY_FILTER, AUTH_VALUE);
        List<User> users = new ArrayList<>();
        when(mockGenericDAO.list(filterMap)).thenReturn(users);

        User user = instance.findByAuthKey(AUTH_VALUE);

        assertThat(user)
            .as("User data should be empty: ")
            .isNull();
    }

    @Test
    public void testFilterByRoles()
    {
        List<User> users = singletonList(new User());
        when(mockGenericDAO.list(singletonMap(ROLE_NAME_FILTER_KEY, singletonList(AUTH_VALUE.toUpperCase()))))
            .thenReturn(users);

        List<User> response = instance.filterBy(singletonList(AUTH_VALUE));

        assertThat(response)
            .as("Users filtered by roles is wrong")
            .isEqualTo(users);
    }

    @Test
    public void testFilterByRolesEmpty()
    {
        when(mockGenericDAO.list(emptyMap())).thenReturn(new ArrayList<>());

        List<User> response = instance.filterBy(emptyList());

        assertThat(response)
            .as("Users filtered is not empty")
            .isEmpty();
    }
}