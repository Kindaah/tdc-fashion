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
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

// Services
import { GenericService } from './generic.service';

// Models
import { ShippingAddress } from '../models/shippingAddress';

/**
 * @author Edson Ruiz Ramirez
 * @file shippingAddress.service.ts
 * @description Shipping Address service
 */
@Injectable({ providedIn: 'root' })
export class ShippingAddressService extends GenericService<ShippingAddress> {

  // Constants
  static resource = 'shippingAddress';

  /**
   * Creates an instance of ShippingAddressService.
   *
   * @param { HttpClient } httpClient The HttpClient to make request calls.
   */
  constructor(httpClient: HttpClient) {
    super(httpClient, ShippingAddressService.resource);
  }

  /**
   * Add a shipping address.
   *
   * @param { number } userId The user id of the Shipping address.
   * @param { ShippingAddress } shippingAddress The shipping address to add.
   *
   * @returns { Observable<ShippingAddress> } The shipping address observable.
   */
  addShippingAddress(userId: number, shippingAddress: ShippingAddress): Observable<ShippingAddress> {
    const requestUrl = `${environment.api}/users/${userId}/${ShippingAddressService.resource}`;
    return this.add(shippingAddress, requestUrl);
  }

  /**
   * Update a shipping address.
   *
   * @param { number } shippingAddressId The Shipping address id.
   * @param { ShippingAddress } shippingAddress The Shipping address to update.
   *
   * @returns { Observable<shippingAddres> } The shipping address updated observable.
   */
  updateShippingAddress(shippingAddressId: number, shippingAddress: ShippingAddress): Observable<ShippingAddress> {
    const requestUrl = `${environment.api}/users/${ShippingAddressService.resource}/${shippingAddressId}`;
    return this.update(shippingAddressId, shippingAddress, requestUrl);
  }

  /**
   * Deletes a shipping address.
   *
   * @param { number } shippingAddressId The Shipping address id.
   *
   * @returns { Observable<shippingAddres> } The shipping address deleted observable.
   */
  deleteShippingAddress(shippingAddressId: number): Observable<ShippingAddress> {
    const requestUrl = `${environment.api}/users/${ShippingAddressService.resource}/${shippingAddressId}`;
    return this.httpClient.delete<ShippingAddress>(requestUrl);
  }
}
