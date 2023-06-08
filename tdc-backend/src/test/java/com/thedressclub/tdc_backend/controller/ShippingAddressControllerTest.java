/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static com.thedressclub.tdc_backend.controller.ShippingAddressController.SHIPPING_ADDRESS_URL;
import static com.thedressclub.tdc_backend.controller.UserController.USERS_URL;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.thedressclub.tdc_backend.controller.config.GenericControllerTest;
import com.thedressclub.tdc_backend.model.ShippingAddress;
import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Test class for {@link ShippingAddressController}.
 *
 * @author Edson Ruiz Ramirez.
 */
public class ShippingAddressControllerTest extends GenericControllerTest<ShippingAddress>
{
    private static final String ADD_SHIPPING_ADDRESS = USERS_URL + SLASH + OBJECT_ID + SHIPPING_ADDRESS_URL;
    private static final String UPDATE_SHIPPING_ADDRESS_URL = USERS_URL + SHIPPING_ADDRESS_URL + SLASH + OBJECT_ID;

    private ShippingAddress shippingAddress;

    @Mock
    private UserService mockUserService;

    @InjectMocks
    private ShippingAddressController shippingAddressController;

    @Before
    public void setUp()
    {
        shippingAddress = new ShippingAddress();
        shippingAddress.setId(OBJECT_ID);

        User user = new User();
        user.setId(OBJECT_ID);

        MockitoAnnotations.initMocks(this);
        shippingAddressController.setGenericService(mockGenericService);
        init(ADD_SHIPPING_ADDRESS, shippingAddress, shippingAddressController);
        when(mockUserService.findById(OBJECT_ID)).thenReturn(user);
    }

    @Test
    public void testAddShippingAddressSuccess()
    throws Exception
    {
        ResultActions result = performAddRequest(shippingAddress);

        result.andExpect(status().isCreated());
    }

    @Test
    public void testAddShippingAddressFail()
    {
        when(mockUserService.findById(OBJECT_ID)).thenReturn(null);

        assertThatThrownBy(() -> performAddRequest(shippingAddress))
            .hasCause(addException);
    }

    @Test
    public void testUpdateShippingAddress()
    throws Exception
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(shippingAddress);
        when(mockGenericService.update(shippingAddress)).thenReturn(shippingAddress);

        ResultActions result = performPutRequest(UPDATE_SHIPPING_ADDRESS_URL, shippingAddress);

        result
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(shippingAddress)));
    }

    @Test
    public void testDeleteShippingAddress()
    throws Exception
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(shippingAddress);
        when(mockGenericService.update(shippingAddress)).thenReturn(shippingAddress);

        ResultActions result = mockMvc.perform(delete(UPDATE_SHIPPING_ADDRESS_URL)
            .contentType(MediaType.APPLICATION_JSON));

        result
            .andExpect(status().isOk());
    }
}