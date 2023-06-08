/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.dto;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.State;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for {@link CountDTO}.
 *
 * @author Daniel Mejia.
 */
public class CountDTOTest
{
    @Mock
    private Order mockOrder;

    @Mock
    private State mockState;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getCountDTOOrder()
    {
        when(mockOrder.getProducts()).thenReturn(Collections.emptySet());

        CountDTO countDTO = CountDTO.getCountDTO(mockOrder);

        assertEquals("The entity is wrong: ", countDTO.getEntity(), mockOrder);
        assertEquals("The count is wrong: ", countDTO.getCount(), 0);
    }

    @Test
    public void getCountDTOState()
    {
        CountDTO countDTO = CountDTO.getCountDTO(mockState, Collections.singletonList(mockOrder));

        assertEquals("The entity is wrong: ", countDTO.getEntity(), mockState);
        assertEquals("The count is wrong: ", countDTO.getCount(), 1);
    }
}