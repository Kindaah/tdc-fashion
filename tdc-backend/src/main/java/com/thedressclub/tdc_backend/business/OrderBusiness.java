/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.business;

import static com.thedressclub.tdc_backend.dto.CountDTO.getCountDTO;
import static com.thedressclub.tdc_backend.model.Role.CUSTOMER_ROLE_NAME;

import com.thedressclub.tdc_backend.dto.CountDTO;
import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.State;
import com.thedressclub.tdc_backend.model.User;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;


/**
 * Contains the helpers and logic methods for {@link Order}.
 *
 * @author Daniel Mejia
 */
@Component
public class OrderBusiness
{
    /**
     * Gets a list of orders filtered by user.
     *
     * @param orders the orders list to filter.
     * @param user to filter the list.
     *
     * @return an orders list filtered by user.
     */
    public List<Order> filterOrdersByUser(Collection<Order> orders, User user)
    {
        return orders
            .stream()
            .filter(order -> hasFilterOrder(order, user))
            .collect(Collectors.toList());
    }

    /**
     * Get a list count dto of orders.
     *
     * @param orders the order to generate the list count dto.
     *
     * @return a count dto list of orders
     */
    public List<CountDTO> getListCountDTO(Collection<Order> orders)
    {
        return orders
            .stream()
            .map(CountDTO::getCountDTO)
            .collect(Collectors.toList());
    }
    /**
     * Get a list count dto of states.
     *
     * @param states the order to generate the list count dto.
     * @param user to filter the list.
     *
     * @return a count dto list of states.
     */
    public List<CountDTO> getListCountDTO(Collection<State> states, User user)
    {
        return states
            .stream()
            .map(state -> getCountDTO(state, filterOrdersByUser(state.getOrders(), user)))
            .collect(Collectors.toList());
    }

    private boolean hasFilterOrder(Order order, User user)
    {
        String currentUserRole = user.getRole().getName();
        User orderUser = order.getUser();

        return !CUSTOMER_ROLE_NAME.equals(currentUserRole) || orderUser.getId() == user.getId();
    }
}
