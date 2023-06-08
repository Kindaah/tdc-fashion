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
import { Router, ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

// Helpers
import * as UserHelper from '../../helpers/userHelper';
import { FormValidatorHelper, IFormValidatorHelper } from '../../helpers/form/formValidatorHelper';

// Services
import { AuthService } from '../../services/auth.service';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';

/**
 * @author Daniel Mejia
 * @file login.component.ts
 * @description Login component
 */
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {

  /**
   * The login form group.
   *
   *  @type { FormGroup }
   */
  loginForm: FormGroup;

  /**
   * User auth data.
   *
   * @type { any }
   */
  loginFormFields: any = {
    username: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  };

  /**
   * The forgot Password form group.
   *
   *  @type { FormGroup }
   */
  forgotPasswordForm: FormGroup;

  /**
   * User auth data.
   *
   * @type { any }
   */
  forgotformFields: any = {
    email: ['', [Validators.required, Validators.email]],
  };

  /**
   * Message to show.
   *
   * @type { string }
   */
  message: string;

  /**
   * The type Message to show.
   *
   * @type { string }
   */
  typeMessage: string;

  /**
  * Variable to control the view showed.
  */
  showLogin = true;

  /**
   * The form validator helpero to validates the FormGroup.
   *
   * @type { IFormValidatorHelper<FormGroup> }
   */
  formValidatorHelper: IFormValidatorHelper<FormGroup>;

  /**
   * The return url after login.
   *
   * @type { string }
   */
  returnUrl: string;

  /**
   * Creates an instance of LoginComponent.
   */
  constructor(
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    public spinner: NgxSpinnerService,
    private formBuilder: FormBuilder) {
    if (UserHelper.isSessionValid()) {
      this.router.navigate(['dashboard']);
    } else {
      UserHelper.cleanLocalStorage();
      this.loginForm = this.formBuilder.group(this.loginFormFields);
      this.forgotPasswordForm = this.formBuilder.group(this.forgotformFields);
      this.formValidatorHelper = new FormValidatorHelper(this.loginForm);
    }
  }

  /**
   * Callback on init component.
   */
  ngOnInit() {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';
    if (this.returnUrl === 'register') {
      this.returnUrl = '/dashboard';
      this.manageMessage('User created successfully. Please check your email and verify it in order to access the application.');
    }
  }

  /**
   * Authenticates a user.
   *
   */
  login(): void {
    this.spinner.show();
    this.authService.login(this.loginForm.value)
      .then(() => this.router.navigateByUrl(this.returnUrl))
      .catch(this.manageError);
  }

  /**
   * Send the recover password to the email.
   */
  sendRecoverPassword(): void {
    this.spinner.show();
    this.authService.sendRecoverPassword(this.forgotPasswordForm.value.email)
      .then(this.manageMessage)
      .catch(this.manageError);
  }

  /**
   * Controls the login or password recover view showed.
   *
   * @param { boolean } showLogin The variable to control the view showed.
   */
  changeView(showLogin: boolean): void {
    this.showLogin = showLogin;
    this.loginForm.reset();
    this.forgotPasswordForm.reset();
  }

  /**
   * Manage the message to show.
   *
   * @param message the response message.
   */
  manageMessage = (message: string): void => {
    this.spinner.hide();
    this.message = message;
    this.typeMessage = 'success';
    this.forgotPasswordForm.reset();
    this.showLogin = true;
  }

  /**
   * Manage the error menssage to show.
   *
   * @param error the error message.
   */
  manageError = (error: string): void => {
    this.spinner.hide();
    this.message = error;
    this.typeMessage = 'danger';
    this.loginForm.valueChanges.subscribe(() => this.message = '');
    this.forgotPasswordForm.valueChanges.subscribe(() => this.message = '');
  }
}
