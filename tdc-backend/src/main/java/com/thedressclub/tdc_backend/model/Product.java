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
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * Model object for product table.
 *
 * @author Edson Ruiz Ramirez.
 */
@Entity
@Table(name = "product")
@SequenceGenerator(name = GenericModel.SEQUENCE, sequenceName = "product_id_seq", allocationSize = 1)
public class Product extends GenericModel
{
    private static final long serialVersionUID = 3L;

    public static final String SKETCH_FILE_KEY = "sketchFiles";
    public static final String TECH_SHEET_FILE_KEY = "technicalSheetFiles";
    public static final String MODEL_FILE_KEY = "modelFiles";

    @Column(name = "description")
    @NotBlank
    @BusinessKey
    private String description;

    @Column(name = "quote")
    private double quote;

    @Column(name = "sketch_urls")
    private String sketchFileUrls;

    @Column(name = "technical_sheet_urls")
    private String technicalSheetFileUrls;

    @Column(name = "model_urls")
    private String modelFileUrls;

    @ManyToOne(targetEntity = Order.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order")
    @JsonIgnore
    private Order order;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderBy("orderInProduct ASC")
    @NotEmpty
    @Valid
    private Set<Feature> features;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderBy("letter ASC")
    @Valid
    private Set<TechnicalSheetRow> technicalSheetRows = new LinkedHashSet<>();

    /**
     * Constructor.
     */
    public Product()
    {
        super();
    }

    /**
     * Sets the order.
     *
     * @param order value of order.
     */
    public void setOrder(Order order)
    {
        this.order = order;
    }

    /**
     * Gets the description.
     *
     * @return The description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Get the features.
     *
     * @return The features.
     */
    public Set<Feature> getFeatures()
    {
        return features;
    }

    /**
     * Set the features.
     *
     * @param features value of features.
     */
    public void setFeatures(Set<Feature> features)
    {
        this.features = features;
    }

    /**
     * Gets the order.
     *
     * @return The order.
     */
    public Order getOrder()
    {
        return order;
    }

    /**
     * Gets the sketch files urls.
     *
     * @return The skecth files urls.
     */
    public String getSketchFileUrls()
    {
        return sketchFileUrls;
    }

    /**
     * Set the sketch files urls.
     *
     * @param sketchFileUrls value of the sketch files urls.
     */
    public void setSketchFileUrls(String sketchFileUrls)
    {
        this.sketchFileUrls = sketchFileUrls;
    }

    /**
     * Gets the technical sheets files urls.
     *
     * @return The technical sheets files urls.
     */
    public String getTechnicalSheetFileUrls()
    {
        return technicalSheetFileUrls;
    }

    /**
     * Set the technical sheet files urls.
     *
     * @param technicalSheetFileUrls value of the technical sheets files urls.
     */
    public void setTechnicalSheetFileUrls(String technicalSheetFileUrls)
    {
        this.technicalSheetFileUrls = technicalSheetFileUrls;
    }

    /**
     * Gets the 3D model files urls.
     *
     * @return The 3D model files urls.
     */
    public String getModelFileUrls()
    {
        return modelFileUrls;
    }

    /**
     * Set the 3D model files urls.
     *
     * @param modelFileUrls value of the 3D model files urls.
     */
    public void setModelFileUrls(String modelFileUrls)
    {
        this.modelFileUrls = modelFileUrls;
    }

    /**
     * Sets the description.
     *
     * @param description value of description.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Gets the product quote.
     *
     * @return the qoute.
     */
    public double getQuote()
    {
        return quote;
    }

    /**
     * Sets the quote.
     *
     * @param quote to be set.
     */
    public void setQuote(double quote)
    {
        this.quote = quote;
    }

    /**
     * Gets technicalSheetRows.
     *
     * @return Value of technicalSheetRows.
     */
    public Set<TechnicalSheetRow> getTechnicalSheetRows()
    {
        return technicalSheetRows;
    }

    /**
     * Sets new technicalSheetRows.
     *
     * @param technicalSheetRows New value of technicalSheetRows.
     */
    public void setTechnicalSheetRows(Set<TechnicalSheetRow> technicalSheetRows)
    {
        this.technicalSheetRows = technicalSheetRows;
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
