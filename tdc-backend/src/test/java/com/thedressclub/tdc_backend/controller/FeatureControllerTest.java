/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static com.thedressclub.tdc_backend.controller.FeatureController.FEATURES_URL;
import static com.thedressclub.tdc_backend.controller.FeatureController.PATTERNS_FILE_KEY;
import static com.thedressclub.tdc_backend.controller.FeatureController.UPLOAD_PATTERNS_URL;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.thedressclub.tdc_backend.business.ProductBusiness;
import com.thedressclub.tdc_backend.controller.config.GenericControllerTest;
import com.thedressclub.tdc_backend.model.Feature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Test class for {@link FeatureController}.
 *
 * @author Daniel Mejia.
 */
public class FeatureControllerTest extends GenericControllerTest<Feature>
{
    private static final String STORED_URL = "url";

    @Mock
    private ProductBusiness mockProductBusiness;

    @Mock
    private Feature mockFeature;

    @InjectMocks
    private FeatureController featureController;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        featureController.setGenericService(mockGenericService);
        init(FEATURES_URL, new Feature(), featureController);

        when(mockGenericService.findById(OBJECT_ID)).thenReturn(mockFeature);
        when(mockGenericService.update(mockFeature)).thenReturn(mockFeature);
    }

    @Test
    public void testUploadPatterns()
    throws Exception
    {
        when(mockProductBusiness.getBasePrefix(mockFeature)).thenReturn(value-> value);
        when(mockProductBusiness.storeFiles(anyString(), any(), eq(null))).thenReturn(STORED_URL);

        String url = FEATURES_URL + SLASH + OBJECT_ID + SLASH + UPLOAD_PATTERNS_URL;

        MockMultipartFile patternsFiles = new MockMultipartFile(PATTERNS_FILE_KEY, new byte[]{});

        ResultActions result = performMultipartRequest(url, singletonList(patternsFiles));

        result
            .andExpect(status().isOk());

        verify(mockFeature).setPatternsUrls(STORED_URL);
    }
}