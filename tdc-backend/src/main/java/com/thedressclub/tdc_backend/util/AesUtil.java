/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.util;

import static com.thedressclub.tdc_backend.util.Utils.getMessage;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Aes Util to encrypt and decrypt information.
 *
 * @author Daniel Mejia.
 */
@Service
public class AesUtil
{
    private static final Logger LOGGER = Logger.getLogger(AesUtil.class.getName());

    static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
    static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String ERROR_DECRYPTING_AES = "error_decrypting_aes";
    private static final String ENCRYPTED_ALGORITHM = "AES";

    private int keySize;
    private int iterationCount;
    private String aesKey;
    private Cipher cipher;

    /**
     * Constructor.
     *
     * @param keySize The key size to generate the key.
     * @param iterationCount The iteration count to generate the key.
     * @param aesKey The pass phrase to generate the key.
     *
     * @throws NoSuchPaddingException If there an error when generate the cipher.
     * @throws NoSuchAlgorithmException If there an error when generate the cipher.
     */
    public AesUtil(
        @Value("${aes.key.size}") int keySize,
        @Value("${aes.iteration.count}")int iterationCount,
        @Value("${aes.pass.phrase}") String aesKey)
    throws NoSuchPaddingException, NoSuchAlgorithmException
    {
        this.keySize = keySize;
        this.iterationCount = iterationCount;
        this.aesKey = aesKey;
        cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    }

    /**
     * Decrypt a text with the salt and iv.
     *
     * @param salt to decrypt the text.
     * @param iv to decrypt the text.
     * @param cipherText to decrypt.
     *
     * @return The cypher text decrypted.
     */
    public String decrypt(String salt, String iv, String cipherText)
    {
        try
        {
            SecretKey key = generateKey(salt, aesKey);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Hex.decodeHex(iv.toCharArray())));
            byte[] decrypted = cipher.doFinal(Base64.decodeBase64(cipherText));

            return new String(decrypted, StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, getMessage(ERROR_DECRYPTING_AES));

            return null;
        }
    }

    /**
     * Generates a key using a salt and passPhrase.
     *
     * @param salt The salt to generate the key.
     * @param aesKey to genrate the key.
     *
     * @return The generated key.
     * @throws NoSuchAlgorithmException If an error is thrown.
     * @throws InvalidKeySpecException If an error is thrown.
     * @throws DecoderException If an error is thrown.
     */
    SecretKey generateKey(String salt, String aesKey)
    throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException
    {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(aesKey.toCharArray(), Hex.decodeHex(salt.toCharArray()), iterationCount, keySize);

        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), ENCRYPTED_ALGORITHM);
    }
}

