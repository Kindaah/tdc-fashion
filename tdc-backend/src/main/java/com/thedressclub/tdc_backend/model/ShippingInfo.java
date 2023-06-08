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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * Model object for the shipping info table.
 *
 * @author Edson Ruiz Ramirez.
 */
@Entity
@Table(name = "shipping_info")
@SequenceGenerator(name = GenericModel.SEQUENCE, sequenceName = "shipping_info_id_seq",
    allocationSize = 1)
public class ShippingInfo extends GenericModel
{
    private static final long serialVersionUID = 6L;

    @NotBlank
    @Column(name = "company")
    private String company;

    @NotBlank
    @Column(name = "tracking_id")
    private String trackingId;

    @NotBlank
    @Column(name = "details")
    private String details;

    @OneToOne(mappedBy = "shippingInfo", fetch = FetchType.LAZY)
    @JsonIgnore
    private Order order;

    /**
     * Constructor.
     */
    public ShippingInfo()
    {
        super();
    }

    /**
     * Gets the order.
     *
     * @return Value of order.
     */
    public Order getOrder()
    {
        return order;
    }

    /**
     * Sets the order.
     *
     * @param order The order.
     */
    public void setOrder(Order order)
    {
        this.order = order;
    }

    /**
     * Gets the company.
     *
     * @return Value of company.
     */
    public String getCompany()
    {
        return company;
    }

    /**
     * Sets the company.
     *
     * @param company The company.
     */
    public void setCompany(String company)
    {
        this.company = company;
    }

    /**
     * Gets the tracking id.
     *
     * @return Value of tracking id.
     */
    public String getTrackingId()
    {
        return trackingId;
    }

    /**
     * Sets the tracking id.
     *
     * @param trackingId The tracking id.
     */
    public void setTrackingId(String trackingId)
    {
        this.trackingId = trackingId;
    }

    /**
     * Gets the details.
     *
     * @return Value of details.
     */
    public String getDetails()
    {
        return details;
    }

    /**
     * Sets the details.
     *
     * @param details The details.
     */
    public void setDetails(String details)
    {
        this.details = details;
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
