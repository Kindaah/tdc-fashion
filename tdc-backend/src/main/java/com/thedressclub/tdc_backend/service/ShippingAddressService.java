/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import com.thedressclub.tdc_backend.model.ShippingAddress;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * Contains the service methods for {@link ShippingAddress}.
 *
 * @author Edson Ruiz Ramirez.
 */
@Service("shippingAddressService")
@Transactional
public class ShippingAddressService extends GenericService<ShippingAddress>
{
}