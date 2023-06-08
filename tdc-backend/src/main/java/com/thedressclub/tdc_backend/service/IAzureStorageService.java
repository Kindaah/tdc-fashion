/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interface for the Azure Storage Service
 *
 * @author Gustavo Nunez.
 */
public interface IAzureStorageService
{
    /**
     * Send the file to store it in the Azure service.
     *
     * @param basePrefix the base name to upload the file.
     * @param fileName the name of the file to be stored.
     *
     * @return the url of the file in the Azure Storage.
     */
    String storeFile(String basePrefix, MultipartFile fileName);

    /**
     * Deletes a files under the basePrefix.
     *
     * @param basePrefix under the files will be deleted.
     */
    void deleteFiles(String basePrefix);

    /**
     * Gets the download url for a file.
     *
     * @return the download url.
     */
    String getDownloadUrl(String fileName);

    /**
     * Sets the container url.
     *
     * @param containerUrl to be set.
     */
    void setContainerUrl(String containerUrl);
}