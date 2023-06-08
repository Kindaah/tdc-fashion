/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.model;

import com.openpojo.business.BusinessIdentity;
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
import org.hibernate.annotations.Where;

/**
 * Model object for user table.
 *
 * @author Edson Ruiz Ramirez.
 */
@Entity
@Table(name = "user_tdc")
@SequenceGenerator(name = GenericModel.SEQUENCE, sequenceName = "user_id_seq", allocationSize = 1)
public class User extends GenericModel
{
    private static final long serialVersionUID = 6L;

    @NotBlank
    @Column(name = "auth_key")
    private String authKey;

    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Column(name = "company_email")
    private String companyEmail;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "payment_key")
    private String paymentKey;

    @ManyToOne(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role")
    private Role role;

    @OneToOne(targetEntity = Company.class, fetch = FetchType.EAGER)
    @JoinColumn(name="id_company")
    private Company company;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @OrderBy("createdAt DESC")
    @Where(clause = "enabled = true")
    private Set<ShippingAddress> shippingAddresses;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @OrderBy("createdAt DESC")
    private Set<Order> orders;

    /**
     * Constructor.
     */
    public User()
    {
        super();
    }

    /**
     * Gets the firstName.
     *
     * @return Value of firstName.
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Sets the firstName.
     *
     * @param firstName The firstName.
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Gets the companyEmail.
     *
     * @return Value of companyEmail.
     */
    public String getCompanyEmail()
    {
        return companyEmail;
    }

    /**
     * Sets the lastName.
     *
     * @param lastName The lastName.
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Sets the authKey.
     *
     * @param authKey The authKey.
     */
    public void setAuthKey(String authKey)
    {
        this.authKey = authKey;
    }

    /**
     * Gets the lastName.
     *
     * @return Value of lastName.
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Gets the phoneNumber.
     *
     * @return Value of phoneNumber.
     */
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    /**
     * Gets the authKey.
     *
     * @return Value of authKey.
     */
    public String getAuthKey()
    {
        return authKey;
    }

    /**
     * Sets the phoneNumber.
     *
     * @param phoneNumber The phoneNumber.
     */
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the companyEmail.
     *
     * @param companyEmail The companyEmail.
     */
    public void setCompanyEmail(String companyEmail)
    {
        this.companyEmail = companyEmail;
    }

    /**
     * Gets the role.
     *
     * @return Value of role.
     */
    public Role getRole()
    {
        return role;
    }

    /**
     * Sets the role.
     *
     * @param role The role.
     */
    public void setRole(Role role)
    {
        this.role = role;
    }

    /**
     * Gets the paymentKey.
     *
     * @return Value of paymentKey.
     */
    public String getPaymentKey()
    {
        return paymentKey;
    }

    /**
     * Sets the paymentKey.
     *
     * @param paymentKey The paymentKey.
     */
    public void setPaymentKey(String paymentKey)
    {
        this.paymentKey = paymentKey;
    }

    /**
     * Sets the company.
     *
     * @param company New value of company.
     */
    public void setCompany(Company company)
    {
        this.company = company;
    }

    /**
     * Gets company.
     *
     * @return Value of company.
     */
    public Company getCompany()
    {
        return company;
    }

    /**
     * Gets orders.
     *
     * @return Value of orders.
     */
    public Set<Order> getOrders()
    {
        return orders;
    }

    /**
     * Sets new orders.
     *
     * @param orders New value of orders.
     */
    public void setOrders(Set<Order> orders)
    {
        this.orders = orders;
    }

    /**
     * Gets shippingAddresses.
     *
     * @return Value of shippingAddresses.
     */
    public Set<ShippingAddress> getShippingAddresses()
    {
        return shippingAddresses;
    }

    /**
     * Sets new shippingAddresses.
     *
     * @param shippingAddresses New value of shippingAddresses.
     */
    public void setShippingAddresses(Set<ShippingAddress> shippingAddresses)
    {
        this.shippingAddresses = shippingAddresses;
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
