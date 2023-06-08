/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { TestBed } from '@angular/core/testing';
import { AesUtil } from '../aesUtil';

/**
 * @author Edson Ruiz Ramirez
 * @file aesUtil.spec.ts
 * @description Aes util test class.
 */
describe('Utilities: AesUtil', () => {
  const testPass = 'Test password';
  const testSalt = 'Test salt';
  let aesUtil: AesUtil;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AesUtil
      ]
    });
    aesUtil = TestBed.get(AesUtil);
  });

  it('Constructor: Should be created', () => {
    expect(aesUtil).toBeTruthy();
  });

  it('encryptPassword: Should return the encrypted pass', () => {
    const encPass = aesUtil.encryptPassword(testPass);
    expect(encPass).toBeDefined();
  });

  it('generateKey: Should generate the key', () => {
    const key = aesUtil.generateKey(testSalt);
    expect(key).toBeDefined();
  });
});
