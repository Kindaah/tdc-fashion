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
import { of } from 'rxjs';
import { PaymentUtil } from '../paymentUtil';

// Services
import { PaymentService } from '../../services/payment.service';

// Models
import { Order } from '../../models/order';
import { Payment } from '../../models/payment';

/**
 * @author Daniel Mejia
 * @file paymentUtil.spec.ts
 * @description Payment util test class.
 */
describe('Utilities: PaymentUtil', () => {
  const testPublicToken = 'publicToken';
  const testMetadata = { account_id: 'accountId' };
  const testAccountToken = 'testAccountToken';
  const bankReponse = { publicToken: testPublicToken, accountId: testMetadata.account_id };
  const testOrder = { id: 1 } as Order;
  const testAmount = 10000;
  const payment = { id: 1 } as Payment;
  const mockBankModal: jasmine.SpyObj<any> =
    jasmine.createSpyObj<any>('BankModal', ['open']);
  const mockPaymentService: jasmine.SpyObj<PaymentService> =
    jasmine.createSpyObj<PaymentService>('PaymentService', ['getBankToken', 'addPaymentCharge']);
  const mockPlaidService = jasmine.createSpyObj('Plaid', ['create']);
  let paymentUtil: PaymentUtil;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        PaymentUtil,
        { provide: PaymentService, useValue: mockPaymentService },
      ]
    });
    mockPlaidService.create.and.returnValue(mockBankModal);
    paymentUtil = TestBed.get(PaymentUtil);
    window['Plaid'] = mockPlaidService;
  });

  it('Constructor: Should be created', () => {
    expect(paymentUtil).toBeTruthy();
  });

  it('onBankModalSuccess: Should get the function success', async () => {
    const testPromise = new Promise<{ publicToken: string, accountId: string }>(resolve => {
      const callback = paymentUtil.onBankModalSuccess(resolve);
      callback(testPublicToken, testMetadata);
    });
    const response = await testPromise;
    expect(response.publicToken).toBe(testPublicToken);
    expect(response.accountId).toBe(testMetadata.account_id);
  });

  it('openBankModal: Should open the bank modal', () => {
    paymentUtil.openBankModal();
    expect(mockBankModal.open).toHaveBeenCalled();
  });

  it('addPayment: Should add the payment', async () => {
    mockPaymentService.getBankToken.and.returnValue(of(testAccountToken));
    mockPaymentService.addPaymentCharge.and.returnValue(of(payment));
    const response = await paymentUtil.addPayment(bankReponse, testOrder, testAmount);
    expect(response).toBe(payment);
  });
});
