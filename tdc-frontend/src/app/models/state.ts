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
 * @file state.ts
 * @description State model
 */
export interface State extends Generic {

  /**
   * The state name.
   *
   * @type { string }
   */
  name: string;

  /**
   * The state sequence.
   *
   * @type { number }
   */
  sequenceState: number;
}
