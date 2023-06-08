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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * Model object for the payment type table.
 *
 * @author Edson Ruiz Ramirez.
 */
@Entity
@Table(name = "payment_type")
@SequenceGenerator(name = GenericModel.SEQUENCE, sequenceName = "payment_type_id_seq",
    allocationSize = 1)
public class PaymentType extends GenericModel
{
    private static final long serialVersionUID = 5L;

    public static final long SAMPLES_ID = 1L;
    public static final long FIRST_PAYMENT_ID = 2L;
    public static final long LAST_PAYMENT_ID = 3L;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "paymentType", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Payment> payments;

    /**
     * Constructor.
     */
    public PaymentType()
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
     * Gets the description.
     *
     * @return Value of description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description The description.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Gets the payments.
     *
     * @return Value of payments.
     */
    public Set<Payment> getPayments()
    {
        return payments;
    }

    /**
     * Sets the payments.
     *
     * @param payments The payments.
     */
    public void setPayments(Set<Payment> payments)
    {
        this.payments = payments;
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
