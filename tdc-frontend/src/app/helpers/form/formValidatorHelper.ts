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
 * @interface IFormValidatorHelper
 * @description The interface with the definition to the validation.
 */
export interface IFormValidatorHelper<T> {

  /**
   * The messages to show.
   */
  messages: object;

  /**
   * Options for the validation helper.
   *
   * @type { [key: string]: any }
   */
  options: { [key: string]: any };

  /**
   * Validates if the field in the form has errors.
   *
   * @param { any } field the field name if is a FormGroup, index if is a FormArray or nothing if is a FormControl.
   * @param { AbstractControl } form the optional form that overrides the default one.
   *
   * @returns true if the field has errors, false otherwise.
   */
  hasErrors(field?: any, form?: AbstractControl): boolean;

  /**
   * Validates if the field is valid.
   *
   * @param { any } field the field to validates.
   * @param { AbstractControl } form the optional form that overrides the default one.
   *
   * @returns true if the field is valid, false otherwise.
   */
  isValid(field?: any, form?: AbstractControl): boolean;

  /**
   * Gets the css class for the form validation according to the state.
   *
   * @param { any } field the field to validates.
   * @param { AbstractControl } form the optional form that overrides the default one.
   *
   * @returns the css class according to the form state.
   */
  getClassCss(field?: any, form?: AbstractControl): string;

  /**
   * Gets the list of errors for a field.
   *
   * @param { any } field the field.
   * @param { AbstractControl } form the optional form that overrides the default one.
   *
   * @returns { string[] } an array of messages with the error.
   */
  errors(field?: any, form?: AbstractControl): any[];

  /**
   * Validates if a specific form control is valid.
   *
   * @param { any } formControl to validates.
   *
   * @returns true if the form control is valid, false otherwise.
   */
  isValidFormControl(formControl: any): boolean;

  /**
  * Validates if a specific form control is has errors.
  *
  * @param { any } formControl to validates.
  *
  * @returns true if the form control has errors, false otherwise.
  */
  hasErrorsFormControl(formControl: any): boolean;

  /**
   * Gets the form control from the field and form.
   *
   * @param { any } field the optional field to search.
   * @param { any } form the form inside will be search, if no provided takes class.
   *
   * @returns { any } the form control.
   */
  getFormControl(field?: any, form?: any): any;
}

/**
 * @author Daniel Mejia
 * @class FormValidatorHelper
 * @description Generic class with the basic validation data.
 */
export class FormValidatorHelper<T extends any> implements IFormValidatorHelper<T> {

  /**
   * The messages to show.
   */
  messages = {
    'required': 'This field is required',
    'email': 'The email format is wrong',
    'emailExists': 'The email already was used',
    'mismatch': 'Password mismatch',
    'minlength': {
      'requiredLength': 'This field is too short. Must have at least $0 characters'
    },
    'maxlength': {
      'requiredLength': 'This field is too long. Must have min $0 characters'
    }
  };

  /**
   * Options for the validation helper.
   *
   * @type { [key: string]: any }
   */
  options: { [key: string]: any } = {
    validClass: 'is-valid',
    invalidClass: 'is-invalid'
  };

  /**
   * Creates an instance for FormValidatorHelper.
   *
   * @param { T } form the form to validates.
   */
  constructor(private form: T) { }

  /** @override (non-Jsdoc) @see {@link IFormValidatorHelper} */
  isValidFormControl(formControl: any): boolean {
    return !formControl.errors && formControl.dirty;
  }

  /** @override (non-Jsdoc) @see {@link IFormValidatorHelper} */
  hasErrorsFormControl(formControl: any): boolean {
    return formControl.errors && formControl.dirty;
  }

  /** @override (non-Jsdoc) @see {@link IFormValidatorHelper} */
  getFormControl(field?: any, form: any = this.form): any {
    return field ? form.controls[field] : form;
  }

  /** @override (non-Jsdoc) @see {@link IFormValidatorHelper} */
  hasErrors(field?: any, form?: AbstractControl): boolean {
    const formControl = this.getFormControl(field, form);

    return this.hasErrorsFormControl(formControl);
  }

  /** @override (non-Jsdoc) @see {@link IFormValidatorHelper} */
  isValid(field?: any, form?: AbstractControl): boolean {
    const formControl = this.getFormControl(field, form);

    return this.isValidFormControl(formControl);
  }

  /** @override (non-Jsdoc) @see {@link IFormValidatorHelper} */
  getClassCss(field?: any, form?: AbstractControl): string {
    const formControl = this.getFormControl(field, form);

    if (this.isValidFormControl(formControl)) {
      return this.options['validClass'];
    }

    if (this.hasErrorsFormControl(formControl)) {
      return this.options['invalidClass'];
    }

    return '';
  }

  /** @override (non-Jsdoc) @see {@link IFormValidatorHelper} */
  errors(field?: any, form?: AbstractControl): string[] {
    const formControl = this.getFormControl(field, form);
    const errors = formControl.errors || {};

    return Object.entries(errors).map(([errorKey, errorValue]) => {
      if (errorKey === 'minlength' || errorKey === 'maxlength') {
        const nestedError = Object.entries(errorValue)[0];
        const nestedErrorKey = nestedError[0];
        const nestedErrorValue = nestedError[1];
        return this.messages[errorKey][nestedErrorKey].replace('$0', nestedErrorValue);
      } else {
        return this.messages[errorKey];
      }
    });
  }
}
