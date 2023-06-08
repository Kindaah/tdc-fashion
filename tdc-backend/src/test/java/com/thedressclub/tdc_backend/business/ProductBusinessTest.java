/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.business;

import static com.thedressclub.tdc_backend.business.ProductBusiness.BASE_NAME_FORMAT;
import static com.thedressclub.tdc_backend.business.ProductBusiness.COMMA_DELIMITER;
import static com.thedressclub.tdc_backend.business.ProductBusiness.FEATURES_FOLDER;
import static com.thedressclub.tdc_backend.business.ProductBusiness.FEATURE_PREFIX_FORMAT;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.thedressclub.tdc_backend.model.Feature;
import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.Product;
import com.thedressclub.tdc_backend.service.AzureStorageService;
import java.util.function.Function;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.web.multipart.MultipartFile;

/**
 * Test class for {@link ProductBusiness}.
 *
 * @author Daniel Mejia.
 */
public class ProductBusinessTest
{
    private static final Long OBJECT_ID = 1L;
    private static final String FOLDER = "folder";
    private static final String STORED_URLS = "file.jpg";
    private static final String FILE_NAME = "file";
    private static final String EXPECTED_PRODUCT =
        format(BASE_NAME_FORMAT, OBJECT_ID, OBJECT_ID, FOLDER);

    @Mock
    private AzureStorageService mockAzureStorageService;

    @Mock
    private Product mockProduct;

    @Mock
    private Order mockOrder;

    @Mock
    private MultipartFile mockFile;

    @Mock
    private Feature mockFeature;

    @Mock
    private Function<String, String> mockFunction;

    @Spy
    @InjectMocks
    private ProductBusiness instance;

    @Before
    public void setUp()
    {
        instance = new ProductBusiness();
        MockitoAnnotations.initMocks(this);

        when(mockProduct.getId()).thenReturn(OBJECT_ID);
        when(mockProduct.getOrder()).thenReturn(mockOrder);
        when(mockOrder.getId()).thenReturn(OBJECT_ID);
    }

    @Test
    public void testGetBasePrefixProduct()
    {
        Function<String, String> getPrefix = instance.getBasePrefix(mockProduct);

        assertThat(getPrefix.apply(FOLDER)).as("The prefix is wrong:").isEqualTo(EXPECTED_PRODUCT);
    }

    @Test
    public void testGetBasePrefixFeature()
    {
        when(mockFeature.getProduct()).thenReturn(mockProduct);
        when(mockFeature.getId()).thenReturn(OBJECT_ID);
        when(instance.getBasePrefix(mockProduct)).thenReturn(mockFunction);
        when(mockFunction.apply(FEATURES_FOLDER)).thenReturn(EXPECTED_PRODUCT);

        String expectedPrefix = format(FEATURE_PREFIX_FORMAT, OBJECT_ID, FOLDER);
        Function<String, String> getPrefix = instance.getBasePrefix(mockFeature);

        assertThat(getPrefix.apply(FOLDER))
            .as("The prefix is wrong:")
            .isEqualTo(EXPECTED_PRODUCT + expectedPrefix);
    }

    @Test
    public void testStoreFiles()
    {
        MultipartFile[] files = new MultipartFile[]{mockFile, mockFile};

        when(mockAzureStorageService.storeFile(FOLDER, mockFile)).thenReturn(FILE_NAME);
        String responseUrl = instance.storeFiles(FOLDER, files, STORED_URLS);
        String expectedUrl = FILE_NAME + COMMA_DELIMITER + FILE_NAME;

        assertThat(responseUrl).as("The urls stored is wrong: ").isEqualTo(expectedUrl);
        verify(mockAzureStorageService).deleteFiles(FOLDER);
    }

    @Test
    public void testStoreFilesNull()
    {
        when(mockAzureStorageService.storeFile(FOLDER, mockFile)).thenReturn(FILE_NAME);
        String responseUrl = instance.storeFiles(FOLDER, null, STORED_URLS);

        assertThat(responseUrl).as("The urls stored is wrong: ").isEqualTo(STORED_URLS);
        verifyZeroInteractions(mockAzureStorageService);
    }

    @Test
    public void testStoreFilesEmpty()
    {
        when(mockAzureStorageService.storeFile(FOLDER, mockFile)).thenReturn(FILE_NAME);
        String responseUrl = instance.storeFiles(FOLDER, new MultipartFile[]{}, STORED_URLS);

        assertThat(responseUrl).as("The urls stored is wrong: ").isEqualTo(STORED_URLS);
        verifyZeroInteractions(mockAzureStorageService);
    }
}