/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openpojo.business.BusinessIdentity;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Model object for Order table.
 *
 * @author Edson Ruiz Ramirez.
 */
@Entity
@Table(name = "order_tdc")
@SequenceGenerator(name = GenericModel.SEQUENCE, sequenceName = "order_id_seq", allocationSize = 1)
public class Order extends GenericModel
{
    private static final long serialVersionUID = 2L;

    public static final long DEFAULT_STATE_ID = 1L;
    public static final long PENDING_INITIAL_QUOTE_APPROVAL_STATE_ID = 3L;
    public static final long SAMPLES_STATE_ID = 8L;
    public static final long FIRST_PAYMENT_STATE_ID = 11L;
    public static final long LAST_PAYMENT_STATE_ID = 13L;
    public static final long ORDER_REJECTED_STATE_ID = 15L;

    @Column(name = "reference_po")
    @NotBlank
    private String referencePo;

    @Column(name = "initial_quote")
    private double initialQuote;

    @Column(name = "final_quote")
    private double finalQuote;

    @Column(name = "comment")
    private String comment;

    @Column(name = "origin_country")
    private String originCountry;

    @Column(name = "delivery_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT_PRESENTATION)
    private Date deliveryTime;

    @OneToOne(targetEntity = ShippingInfo.class, fetch = FetchType.EAGER)
    @JoinColumn(name="id_shipping_info")
    private ShippingInfo shippingInfo;

    @ManyToOne(targetEntity = State.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_state")
    private State state;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private User user;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "shipping_addresses")
    private Map<String, ShippingAddress> shippingAddresses = new HashMap<>();

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @OrderBy("id ASC")
    private Set<Product> products;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @OrderBy("createdAt DESC")
    private Set<Payment> payments;

    /**
     * Constructor.
     */
    public Order()
    {
        super();
    }

    /**
     * Get the products.
     *
     * @return The products.
     */
    public Set<Product> getProducts()
    {
        return products;
    }

    /**
     * Set the products.
     *
     * @param products value of products.
     */
    public void setProducts(Set<Product> products)
    {
        this.products = products;
    }

    /**
     * Sets the referencePo.
     *
     * @param referencePo value of referencePo.
     */
    public void setReferencePo(String referencePo)
    {
        this.referencePo = referencePo;
    }

    /**
     * Gets the referencePo.
     *
     * @return The referencePo.
     */
    public String getReferencePo()
    {
        return referencePo;
    }

    /**
     * Gets the state.
     *
     * @return The state.
     */
    public State getState()
    {
        return state;
    }

    /**
     * Set the state of the order.
     *
     * @param state the state of the order.
     */
    public void setState(State state)
    {
        this.state = state;
    }

    /**
     * Gets the initial quote.
     *
     * @return The initial quote.
     */
    public double getInitialQuote()
    {
        return initialQuote;
    }

    /**
     * Sets the initial quote.
     *
     * @param initialQuote value of the initial quote.
     */
    public void setInitialQuote(double initialQuote)
    {
        this.initialQuote = initialQuote;
    }

    /**
     * Gets the final quote.
     *
     * @return The final quote.
     */
    public double getFinalQuote()
    {
        return finalQuote;
    }

    /**
     * Sets the final quote.
     *
     * @param finalQuote value of the final quote.
     */
    public void setFinalQuote(double finalQuote)
    {
        this.finalQuote = finalQuote;
    }

    /**
     * Gets the comment.
     *
     * @return The comment.
     */
    public String getComment()
    {
        return comment;
    }

    /**
     * Sets the comment.
     *
     * @param comment value of the comment.
     */
    public void setComment(String comment)
    {
        this.comment = comment;
    }

    /**
     * Gets the origin country.
     *
     * @return The origin country.
     */
    public String getOriginCountry()
    {
        return originCountry;
    }

    /**
     * Sets the origin country.
     *
     * @param originCountry value of the origin country.
     */
    public void setOriginCountry(String originCountry)
    {
        this.originCountry = originCountry;
    }

    /**
     * Gets the delivery time.
     *
     * @return The delivery time.
     */
    public Date getDeliveryTime()
    {
        return deliveryTime;
    }

    /**
     * Sets the delivery time.
     *
     * @param deliveryTime value of the delivery time.
     */
    public void setDeliveryTime(Date deliveryTime)
    {
        this.deliveryTime = deliveryTime;
    }

    /**
     * Gets the shipping info.
     *
     * @return The shipping info.
     */
    public ShippingInfo getShippingInfo()
    {
        return shippingInfo;
    }

    /**
     * Sets the shipping info.
     *
     * @param shippingInfo value of the shipping info.
     */
    public void setShippingInfo(ShippingInfo shippingInfo)
    {
        this.shippingInfo = shippingInfo;
    }

    /**
     * Gets the payments.
     *
     * @return The payments.
     */
    public Set<Payment> getPayments()
    {
        return payments;
    }

    /**
     * Sets the payments.
     *
     * @param payments value of the payments.
     */
    public void setPayments(Set<Payment> payments)
    {
        this.payments = payments;
    }

    /**
     * Gets user.
     *
     * @return Value of user.
     */
    public User getUser()
    {
        return user;
    }

    /**
     * Sets new user.
     *
     * @param user New value of user.
     */
    public void setUser(User user)
    {
        this.user = user;
    }

    /**
     * Sets new shippingAddresses.
     *
     * @param shippingAddresses New value of shippingAddresses.
     */
    public void setShippingAddresses(Map<String, ShippingAddress> shippingAddresses)
    {
        this.shippingAddresses = shippingAddresses;
    }

    /**
     * Gets shippingAddresses.
     *
     * @return Value of shippingAddresses.
     */
    public Map<String, ShippingAddress> getShippingAddresses()
    {
        return shippingAddresses;
    }

    /** (non-Javadoc) @see {@link Object} */
    @Override
    public int hashCode()
    {
        return BusinessIdentity.getHashCode(this);
    }

    /** (non-Javadoc) @see {@link Object} */
    @Override
    public boolean equals(final Object obj)
    {
        return BusinessIdentity.areEqual(this, obj);
    }

    /** (non-Javadoc) @see {@link Object} */
    @Override
    public String toString()
    {
        return BusinessIdentity.toString(this);
    }
}