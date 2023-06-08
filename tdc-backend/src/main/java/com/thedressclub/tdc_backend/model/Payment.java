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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * Model object for the payment table.
 *
 * @author Edson Ruiz Ramirez.
 */
@Entity
@Table(name = "payment")
@SequenceGenerator(name = GenericModel.SEQUENCE, sequenceName = "payment_id_seq",
    allocationSize = 1)
public class Payment extends GenericModel
{
    private static final long serialVersionUID = 4L;
    public static final String FAILED_STATE = "failed";
    public static final String SUCCEEDED_STATE = "succeeded";

    @NotBlank
    @Column(name = "status")
    private String status;

    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "amount")
    private double amount;

    @ManyToOne(targetEntity = PaymentType.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_payment_type")
    private PaymentType paymentType;

    @ManyToOne(targetEntity = Order.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order")
    @JsonIgnore
    private Order order;

    /**
     * Constructor.
     */
    public Payment()
    {
        super();
    }

    /**
     * Gets the payment key.
     *
     * @return The payment key.
     */
    public String getPaymentKey()
    {
        return paymentKey;
    }

    /**
     * Sets the payment key.
     *
     * @param paymentKey The payment key.
     */
    public void setPaymentKey(String paymentKey)
    {
        this.paymentKey = paymentKey;
    }

    /**
     * Gets the status.
     *
     * @return Value of status.
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status The status.
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * Gets the paymentType object.
     *
     * @return Value of paymentType object.
     */
    public PaymentType getPaymentType()
    {
        return paymentType;
    }

    /**
     * Sets the paymentType object.
     *
     * @param paymentType The paymentType object.
     */
    public void setPaymentType(PaymentType paymentType)
    {
        this.paymentType = paymentType;
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
     * Gets amount.
     *
     * @return Value of amount.
     */
    public double getAmount()
    {
        return amount;
    }

    /**
     * Sets new amount.
     *
     * @param amount New value of amount.
     */
    public void setAmount(double amount)
    {
        this.amount = amount;
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
