/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

import { Generic } from './generic';
import { PaymentType } from './paymentType';

/**
 * @author Daniel Mejia
 * @file product.ts
 * @description Product model
 */
export interface Payment extends Generic {

  /**
   * The payment status.
   *
   * @type { string }
   */
  status: string;

  /**
   * The payment key.
   *
   * @type { string }
   */
  paymentKey: string;

  /**
   * The payment amount.
   *
   * @type { number }
   */
  amount: number;

  /**
   * The payment type.
   *
   * @type { PaymentType }
   */
  paymentType: PaymentType;
}
