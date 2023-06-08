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
 * @file environment.prod.ts
 * @description Environment for production
 */

export const environment = {
  production: true,
  appName: 'TDC Fashion',
  app: 'https://app.tdc.fashion',
  apiDomain: 'api.tdc.fashion',
  api: 'https://api.tdc.fashion/v1',
  auth: {
    clientID: 'nzebw2DvhSb3Q7WUUxL3234f873nzLAW',
    domain: 'tdc-prod.auth0.com',
    connection: 'production',
    apiAudience: 'https://api.tdc.fashion',
    registerKey: 'oDjRemOgYUs/as1TS0LW9Erg+JeIc+j/jMWSrW2nIgo='
  },
  aes: {
    passPhrase: 'app.tdc.fashion',
    keySize: 128,
    iterationCount: 1000
  },
  payment: {
    stripeKey: 'pk_live_yspDrTfiBtcVcfmuiVxoIumX',
    plaid: {
      env: 'development',
      publicKey: '8a18454ef9989f02b6f1ead2a92387',
      product: 'auth',
    }
  },
  recaptchaKey: '6Lfa-4MUAAAAANbtNLIFIMvjsLLH96P8-0cpiJes'
};
