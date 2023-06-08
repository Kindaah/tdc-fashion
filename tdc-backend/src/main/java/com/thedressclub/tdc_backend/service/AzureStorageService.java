/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import static com.microsoft.applicationinsights.core.dependencies.googlecommon.base.Preconditions.checkArgument;
import static com.microsoft.applicationinsights.core.dependencies.googlecommon.base.Preconditions.checkNotNull;
import static java.util.Objects.requireNonNull;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import com.microsoft.azure.storage.blob.SharedAccessBlobPermissions;
import com.microsoft.azure.storage.blob.SharedAccessBlobPolicy;
import com.thedressclub.tdc_backend.controller.config.RestException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.time.Instant;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.bytecode.stackmap.TypeData.ClassName;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Services for the Azure Storage.
 *
 * @author Gustavo Nunez.
 * @author Daniel Mejia.
 */
@Service("azureStorageService")
@Transactional
public class AzureStorageService implements IAzureStorageService
{
    private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());

    private static final String NOT_UPLOAD_FILES = "not_upload_files";
    private static final String SAVING_FILE_FORMAT = "saving_file";
    private static final String DELETING_FILE = "deleting_file";
    private static final long HOUR_SECONDS = 3600;

    static final String POLICY_KEY = "DownloadPolicy";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CloudStorageAccount cloudStorageAccount;

    @Value("${azure.container}")
    private String containerUrl;

    /** (non-Javadoc) @see {@link IAzureStorageService} */
    @Override
    public String storeFile(String basePrefix, MultipartFile file)
    {
        String uploadFileName = null;
        try
        {
            checkNotNull(file);
            String fileName = requireNonNull(file.getOriginalFilename());

            CloudBlobContainer container = getCloudContainer();
            container.createIfNotExists();

            uploadFileName = basePrefix + fileName;
            CloudBlockBlob blob = container.getBlockBlobReference(uploadFileName);
            InputStream inputStream = new BufferedInputStream(file.getInputStream());
            blob.upload(inputStream, -1L);

            String messageFormat = messageSource.getMessage(SAVING_FILE_FORMAT, null, Locale.US);
            String message = String.format(messageFormat, uploadFileName);

            LOGGER.log(Level.INFO, message);
        }
        catch (NullPointerException exception)
        {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);
        }
        catch (IOException | URISyntaxException | StorageException ex)
        {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);

            throw new RestException(NOT_UPLOAD_FILES, HttpStatus.BAD_REQUEST);
        }

        return uploadFileName;
    }

    /** (non-Javadoc) @see {@link IAzureStorageService} */
    @Override
    public void deleteFiles(String basePrefix)
    {
        try
        {
            CloudBlobContainer container = getCloudContainer();

            for (ListBlobItem item : container.listBlobs(basePrefix))
            {
                CloudBlockBlob file = (CloudBlockBlob) item;
                file.deleteIfExists();

                String messageFormat = messageSource.getMessage(DELETING_FILE, null, Locale.US);
                String message = String.format(messageFormat, file);

                LOGGER.log(Level.INFO, message);
            }
        }
        catch (URISyntaxException | StorageException exception)
        {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);
        }
    }

    /** (non-Javadoc) @see {@link IAzureStorageService} */
    @Override
    public String getDownloadUrl(String fileName)
    {
        String downloadUrl = null;
        try
        {
            CloudBlobContainer container = getCloudContainer();
            BlobContainerPermissions permissions = getAccessPermissions();
            SharedAccessBlobPolicy itemPolicy = getAccessPolicy();

            container.uploadPermissions(permissions);
            CloudBlockBlob blob = container.getBlockBlobReference(fileName);
            checkArgument(blob.exists());

            String baseUrl = blob.getUri().toString();
            String accessToken = blob.generateSharedAccessSignature(itemPolicy, POLICY_KEY);

            downloadUrl = String.format("%s?%s", baseUrl, accessToken);

        }
        catch (URISyntaxException | StorageException | InvalidKeyException | IllegalArgumentException ex)
        {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }

         return downloadUrl;
    }

    /** (non-Javadoc) @see {@link IAzureStorageService} */
    @Override
    public void setContainerUrl(String containerUrl)
    {
        this.containerUrl = containerUrl;
    }

    private CloudBlobContainer getCloudContainer()
    throws URISyntaxException, StorageException
    {
        CloudBlobClient blobClient = cloudStorageAccount.createCloudBlobClient();

        return blobClient.getContainerReference(containerUrl);
    }

    private BlobContainerPermissions getAccessPermissions()
    {
        SharedAccessBlobPolicy readPolicy = new SharedAccessBlobPolicy();
        readPolicy.setPermissions(EnumSet.of(SharedAccessBlobPermissions.READ));

        BlobContainerPermissions permissions = new BlobContainerPermissions();
        permissions.getSharedAccessPolicies().put(POLICY_KEY, readPolicy);

        return permissions;
    }

    private SharedAccessBlobPolicy getAccessPolicy()
    {
        SharedAccessBlobPolicy itemPolicy = new SharedAccessBlobPolicy();

        Instant now = Instant.now().minusSeconds(900);
        Date startTime = Date.from(now);

        now = Instant.now().plusSeconds(HOUR_SECONDS);
        Date expirationTime = Date.from(now);

        itemPolicy.setSharedAccessStartTime(startTime);
        itemPolicy.setSharedAccessExpiryTime(expirationTime);

        return itemPolicy;
    }
}