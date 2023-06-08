/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Depedencies
import { Component, OnInit, Input } from '@angular/core';

// Models
import { ShippingInfo } from '../../models/shippingInfo';

/**
 * @author Edson Ruiz Ramirez
 * @file shipping-info.component.ts
 * @description Shipping Info component
 */

@Component({
  selector: 'app-shipping-info',
  templateUrl: './shipping-info.component.html',
  styleUrls: ['./shipping-info.component.css']
})
export class ShippingInfoComponent implements OnInit {

  /**
   * The ShippingInfo object.
   *
   * @type { ShippingInfo }
   */
  @Input() shippingInfo: ShippingInfo;

  /**
   * Creates an instance of ShippingInfoComponent.
   */
  constructor() { }

  /**
   * Callback on init component.
   */
  ngOnInit() {
  }

}
