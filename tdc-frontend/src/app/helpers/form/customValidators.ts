/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { AbstractControl } from '@angular/forms';

/**
 * @author Daniel Mejia
 * @class CustomValidators
 * @description Class with form custom validator.
 */
export class CustomValidators {

  /**
   * Validator to check if password match inside a control.
   *
   * @param { AbstractControl } control the control to check.
   *
   * @returns { { [key: string]: boolean } } the object with the key and value.
   */
  static passwordMatch(control: AbstractControl): { [key: string]: boolean } {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');

    if (!password || !confirmPassword) {
      return null;
    }

    if (password.value === confirmPassword.value) {
      return null;
    }

    return { mismatch: true };
  }
}
