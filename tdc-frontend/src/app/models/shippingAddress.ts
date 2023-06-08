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
 * @file shippingAddress.ts
 * @description Shipping Address model
 */

// Models
import { Generic } from './generic';

export interface ShippingAddress extends Generic {

  /**
   * Contact name.
   *
   * @type { string }
   */
  contactName: string;

  /**
   * Country.
   *
   * @type { string }
   */
  country: string;

  /**
   * Street address.
   *
   * @type { string }
   */
  streetAddress: string;

  /**
   * Street address info.
   *
   * @type { string }
   */
  streetAddressAddInfo: string;

  /**
   * State.
   *
   * @type { string }
   */
  state: string;

  /**
   * City.
   *
   * @type { string }
   */
  city: string;

  /**
   * Postal Code.
   *
   * @type { string }
   */
  postalCode: string;

  /**
   * Country code.
   *
   * @type { string }
   */
  countryCode: string;

  /**
   * Phone number.
   *
   * @type { string }
   */
  phoneNumber: string;

}
