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
import com.openpojo.business.annotation.BusinessKey;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

/**
 * Model object for features table.
 *
 * @author Edson Ruiz Ramirez.
 */
@Entity
@Table(name = "feature")
@SequenceGenerator(name = GenericModel.SEQUENCE, sequenceName = "feature_id_seq", allocationSize = 1)
public class Feature extends GenericModel
{
    private static final long serialVersionUID = 1L;

    @Column(name = "color")
    @NotBlank
    @BusinessKey
    private String color;

    @Column(name = "feature_size")
    @NotBlank
    @BusinessKey
    private String size;

    @Column(name = "quantity")
    @Range(min = 10, max = 500)
    @BusinessKey
    private int quantity;

    @Column(name = "sample_quantity")
    private int sampleQuantity;

    @Column(name = "patterns_urls")
    private String patternsUrls;

    @Column(name = "order_in_product")
    private int orderInProduct;

    @ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product")
    @JsonIgnore
    private Product product;

    /**
     * Constructor.
     */
    public Feature()
    {
        super();
    }

    /**
     * Sets the size.
     *
     * @param size value of size.
     */
    public void setSize(String size)
    {
        this.size = size;
    }

    /**
     * Sets the color.
     *
     * @param color value of color.
     */
    public void setColor(String color)
    {
        this.color = color;
    }

    /**
     * Sets the quantity.
     *
     * @param quantity value of quantity.
     */
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    /**
     * Sets the product.
     *
     * @param product value of product.
     */
    public void setProduct(Product product)
    {
        this.product = product;
    }

    /**
     * Gets the quantity.
     *
     * @return The quantity.
     */
    public int getQuantity()
    {
        return quantity;
    }

    /**
     * Gets the size.
     *
     * @return The size.
     */
    public String getSize()
    {
        return size;
    }

    /**
     * Gets the product.
     *
     * @return The product.
     */
    public Product getProduct()
    {
        return product;
    }

    /**
     * Gets the color.
     *
     * @return The color.
     */
    public String getColor()
    {
        return color;
    }

    /**
     * Sets the sample quantity.
     *
     * @param sampleQuantity value of sample quantity.
     */
    public void setSampleQuantity(int sampleQuantity)
    {
        this.sampleQuantity = sampleQuantity;
    }

    /**
     * Gets the sample quantity.
     *
     * @return The sample quantity.
     */
    public int getSampleQuantity()
    {
        return sampleQuantity;
    }

    /**
     * Gets patternsUrls.
     *
     * @return Value of patternsUrls.
     */
    public String getPatternsUrls()
    {
        return patternsUrls;
    }

    /**
     * Sets new patternsUrls.
     *
     * @param patternsUrls New value of patternsUrls.
     */
    public void setPatternsUrls(String patternsUrls)
    {
        this.patternsUrls = patternsUrls;
    }

    /**
     * Gets orderInProduct.
     *
     * @return Value of orderInProduct.
     */
    public int getOrderInProduct()
    {
        return orderInProduct;
    }

    /**
     * Sets new orderInProduct.
     *
     * @param orderInProduct New value of orderInProduct.
     */
    public void setOrderInProduct(int orderInProduct)
    {
        this.orderInProduct = orderInProduct;
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
