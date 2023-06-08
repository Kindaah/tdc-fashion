/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.State;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 * Test class for {@link ScheduledService}.
 *
 * @author Edson Ruiz Ramirez.
 */
public class ScheduledServiceTest
{
    private static final Long DELAY_TEST = 1L;
    private static final Long ORDER_ID = 1L;

    @Mock
    private Calendar mockCalendar;

    @Mock
    private OrderService mockOrderService;

    @Mock
    private Order mockOrder;

    @Mock
    private Date mockDate;

    @Mock
    private State mockState;

    @Mock
    private StateService mockStateService;

    @Spy
    private Timer mockTimer;

    @InjectMocks
    @Spy
    private IScheduledService instance = new ScheduledService(0);

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExpireOrder()
    {
        when(mockCalendar.getTime()).thenReturn(mockDate);
        when(mockDate.getTime()).thenReturn(DELAY_TEST);
        doNothing().when(instance).changeState(anyLong());

        instance.expireOrder(ORDER_ID);
        verify(mockTimer).schedule(any(TimerTask.class), eq(DELAY_TEST));
    }

    @Test
    public void testChangeStateExpireOrder()
    {
        when(mockOrderService.findById(ORDER_ID)).thenReturn(mockOrder);
        when(mockOrder.getState()).thenReturn(mockState);
        when(mockState.getId()).thenReturn(Order.PENDING_INITIAL_QUOTE_APPROVAL_STATE_ID);
        when(mockStateService.findById(Order.ORDER_REJECTED_STATE_ID)).thenReturn(mockState);
        when(mockOrderService.update(mockOrder)).thenReturn(mockOrder);

        instance.changeState(ORDER_ID);
        verify(mockOrderService).update(mockOrder);
    }

    @Test
    public void testChangeStateNoExpireOrder()
    {
        when(mockOrderService.findById(ORDER_ID)).thenReturn(mockOrder);
        when(mockOrder.getState()).thenReturn(mockState);
        when(mockState.getId()).thenReturn(Order.DEFAULT_STATE_ID);
        when(mockOrderService.update(mockOrder)).thenReturn(mockOrder);

        instance.changeState(ORDER_ID);
        verify(mockOrderService, times(0)).update(mockOrder);
    }
}