/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { BaseOrder } from './base-order';
import { Validators } from '@angular/forms';

// Services
import { OrderService } from '../../services/order.service';
import { UserService } from '../../services/user.service';

// Components
import { LayoutComponent } from '../../components/layout/layout.component';

// Helpers
import { NotificationUtil } from '../../utilities/notificationUtil';

// Models
import { Order } from '../../models/order';
import { User } from '../../models/user';

/**
 * @author Daniel Mejia
 * @file state-machine.ts
 * @description Class that contains the function to the state machine.
 */
export abstract class StateMachine extends BaseOrder {

  /**
   * Creates an instance of StateMachine.
   *
   * @param { LayoutComponent } layout component LayoutComponent
   * @param { OrderService } orderService service OrderService
   * @param { NotificationUtil } notificationUtil helper NotificationUtil
   * @param { UserService } userService service UserService
   */
  constructor(
    protected layout: LayoutComponent,
    protected orderService: OrderService,
    protected notificationUtil: NotificationUtil,
    protected userService: UserService
  ) {
    super(layout, orderService);
  }

  /**
  * Sets the order to the view.
  *
  * @param order the order.
  */
  setOrder = (storedOrder: Order): void => {
    this.order = { ...storedOrder };
    this.currentTab = this.order.products.length - 1;
    storedOrder.deliveryTime = this.orderAfterState(5) ? this.deliverDays : storedOrder.deliveryTime;
    this.orderForm.patchValue(storedOrder);

    if (this.order && this.orderState && this.orderState.id === 6) {
      this.orderForm.controls['originCountry'].setValidators([Validators.required]);
      this.orderForm.controls['deliveryTime'].setValidators([Validators.required]);
    } else {
      this.orderForm.controls['originCountry'].clearValidators();
      this.orderForm.controls['deliveryTime'].clearValidators();
    }

    if (this.isOnSamplesOrManufacture) {
      this.getCurrentUser();
    }
    this.layout.spinner.hide();
    this.thereChanges = false;
  }

  /**
   * Gets the current user.
   */
  getCurrentUser(): void {
    const userData = this.userHelper.getUserData();
    const setCurrentUser = (user: User) => {
      this.currentUser = user;
    };
    this.userService.findByAuthKey(userData.authKey)
      .subscribe(setCurrentUser, this.manageError);
  }

  /**
   * Gets the flag if the order has the status Request Samples or Manufacture Order.
   *
   * @returns { boolean } True if the orders has the status Request Samples or Manufacture Order.
   */
  get isOnSamplesOrManufacture(): boolean {
    return this.mustBeShowed(8, 'CUSTOMER') || this.mustBeShowed(11, 'CUSTOMER');
  }

  /**
   * Manage when a error happens.
   *
   * @param error the error.
   */
  manageError = (error: any): void => {
    this.layout.spinner.hide();
    this.errorMessage = error;
  }

  /**
   * Checks if the order is on the state and the current user has role.
   *
   * @param state the order state id to check.
   * @param role the user role to check.
   *
   * @returns true the order has the state and the user has the role.
   */
  mustBeShowed(state: number, role: string): boolean {
    return this.order && this.orderState.id === state && this.userHelper.sessionUserIs(role);
  }

  /**
   * Validates if the order if after the specific state.
   *
   * @param { number } state to validate the order.
   *
   * @return true if the order is after state, false otherwise.
   */
  orderAfterState(state: number): boolean {
    return this.order && this.orderState.id > state;
  }

  /**
   * Validates if the order if before the specific state.
   *
   * @param { number } state to validate the order.
   *
   * @return true if the order is before state, false otherwise.
   */
  orderBeforeState(state: number): boolean {
    return this.order && this.orderState.id < state;
  }

  /**
   * Validates if the order has state rejected.
   *
   * @return { boolean } true if the order has state rejected, false otherwise.
   */
  orderRejected(): boolean {
    return this.order && this.orderState.id === 15;
  }

  /**
   * Change the order state.
   *
   * @param nextState the next order state.
   */
  changeOrderState(nextState: number, message: string): void {
    this.layout.spinner.show();
    this.orderService.orderStateMachine(this.order, nextState)
      .subscribe((order: Order) => this.onOrderUpdated(order, message), this.manageError);
  }

  /**
   * Sets to PendingInitialQuote for the admin.
   */
  requestQuote(): void {
    this.changeOrderState(2, 'Quote submitted');
  }

  /**
   * Creates a initial quote and sets to PendingInitialQuoteApproval for customer.
   */
  createInitialQuote(): void {
    const onInitialQuoteUpdated = (): void => {
      this.changeOrderState(3, 'Quote created');
    };
    this.layout.spinner.show();
    this.orderService.updateOrder('quote', this.order, this.orderForm.value)
      .subscribe(onInitialQuoteUpdated, this.manageError);
  }

  /**
   * Sets to Pending3DModel for the designer.
   */
  approveInitialQuote(): void {
    this.changeOrderState(4, 'Quote approved');
  }

  /**
   * Upload the files and sets to Pending3DModelAdminApproval for the admin.
   */
  generate3DModels(): void {
    this.changeOrderState(5, '3D models generated successfully');
  }

  /**
   * Sets to PendingFinalQuote for the admin.
   */
  approveModels3DAdmin(): void {
    this.changeOrderState(6, '3D models approved');
  }

  /**
   * Reject the 3D model by admin or customer and change to Pending 3D Model.
   */
  rejectModels3D(): void {
    this.updateOrderCommentAndChangeState(4);
  }

  /**
   * Generates the final quote.
   */
  generateFinalQuote(): void {
    this.changeOrderState(7, 'Final quote created.');
  }

  /**
   * Approve the final quote and 3D models and set to RequestSamples to customer.
   */
  approveModels3DCustomer(): void {
    this.changeOrderState(8, 'Final quote and 3D models approved');
  }

  /**
   * Sets the state to Samples in Production.
   */
  samplesInProduction = (): void => {
    this.changeOrderState(9, 'Samples requested');
  }

  /**
   * Sets the state to Confirm Order Production.
   */
  confirOrderProduction(): void {
    this.changeOrderState(11, 'Order to be confirmed');
  }

  /**
   * Sets the state to Order in Production.
   */
  orderInProduction = (): void => {
    this.changeOrderState(12, 'Order ready to manufacture');
  }

  /**
   * Sets the state to Pending Order Last Payment.
   */
  pendingOrderLastPayment(): void {
    this.changeOrderState(13, 'Order sent for last payment');
  }

  /**
   * Sets the state to Shipping Order.
   */
  shippingOrder = (): void => {
    this.changeOrderState(16, 'Last payment successfully');
  }

  /**
   * Puts a comments and sets to OrderRejected and the order is now unavailable.
   */
  rejectOrder(): void {
    this.updateOrderCommentAndChangeState(15);
  }

  /**
   * Updates the order comment and change the order state.
   *
   * @param { number } newState the new state for the order.
   */
  updateOrderCommentAndChangeState(newState: number): void {
    const onCommentUpdted = (order): void => {
      this.setOrder(order);
      this.changeOrderState(newState, 'Order rejected');
    };

    this.layout.spinner.show();
    this.orderService.updateOrder('comment', this.order, { comment: this.comment })
      .subscribe(onCommentUpdted, this.manageError);
  }

  /**
   * Shows a notification and set the order.
   *
   * @param { Order } updatedOrder the updated order.
   */
  onOrderUpdated = (updatedOrder: Order, message: string): void => {
    this.notificationUtil.onSuccess(message);
    this.setOrder(updatedOrder);
  }
}
