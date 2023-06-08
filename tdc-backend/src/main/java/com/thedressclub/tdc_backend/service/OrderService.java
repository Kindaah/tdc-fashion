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
import static java.util.Collections.singletonMap;

import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.User;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

/**
 * Service class for Order.
 *
 * @author Edson Ruiz Ramirez.
 */
@Service("orderService")
@Transactional
public class OrderService extends GenericService<Order>
{
    static final String CREATION_USER_KEY = "user.id";
    static final String STATE_ORDER_KEY = "state.filter";

    /**
     * Filter the order by role.
     *
     * @param user The user to filter the orders.
     *
     * @return The orders list filtered by role.
     */
    public List<Order> filterByRole(@NotNull User user)
    {
        String userRoleName = user.getRole().getName();
        Map<String, Object> filter =
            CUSTOMER_ROLE_NAME.equals(userRoleName) ?
                singletonMap(CREATION_USER_KEY, user.getId()) :
                singletonMap(STATE_ORDER_KEY, asList(userRoleName, FILTER_ALL));

        return filterBy(filter);
    }
}
