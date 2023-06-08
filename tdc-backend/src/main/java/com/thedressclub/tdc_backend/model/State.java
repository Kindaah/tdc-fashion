/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openpojo.business.BusinessIdentity;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * Model object for state table.
 *
 * @author Edson Ruiz Ramirez.
 */
@Entity
@Table(name = "state")
@SequenceGenerator(name = GenericModel.SEQUENCE, sequenceName = "state_id_seq", allocationSize = 1)
public class State extends GenericModel
{
    private static final long serialVersionUID = 6L;
    public static final String FILTER_ALL = "ALL";

    @NotBlank
    @Column(name = "name")
    private String name;

    @Column(name = "filter")
    private String filter;

    @Column(name = "sequence")
    private long sequenceState;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Order> orders;

    /**
     * Constructor.
     */
    public State()
    {
        super();
    }

    /**
     * Gets the name.
     *
     * @return Value of name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name The name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the filter.
     *
     * @return Value of filter.
     */
    public String getFilter()
    {
        return filter;
    }

    /**
     * Sets the filter.
     *
     * @param filter The filter.
     */
    public void setFilter(String filter)
    {
        this.filter = filter;
    }

    /**
     * Gets the sequence of the state.
     *
     * @return Value of sequence.
     */
    public long getSequenceState()
    {
        return sequenceState;
    }

    /**
     * Sets the sequence of the state.
     *
     * @param sequenceState The sequence of the state.
     */
    public void setSequenceState(long sequenceState)
    {
        this.sequenceState = sequenceState;
    }

    /**
     * Gets the orders.
     *
     * @return The orders.
     */
    public Set<Order> getOrders()
    {
        return orders;
    }

    /**
     * Sets the orders.
     *
     * @param orders the value of orders.
     */
    public void setOrders(Set<Order> orders)
    {
        this.orders = orders;
    }

    /** (non-Javadoc) @see {@link Object} */
    @Override
    public int hashCode()
    {
        return BusinessIdentity.getHashCode(this);
    }

    /** (non-Javadoc) @see {@link Object} */
    @Override
    public String toString()
    {
        return BusinessIdentity.toString(this);
    }

    /** (non-Javadoc) @see {@link Object} */
    @Override
    public boolean equals(final Object obj)
    {
        return BusinessIdentity.areEqual(this, obj);
    }
}
