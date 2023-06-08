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
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';

// Services
import { UserService } from '../../services/user.service';
import { ShippingAddressService } from 'src/app/services/shippingAddress.service';

// Helpers
import * as UserHelper from '../../helpers/userHelper';
import * as UtilHelper from '../../helpers/utilHelper';


// Models
import { User } from '../../models/user';
import { ShippingAddress } from '../../models/shippingAddress';

/**
 * @author Edson Ruiz Ramirez
 * @file addresses.component.ts
 * @description Addresses component
 */

@Component({
  selector: 'app-addresses',
  templateUrl: './addresses.component.html',
  styleUrls: ['./addresses.component.css']
})
export class AddressesComponent implements OnInit {

  /**
   * The current user in the application.
   *
   * @type { User }
   */
  currentUser: User;

  /**
   * The selected shipping address.
   *
   * @type { ShippingAddress }
   */
  selectedShipping: ShippingAddress;

  /**
   * Creates an instance of Profile component.
   *
   * @param { UserService } userService the user service to make requests.
   * @param { NgxSpinnerService } spinner the loader to show.
   * @param { NgbModal } modalService The modal service.
   * @param { ShippingAddressService } shippingAddressService The shipping address service.
   */
  constructor(
    public userService: UserService,
    public spinner: NgxSpinnerService,
    public modalService: NgbModal,
    private shippingAddressService: ShippingAddressService
  ) { }

  /**
   * Callback on init component.
   */
  ngOnInit() {
    const userData = UserHelper.getUserData();
    this.spinner.show();
    this.userService.findByAuthKey(userData.authKey)
      .subscribe(this.setUser, this.manageError);
  }

  /**
   * Sets the user to the componenet and his properties.
   *
   * @param { User } storedUser The stored user fetched.
   */
  setUser = (storedUser: User): void => {
    this.currentUser = { ...storedUser };
    this.spinner.hide();
  }

  /**
   * Updates the shipping list after some operation
   *
   * @param { ShippingAddress } shippingAddress The shipping address to updates in the list.
   * @param { boolean } remove true if the shipping will be deleted, false otherwise.
   */
  updateShippingList = (shippingAddress: ShippingAddress, remove: boolean = false): void => {
    UtilHelper.updateList(this.currentUser.shippingAddresses, shippingAddress, remove);
  }

  /**
   * Deletes a shipping address.
   *
   * @param { number } shippingAddressId The shipping address id to delete.
   */
  deleteShippingAddress(shippingAddressId: number): void {
    this.shippingAddressService.deleteShippingAddress(shippingAddressId)
      .subscribe(deletedAddress => this.updateShippingList(deletedAddress, true), this.manageError);
  }

  /**
   * Selects a shipping address as current address.
   *
   * @param { ShippingAddress } shippingAddress The shipping address to select.
   */
  selectAddress(shippingAddress: ShippingAddress): void {
    this.selectedShipping = shippingAddress;
  }

  /**
   * Manage the error in the component.
   */
  manageError = (): void => {
    this.spinner.hide();
  }
}
