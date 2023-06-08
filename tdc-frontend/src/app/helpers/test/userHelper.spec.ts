/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

import * as UserHelper from '../userHelper';

/**
 * @author Daniel Mejia
 * @file userHelper.spec.ts
 * @description Test class for {@link UserHelper}
 */
describe('User Helper', () => {
  const dataToken = {
    accessToken: 'test_token',
    idToken: 'test_api'
  };
  const userData = {
    authKey: 'testKey',
    companyEmail: 'testEmail',
    role: {
      id: 1
    }
  };

  beforeEach(() => {
    UserHelper.setUserData(userData);
    UserHelper.setSessionData(dataToken);
  });

  afterEach(() => {
    UserHelper.cleanLocalStorage();
  });

  it('getUserData: should save user localStorage', () => {
    expect(UserHelper.getUserData()).toBeTruthy();
  });

  it('isSessionValid: Should validate if user is logged', () => {
    expect(UserHelper.isSessionValid()).toBeTruthy();
  });

  it('isSessionValid: Should validate if user is not logged', () => {
    UserHelper.cleanLocalStorage();
    expect(UserHelper.isSessionValid()).toBeFalsy();
  });

  it('getAccessToken: Should validate if access token is valid', () => {
    expect(UserHelper.getAccessToken()).toBe(dataToken.accessToken);
  });

  it('sessionUserIs: Should check user session has role', () => {
    expect(UserHelper.sessionUserIs('ADMIN')).toBeTruthy();
  });
});
