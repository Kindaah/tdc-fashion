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

import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.State;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Scheduled Services.
 *
 * @author Edson Ruiz Ramirez.
 */
@Service("scheduledService")
@Transactional
public class ScheduledService implements IScheduledService
{
    private static final Logger LOGGER = Logger.getLogger(ScheduledService.class.getName());

    private static final String TIMER_TASK_NAME = "Expire Order";
    private static final String TASK_SCHEDULED_KEY = "task_scheduled";
    private static final String ORDER_EXPIRED = "order_expired";

    @Autowired
    private OrderService orderService;

    @Autowired
    private StateService stateService;

    private int expirationDays;
    private Timer timer = new Timer(TIMER_TASK_NAME);
    private Calendar calendar;

    /**
     * Constructor.
     *
     * @param expirationDays the number of days to expire the order.
     */
    public ScheduledService(@Value("${expiration.time.days}") int expirationDays)
    {
        this.expirationDays = expirationDays;
        this.calendar = Calendar.getInstance();
    }

    /** (non-Javadoc) @see {@link IScheduledService} */
    @Override
    public void expireOrder(long idOrder)
    {
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                changeState(idOrder);
            }
        };

        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + expirationDays);
        Date time = calendar.getTime();

        Object[] params = { idOrder, time };
        LOGGER.log(Level.INFO, getMessage(TASK_SCHEDULED_KEY), params);

        timer.schedule(task, time.getTime());
    }

    /** (non-Javadoc) @see {@link IScheduledService} */
    @Override
    public void changeState(long idOrder)
    {
        Order order = orderService.findById(idOrder);

        if(order.getState().getId() == Order.PENDING_INITIAL_QUOTE_APPROVAL_STATE_ID)
        {
            LOGGER.log(Level.INFO, getMessage(ORDER_EXPIRED), idOrder);

            State nextState = stateService.findById(Order.ORDER_REJECTED_STATE_ID);
            order.setState(nextState);
            orderService.update(order);
        }
    }
}