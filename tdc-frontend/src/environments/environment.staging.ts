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
 * @file environment.staging.ts
 * @description Environment for staging
 */

export const environment = {
  production: false,
  appName: 'TDC Fashion',
  app: 'https://app-stg.tdc.fashion',
  apiDomain: 'api-stg.tdc.fashion',
  api: 'https://api-stg.tdc.fashion/v1',
  auth: {
    clientID: 'mJJ4QA9ZcN5VpyZ8ly4UHXraLr3XDizh',
    domain: 'tdc-stg.auth0.com',
    connection: 'staging',
    apiAudience: 'https://api-stg.tdc.fashion',
    registerKey: 'CYYXZxU7bmXcDfNwIV1zUKJBrWyFW+/7gk9XUWKSv+o='
  },
  aes: {
    passPhrase: 'app.tdc.fashion.stg',
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
  recaptchaKey: '6Lc-e4QUAAAAAKsm_KrljGKQBH0p8Ftnw4WbBVV3'
};
