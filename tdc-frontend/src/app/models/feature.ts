/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

import { Generic } from './generic';

/**
 * @author Daniel Mejia
 * @file feature.ts
 * @description Feature model
 */
export interface Feature extends Generic {

  /**
   * The feature color.
   *
   * @type { string }
   */
  color: string;

  /**
   * The feature size.
   *
   * @type { string }
   */
  size: string;

  /**
   * The feature quantity.
   *
   * @type { number }
   */
  quantity: number;

  /**
   * The feature sample quantity.
   *
   * @type { number }
   */
  sampleQuantity: number;

  /**
   * The patterns urls.
   *
   * @type { string }
   */
  patternsUrls: string;

  /**
   * The feature order in the product.
   *
   * @type { number }
   */
  orderInProduct: number;
}
