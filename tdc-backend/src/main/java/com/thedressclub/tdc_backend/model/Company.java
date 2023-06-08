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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * Model object for company table.
 *
 * @author Daniel Mejia.
 */
@Entity
@Table(name = "company")
@SequenceGenerator(name = GenericModel.SEQUENCE, sequenceName = "company_id_seq", allocationSize = 1)
public class Company extends GenericModel
{
    private static final long serialVersionUID = 16L;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "country")
    private String country;

    @NotBlank
    @Column(name = "state")
    private String state;

    @NotBlank
    @Column(name = "city")
    private String city;

    @NotBlank
    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "address_additional_info")
    private String addressAdditionalInfo;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "website")
    private String website;

    @Column(name = "trademark_name")
    private String trademarkName;

    @Column(name = "trademark_registration_number")
    private String trademarkRegistrationNumber;

    @Column(name = "sales_tax_permit")
    private String salesTaxPermit;

    @Column(name = "dun_number")
    private String dunNumber;

    @Column(name = "ein_number")
    private String einNumber;

    @Column(name = "nearest_airport")
    private String nearestAirport;

    @Column(name = "second_nearest_airport")
    private String secondNearestAirport;

    @Column(name = "line_business")
    private String lineBusiness;

    @OneToOne(mappedBy = "company", fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    /**
     * Constructor.
     */
    public Company()
    {
        super();
    }
    
    /**
     * Sets the postalCode.
     *
     * @param postalCode New value of postalCode.
     */
    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    /**
     * Sets the country.
     *
     * @param country New value of country.
     */
    public void setCountry(String country)
    {
        this.country = country;
    }

    /**
     * Sets the secondNearestAirport.
     *
     * @param secondNearestAirport New value of secondNearestAirport.
     */
    public void setSecondNearestAirport(String secondNearestAirport)
    {
        this.secondNearestAirport = secondNearestAirport;
    }

    /**
     * Sets the lineBusiness.
     *
     * @param lineBusiness New value of lineBusiness.
     */
    public void setLineBusiness(String lineBusiness)
    {
        this.lineBusiness = lineBusiness;
    }

    /**
     * Gets trademarkRegistrationNumber.
     *
     * @return Value of trademarkRegistrationNumber.
     */
    public String getTrademarkRegistrationNumber()
    {
        return trademarkRegistrationNumber;
    }

    /**
     * Gets salesTaxPermit.
     *
     * @return Value of salesTaxPermit.
     */
    public String getSalesTaxPermit()
    {
        return salesTaxPermit;
    }

    /**
     * Gets name.
     *
     * @return Value of name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets website.
     *
     * @return Value of website.
     */
    public String getWebsite()
    {
        return website;
    }

    /**
     * Gets secondNearestAirport.
     *
     * @return Value of secondNearestAirport.
     */
    public String getSecondNearestAirport()
    {
        return secondNearestAirport;
    }

    /**
     * Sets the trademarkName.
     *
     * @param trademarkName New value of trademarkName.
     */
    public void setTrademarkName(String trademarkName)
    {
        this.trademarkName = trademarkName;
    }

    /**
     * Sets the name.
     *
     * @param name New value of name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets lineBusiness.
     *
     * @return Value of lineBusiness.
     */
    public String getLineBusiness()
    {
        return lineBusiness;
    }

    /**
     * Sets the city.
     *
     * @param city New value of city.
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    /**
     * Gets city.
     *
     * @return Value of city.
     */
    public String getCity()
    {
        return city;
    }

    /**
     * Gets einNumber.
     *
     * @return Value of einNumber.
     */
    public String getEinNumber()
    {
        return einNumber;
    }

    /**
     * Sets the state.
     *
     * @param state New value of state.
     */
    public void setState(String state)
    {
        this.state = state;
    }

    /**
     * Sets the nearestAirport.
     *
     * @param nearestAirport New value of nearestAirport.
     */
    public void setNearestAirport(String nearestAirport)
    {
        this.nearestAirport = nearestAirport;
    }

    /**
     * Sets the streetAddress.
     *
     * @param streetAddress New value of streetAddress.
     */
    public void setStreetAddress(String streetAddress)
    {
        this.streetAddress = streetAddress;
    }

    /**
     * Gets nearestAirport.
     *
     * @return Value of nearestAirport.
     */
    public String getNearestAirport()
    {
        return nearestAirport;
    }

    /**
     * Gets postalCode.
     *
     * @return Value of postalCode.
     */
    public String getPostalCode()
    {
        return postalCode;
    }

    /**
     * Gets dunNumber.
     *
     * @return Value of dunNumber.
     */
    public String getDunNumber()
    {
        return dunNumber;
    }

    /**
     * Sets the salesTaxPermit.
     *
     * @param salesTaxPermit New value of salesTaxPermit.
     */
    public void setSalesTaxPermit(String salesTaxPermit)
    {
        this.salesTaxPermit = salesTaxPermit;
    }

    /**
     * Sets the dunNumber.
     *
     * @param dunNumber New value of dunNumber.
     */
    public void setDunNumber(String dunNumber)
    {
        this.dunNumber = dunNumber;
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
     * Sets the user.
     *
     * @param user New value of user.
     */
    public void setUser(User user)
    {
        this.user = user;
    }

    /**
     * Gets addressAdditionalInfo.
     *
     * @return Value of addressAdditionalInfo.
     */
    public String getAddressAdditionalInfo()
    {
        return addressAdditionalInfo;
    }

    /**
     * Gets country.
     *
     * @return Value of country.
     */
    public String getCountry()
    {
        return country;
    }

    /**
     * Gets state.
     *
     * @return Value of state.
     */
    public String getState()
    {
        return state;
    }

    /**
     * Sets the einNumber.
     *
     * @param einNumber New value of einNumber.
     */
    public void setEinNumber(String einNumber)
    {
        this.einNumber = einNumber;
    }

    /**
     * Gets streetAddress.
     *
     * @return Value of streetAddress.
     */
    public String getStreetAddress()
    {
        return streetAddress;
    }

    /**
     * Gets trademarkName.
     *
     * @return Value of trademarkName.
     */
    public String getTrademarkName()
    {
        return trademarkName;
    }

    /**
     * Sets the trademarkRegistrationNumber.
     *
     * @param trademarkRegistrationNumber New value of trademarkRegistrationNumber.
     */
    public void setTrademarkRegistrationNumber(String trademarkRegistrationNumber)
    {
        this.trademarkRegistrationNumber = trademarkRegistrationNumber;
    }

    /**
     * Sets the addressAdditionalInfo.
     *
     * @param addressAdditionalInfo New value of addressAdditionalInfo.
     */
    public void setAddressAdditionalInfo(String addressAdditionalInfo)
    {
        this.addressAdditionalInfo = addressAdditionalInfo;
    }

    /**
     * Sets the website.
     *
     * @param website New value of website.
     */
    public void setWebsite(String website)
    {
        this.website = website;
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
