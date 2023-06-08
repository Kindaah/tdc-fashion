/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Enviroments
import { environment } from '../../environments/environment';

// Services
import { GenericService } from './generic.service';

// Models
import { ShippingInfo } from '../models/shippingInfo';

/**
 * @author Edson Ruiz Ramirez
 * @file shippingInfo.service.ts
 * @description Shipping Info service
 */
@Injectable({ providedIn: 'root' })
export class ShippingInfoService extends GenericService<ShippingInfo> {

  // Constants
  private static resource = 'shippingInfo';

  /**
   * Creates an instance of ShippingInfoService.
   *
   * @param { HttpClient } httpClient The HttpClient to make request calls.
   */
  constructor(httpClient: HttpClient) {
    super(httpClient, ShippingInfoService.resource);
  }

  /**
   * Add a shipping info.
   *
   * @param { number } orderId The Order of the Shipping Info.
   * @param { ShippingInfo } shippingInfo The shipping info to add.
   *
   * @returns { Observable<ShippingInfo> } The shipping info observable.
   */
  addShippingInfo(orderId: number, shippingInfo: ShippingInfo): Observable<ShippingInfo> {
    const requestUrl = `${environment.api}/orders/${orderId}/shippingInfo`;
    return this.add(shippingInfo, requestUrl);
  }

  /**
   * Update a shipping info.
   *
   * @param { number } shippingInfoId The Shipping Info id.
   * @param { ShippingInfo } shippingInfo The Shipping Info to update.
   *
   * @returns { Observable<ShippingInfo> } The shipping info observable.
   */
  updateShippingInfo(shippingInfoId: number, shippingInfo: ShippingInfo): Observable<ShippingInfo> {
    const requestUrl = `${environment.api}/orders/shippingInfo/${shippingInfoId}`;
    return this.update(shippingInfoId, shippingInfo, requestUrl);
  }
}
