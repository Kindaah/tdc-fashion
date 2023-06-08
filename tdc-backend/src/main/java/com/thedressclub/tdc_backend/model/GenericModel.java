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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.openpojo.business.BusinessIdentity;
import com.openpojo.business.annotation.BusinessKey;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonNodeStringType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Generic model class.
 *
 * @author Edson Ruiz Ramirez.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@TypeDef(name = "string-array", typeClass = StringArrayType.class)
@TypeDef(name = "int-array", typeClass = IntArrayType.class)
@TypeDef(name = "json", typeClass = JsonStringType.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@TypeDef(name = "jsonb-node", typeClass = JsonNodeBinaryType.class)
@TypeDef(name = "json-node", typeClass = JsonNodeStringType.class)
public class GenericModel implements Serializable
{
    static final String SEQUENCE = "generic_seq";

    private static final long serialVersionUID = 0L;
    static final String DATE_FORMAT_PRESENTATION = "yyyy-MM-dd'T'HH:mm:ss";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    @BusinessKey
    private long id;

    @Column(name = "created", nullable = false, updatable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT_PRESENTATION)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdAt;

    @Column(name = "updated", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT_PRESENTATION)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updatedAt;

    /**
     * Gets the models package route in project.
     *
     * @return Models package name.
     */
    public static String getModelPackageName()
    {
        return GenericModel.class.getPackage().getName();
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    public long getId()
    {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id value of id.
     */
    public void setId(long id)
    {
        this.id = id;
    }

    /**
     * Gets the created date.
     *
     * @return The created date.
     */
    public Date getCreatedAt()
    {
        return createdAt;
    }

    /**
     * Sets the created date.
     *
     * @param createdAt value of created date.
     */
    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    /**
     * Sets the updated date.
     *
     * @param updatedAt value of updated date.
     */
    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the updated date.
     *
     * @return The updated date.
     */
    public Date getUpdatedAt()
    {
        return updatedAt;
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