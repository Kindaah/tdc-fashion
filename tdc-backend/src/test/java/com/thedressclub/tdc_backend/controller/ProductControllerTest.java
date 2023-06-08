/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static com.thedressclub.tdc_backend.controller.ProductController.FILE_NAME_KEY;
import static com.thedressclub.tdc_backend.controller.ProductController.FILE_URL;
import static com.thedressclub.tdc_backend.model.Product.MODEL_FILE_KEY;
import static com.thedressclub.tdc_backend.model.Product.SKETCH_FILE_KEY;
import static com.thedressclub.tdc_backend.model.Product.TECH_SHEET_FILE_KEY;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.thedressclub.tdc_backend.business.ProductBusiness;
import com.thedressclub.tdc_backend.controller.config.GenericControllerTest;
import com.thedressclub.tdc_backend.model.Feature;
import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.Product;
import com.thedressclub.tdc_backend.model.TechnicalSheetRow;
import com.thedressclub.tdc_backend.service.IAzureStorageService;
import com.thedressclub.tdc_backend.service.OrderService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Test class for {@link ProductController}.
 *
 * @author Daniel Mejia.
 */
public class ProductControllerTest extends GenericControllerTest<Product>
{
    private static final String PRODUCTS_URL = "/orders/" + OBJECT_ID + "/products/";
    private static final String COLOR = "color";
    private static final int QUANTITY = 10;
    private static final String SIZE = "size";
    private static final String DESCRIPTION = "description";
    private static final String REFERENCE_PO = "reference_po";
    private static final String STORED_URL = "url";
    private static final String FILE = "file.file";
    private static final String TWELVE = "twelve";
    private static final String LETTER = "A";

    private Product product;

    @Mock
    private OrderService mockOrderService;

    @Mock
    private ProductBusiness mockProductBusiness;

    @Mock
    private IAzureStorageService mockAzureStorageService;

    @Mock
    private Product mockProduct;

    @InjectMocks
    private ProductController productController;

    @Before
    public void setUp()
    {
        Order order = new Order();
        order.setId(OBJECT_ID);
        order.setReferencePo(REFERENCE_PO);

        Feature feature = new Feature();
        feature.setColor(COLOR);
        feature.setQuantity(QUANTITY);
        feature.setSize(SIZE);
        feature.setOrderInProduct(1);
        Set<Feature> features = singleton(feature);

        TechnicalSheetRow technicalSheetRow = new TechnicalSheetRow();
        technicalSheetRow.setLetter(LETTER);
        technicalSheetRow.setDescription(DESCRIPTION);
        technicalSheetRow.setSizes(Collections.emptyMap());
        technicalSheetRow.setTol(1);
        technicalSheetRow.setTwelve(TWELVE);

        product = new Product();
        product.setId(OBJECT_ID);
        product.setDescription(DESCRIPTION);
        product.setFeatures(features);
        product.setTechnicalSheetRows(singleton(technicalSheetRow));

        MockitoAnnotations.initMocks(this);
        productController.setGenericService(mockGenericService);
        init(PRODUCTS_URL, product, productController);

        when(mockGenericService.findById(OBJECT_ID)).thenReturn(mockProduct);
        when(mockGenericService.update(mockProduct)).thenReturn(mockProduct);
        when(mockOrderService.findById(OBJECT_ID)).thenReturn(order);
    }

    @Test
    public void testAddProductSuccess()
    throws Exception
    {
        ResultActions result = performAddRequest(product);

        result.andExpect(status().isCreated());
    }

    @Test
    public void testAddProductFail()
    {
        assertThatThrownBy(() -> performAddRequest(null))
            .hasCause(addException);
    }

    @Test
    public void testUpdateProductSuccess()
    throws Exception
    {
        ResultActions result = performUpdateRequest(product, product);
        result.andExpect(status().isOk()).andExpect(content().string(asJsonString(product)));
    }

    @Test
    public void testUpdateProductNotFound()
    {
        assertThatThrownBy(() -> performUpdateRequest(null, null))
            .hasCause(noFoundException);
    }

    @Test
    public void testUpdateProductBadRequest()
    {
        assertThatThrownBy(() -> performUpdateRequest(product, null))
            .hasCause(updatedException);
    }

    @Test
    public void testUploadFiles()
    throws Exception
    {
        when(mockProductBusiness.getBasePrefix(mockProduct)).thenReturn(value-> value);
        when(mockProductBusiness.storeFiles(anyString(), any(), eq(null))).thenReturn(STORED_URL);

        String url = "/products" + SLASH + OBJECT_ID + SLASH + "uploadFiles";

        MockMultipartFile sketchFiles = new MockMultipartFile(SKETCH_FILE_KEY, new byte[]{});
        MockMultipartFile techFiles = new MockMultipartFile(TECH_SHEET_FILE_KEY, new byte[]{});
        MockMultipartFile modelFiles = new MockMultipartFile(MODEL_FILE_KEY, new byte[]{});

        List<MockMultipartFile> params = Arrays.asList(sketchFiles, techFiles, modelFiles);

        ResultActions result = performMultipartRequest(url, params);
        result.andExpect(status().isOk());

        verify(mockProduct).setSketchFileUrls(STORED_URL);
        verify(mockProduct).setTechnicalSheetFileUrls(STORED_URL);
        verify(mockProduct).setModelFileUrls(STORED_URL);
    }

    @Test
    public void testGetDownloadFileUrl()
    throws Exception
    {
        when(mockAzureStorageService.getDownloadUrl(FILE)).thenReturn(FILE);

        String url = "/products" + SLASH + FILE_URL + "?" + FILE_NAME_KEY + "=" + FILE;
        String expectedResponse = asJsonString(Collections.singletonMap(FILE_URL, FILE));

        ResultActions result = performGetRequest(url);
        result.andExpect(status().isOk());
        result.andExpect(content().string(containsString(expectedResponse)));
    }

    @Test
    public void testGetDownloadFileUrlError()
    {
        when(mockAzureStorageService.getDownloadUrl(FILE)).thenReturn(null);

        String url = "/products" + SLASH + FILE_URL + "?" + FILE_NAME_KEY + "=" + FILE;

        assertThatThrownBy(() -> performGetRequest(url))
            .hasCause(noFoundException);
    }
}