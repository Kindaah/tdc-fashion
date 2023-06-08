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

// Services
import { GenericService } from './generic.service';

// Models
import { CountDTO } from '../dto/countDTO';
import { Order } from '../models/order';

/**
 * @author Daniel Mejia
 * @file order.service.ts
 * @description Order service
 */
@Injectable({ providedIn: 'root' })
export class OrderService extends GenericService<Order> {

  // Constants
  private static resource = 'orders';

  /**
   * Creates an instance of OrderService.
   *
   * @param { HttpClient } httpClient The HttpClient to make request calls.
   */
  constructor(httpClient: HttpClient) {
    super(httpClient, OrderService.resource);
  }

  /**
   * Get all orders in a CountDTO object.
   *
   * @returns { Observable<CountDTO> } the countDTO observable.
   */
  getAllDTO(): Observable<CountDTO[]> {
    return this.httpClient.get<CountDTO[]>(this.apiUrl);
  }

  /**
   * Get all states with the Dashboard info.
   *
   * @returns { Observable<CountDTO> } the countDTO observable.
   */
  getDashboard(): Observable<CountDTO[]> {
    const requestUrl = `${this.apiUrl}/dashboard`;
    return this.httpClient.get<CountDTO[]>(requestUrl);
  }

  /**
   * Updates order and shipping address relation.
   *
   * @param { number } orderId the order ide.
   * @param { number } addressId the shipping address id.
   * @param { string } shippingType the params map.
   *
   * @returns { Observable<Order> } the typed observable.
   */
  addOrderShippingAddress(orderId: number, addressId: number, shippingType: string): Observable<Order> {
    const requestUrl = `${this.apiUrl}/${orderId}/shippingAddress/${addressId}`;
    return this.updatePatch(orderId, { shippingType }, requestUrl);
  }

  /**
   * Updates the order with the next state.
   *
   * @param { Order } Order the order to update.
   * @param { number } nextState the order state to update.
   *
   * @returns { Observable<T> } the typed observable.
   */
  orderStateMachine(order: Order, nextState: number): Observable<Order> {
    const requestUrl = `${this.apiUrl}/${order.id}/state/${nextState}`;
    return this.updatePatch(order.id, {}, requestUrl);
  }

  /**
   * Updates a specific params for an order.
   *
   * @param { Order } order the order to update.
   * @param { string } orderKey the specific url param to update.
   * @param { any } params the params map.
   *
   * @returns { Observable<T> } the typed observable.
   */
  updateOrder(orderKey: string, order: Order, params: any): Observable<Order> {
    const requestUrl = `${this.apiUrl}/${order.id}/${orderKey}`;
    return this.updatePatch(order.id, params, requestUrl);
  }
}
