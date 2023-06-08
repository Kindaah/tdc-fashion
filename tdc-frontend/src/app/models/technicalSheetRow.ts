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
 * @file technicalSheet.ts
 * @description TechnicalSheet model
 */
export interface TechnicalSheetRow extends Generic {

  /**
   * The technical sheet description.
   *
   * @type { string }
   */
  description: string;

  /**
   * The technical sheet tol value.
   *
   * @type { number }
   */
  tol: number;

  /**
   * The technical sheet twelve attribute.
   *
   * @type { string }
   */
  twelve: string;

  /**
   * The sizes object.
   *
   * @type { { [key: string]: string } }
   */
  sizes: { [key: string]: string };
}
