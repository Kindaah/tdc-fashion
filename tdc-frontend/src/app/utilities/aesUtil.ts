/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import * as CryptoJS from 'crypto-js';

/**
 * @author Daniel Mejia.
 * @file aesUtil.ts
 * @description Aes Util.
 */

@Injectable({ providedIn: 'root' })
export class AesUtil {

  /**
   * Passphrase for AES.
   *
   * @param { string }
   */
  passPhrase: string;

  /**
   * Passphrase for AES.
   *
   * @param { number }
   */
  keySize: number;

  /**
   * Passphrase for AES.
   *
   * @param { number }
   */
  iterationCount: number;

  /**
   * Constructor.
   */
  constructor() {
    this.passPhrase = environment.aes.passPhrase;
    this.keySize = environment.aes.keySize;
    this.iterationCount = environment.aes.iterationCount;
  }

  /**
   * Encrypts the password.
   *
   * @param { string } password The password to encrypt.
   *
   * @returns { string } the encrypted password.
   */
  encryptPassword(password: string): string {
    const iv = CryptoJS.lib.WordArray.random(this.keySize / 8).toString(CryptoJS.enc.Hex);
    const salt = CryptoJS.lib.WordArray.random(this.keySize / 8).toString(CryptoJS.enc.Hex);
    const key = this.generateKey(salt);
    const encrypted = CryptoJS.AES.encrypt(password, key, { iv: CryptoJS.enc.Hex.parse(iv) });
    const cipherPass = encrypted.ciphertext.toString(CryptoJS.enc.Base64);
    const aesPassword = (`${salt}::${iv}::${cipherPass}`);

    return btoa(aesPassword);
  }

  /**
   * Generates a key to encrypt.
   *
   * @param { string } salt The salt value.
   *
   * @returns { string } the generated key.
   */
  generateKey(salt: string): string {
    return CryptoJS.PBKDF2(this.passPhrase, CryptoJS.enc.Hex.parse(salt), { keySize: this.keySize / 32, iterations: this.iterationCount });
  }

}
