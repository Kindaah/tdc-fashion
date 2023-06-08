/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

// Services
import { ShippingInfoService } from '../../services/shippingInfo.service';
import { OrderService } from '../../services/order.service';

// Components
import { LayoutComponent } from '../../components/layout/layout.component';

// Helpers
import { FormValidatorHelper } from '../../helpers/form/formValidatorHelper';
import { NotificationUtil } from '../../utilities/notificationUtil';

// Models
import { Order } from '../../models/order';
import { ShippingInfo } from '../../models/shippingInfo';

/**
 * @author Edson Ruiz Ramirez
 * @file shipping-info-modal.component.ts
 * @description Shipping Info Modal component
 */

@Component({
  selector: 'app-shipping-info-modal',
  templateUrl: './shipping-info-modal.component.html',
  styleUrls: ['./shipping-info-modal.component.css']
})
export class ShippingInfoModalComponent implements OnInit {

  /**
   * The form for the view.
   *
   * @type { FormGroup }
   */
  shippingInfoForm: FormGroup;

  /**
   * The error message to show.
   *
   * @type { any }
   */
  errorMessage: any;

  /**
   * The order id.
   *
   * @type { number }
   */
  @Input() orderId: number;

  /**
   * Shipping Info object.
   *
   * @type { ShippingInfo }
   */
  shippingInfo: ShippingInfo;

  /**
   * Flag that indicates if the inputs must be read only.
   *
   * @type { boolean }
   */
  readOnly = false;

  /**
   * The order object.
   *
   * @type { Order }
   */
  order: Order;

  /**
   * Event for the shipping address creation.
   *
   * @type { EventEmitter }
   */
  @Output() changeStateEvent = new EventEmitter();

  /**
   * The form validator helper to validates the FormGroup.
   *
   * @type { FormValidatorHelper<FormGroup> }
   */
  formValidatorHelper: FormValidatorHelper<FormGroup>;

  /**
   * Creates an instance of AddressInfoModalComponent.
   *
   * @param { NgbActiveModal } activeModal dependencie NgbActiveModal
   * @param { FormBuilder } formBuilder dependencie FormBuilder
   * @param { LayoutComponent } layout component LayoutComponent
   * @param { ShippingInfoService } shippingInfoService service ShippingInfoService
   * @param { OrderService } orderService service OrderService
   * @param { NotificationUtil } notificationUtil helper NotificationUtil
   */
  constructor(
    public activeModal: NgbActiveModal,
    private formBuilder: FormBuilder,
    private layout: LayoutComponent,
    private shippingInfoService: ShippingInfoService,
    private orderService: OrderService,
    private notificationUtil: NotificationUtil
  ) {
    this.createForm();
   }

  /**
   * Callback on init component.
   */
  ngOnInit() {
    if (this.orderId) {
      this.getOrder(this.orderId);
    }
  }

  /**
   * Creates the form.
   */
  createForm(): void {
    this.shippingInfoForm = this.formBuilder.group({
      company: ['', Validators.required],
      trackingId: ['', Validators.required],
      details: ['', Validators.required]
    });

    this.formValidatorHelper = new FormValidatorHelper(this.shippingInfoForm);
  }

  /**
   * Set the order.
   *
   * @param order the order object.
   */
  setOrder = (order: Order) => {
    this.order = order;
    this.shippingInfo = order.shippingInfo;
    if (this.shippingInfo) {
      this.readOnly = true;
      this.shippingInfoForm.patchValue(this.shippingInfo);
    }
    this.layout.spinner.hide();
  }

  /**
   * Enable the form fields.
   */
  editShippingInfo(): void {
    this.readOnly = false;
  }

  /**
   * Gets the order by id.
   *
   * @param orderId to search the order.
   */
  getOrder(orderId: number): void {
    this.orderService.findBy(orderId).
      subscribe(this.setOrder, this.manageError);
  }

  /**
   * Save a shipping info.
   */
  submitForm(): void {
    this.addOrUpdate();
  }

  /**
   * Defines if the Shipping Info should be created or updated.
   */
  addOrUpdate(): void {
    this.layout.spinner.show();
    const stateToChange = () => {
      this.stateToChange();
      this.closeModal();
    };

    if (this.shippingInfo) {
      this.shippingInfoService.updateShippingInfo(this.shippingInfo.id, this.shippingInfoForm.value)
      .subscribe(stateToChange, this.manageError);
    } else {
      this.shippingInfoService.addShippingInfo(this.orderId, this.shippingInfoForm.value)
      .subscribe(stateToChange, this.manageError);
    }
  }

  /**
   * Close the modal.
   */
  closeModal(): void {
    this.activeModal.close();
  }

  /**
   * Change the order state.
   *
   * @param nextState the next order state.
   */
  changeOrderState(nextState: number, message: string): void {
    const onStateChanged = (order): void => {
      this.closeModal();
      this.notificationUtil.onSuccess(message);
      this.changeStateEvent.emit(order);
    };
    this.orderService.orderStateMachine(this.order, nextState)
      .subscribe(onStateChanged, this.manageError);
  }

  /**
   * Sets the state to Confirm Samples.
   */
  confirmSamples(): void {
    this.changeOrderState(10, 'Samples sent');
  }

  /**
   * Sets the state to Order Complete.
   */
  orderComplete(): void {
    this.changeOrderState(14, 'Order sent and completed');
  }

  /**
   * Validates to wich state to change next.
   */
  stateToChange(): void {
    if (this.order.state.id === 9) {
      this.confirmSamples();
    } else if (this.order.state.id === 16) {
      this.orderComplete();
    }
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

}
