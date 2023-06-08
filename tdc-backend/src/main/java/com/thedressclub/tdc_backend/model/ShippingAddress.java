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

/**
 * Model object for the shipping address table.
 *
 * @author Edson Ruiz Ramirez.
 */
@Entity
@Table(name = "shipping_address")
@SequenceGenerator(name = GenericModel.SEQUENCE, sequenceName = "shipping_address_id_seq", allocationSize = 1)
public class ShippingAddress extends GenericModel
{
    private static final long serialVersionUID = 6L;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "country")
    private String country;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "street_address_add_info")
    private String streetAddressAddInfo;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "enabled", insertable = false)
    private boolean enabled = true;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", updatable = false)
    @JsonIgnore
    private User user;

    /**
     * Constructor.
     */
    public ShippingAddress()
    {
        super();
    }

    /**
     * Gets the contactName.
     *
     * @return Value of contactName.
     */
    public String getContactName()
    {
        return contactName;
    }

    /**
     * Sets the contactName.
     *
     * @param contactName The contactName.
     */
    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    /**
     * Gets the country.
     *
     * @return Value of country.
     */
    public String getCountry()
    {
        return country;
    }

    /**
     * Sets the country.
     *
     * @param country The country.
     */
    public void setCountry(String country)
    {
        this.country = country;
    }

    /**
     * Gets the streetAddress.
     *
     * @return Value of streetAddress.
     */
    public String getStreetAddress()
    {
        return streetAddress;
    }

    /**
     * Sets the contactName.
     *
     * @param streetAddress The streetAddress.
     */
    public void setStreetAddress(String streetAddress)
    {
        this.streetAddress = streetAddress;
    }

    /**
     * Gets the streetAddressAddInfo.
     *
     * @return Value of streetAddressAddInfo.
     */
    public String getStreetAddressAddInfo()
    {
        return streetAddressAddInfo;
    }

    /**
     * Sets the streetAddressAddInfo.
     *
     * @param streetAddressAddInfo The streetAddressAddInfo.
     */
    public void setStreetAddressAddInfo(String streetAddressAddInfo)
    {
        this.streetAddressAddInfo = streetAddressAddInfo;
    }

    /**
     * Gets the state.
     *
     * @return Value of state.
     */
    public String getState()
    {
        return state;
    }

    /**
     * Sets the state.
     *
     * @param state The state.
     */
    public void setState(String state)
    {
        this.state = state;
    }

    /**
     * Gets the city.
     *
     * @return Value of city.
     */
    public String getCity()
    {
        return city;
    }

    /**
     * Sets the city.
     *
     * @param city The city.
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    /**
     * Gets the postalCode.
     *
     * @return Value of postalCode.
     */
    public String getPostalCode()
    {
        return postalCode;
    }

    /**
     * Sets the postalCode.
     *
     * @param postalCode The postalCode.
     */
    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    /**
     * Gets the countryCode.
     *
     * @return Value of countryCode.
     */
    public String getCountryCode()
    {
        return countryCode;
    }

    /**
     * Sets the countryCode.
     *
     * @param countryCode The countryCode.
     */
    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
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
     * Sets the phoneNumber.
     *
     * @param phoneNumber The phoneNumber.
     */
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
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
     * Sets new enabled.
     *
     * @param enabled New value of enabled.
     */
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    /**
     * Gets enabled.
     *
     * @return Value of enabled.
     */
    public boolean isEnabled()
    {
        return enabled;
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
