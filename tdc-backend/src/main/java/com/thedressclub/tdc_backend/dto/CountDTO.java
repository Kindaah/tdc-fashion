/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.dto;

import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.State;
import java.util.List;

/**
 * DTO object for response.
 *
 * @author Daniel Mejia.
 */
public class CountDTO
{
    private Object entity;
    private int count;

    /**
     * Gets the CountDTO for an order.
     *
     * @param order the order to map in count DTO.
     *
     * @return the countDTO
     */
    public static CountDTO getCountDTO(Order order)
    {
        CountDTO countDTO = new CountDTO();
        countDTO.setCount(order.getProducts().size());
        countDTO.setEntity(order);
        order.setProducts(null);

        return countDTO;
    }

    /**
     * Gets the CountDTO for an state.
     *
     * @param state the state to map in count DTO.
     *
     * @return the countDTO
     */
    public static CountDTO getCountDTO(State state, List<Order> orders)
    {
        CountDTO countDTO = new CountDTO();
        countDTO.setCount(orders.size());
        countDTO.setEntity(state);

        return countDTO;
    }

    /**
     * Gets the entity.
     *
     * @return the entity.
     */
    public Object getEntity()
    {
        return entity;
    }

    /**
     * Sets the entity.
     *
     * @param entity to be set.
     */
    public void setEntity(Object entity)
    {
        this.entity = entity;
    }

    /**
     * Gets the count.
     *
     * @return the count.
     */
    public int getCount()
    {
        return count;
    }

    /**
     * Sets the count.
     *
     * @param count to be set.
     */
    public void setCount(int count)
    {
        this.count = count;
    }
}
