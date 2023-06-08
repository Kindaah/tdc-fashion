/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import static com.thedressclub.tdc_backend.service.AzureStorageService.POLICY_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import com.microsoft.azure.storage.blob.SharedAccessBlobPolicy;
import com.thedressclub.tdc_backend.controller.config.RestException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Test class for {@link AzureStorageService}.
 *
 * @author Daniel Mejia.
 */
public class AzureStorageServiceTest
{
    private static final String PREFIX = "test/";
    private static final String FILE_NAME = "fileName.extension";
    private static final String CONTAINER_URL = "container";
    private static final String ACCESS_TOKEN = "token";
    private static final String LOG_MESSAGE = "log message";

    @Mock
    private CloudStorageAccount mockCloudStorageAccount;

    @Mock
    private CloudBlobClient mockCloudBlobClient;

    @Mock
    private MessageSource mockMessageSource;

    @Mock
    private CloudBlobContainer mockContainer;

    @Mock
    private CloudBlockBlob mockBlob;

    @Mock
    private InputStream mockInputStream;

    @Mock
    private MultipartFile mockFile;

    @Mock
    private URI mockURI;

    @Mock
    private URISyntaxException mockUriSyntaxException;

    @InjectMocks
    private IAzureStorageService instance;

    @Before
    public void setUp()
    throws URISyntaxException, StorageException, IOException
    {
        instance = new AzureStorageService();
        instance.setContainerUrl(CONTAINER_URL);
        MockitoAnnotations.initMocks(this);

        when(mockCloudStorageAccount.createCloudBlobClient()).thenReturn(mockCloudBlobClient);
        when(mockCloudBlobClient.getContainerReference(CONTAINER_URL)).thenReturn(mockContainer);
        when(mockFile.getOriginalFilename()).thenReturn(FILE_NAME);
        when(mockFile.getInputStream()).thenReturn(mockInputStream);
        when(mockMessageSource.getMessage(anyString(), eq(null), eq(Locale.US))).thenReturn(LOG_MESSAGE);
    }

    @Test
    public void testStoreFileSuccess()
    throws URISyntaxException, StorageException, IOException
    {
        String expectedUrl = PREFIX + FILE_NAME;
        when(mockContainer.getBlockBlobReference(expectedUrl)).thenReturn(mockBlob);

        String responseUrl = instance.storeFile(PREFIX, mockFile);

        assertEquals("The url response is wrong: " , expectedUrl, responseUrl);
        verify(mockBlob).upload(any(InputStream.class), eq(-1L));
    }

    @Test
    public void testStoreFileNull()
    {
        String responseUrl = instance.storeFile(PREFIX, null);

        assertNull("The url response is wrong: ", responseUrl);
    }

    @Test(expected = RestException.class)
    public void testStoreFileError()
    throws IOException
    {
        when(mockFile.getInputStream()).thenThrow(new IOException());

        instance.storeFile(PREFIX, mockFile);
    }

    @Test
    public void testDeleteFilesSuccess()
    throws StorageException
    {
        List<ListBlobItem> listBlobItems = Collections.singletonList(mockBlob);
        when(mockContainer.listBlobs(PREFIX)).thenReturn(listBlobItems);

        instance.deleteFiles(PREFIX);

        verify(mockBlob).deleteIfExists();
    }

    @Test
    public void testDeleteFilesError()
    throws URISyntaxException, StorageException
    {
        when(mockCloudBlobClient.getContainerReference(CONTAINER_URL))
            .thenThrow(mockUriSyntaxException);

        instance.deleteFiles(PREFIX);

        verifyZeroInteractions(mockBlob);
    }

    @Test
    public void testGetDownloadUrlSuccess()
    throws StorageException, InvalidKeyException, URISyntaxException
    {
        when(mockContainer.getBlockBlobReference(FILE_NAME)).thenReturn(mockBlob);
        when(mockBlob.getUri()).thenReturn(mockURI);
        when(mockURI.toString()).thenReturn(PREFIX + FILE_NAME);
        when(mockBlob.generateSharedAccessSignature(any(SharedAccessBlobPolicy.class), eq(POLICY_KEY)))
            .thenReturn(ACCESS_TOKEN);
        when(mockBlob.exists()).thenReturn(true);

        String responseUrl = instance.getDownloadUrl(FILE_NAME);
        String expectedUrl = String.format("%s?%s", PREFIX + FILE_NAME, ACCESS_TOKEN);

        assertEquals("The download url is wrong:", expectedUrl  ,responseUrl);
        verify(mockContainer).uploadPermissions(any(BlobContainerPermissions.class));
    }


    @Test
    public void testGetDownloadUrlError()
    throws URISyntaxException, StorageException
    {
        when(mockContainer.getBlockBlobReference(FILE_NAME)).thenThrow(mockUriSyntaxException);

        String responseUrl = instance.getDownloadUrl(FILE_NAME);

        assertNull("The download url is not null:", responseUrl);
        verify(mockContainer).uploadPermissions(any(BlobContainerPermissions.class));
    }
}