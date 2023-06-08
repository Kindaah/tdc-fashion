/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

// Helpers
import { NotificationUtil } from '../../utilities/notificationUtil';
import { FormValidatorHelper } from '../../helpers/form/formValidatorHelper';

// Components
import { LayoutComponent } from '../layout/layout.component';

// Services
import { ShippingAddressService } from '../../services/shippingAddress.service';

// Models
import { User } from '../../models/user';
import { ShippingAddress } from '../../models/shippingAddress';

/**
 * @author Daniel Mejia
 * @file address-form.component.ts
 * @description Address Form component
 */

@Component({
  selector: 'app-address-form',
  templateUrl: './address-form.component.html',
  styleUrls: ['./address-form.component.css']
})
export class AddressFormComponent implements OnInit {

  /**
   * The current user object.
   *
   * @type { User }
   */
  @Input() user: User;

  /**
   * The shipping address.
   *
   * @type { ShippingAddress }
   */
  @Input() shippingAddress: ShippingAddress;

  /**
   * Emiiter to notify the order when the product was saved.
   *
   * @type { EventEmitter<any> }
   */
  @Output() notifyShippingSaved: EventEmitter<ShippingAddress> = new EventEmitter<ShippingAddress>();

  /**
   * The form for the view.
   *
   * @type { FormGroup }
   */
  addressForm: FormGroup;

  /**
   * The form validator helper to validates the FormGroup.
   *
   * @type { FormValidatorHelper<FormGroup> }
   */
  formValidatorHelper: FormValidatorHelper<FormGroup>;

  /**
   * Constructor.
   *
   * @param { FormBuilder } formBuilder The form builder.
   * @param { LayoutComponent } layout The layout component.
   * @param { NotificationUtil } notificationUtil The notification util.
   * @param { ShippingAddressService } shippingAddressService The shipping address service.
   */
  constructor(
    private formBuilder: FormBuilder,
    private layout: LayoutComponent,
    private notificationUtil: NotificationUtil,
    private shippingAddressService: ShippingAddressService) {
      this.createForm();
  }

  /**
   * Called when the component is init.
   */
  ngOnInit() {
    if (this.shippingAddress) {
      this.setShippingAddress(this.shippingAddress);
    }
  }

  /**
   * Creates the form.
   */
  createForm(): void {
    this.addressForm = this.formBuilder.group({
      contactName: ['', Validators.required],
      country: ['', Validators.required],
      streetAddress: ['', Validators.required],
      streetAddressAddInfo: '',
      state: ['', Validators.required],
      city: ['', Validators.required],
      postalCode: ['', Validators.required],
      countryCode: ['', Validators.required],
      phoneNumber: ['', Validators.required]
    });
    this.formValidatorHelper = new FormValidatorHelper(this.addressForm);
  }

  /**
   * Sets the shipping address and reset the form with the form.
   *
   * @param { ShippingAddress } shippingAddress The shipping address.
   */
  setShippingAddress(shippingAddress: ShippingAddress): void {
    this.shippingAddress = shippingAddress;
    this.addressForm.reset(shippingAddress);
  }

  /**
   * Saves a shipping address.
   */
  saveShippingAddress = (): void => {
    this.layout.spinner.show();
    this.shippingAddressService.addShippingAddress(this.user.id, this.addressForm.value)
      .subscribe(this.onShippingAddressSaved);
  }

  /**
   * Saves a shipping address.
   */
  updateShippingAddress = (): void => {
    this.layout.spinner.show();
    this.shippingAddressService.updateShippingAddress(this.shippingAddress.id, this.addressForm.value)
      .subscribe(this.onShippingAddressSaved);
  }

  /**
   * Callback when the shipping is saved or updated.
   *
   * @param { ShippingAddress } storedAddress The stored address.
   */
  onShippingAddressSaved = (storedAddress: ShippingAddress): void => {
    this.layout.spinner.hide();
    this.notificationUtil.onSuccess('Shipping Address saved');
    this.notifyShippingSaved.next(storedAddress);
  }
}
