/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, Validators, FormBuilder, AbstractControl } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';

// Services
import { AuthService } from '../../services/auth.service';

// Helpers
import * as UserHelper from '../../helpers/userHelper';
import { FormValidatorHelper } from '../../helpers/form/formValidatorHelper';
import { CustomValidators } from '../../helpers/form/customValidators';
import { NotificationUtil } from '../../utilities/notificationUtil';

/**
 * @author Daniel Mejia
 * @file register.component.ts
 * @description Register component
 */

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})

export class RegisterComponent implements OnInit {

  /**
   * The register form group.
   *
   *  @type { FormGroup }
   */
  registerForm: FormGroup;

  /**
   * The form validator helpero to validates the FormGroup.
   *
   * @type { FormValidatorHelper<FormGroup> }
   */
  formValidatorHelper: FormValidatorHelper<FormGroup>;

  /**
   * Creates an instance of RegisterComponent.
   *
   * @param { Router } router dependencie Router
   * @param { FormBuilder } formBuilder dependencie FormBuilder
   * @param { AuthService } authService service AuthService
   * @param { NotificationUtil } notificationUtil helper NotificationUtil
   * @param { NgxSpinnerService } spinner dependencie NgxSpinnerService
   */
  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private notificationUtil: NotificationUtil,
    public spinner: NgxSpinnerService
    ) {
    if (UserHelper.isSessionValid()) {
      this.router.navigate(['dashboard']);
    } else {
      UserHelper.cleanLocalStorage();
      this.registerForm = this.formBuilder.group({
        authKey: [''],
        firstName: ['', Validators.required],
        lastName: ['', Validators.required],
        companyEmail: ['', [Validators.required, Validators.email]],
        phoneNumber: ['', Validators.required],
        agree: [false, Validators.requiredTrue],
        recaptchaField: ['', Validators.required],
        passwordGroup: this.formBuilder.group({
          password: ['', [Validators.required, Validators.minLength(6)]],
          confirmPassword: [''],
        }, { validator: CustomValidators.passwordMatch })
      });

      this.formValidatorHelper = new FormValidatorHelper(this.registerForm);
    }
  }

  /**
   * Callback on init component.
   */
  ngOnInit() {
  }

  /**
   * Register an user in the application.
   */
  register(): void {
    this.spinner.show();
    const { passwordGroup: { password }, ...user } = this.registerForm.value;
    this.authService.signUp(user, password)
      .then(this.onUserSignUp)
      .catch(this.manageError);
  }

  /**
   * Manage the process after user sign up.
   */
  onUserSignUp = (): void => {
    this.notificationUtil.onSuccess('User created sucessfully!');
    this.router.navigate(['login'], { queryParams: { returnUrl: 'register' }});
  }

  /**
   * Manage the error menssage to show.
   *
   * @param error the error message.
   */
  manageError = (error: any): void => {
    this.spinner.hide();
    if (error.code === 'user_exists') {
      this.registerForm.controls['companyEmail'].setErrors({ 'emailExists' : true });
    }
  }

  /**
   * Redirects to an external page.
   */
  redirect(): void {
    window.open('https://www.tdc.fashion/privacy/', '_blank');
  }

  /**
   * Gets the password group.
   *
   * @returns { AbstractControl } the abstract control of password group.
   */
  get passwordGroup(): AbstractControl {
    return this.registerForm.get('passwordGroup');
  }
}
