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
 * @author Edson Ruiz Ramirez
 * @file company.ts
 * @description Company model
 */

export interface Company extends Generic {

  /**
   * the company name.
   *
   * @type { string }
   */
  name: string;

  /**
   * the company country.
   *
   * @type { string }
   */
  country: string;

  /**
   * the company state.
   *
   * @type { string }
   */
  state: string;

  /**
   * the company city.
   *
   * @type { string }
   */
  city: string;

  /**
   * the company street address.
   *
   * @type { string }
   */
  streetAddress: string;

  /**
   * the company address aditional info.
   *
   * @type { string }
   */
  addressAditionalInfo: string;

  /**
   * the company postal code.
   *
   * @type { string }
   */
  postalCode: string;

  /**
   * the company website.
   *
   * @type { string }
   */
  website: string;

  /**
   * the company trademark name.
   *
   * @type { string }
   */
  trademarkName: string;

  /**
   * the company trademark registration number.
   *
   * @type { string }
   */
  trademarkRegistrationNumber: string;

  /**
   * the company sales tax permit.
   *
   * @type { string }
   */
  salesTaxPermit: string;

  /**
   * the company DUN number.
   *
   * @type { string }
   */
  dunNumber: string;

  /**
   * the company EIN number.
   *
   * @type { string }
   */
  einNumber: string;

  /**
   * the company nearest airport.
   *
   * @type { string }
   */
  nearestAirport: string;

  /**
   * the company 2ND neares airport.
   *
   * @type { string }
   */
  secondNearestAirport: string;

  /**
   * the company line of business.
   *
   * @type { string }
   */
  lineBusiness: string;
}
