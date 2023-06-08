/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import static java.util.Collections.singletonMap;

import com.thedressclub.tdc_backend.model.Role;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * Contains the service methods for {@link Role}.
 *
 * @author Edson Ruiz Ramirez.
 */
@Service("roleService")
@Transactional
public class RoleService extends GenericService<Role>
{
    static final String ROLE_NAME_KEY = "name";

    /**
     * Find an role by the role name.
     *
     * @param roleName The role name.
     *
     * @return The role founded.
     */
    public Role findBy(String roleName)
    {
        Map<String, Object> filterMap = singletonMap(ROLE_NAME_KEY, roleName);
        List<Role> rolesFiltered = filterBy(filterMap);

        return rolesFiltered.isEmpty() ? null : rolesFiltered.get(0);
    }
}