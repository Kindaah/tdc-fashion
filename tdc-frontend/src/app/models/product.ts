/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

import { Generic } from './generic';
import { Feature } from './feature';
import { TechnicalSheetRow } from './technicalSheetRow';

/**
 * @author Daniel Mejia
 * @file product.ts
 * @description Product model
 */
export interface Product extends Generic {

  /**
   * the product description.
   *
   * @type { string }
   */
  description: string;

  /**
   * the product quote.
   *
   * @type { number }
   */
  quote: number;

  /**
   * the product sketch file urls.
   *
   * @type { string }
   */
  sketchFileUrls: string;

  /**
   * the product technical Sheet file urls.
   *
   * @type { string }
   */
  technicalSheetFileUrls: string;

  /**
   * the product model files urls.
   *
   * @type { string }
   */
  modelFileUrls: string;

  /**
   * the features list.
   *
   * @type { Feature[] }
   */
  features: Feature[];

  /**
   * the technicalSheetRows list.
   *
   * @type { TechnicalSheetRow[] }
   */
  technicalSheetRows: TechnicalSheetRow[];
}
