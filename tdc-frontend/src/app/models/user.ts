/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

import { Generic } from './generic';
import { Role } from './role';
import { Company } from './company';
import { ShippingAddress } from './shippingAddress';

/**
 * @author Daniel Mejia
 * @file user.ts
 * @description User model
 */
export interface User extends Generic {

  /**
   * Authentication key.
   *
   * @type { string }
   */
  authKey: string;

  /**
   * Payment customer key.
   *
   * @type { string }
   */
  paymentKey: string;

  /**
   * First name.
   *
   * @type { string }
   */
  firstName: string;

  /**
   * Last name.
   *
   * @type { string }
   */
  lastName: string;

  /**
   * Company email.
   *
   * @type { string }
   */
  companyEmail: string;

  /**
   * Phone number.
   *
   * @type { string }
   */
  phoneNumber: string;

  /**
   * The user role.
   *
   * @type { Role }
   */
  role: Role;

  /**
   * The company of the user.
   *
   * @type { Company }
   */
  company: Company;

  /**
   * The shipping adresses list.
   *
   * @type { ShippingAddress[] }
   */
  shippingAddresses: ShippingAddress[];
}
