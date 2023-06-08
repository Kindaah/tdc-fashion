/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.business;

import static com.microsoft.applicationinsights.core.dependencies.googlecommon.base.Preconditions.checkArgument;
import static com.microsoft.applicationinsights.core.dependencies.googlecommon.base.Preconditions.checkNotNull;
import static java.lang.String.format;

import com.thedressclub.tdc_backend.model.Feature;
import com.thedressclub.tdc_backend.model.Product;
import com.thedressclub.tdc_backend.service.AzureStorageService;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javassist.bytecode.stackmap.TypeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Contains the helpers and logic methods for {@link Product}.
 *
 * @author Daniel Mejia
 */
@Component
public class ProductBusiness
{
    private static final Logger LOGGER = Logger.getLogger(TypeData.ClassName.class.getName());

    static final String BASE_NAME_FORMAT = "orders/order_%s/product_%s/%s/";
    static final String FEATURE_PREFIX_FORMAT = "feature_%s/%s/";
    static final CharSequence COMMA_DELIMITER = ",";
    static final String FEATURES_FOLDER = "features";

    @Autowired
    private AzureStorageService azureStorageService;

    /**
     * Gets the function to generate a base prefix.
     *
     * @param product the product to generate the prefix.
     *
     * @return A function that has an subfolder input and returns the prefix.
     */
    public Function<String, String> getBasePrefix(Product product)
    {
        return folder ->
            format(BASE_NAME_FORMAT, product.getOrder().getId(), product.getId(), folder);
    }

    /**
     * Gets the function to generate a base prefix for upload in azure.
     *
     * @param feature the feature to generate the prefix.
     *
     * @return A function that has an subfolder input and returns the prefix.
     */
    public Function<String, String> getBasePrefix(Feature feature)
    {
        Product product = feature.getProduct();
        String productPrefix = getBasePrefix(product).apply(FEATURES_FOLDER);

        return folder ->
            productPrefix + format(FEATURE_PREFIX_FORMAT, feature.getId(), folder);
    }

    /**
     * Store a list of files in the azure storage.
     *
     * @param basePrefix the prefix for save the files.
     * @param files list.
     * @param storedUrls the old urls stored.
     *
     * @return the new urls stored.
     */
    public String storeFiles(String basePrefix, MultipartFile[] files, String storedUrls)
    {
        try
        {
            checkNotNull(files);
            checkArgument(files.length > 0);

            azureStorageService.deleteFiles(basePrefix);
            storedUrls =
                Stream.of(files)
                    .map(file -> azureStorageService.storeFile(basePrefix, file))
                    .collect(Collectors.joining(COMMA_DELIMITER));
        }
        catch (NullPointerException | IllegalArgumentException exception)
        {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);
        }

        return storedUrls;
    }
}
