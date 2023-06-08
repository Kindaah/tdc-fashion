/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { IFormValidatorHelper, FormValidatorHelper } from '../form/formValidatorHelper';
import { FormGroup, FormBuilder, AbstractControl } from '@angular/forms';

/**
 * @author Daniel Mejia
 * @file formValidatorHelper.spec.ts
 * @description Test class for {@link FormValidatorHelper}
 */
describe('Helpers: FormValidatorHelper', () => {
  const testField = 'test';
  let formValidator: IFormValidatorHelper<FormGroup>;
  let formGroup: FormGroup;
  let testForm: AbstractControl;

  beforeEach(() => {
    const formBuilder = new FormBuilder();
    formGroup = formBuilder.group({});
    testForm = formBuilder.control('');
    formValidator = new FormValidatorHelper<FormGroup>(formGroup);
  });

  it('isValidFormControl: Should verify if the form is valid', () => {
    formGroup.setErrors(null);
    formGroup.markAsDirty();
    const response = formValidator.isValidFormControl(formGroup);
    expect(response).toBeTruthy();
  });

  it('hasErrorsFormControl: Should verify if the form has errors', () => {
    formGroup.setErrors(['Error']);
    formGroup.markAsDirty();
    const response = formValidator.hasErrorsFormControl(formGroup);
    expect(response).toBeTruthy();
  });

  describe('getFormControl: Should get the form control', () => {

    it('Default control', () => {
      const response = formValidator.getFormControl();
      expect(response).toBe(formGroup);
    });

    it('Default control with field', () => {
      formGroup.setControl(testField, testForm);
      const response = formValidator.getFormControl(testField);
      expect(response).toBe(testForm);
    });

    it('Given control with field', () => {
      formGroup.setControl(testField, testForm);
      const response = formValidator.getFormControl(testField, formGroup);
      expect(response).toBe(testForm);
    });
  });

  it('hasErrors: Should verify if has errors', () => {
    spyOn(formValidator, 'getFormControl').and.returnValue(testForm);
    spyOn(formValidator, 'isValidFormControl').and.returnValue(true);
    const response = formValidator.isValid();
    expect(response).toBeTruthy();
  });

  it('isValid: Should verify if is valid', () => {
    spyOn(formValidator, 'getFormControl').and.returnValue(testForm);
    spyOn(formValidator, 'hasErrorsFormControl').and.returnValue(true);
    const response = formValidator.hasErrors();
    expect(response).toBeTruthy();
  });

  it('getClassCss: Should get the css class according to state', () => {
    spyOn(formValidator, 'getFormControl').and.returnValue(formGroup);
    spyOn(formValidator, 'isValidFormControl').and.returnValues(true, false, false);
    spyOn(formValidator, 'hasErrorsFormControl').and.returnValues(true, false);

    const responseValid = formValidator.getClassCss();
    expect(responseValid).toBe(formValidator.options['validClass']);

    const responseInvalid = formValidator.getClassCss();
    expect(responseInvalid).toBe(formValidator.options['invalidClass']);

    const responseNone = formValidator.getClassCss();
    expect(responseNone).toBeFalsy();
  });

  describe('errors: Should get errros list: ', () => {
    beforeEach(() => {
      spyOn(formValidator, 'getFormControl').and.returnValue(formGroup);
    });

    it('Default message', () => {
      formGroup.setErrors({ 'email': true });
      const response = formValidator.errors();
      expect(response).toContain(formValidator.messages['email']);
    });

    it('Numeric message', () => {
      const errorKey = 'minlength';
      const errorNestedKey = 'requiredLength';
      const minlengthTest = 6;
      const error = {
        [errorKey]: {
          [errorNestedKey]: minlengthTest
        }
      };
      formGroup.setErrors(error);
      const response = formValidator.errors();
      const expectedMessage = formValidator.messages[errorKey][errorNestedKey]
        .replace('$0', minlengthTest);
      expect(response).toContain(expectedMessage);
    });

    it('None message', () => {
      const response = formValidator.errors();
      expect(response).toEqual([]);
    });
  });
});
