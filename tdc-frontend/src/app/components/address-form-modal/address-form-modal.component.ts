/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { Component, OnInit, Input, Output, EventEmitter, ViewChild, ViewChildren } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

// Services
import { OrderService } from '../../services/order.service';

// Components
import { LayoutComponent } from '../../components/layout/layout.component';
import { AddressFormComponent } from '../address-form/address-form.component';

// Helpers
import { NotificationUtil } from '../../utilities/notificationUtil';
import * as UtilHelper from '../../helpers/utilHelper';

// Models
import { Order } from '../../models/order';
import { User } from '../../models/user';
import { ShippingAddress } from '../../models/shippingAddress';

/**
 * @author Edson Ruiz Ramirez
 * @file address-form-modal.component.ts
 * @description Address Form Modal component
 */

@Component({
  selector: 'app-address-form-modal',
  templateUrl: './address-form-modal.component.html',
  styleUrls: ['./address-form-modal.component.css']
})
export class AddressFormModalComponent implements OnInit {

  /**
   * View child address form componenet.
   *
   * @type { boolean }
   */
  @ViewChild(AddressFormComponent)
  addressFormComponent: AddressFormComponent;

  /**
   * The order object.
   *
   * @type { Order }
   */
  @Input() order: Order;

  /**
   * The current user object.
   *
   * @type { User }
   */
  @Input() user: User;

  /**
   * The order states types according to order state.
   *
   * @type { string }
   */
  @Input() shippingAddressType;

  /**
   * Event for the shipping address creation.
   *
   * @type { EventEmitter<Order> }
   */
  @Output() closeEvent: EventEmitter<Order> = new EventEmitter();

  /**
  * The flag that indicates if the form must be showed.
  *
  * @type { boolean }
  */
  shouldShowForm = false;

  /**
   * The selected shipping address.
   *
   * @type { ShippingAddress }
   */
  selectedShipping: ShippingAddress;

  /**
   * Creates an instance of AddressComponent.
   *
   * @param { NgbActiveModal } activeModal dependencie NgbActiveModal
   * @param { FormBuilder } formBuilder dependencie FormBuilder
   * @param { LayoutComponent } layout component LayoutComponent
   * @param { OrderService } orderService service OrderService
   * @param { NotificationUtil } notificationUtil helper NotificationUtil
   */
  constructor(
    public activeModal: NgbActiveModal,
    private layout: LayoutComponent,
    private orderService: OrderService,
    private notificationUtil: NotificationUtil
  ) {
  }

  /**
   * Callback on init component.
   */
  ngOnInit() {
    this.selectedShipping = this.order.shippingAddresses[this.shippingAddressType];
  }

  /**
   * Callback on saves a shipping address.
   *
   * @param { shippingAddress } shippingAddress The shipping address saved.
   */
  onAddressSaved(shippingAddress: ShippingAddress): void {
    UtilHelper.updateList(this.user.shippingAddresses, shippingAddress);
    this.shouldShowForm = false;
  }

  /**
   * Add the shipping address to the order.
   *
   * @param { ShippingAddress } shippingAddress The shipping address to add in the order.
   */
  updateOrderAddress = (shippingAddress: ShippingAddress): void => {
    this.layout.spinner.show();
    const onRelationUpdated = (order: Order) => {
      this.order = order;
      this.notificationUtil.onSuccess('Shipping Address saved');
      this.closeModal();
    };
    this.orderService.addOrderShippingAddress(this.order.id, shippingAddress.id, this.shippingAddressType)
      .subscribe(onRelationUpdated, this.manageError);
  }

  /**
   * Show the address form.
   *
   * @param { boolean } newStatus The new status to show the form.
   * @param { shippingAddress } shippingAddress The shipping address optinal to show in the form.
   */
  showNewAddress(newStatus: boolean, shippingAddress: ShippingAddress = {} as ShippingAddress): void {
    this.shouldShowForm = newStatus;
    this.addressFormComponent.setShippingAddress(shippingAddress);
  }

  /**
   * Validates if the addresses list should be showed.
   *
   * @return { boolean } true if the addresses should be showed, false otherwise.
   */
  get showAddresses(): boolean {
    return this.user.shippingAddresses.length && !this.shouldShowForm;
  }

  /**
   * Close the modal.
   */
  closeModal(): void {
    this.layout.spinner.hide();
    this.activeModal.close();
    this.closeEvent.emit(this.order);
  }

  /**
   * Manage when a error happens.
   */
  manageError = (): void => {
    this.layout.spinner.hide();
  }
}
