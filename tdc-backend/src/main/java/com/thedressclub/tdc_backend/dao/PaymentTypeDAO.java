/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.dao;

import com.thedressclub.tdc_backend.model.PaymentType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contains the data access methods for {@link PaymentType}.
 *
 * @author Daniel Mejia.
 */
@Repository
@Transactional
public class PaymentTypeDAO extends GenericDAO<PaymentType>
{
    /**
     * Constructor.
     */
    public PaymentTypeDAO()
    {
        super(PaymentType.class);
    }
}