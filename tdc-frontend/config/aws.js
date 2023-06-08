/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

module.exports = {
  'dev': {
    's3': {
      'accessKeyId': process.env.AWS_ACCESS_KEY_ID,
      'secretAccessKey': process.env.AWS_SECRET_ACCESS_KEY,
      'region': 'us-east-1',
      'params': {
        'Bucket': 'app-dev.tdc.fashion'
      }
    },
    's3Headers': {},
    'cf': {
      'distribution': 'EPMFXZVCZN6AY',
      'accessKeyId': process.env.AWS_ACCESS_KEY_ID,
      'secretAccessKey': process.env.AWS_SECRET_ACCESS_KEY,
      'originPath': '/',
      'indexRootPath': 'true'
    }
  },
  'staging': {
    's3': {
      'accessKeyId': process.env.AWS_ACCESS_KEY_ID,
      'secretAccessKey': process.env.AWS_SECRET_ACCESS_KEY,
      'region': 'us-east-1',
      'params': {
        'Bucket': 'app-stg.tdc.fashion'
      }
    },
    's3Headers': {},
    'cf': {
      'distribution': 'E3HOKN4VLHKOAK',
      'accessKeyId': process.env.AWS_ACCESS_KEY_ID,
      'secretAccessKey': process.env.AWS_SECRET_ACCESS_KEY,
      'originPath': '/',
      'indexRootPath': 'true'
    }
  },
  'prod': {
    's3': {
      'accessKeyId': process.env.AWS_ACCESS_KEY_ID,
      'secretAccessKey': process.env.AWS_SECRET_ACCESS_KEY,
      'region': 'us-east-1',
      'params': {
        'Bucket': 'app.tdc.fashion'
      }
    },
    's3Headers': {},
    'cf': {
      'distribution': 'E2VIL44TK0CIC0',
      'accessKeyId': process.env.AWS_ACCESS_KEY_ID,
      'secretAccessKey': process.env.AWS_SECRET_ACCESS_KEY,
      'originPath': '/',
      'indexRootPath': 'true'
    }
  }
};