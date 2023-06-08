/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import static com.thedressclub.tdc_backend.model.Role.CUSTOMER_ROLE_NAME;
import static com.thedressclub.tdc_backend.model.State.FILTER_ALL;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

import com.thedressclub.tdc_backend.model.State;
import com.thedressclub.tdc_backend.model.User;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

/**
 * Contains the service methods for {@link State}.
 *
 * @author Edson Ruiz Ramirez.
 */
@Service("stateService")
@Transactional
public class StateService extends GenericService<State>
{
    static final String STATE_FILTER_KEY = "filter";

    /**
     * Filter the states by role.
     *
     * @param user The user to filter the states.
     *
     * @return The states list filtered by role.
     */
    public List<State> filterByRole(@NotNull User user)
    {
        String userRoleName = user.getRole().getName();
        Map<String, Object> filter =
            CUSTOMER_ROLE_NAME.equals(userRoleName) ?
                emptyMap() :
                singletonMap(STATE_FILTER_KEY, asList(userRoleName, FILTER_ALL));

        return filterBy(filter);
    }
}