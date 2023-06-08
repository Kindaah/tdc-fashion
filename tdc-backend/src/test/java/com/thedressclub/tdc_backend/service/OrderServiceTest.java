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
import static com.thedressclub.tdc_backend.service.OrderService.CREATION_USER_KEY;
import static com.thedressclub.tdc_backend.service.OrderService.STATE_ORDER_KEY;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.Role;
import com.thedressclub.tdc_backend.model.User;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * Test class for {@link OrderService}.
 *
 * @author Daniel Mejia.
 */
public class OrderServiceTest extends GenericServiceTest<Order>
{
    private static final Long OBJECT_ID = 1L;
    private static final String ANOTHER_ROLE = "other role";

    @Mock
    private User mockUser;

    @Mock
    private Role mockRole;

    @InjectMocks
    private OrderService instance;

    @Test
    public void testConstructor()
    throws NoSuchMethodException
    {
        Constructor<OrderService> constructor = OrderService.class.getDeclaredConstructor();
        boolean isPublic = Modifier.isPublic(constructor.getModifiers());
        OrderService orderService = new OrderService();

        assertThat(isPublic).isTrue();
        assertThat(orderService.getGenericDAO()).isNull();
    }

    @Test
    public void testFilterByRoleCustomer()
    {
        List<Order> orders = singletonList(new Order());
        when(mockUser.getRole()).thenReturn(mockRole);
        when(mockUser.getId()).thenReturn(OBJECT_ID);
        when(mockRole.getName()).thenReturn(CUSTOMER_ROLE_NAME);
        when(mockGenericDAO.list(singletonMap(CREATION_USER_KEY, OBJECT_ID))).thenReturn(orders);

        List<Order> response = instance.filterByRole(mockUser);

        assertThat(response)
            .as("Orders filtered by customer role is wrong")
            .isEqualTo(orders);
    }

    @Test
    public void testFilterByAnotherRole()
    {
        List<Order> orders = singletonList(new Order());
        when(mockUser.getRole()).thenReturn(mockRole);
        when(mockUser.getId()).thenReturn(OBJECT_ID);
        when(mockRole.getName()).thenReturn(ANOTHER_ROLE);
        when(mockGenericDAO.list(singletonMap(STATE_ORDER_KEY, asList(ANOTHER_ROLE, FILTER_ALL))))
            .thenReturn(orders);

        List<Order> response = instance.filterByRole(mockUser);

        assertThat(response)
            .as("Orders filtered by another role is wrong")
            .isEqualTo(orders);
    }
}