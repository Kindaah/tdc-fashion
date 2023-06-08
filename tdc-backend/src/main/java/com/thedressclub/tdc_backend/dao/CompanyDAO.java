/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.dao;

import com.thedressclub.tdc_backend.model.Company;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

/**
 * Contains the data access methods for {@link Company}. *
 *
 * @author Daniel Mejia.
 */
@Repository
@Transactional
public class CompanyDAO extends GenericDAO<Company>
{
    /**
     * Constructor.
     */
    public CompanyDAO()
    {
        super(Company.class);
    }
}
