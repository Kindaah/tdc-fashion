/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

import { Component, Input } from '@angular/core';
import { Payment } from 'src/app/models/payment';

/**
 * @author Daniel Mejia
 * @file payment.component.css
 * @description Payment styles
 */

@Component({
  selector: 'app-payment-info',
  templateUrl: './payment-info.component.html',
  styleUrls: ['./payment-info.component.css']
})
export class PaymentInfoComponent {

  /**
   * Object payment.
   *
   * @type { Payment }
   */
  @Input() payment: Payment;

  /**
   * Creates an instance of PaymentInfoComponent
   */
  constructor() { }
}
