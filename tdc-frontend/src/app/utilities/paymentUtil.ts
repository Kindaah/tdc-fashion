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
import { environment } from 'src/environments/environment';

// Services
import { PaymentService } from '../services/payment.service';

// Models
import { Order } from '../models/order';
import { Payment } from '../models/payment';

/**
 * @author Daniel Mejia
 * @file plaidService.service.ts
 * @description plaid service.
 */
@Injectable({ providedIn: 'root' })
export class PaymentUtil {

  /**
   * The bank account modal.
   *
   * @type { any }
   */
  bankAccountModal: any;

  /**
   * The modal promise when the bank modal is closed.
   *
   * @type { Promise<{ publicToken: string, accountId: string }> }
   */
  modalPromise: Promise<{ publicToken: string, accountId: string }>;

  /**
   * Creates an instance for payment util.
   *
   * @param { PaymentService } paymentService The payment service.
   */
  constructor(private paymentService: PaymentService) { }

  /**
   * Initializes the bank modal.
   */
  initBankModal(): void {
    this.modalPromise = new Promise((resolve, reject) => {
      const modalOptions = {
        selectAccount: true,
        clientName: environment.appName,
        env: environment.payment.plaid.env,
        key: environment.payment.plaid.publicKey,
        product: [environment.payment.plaid.product],
        onSuccess: this.onBankModalSuccess(resolve)
      };

      this.bankAccountModal = Plaid.create(modalOptions);
    });
  }

  /**
   * Gets the function to execute the the modal was success.
   *
   * @param { Function } resolve The resolve function to the promise.
   *
   * @return { Function } The function to execute after sucess;
   */
  onBankModalSuccess = (resolve: Function): Function => {
    return (publicToken, metadata) => {
      const { account_id: accountId } = metadata;
      resolve({ publicToken, accountId });
    };
  }

  /**
   * Opens a bank modal and gets his information.
   *
   * @returns { Promise<{ publicToken: string, accountId: string }> } The promise that contains the publickToken and accountId.
   */
  openBankModal(): Promise<{ publicToken: string, accountId: string }> {
    this.initBankModal();
    this.bankAccountModal.open();
    return this.modalPromise;
  }

  /**
   * Adds the payment.
   *
   * @param { Order } order The order to add the payment.
   * @param { number } amount The payment amount.
   *
   * @returns { Promise<Payment> } A promise with the paymet created.
   */
  async addPayment(
    bankResponse: { publicToken: string, accountId: string },
    order: Order,
    amount: number): Promise<Payment> {
    const { accountToken } = await this.paymentService.getBankToken(bankResponse.publicToken, bankResponse.accountId).toPromise();
    return this.paymentService.addPaymentCharge(order, amount, accountToken).toPromise();
  }
}
