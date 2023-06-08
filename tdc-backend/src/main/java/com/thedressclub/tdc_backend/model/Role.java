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
 * Model object for role table.
 *
 * @author Edson Ruiz Ramirez.
 */
@Entity
@Table(name = "role_tdc")
@SequenceGenerator(name = GenericModel.SEQUENCE, sequenceName = "role_tdc_id_seq", allocationSize = 1)
public class Role extends GenericModel
{
    private static final long serialVersionUID = 6L;

    public static final long CUSTOMER_ROLE = 2L;
    public static final String CUSTOMER_ROLE_NAME = "CUSTOMER";

    @NotBlank
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<User> users;

    /**
     * Constructor.
     */
    public Role()
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
     * Gets the users.
     *
     * @return The users.
     */
    public Set<User> getUsers()
    {
        return users;
    }

    /**
     * Sets the users.
     *
     * @param users the value of users.
     */
    public void setUsers(Set<User> users)
    {
        this.users = users;
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
