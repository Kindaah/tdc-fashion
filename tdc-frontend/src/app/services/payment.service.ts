/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenResponse } from '../dto/tokenResponse';
import { Payment } from '../models/payment';
import { Order } from '../models/order';
import { GenericService } from './generic.service';

/**
 * @author Daniel Mejia
 * @file payment.service.ts
 * @description Payment service.
 */
@Injectable({ providedIn: 'root' })
export class PaymentService extends GenericService<Payment> {

  // Constants
  private static resource = 'payments';

  /**
   * Creates an instance of PaymentService.
   *
   * @param { HttpClient } httpClient The HttpClient to make request calls.
   */
  constructor(httpClient: HttpClient) {
    super(httpClient, PaymentService.resource);
  }

  /**
   * Gets the bank token from using the data in the form.
   *
   * @param { string } publicToken The public token for the bank account.
   * @param { string } accountId The bank account owner id.
   *
   * @returns { Observable<TokenResponse> } The observable with a token response.
   */
  getBankToken(publicToken, accountId): Observable<TokenResponse> {
    const requestUrl = `${this.apiUrl}/bankToken`;
    const paramsObject = { publicToken, accountId };

    return this.httpClient.post<TokenResponse>(requestUrl, this.getHttpParams(paramsObject));
  }

  /**
   * Adds a payment charge for the order.
   *
   * @param { Order } order The order to add the payment.
   * @param { number } amount The payment amount
   * @param { string } bankToken The bank account token that will be charged.
   *
   * @returns { Observable<Payment> } The observable with the payment created.
   */
  addPaymentCharge(order: Order, amount: number, bankToken: string): Observable<Payment> {
    const requestUrl = `${this.apiUrl}/orders/${order.id}/bankCharge`;
    const paramsObject = { bankToken, amount };

    return this.httpClient.post<Payment>(requestUrl, this.getHttpParams(paramsObject));
  }
}
