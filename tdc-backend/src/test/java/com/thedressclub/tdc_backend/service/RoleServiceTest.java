/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import static com.thedressclub.tdc_backend.service.RoleService.ROLE_NAME_KEY;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.thedressclub.tdc_backend.model.Role;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * Test class for {@link RoleService}.
 *
 * @author Edson Ruiz Ramirez.
 */
public class RoleServiceTest extends GenericServiceTest<Role>
{
    private static final String ROLE_TEST = "ROLE_TEST";

    @InjectMocks
    private RoleService instance;

    @Test
    public void testConstructor()
    throws NoSuchMethodException
    {
        Constructor<RoleService> constructor = RoleService.class.getDeclaredConstructor();
        boolean isPublic = Modifier.isPublic(constructor.getModifiers());
        RoleService roleService = new RoleService();

        assertThat(isPublic).as("The constructor is not public: ").isTrue();
        assertThat(roleService.getGenericDAO()).as("The roleDao is not null").isNull();
    }

    @Test
    public void testFindBy()
    {
        Map<String, Object> filterMap = singletonMap(ROLE_NAME_KEY, ROLE_TEST);
        List<Role> roles = singletonList(new Role());
        when(mockGenericDAO.list(filterMap)).thenReturn(roles);

        Role role = instance.findBy(ROLE_TEST);

        assertThat(role)
            .as("Role retrieved doesn't match: ")
            .isEqualTo(roles.get(0));
    }

    @Test
    public void testFindByNotFound()
    {
        Map<String, Object> filterMap = singletonMap(ROLE_NAME_KEY, ROLE_TEST);
        when(mockGenericDAO.list(filterMap)).thenReturn(emptyList());

        Role role = instance.findBy(ROLE_TEST);

        assertThat(role).isNull();
    }
}