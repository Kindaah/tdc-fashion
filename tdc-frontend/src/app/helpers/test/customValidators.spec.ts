/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { CustomValidators } from '../form/customValidators';
import { AbstractControl } from '@angular/forms';

/**
 * @author Daniel Mejia
 * @file customValidators.spec.ts
 * @description Test class for {@link CustomValidators}
 */
describe('CustomValidators', () => {

  describe('passwordMatch: Checks if an password match in a abstract control', () => {
    const mockControl: jasmine.SpyObj<AbstractControl> =
      jasmine.createSpyObj<AbstractControl>('AbstractControl', ['get']);

    it('Null controls', () => {
      mockControl.get.and.returnValue(null);
      const response = CustomValidators.passwordMatch(mockControl);
      expect(response).toBeNull();
    });

    it('Same values', () => {
      mockControl.get.and.returnValue({ value: 'testValue' });
      const response = CustomValidators.passwordMatch(mockControl);
      expect(response).toBeNull();
    });

    it('Mismatch values', () => {
      mockControl.get.and.returnValues({ value: 'testValue' }, { value: 'anotherValue' });
      const response = CustomValidators.passwordMatch(mockControl);
      expect(response.mismatch).toBeTruthy();
    });
  });
});
