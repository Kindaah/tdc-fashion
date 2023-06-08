/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.thedressclub.tdc_backend.controller.config.GenericControllerTest;
import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.ShippingInfo;
import com.thedressclub.tdc_backend.service.OrderService;
import java.util.Collections;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Test class for {@link ShippingInfoController}.
 *
 * @author Edson Ruiz Ramirez.
 */
public class ShippingInfoControllerTest extends GenericControllerTest<ShippingInfo>
{
    private static final String SHIPPING_INFO_URL = "/orders/" + OBJECT_ID + "/shippingInfo/";
    private static final String SHIPPING_INFO_UPDATE_URL = "/orders/shippingInfo/" + OBJECT_ID;
    private static final String COMPANY_VALUE = "value";
    private static final String TRACKING_VALUE = "12345abc";
    private static final String DETAILS_VALUE = "value";
    private static final String REFERENCE_PO = "reference_po";

    private ShippingInfo shippingInfo;

    @Mock
    private OrderService mockOrderService;

    @Mock
    private Order mockOrder;

    @InjectMocks
    private ShippingInfoController shippingInfoController;

    @Before
    public void setUp()
    {
        Order order = new Order();
        order.setId(OBJECT_ID);
        order.setReferencePo(REFERENCE_PO);

        shippingInfo = new ShippingInfo();
        shippingInfo.setId(OBJECT_ID);
        shippingInfo.setCompany(COMPANY_VALUE);
        shippingInfo.setDetails(DETAILS_VALUE);
        shippingInfo.setTrackingId(TRACKING_VALUE);

        MockitoAnnotations.initMocks(this);
        shippingInfoController.setGenericService(mockGenericService);
        init(SHIPPING_INFO_URL, shippingInfo, shippingInfoController);

        when(mockOrderService.findById(OBJECT_ID)).thenReturn(mockOrder);
    }

    @Test
    public void testAddShippingInfoSuccess()
    throws Exception
    {
        ResultActions result = performAddRequest(shippingInfo);

        result.andExpect(status().isCreated());
        verify(mockOrderService).update(mockOrder);
    }

    @Test
    public void testAddShippingInfoFail()
    {
        assertThatThrownBy(() -> performAddRequest(null))
            .hasCause(addException);
    }

    @Test
    public void testUpdateShippingInfoSuccess()
    throws Exception
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(shippingInfo);
        when(mockGenericService.update(shippingInfo)).thenReturn(shippingInfo);

        ResultActions result = performPutRequest(SHIPPING_INFO_UPDATE_URL, shippingInfo);
        result.andExpect(status().isOk()).andExpect(content().string(asJsonString(shippingInfo)));
    }

    @Test
    public void testUpdateShippingInfoNotFound()
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(null);

        assertThatThrownBy(() -> performPutRequest(SHIPPING_INFO_UPDATE_URL, shippingInfo))
            .hasCause(noFoundException);
    }

    @Test
    public void testUpdateShippingInfoBadRequest()
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(shippingInfo);
        when(mockGenericService.update(shippingInfo)).thenReturn(null);

        assertThatThrownBy(() -> performPutRequest(SHIPPING_INFO_UPDATE_URL, shippingInfo))
            .hasCause(updatedException);
    }
}