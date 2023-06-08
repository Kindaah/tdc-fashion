/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.business;

import static com.thedressclub.tdc_backend.model.Role.CUSTOMER_ROLE_NAME;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.thedressclub.tdc_backend.dto.CountDTO;
import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.Role;
import com.thedressclub.tdc_backend.model.State;
import com.thedressclub.tdc_backend.model.User;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link OrderBusiness}.
 *
 * @author Daniel Mejia.
 */
public class OrderBusinessTest
{
    private static final String TEST_ROLE = "ROLE";
    private static final Long OBJECT_ID = 1L;

    @Mock
    private User mockUser;

    @Mock
    private User mockAnotherUser;

    @Mock
    private Order mockOrder;

    @Mock
    private Role mockRole;

    @Mock
    private State mockState;

    @Mock
    private Order mockAnotherOrder;

    private OrderBusiness instance;

    @Before
    public void setUp()
    {
        instance = new OrderBusiness();
        MockitoAnnotations.initMocks(this);
        when(mockUser.getId()).thenReturn(OBJECT_ID);
        when(mockUser.getRole()).thenReturn(mockRole);
        when(mockUser.getId()).thenReturn(2L);
        when(mockAnotherUser.getRole()).thenReturn(mockRole);
        when(mockRole.getName()).thenReturn(TEST_ROLE);
        when(mockOrder.getUser()).thenReturn(mockUser);
        when(mockAnotherOrder.getUser()).thenReturn(mockAnotherUser);
    }

    @Test
    public void filterOrdersByUserNotCustomer()
    {
        List<Order> orders = getListOrders();
        List<Order> responseList = instance.filterOrdersByUser(orders, mockUser);
        assertEquals("The list size is wrong", orders.size(), responseList.size());
    }

    @Test
    public void testFilterOrdersByUserCustomer()
    {
        when(mockRole.getName()).thenReturn(CUSTOMER_ROLE_NAME);

        List<Order> orders = getListOrders();
        List<Order> responseList = instance.filterOrdersByUser(orders, mockUser);
        assertEquals("The list size is wrong", 1, responseList.size());
    }

    @Test
    public void getListCountDTOOrders()
    {
        when(mockOrder.getProducts()).thenReturn(Collections.emptySet());

        List<Order> orders = Collections.singletonList(mockOrder);
        List<CountDTO> responseList = instance.getListCountDTO(orders);

        assertEquals("The list size is wrong: ", 1, responseList.size());
        assertEquals("The countDTO entity is wrong: ", mockOrder, responseList.get(0).getEntity());
        assertEquals("The countDTO number is wrong: ", 0, responseList.get(0).getCount());
    }

    @Test
    public void getListCountDTOStates()
    {
        Set<Order> orders = Collections.singleton(mockOrder);
        when(mockState.getOrders()).thenReturn(orders);

        List<State> states = Collections.singletonList(mockState);
        List<CountDTO> responseList = instance.getListCountDTO(states, mockUser);

        assertEquals("The list size is wrong: ", 1, responseList.size());
        assertEquals("The countDTO entity is wrong: ", mockState, responseList.get(0).getEntity());
        assertEquals("The countDTO number is wrong: ", 1, responseList.get(0).getCount());
    }

    private List<Order> getListOrders()
    {
        return Arrays.asList(mockOrder, mockAnotherOrder);
    }
}