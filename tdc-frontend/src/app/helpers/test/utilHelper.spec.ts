/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Helpers
import * as UtilHelper from '../utilHelper';

// Models
import { Generic } from 'src/app/models/generic';

/**
 * @author Daniel Mejia
 * @file utilHelper.spec.ts
 * @description Test class for {@link UtilHelper}
 */
describe('Util Helper', () => {
  let list: Generic[];
  const object1 = { id: 1 } as Generic;
  const object2 = { id: 2 } as Generic;

  beforeEach(() => {
    list = [ object1, object2 ];
  });

  it('updateList: should update the list, add', () => {
    const parameter = { id: 3 } as Generic;
    UtilHelper.updateList(list, parameter);
    expect(list).toBeTruthy(list.push(parameter));
  });

  it('updateList: should update the list, update', () => {
    const parameter = { id: 1 } as Generic;
    UtilHelper.updateList(list, parameter);
    expect(list).toBeTruthy([ parameter, object2 ]);
  });

  it('updateList: should update the list, remove', () => {
    const parameter = { id: 2 } as Generic;
    UtilHelper.updateList(list, parameter, true);
    expect(list).toBeTruthy([ object1 ]);
  });
});
