/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

/**
 * @author Daniel Mejia
 * @file environment.dev.ts
 * @description Environment for development
 */

export const environment = {
  production: false,
  appName: 'TDC Fashion',
  app: 'https://app-dev.tdc.fashion',
  apiDomain: 'api-dev.tdc.fashion',
  api: 'https://api-dev.tdc.fashion/v1',
  auth: {
    clientID: 'BIeHrTbSj3Ap21y1Z5kH5eQlL58FQNeL',
    domain: 'tdc-dev.auth0.com',
    connection: 'develop',
    apiAudience: 'https://api-dev.tdc.fashion',
    registerKey: 'XJeCha2HHLSXypKKFi+zNgAIGm7Ak5oHIvVShQG9AGY='
  },
  aes: {
    passPhrase: 'app.tdc.fashion.dev',
    keySize: 128,
    iterationCount: 1000
  },
  payment: {
    stripeKey: 'pk_test_gaHcGA7RnxPuHyEMzFbBhluz',
    plaid: {
      env: 'sandbox',
      publicKey: '8a18454ef9989f02b6f1ead2a92387',
      product: 'auth',
    }
  },
  recaptchaKey: '6LdzeoQUAAAAALSybtl2oSll6qm86jloEFqFuU83'
};
