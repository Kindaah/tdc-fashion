/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { of } from 'rxjs';

// Services
import { OrderService } from '../order.service';

// Models
import { Order } from '../../models/order';
import { CountDTO } from './../../dto/countDTO';

/**
 * @author Daniel Mejia
 * @file order.service.spec.ts
 * @description Order Service test class
 */
describe('Service: OrderService', () => {
  let service: OrderService;
  let httpMock: HttpTestingController;
  const mockOrder: Order = {
    id: 1
  } as Order;
  const mockCountDTO: CountDTO = {
    entity: { id: 1 },
    count: 1
  } as CountDTO;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [OrderService]
    });
    service = TestBed.get(OrderService);
    httpMock = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('Constructor: Should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getAllDTO: Should get all order in a CountDTO', () => {
    service.getAllDTO()
      .subscribe(response => expect(response).toEqual([mockCountDTO]));

    const testRequest = httpMock.expectOne(service.apiUrl);
    expect(testRequest.request.method).toBe('GET');
    testRequest.flush([mockCountDTO]);
  });

  it('getDashboard: Should get the dashboard CountDTO', () => {
    service.getDashboard()
      .subscribe(response => expect(response).toEqual([mockCountDTO]));

    const testRequest = httpMock.expectOne(`${service.apiUrl}/dashboard`);
    expect(testRequest.request.method).toBe('GET');
    testRequest.flush([mockCountDTO]);
  });

  it('addOrderShippingAddress: Should add a shipping address to an order', () => {
    const addressId = 1;
    const shippingType = 'test';
    spyOn(service, 'updatePatch').and.returnValue(of(mockOrder));
    service.addOrderShippingAddress(mockOrder.id, addressId, shippingType)
      .subscribe(response => expect(response).toBe(mockOrder));
  });

  it('orderStateMachine: Should change the order state', () => {
    const nextState = 9;
    spyOn(service, 'updatePatch').and.returnValue(of(mockOrder));
    service.orderStateMachine(mockOrder, nextState)
      .subscribe(response => expect(response).toBe(mockOrder));
  });

  it('updateOrder: Should update the order', () => {
    const orderKey = 'testField';
    const params = { test: 'test' };
    spyOn(service, 'updatePatch').and.returnValue(of(mockOrder));
    service.updateOrder(orderKey, mockOrder, params)
      .subscribe(response => expect(response).toBe(mockOrder));
  });
});
