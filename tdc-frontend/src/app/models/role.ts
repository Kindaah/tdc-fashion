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
 * @file role.ts
 * @description Role model
 */
export interface Role extends Generic {

  /**
   * the role name.
   *
   * @type { string }
   */
  name: string;
}
