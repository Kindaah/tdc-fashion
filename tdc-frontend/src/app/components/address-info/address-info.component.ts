/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { Component, Input, Output, EventEmitter } from '@angular/core';

// Models
import { ShippingAddress } from '../../models/shippingAddress';

/**
 * @author Cristian Ricardo
 * @file address-info.component.ts
 * @description Address component
 */

@Component({
  selector: 'app-address-info',
  templateUrl: './address-info.component.html',
  styleUrls: ['./address-info.component.css']
})
export class AddressInfoComponent {

  /**
   * The ShippingAddress.
   *
   * @type { ShippingAddress }
   */
  @Input() shippingAddress: ShippingAddress;

  /**
   * The ShippingAddress type.
   *
   * @type { string }
   */
  @Input() shippingAddressType: string;

  /**
   * Checks if the selector must be showed.
   *
   * @type { boolean }
   */
  @Input() showSelector = true;

  /**
   * Event for the shipping address selected.
   *
   * @type { EventEmitter<ShippingAddress> }
   */
  @Output() shippingAddressSelect = new EventEmitter<ShippingAddress>();

  /**
   * Flag that indicates the shipping address selected.
   *
   * @type { boolean }
   */
  @Input() selected = false;

  /**
   * Creates an instance of AddressComponent.
   */
  constructor() { }

  /**
   * Send the address selected.
   */
  addressSelected(): void {
    this.selected = !this.selected;
    this.shippingAddressSelect.emit(this.shippingAddress);
  }
}
