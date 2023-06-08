/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

/**
 * Interface for the Scheduled Service.
 *
 * @author Edson Ruiz Ramirez.
 */
public interface IScheduledService
{
    /**
     * Expires the order if the initial quote was placed 48 hours ago.
     *
     * @param idOrder the order id.
     */
    void expireOrder(long idOrder);

    /**
     * Changes the state of the order.
     *
     * @param idOrder the order id.
     */
    void changeState(long idOrder);
}