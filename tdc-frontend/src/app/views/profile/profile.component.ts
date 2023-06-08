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
import { Validators, FormBuilder, FormGroup, AbstractControl } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';

// Services
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

// Helpers
import { FormValidatorHelper } from '../../helpers/form/formValidatorHelper';
import * as UserHelper from '../../helpers/userHelper';
import { CustomValidators } from '../../helpers/form/customValidators';
import { NotificationUtil } from '../../utilities/notificationUtil';

// Models
import { User } from '../../models/user';
import { AesUtil } from 'src/app/utilities/aesUtil';

/**
 * @author Daniel Mejia
 * @file profile.component.ts
 * @description User Profile component
 */
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})

export class ProfileComponent implements OnInit {

  /**
   * The register form group.
   *
   *  @type { FormGroup }
   */
  profileForm: FormGroup;

  /**
   * The new password form group.
   *
   *  @type { FormGroup }
   */
  changePasswordForm: FormGroup;

  /**
   * The form validator helper to validates the FormGroup.
   *
   * @type { FormValidatorHelper<FormGroup> }
   */
  formValidatorHelper: FormValidatorHelper<FormGroup>;

  /**
   * The form validator helper to validates the FormGroup for the password.
   *
   * @type { FormValidatorHelper<FormGroup> }
   */
  formValidatorHelperPassword: FormValidatorHelper<FormGroup>;

  /**
   * The current user in the application.
   *
   * @type { User }
   */
  currentUser: User;

  /**
   * Flat to indicate if the information can be updated.
   *
   * @type { boolean }
   */
  canEditInformation = false;

  /**
   * Creates an instance of Profile component.
   *
   * @param { FormBuilder } formBuilder the form builder.
   * @param { UserService } userService the user service to make requests.
   * @param { NgxSpinnerService } spinner the loader to show.
   * @param { NgbModal } modalService dependencie NgbModal
   * @param { NotificationUtil } notificationUtil helper NotificationUtil
   * @param { AesUtil } aesUtil helper AesUtil
   */
  constructor(
    private formBuilder: FormBuilder,
    public userService: UserService,
    public spinner: NgxSpinnerService,
    private modalService: NgbModal,
    private notificationUtil: NotificationUtil,
    private aesUtil: AesUtil,
    private authService: AuthService
    ) {
    this.profileForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      companyEmail: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.required],
    });

    this.changePasswordForm = this.formBuilder.group({
      currentPassword: ['', Validators.required],
      newPasswordGroup: this.formBuilder.group({
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: [''],
      }, { validator: CustomValidators.passwordMatch })
    });

    this.formValidatorHelper = new FormValidatorHelper(this.profileForm);
    this.formValidatorHelperPassword = new FormValidatorHelper(this.changePasswordForm);
  }

  /**
   * Callback on init component.
   */
  ngOnInit() {
    const userData = UserHelper.getUserData();
    this.spinner.show();
    this.userService.findByAuthKey(userData.authKey)
      .subscribe(this.setUser, this.spinnerHide);
  }

  /**
   * Updates the current user with the information in the form.
   */
  updateUser(): void {
    this.spinner.show();
    this.currentUser = Object.assign(this.currentUser, this.profileForm.value);
    this.userService.update(this.currentUser.id, this.currentUser)
      .subscribe(this.setUser, this.spinnerHide);
  }

  /**
   * Sets the user to the componenet and his properties.
   *
   * @param { User } storedUser the stored user fetched.
   */
  setUser = (storedUser: User): void => {
    this.currentUser = { ...storedUser };
    this.spinnerHide();
    this.resetForm();
  }

  /**
   * Open the modal to add a comment when the order will be reject.
   *
   * @param { any } content The modal to open.
   */
  openModal = (content: any): void => {
    this.changePasswordForm.reset();
    this.modalService.open(content, { size: 'lg' });
  }

  /**
   * Changes the password of the user.
   */
  changePassword(): void {
    const onPasswordChanged = (): void => {
      this.spinnerHide();
      this.notificationUtil.onSuccess('Password changed');
    };

    const { newPasswordGroup: { password } } = this.changePasswordForm.value;
    const userData = UserHelper.getUserData();
    this.userService.changePassword(userData.authKey, { passToken: this.aesUtil.encryptPassword(password)})
      .subscribe(onPasswordChanged, this.spinnerHide);
  }

  /**
   * Validates the current password.
   */
  validateUser(): void {
    const wrongPass = (): void => {
      this.spinnerHide();
      this.notificationUtil.onError('Current Password incorrect');
    };

    this.spinner.show();
    const { currentPassword: password } = this.changePasswordForm.value;
    this.authService.login({ username: this.currentUser.companyEmail, password })
      .then(() => this.changePassword())
      .catch(wrongPass);
  }

  /**
   * Close the spinner if an error happend.
   */
  spinnerHide = (): void => {
    this.spinner.hide();
  }

  /**
   * Resets the form to the initial state.
   */
  resetForm(): void {
    this.profileForm.reset(this.currentUser);
    this.canEditInformation = false;
  }

  /**
   * Gets the new password group.
   *
   * @returns { AbstractControl } the abstract control of password group.
   */
  get newPasswordGroup(): AbstractControl {
    return this.changePasswordForm.get('newPasswordGroup');
  }
}
