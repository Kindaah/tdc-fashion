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
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

// Services
import { UserService } from '../../services/user.service';
import { CompanyService } from '../../services/company.service';

// Components
import { LayoutComponent } from '../../components/layout/layout.component';

// Helpers
import { FormValidatorHelper } from '../../helpers/form/formValidatorHelper';

// Models
import { User } from '../../models/user';
import { Company } from '../../models/company';

/**
 * @author Edson Ruiz Ramirez
 * @file company-info.component.ts
 * @description Company Info component
 */

@Component({
  selector: 'app-company-info',
  templateUrl: './company-info.component.html',
  styleUrls: ['./company-info.component.css']
})
export class CompanyInfoComponent implements OnInit {

  /**
   * The form for the view.
   *
   * @type { FormGroup }
   */
  companyInfoForm: FormGroup;

  /**
   * The error message to show.
   *
   * @type { any }
   */
  errorMessage: any;

  /**
   * Company object.
   *
   * @type { Company }
   */
  company: Company;

  /**
   * The form validator helper to validates the FormGroup.
   *
   * @type { FormValidatorHelper<FormGroup> }
   */
  formValidatorHelper: FormValidatorHelper<FormGroup>;

  /**
   * Flat to indicate if the information can be updated.
   *
   * @type { boolean }
   */
  canEditInformation = false;

  /**
   * The user id.
   *
   * @type { number }
   */
  userId: number;

  /**
   * Creates an instance of ShippingInfoComponent.
   *
   * @param { FormBuilder } formBuilder dependencie FormBuilder
   * @param { LayoutComponent } layout component LayoutComponent
   * @param { UserService } userService service UserService
   * @param { ActivatedRoute } route dependencie ActivatedRoute
   * @param { CompanyService } companyService service CompanyService
   */
  constructor(
    private formBuilder: FormBuilder,
    private layout: LayoutComponent,
    private userService: UserService,
    private route: ActivatedRoute,
    private companyService: CompanyService
  ) {
    this.createForm();
  }

  /**
   * Callback on init component.
   */
  ngOnInit(): void {
    this.userId = +this.route.snapshot.paramMap.get('id');
    this.getUser(this.userId);
  }

  /**
   * Creates the form.
   */
  createForm(): void {
    this.companyInfoForm = this.formBuilder.group({
      name: ['', Validators.required],
      country: ['', Validators.required],
      state: ['', Validators.required],
      city: ['', Validators.required],
      streetAddress: ['', Validators.required],
      addressAditionalInfo: [''],
      postalCode: ['', Validators.required],
      website: ['', Validators.required],
      trademarkName: ['', Validators.required],
      trademarkRegistrationNumber: ['', [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(8),
          Validators.pattern('^[0-9]{8}')
        ]],
      salesTaxPermit: ['', [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(10),
          Validators.pattern('^[0-9]{10}')
        ]],
      dunNumber: ['', [
          Validators.required,
          Validators.minLength(9),
          Validators.maxLength(9),
          Validators.pattern('^[0-9]{9}')
        ]],
      einNumber: ['', [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(10),
          Validators.pattern('^[0-9]{2}-[0-9]{7}')
        ]],
      nearestAirport: ['', Validators.required],
      secondNearestAirport: ['', Validators.required],
      lineBusiness: ['', Validators.required]
    });

    this.formValidatorHelper = new FormValidatorHelper(this.companyInfoForm);
  }

  /**
   * Set the company info.
   *
   * @param user the user object.
   */
  setCompany = (user: User) => {
    this.company = user.company;
    if (this.company) {
      this.companyInfoForm.patchValue(this.company);
      this.layout.spinner.hide();
    }
  }

  /**
   * Gets the user by id.
   *
   * @param userId to search the user.
   */
  getUser(userId: number): void {
    this.userService.findBy(userId)
      .subscribe(this.setCompany, this.manageError);
  }

  /**
   * Resets the form to the initial state.
   */
  resetForm(): void {
    this.companyInfoForm.reset(this.company);
    this.canEditInformation = false;
  }

  /**
   * Save the company info.
   */
  saveCompanyInfo(): void {
    this.layout.spinner.show();
    const setCompany = (company: Company) => {
      this.company = company;
      this.companyInfoForm.patchValue(company);
      this.resetForm();
      this.layout.spinner.hide();
    };
    if (this.company) {
      this.companyService.updateCompany(this.company.id, this.companyInfoForm.value)
        .subscribe(setCompany, this.manageError);
    } else {
      this.companyService.addCompany(this.userId, this.companyInfoForm.value)
        .subscribe(setCompany, this.manageError);
    }
  }

  /**
   * Manage when a error happens.
   *
   * @param error the error.
   */
  manageError = (error: any): void => {
    this.layout.spinner.hide();
    this.errorMessage = error;
  }
}
