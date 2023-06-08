/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.util;

import static com.thedressclub.tdc_backend.util.AesUtil.CIPHER_ALGORITHM;
import static com.thedressclub.tdc_backend.util.AesUtil.SECRET_KEY_ALGORITHM;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Test class for {@link AesUtil}.
 *
 * @author Daniel Mejia.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Cipher.class, SecretKeyFactory.class})
public class AesUtilTest
{
    private static final String TEST_AES_KEY = "";
    private static final int TEST_ITERATION_COUNT = 2;
    private static final int TEST_KEY_SIZE = 1;
    private static final String TEST_SALT = "1234";
    private static final String TEST_PASSWORD = "pass";
    private static final String TEST_IV = "";

    @Mock
    private Cipher mockCipher;

    @Mock
    private SecretKey mockSecretKey;

    @InjectMocks
    @Spy
    private AesUtil instance = new AesUtil(TEST_KEY_SIZE, TEST_ITERATION_COUNT, TEST_AES_KEY);

    @Mock
    private SecretKeyFactory mockSecretKeyFactory;

    public AesUtilTest()
    throws NoSuchAlgorithmException, NoSuchPaddingException
    {
    }

    @Before
    public void setUp()
    throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        mockStatic(Cipher.class);
        mockStatic(SecretKeyFactory.class);
        MockitoAnnotations.initMocks(this);
        when(Cipher.getInstance(CIPHER_ALGORITHM)).thenReturn(mockCipher);
    }

    @Test
    public void testDecryptSuccess()
    throws NoSuchAlgorithmException, DecoderException, InvalidKeySpecException, BadPaddingException,
        IllegalBlockSizeException
    {
        doReturn(mockSecretKey).when(instance).generateKey(TEST_SALT, TEST_AES_KEY);
        when(mockCipher.doFinal(Base64.decodeBase64(TEST_PASSWORD))).thenReturn(new byte[]{});

        String response = instance.decrypt(TEST_SALT, TEST_IV, TEST_PASSWORD);

        assertThat(response).isEqualTo(new String(new byte[]{}, StandardCharsets.UTF_8));
    }

    @Test
    public void testDecryptFail()
    throws BadPaddingException, IllegalBlockSizeException
    {
        doThrow(new BadPaddingException()).when(mockCipher).doFinal(Base64.decodeBase64(TEST_PASSWORD));

        String response = instance.decrypt(TEST_SALT, TEST_IV, TEST_PASSWORD);

        assertThat(response).isNull();
    }

    @Test
    public void generateKey()
    throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException
    {
        when(SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM)).thenReturn(mockSecretKeyFactory);
        when(mockSecretKeyFactory.generateSecret(any(KeySpec.class))).thenReturn(mockSecretKey);
        when(mockSecretKey.getEncoded()).thenReturn(TEST_PASSWORD.getBytes());

        SecretKey response = instance.generateKey(TEST_SALT, TEST_AES_KEY);

        assertThat(response).isNotNull();
    }
}