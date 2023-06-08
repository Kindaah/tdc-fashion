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
 * @file generic.ts
 * @description Generic model
 */
export interface Generic {

  /**
   * model id.
   *
   * @type { number }
   */
  id: number;

  /**
   * model create date.
   *
   * @type { number }
   */
  createdAt: string;

  /**
   * model updated date.
   *
   * @type { number }
   */
  updatedAt: string;
}
