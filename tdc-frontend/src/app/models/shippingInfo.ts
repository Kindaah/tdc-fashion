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
 * @file shippingInfo.ts
 * @description Shipping Info model
 */

// Models
import { Generic } from './generic';

export interface ShippingInfo extends Generic {

  /**
   * Company name.
   *
   * @type { string }
   */
  company: string;

  /**
   * Tracking id.
   *
   * @type { string }
   */
  trackingId: string;

  /**
   * Details of the shipping.
   *
   * @type { string }
   */
  details: string;

}
