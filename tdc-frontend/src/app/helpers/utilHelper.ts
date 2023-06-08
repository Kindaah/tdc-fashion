/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { Generic } from '../models/generic';

/**
 * @author Daniel Mejia
 * @file utilHelper.ts
 * @description Utils functions.
 */

/**
 * Updates a list with the new parameter.
 *
 * @param { Generic[] } list The list to update.
 * @param { Generic } parameter The parameter.
 * @param { boolena } remove The flag to check if must remove the parameter from list.
 */
export const updateList = (list: Generic[], parameter: Generic, remove: boolean = false): void => {
  let index = list.findIndex(object => object.id === parameter.id);
  index = index >= 0 ? index : list.length;
  const execute = remove ? () => list.splice(index, 1) : () => list.splice(index, 1, parameter);
  execute();
};
