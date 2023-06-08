/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

/**
 * @author Edson Ruiz Ramirez
 * @file countDTO.ts
 * @description CountDTO model
 */
export interface CountDTO {

  /**
   * Variable to contain an entity of Order/State.
   *
   * @type { any }
   */
  entity: any;

  /**
   * Variable that counts the products/orders.
   *
   * @type { number }
   */
  count: number;
}
