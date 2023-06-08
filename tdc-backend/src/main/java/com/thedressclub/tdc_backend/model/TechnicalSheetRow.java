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
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.Type;

/**
 * Model object for technical sheet table.
 *
 * @author Daniel Mejia.
 */
@Entity
@Table(name = "technical_sheet_row")
@SequenceGenerator(name = GenericModel.SEQUENCE, sequenceName = "technical_sheet_row_id_seq", allocationSize = 1)
public class TechnicalSheetRow extends GenericModel
{
    private static final long serialVersionUID = 16L;

    @NotBlank
    @Column(name = "letter")
    @BusinessKey
    private String letter;

    @NotBlank
    @Column(name = "description")
    private String description;

    @DecimalMin("0.5")
    @DecimalMax("1.5")
    @Column(name = "tol")
    private double tol;

    @Column(name = "twelve")
    private String twelve;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "sizes")
    private Map<String, String> sizes;

    @ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product")
    @JsonIgnore
    private Product product;

    /**
     * Constructor.
     */
    public TechnicalSheetRow()
    {
        super();
    }

    /**
     * Gets the letter.
     *
     * @return Value of letter.
     */
    public String getLetter()
    {
        return letter;
    }

    /**
     * Sets new letter.
     *
     * @param letter New value of letter.
     */
    public void setLetter(String letter)
    {
        this.letter = letter;
    }

    /**
     * Gets sizes.
     *
     * @return Value of sizes.
     */
    public Map<String, String> getSizes()
    {
        return sizes;
    }

    /**
     * Sets new twelve.
     *
     * @param twelve New value of twelve.
     */
    public void setTwelve(String twelve)
    {
        this.twelve = twelve;
    }

    /**
     * Gets twelve.
     *
     * @return Value of twelve.
     */
    public String getTwelve()
    {
        return twelve;
    }

    /**
     * Sets new tol.
     *
     * @param tol New value of tol.
     */
    public void setTol(double tol)
    {
        this.tol = tol;
    }

    /**
     * Gets description.
     *
     * @return Value of description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets new description.
     *
     * @param description New value of description.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Sets new product.
     *
     * @param product New value of product.
     */
    public void setProduct(Product product)
    {
        this.product = product;
    }

    /**
     * Gets tol.
     *
     * @return Value of tol.
     */
    public double getTol()
    {
        return tol;
    }

    /**
     * Gets product.
     *
     * @return Value of product.
     */
    public Product getProduct()
    {
        return product;
    }

    /**
     * Sets new sizes.
     *
     * @param sizes New value of sizes.
     */
    public void setSizes(Map<String, String> sizes)
    {
        this.sizes = sizes;
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
