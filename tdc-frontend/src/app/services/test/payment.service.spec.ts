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

// Services
import { PaymentService } from '../payment.service';

// Models
import { TokenResponse } from 'src/app/dto/tokenResponse';
import { Order } from 'src/app/models/order';
import { Payment } from 'src/app/models/payment';

/**
 * @author Daniel Mejia
 * @file payment.service.spec.ts
 * @description Payment Service test class
 */
describe('Service: PaymentService', () => {
  let service: PaymentService;
  let httpMock: HttpTestingController;
  const testPublicToken = 'public token';
  const testAccountId = 'account id';
  const testAmount = 10000;
  const testTokenResponse: TokenResponse = {
    accountToken: 'test token'
  } as TokenResponse;
  const testOrder: Order = {
    id: 1
  } as Order;
  const testPayment: Payment = {
    status: 'pending',
    paymentKey: 'testPaymentKey',
    amount: testAmount,
  } as Payment;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PaymentService]
    });
    service = TestBed.get(PaymentService);
    httpMock = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('Constructor: Should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getBankToken: Should get the bank token', () => {
    service.getBankToken(testPublicToken, testAccountId)
      .subscribe(response => expect(response).toBe(testTokenResponse));

    const testRequest = httpMock.expectOne(`${service.apiUrl}/bankToken`);
    expect(testRequest.request.method).toBe('POST');
    testRequest.flush(testTokenResponse);
  });

  it('addPaymentCharge: Should add a payment', () => {
    service.addPaymentCharge(testOrder, testAmount, testTokenResponse.accountToken)
      .subscribe(response => expect(response).toBe(testPayment));

    const testRequest = httpMock.expectOne(`${service.apiUrl}/orders/${testOrder.id}/bankCharge`);
    expect(testRequest.request.method).toBe('POST');
    testRequest.flush(testPayment);
  });
});
