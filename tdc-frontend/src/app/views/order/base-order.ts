/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

 // Dependencies
import { LayoutComponent } from '../../components/layout/layout.component';
import * as moment from 'moment';
import { Validators } from '@angular/forms';

// Helpers
import * as UserHelper from '../../helpers/userHelper';

// Services
import { OrderService } from '../../services/order.service';

// Models
import { State } from 'src/app/models/state';
import { Order } from '../../models/order';
import { Product } from '../../models/product';
import { User } from '../../models/user';

/**
 * @author Daniel Mejia
 * @file base-order.ts
 * @description Base order class with the general attributes and functions for order component.
 */
export abstract class BaseOrder {

  /**
   * Order oject.
   *
   *  @type { Order }
   */
  order: Order;

  /**
   * Current user object.
   *
   *  @type { User }
   */
  currentUser: User = { company: {} } as User;

  /**+
   * Order form.
   *
   * @type { any }
   */
  orderForm: any;

  /**
   * Order form fields.
   *
   * @type { object }
   */
  formFields: object =  {
    referencePo: ['', Validators.required],
    initialQuote: [''],
    finalQuote: [''],
    originCountry: [''],
    deliveryTime: 0,
    samplesTotal: 0
  };

  /**
   * The comment for the order.
   *
   * @type { string }
   */
  comment = '';

  /**
   * The error message to show.
   *
   * @type { any }
   */
  errorMessage: any;

   /**
   * Flag to check if the view has changes.
   *
   * @type { boolean }
   */
  thereChanges = false;

  /**
   * Current tab index.
   *
   * @type { number }
   */
  currentTab = 0;

  /**
   * Flag to check if the product form is valid.
   *
   * @type { boolean }
   */
  productFormValid = true;

  /**
   * User helper class instance.
   */
  userHelper = UserHelper;

  /**
   * Creates an instance of BaseOrder.
   *
   * @param { LayoutComponent } layout The layout componenet.
   * @param { OrderService } orderService The order service..
   */
  constructor(protected layout: LayoutComponent, protected orderService: OrderService) {
  }

  /**
   * Validates if the product is complete.
   *
   * @param { Product } product the product to check
   *
   * @returns { boolean } true if the product exists and has the files required, false otherwise.
   */
  isProductComplete = (product: Product): boolean => {
    return product &&
      !!product.sketchFileUrls &&
      !!(product.technicalSheetFileUrls || product.technicalSheetRows.length);
  }

  /**
   * Validates if the can be added a product.
   *
   * @returns { boolean } true if the user can add the product, false otherwise.
   */
  get canAddProduct(): boolean {
    return this.order && this.order.products.every(this.isProductComplete);
  }

  /**
   * Gets the deliver days in ordinal way.
   *
   * @returns { number } the ordinal number of days till deliver.
   */
  get deliverDays(): number {
    const deliveryDate = moment.utc(this.order.deliveryTime);
    const updatedDate = moment.utc(this.order.updatedAt);
    const difference = moment.duration(deliveryDate.diff(updatedDate));

    return Math.ceil(difference.asDays());
  }

  /**
   * Validates if the form has to be disabled.
   *
   * @returns { boolean } false if the order state is incomplete, true otherwise.
   */
  get formDisabled(): boolean {
    return this.order && this.orderState.id > 1;
  }

  /**
   * Validates if the save button has to be enabled.
   *
   * @returns { boolean } true if the user can save the order, false otherwise.
   */
  get enabledButtonSave(): boolean {
    return (this.thereChanges && this.orderForm.valid && !this.order.products.length) ||
      (this.thereChanges && this.orderForm.valid && this.order.products.length && this.productFormValid);
  }

  /**
   * Validates if the request quote button has to be enabled.
   *
   * @returns { boolean } true if the user can call request quote, false otherwise.
   */
  get enabledButtonRequestQuote(): boolean {
    return this.order && this.order.products.length && this.order.products.every(this.isProductComplete);
  }

  /**
   * Validates if the generate 3D models button has to be enabled.
   *
   * @returns { boolean } true if the designer cant generate 3D models, false otherwise.
   */
  get enabledButtonGenerate3DModel(): boolean {
    const hasModels = (product: Product): boolean => {
      return product && !!product.modelFileUrls;
    };

    return this.order.products.every(hasModels);
  }

  /**
   * Validates if the save final quote button has to be enabled.
   *
   * @returns { boolean } true if the user can save the final quote, false otherwise.
   */
  get enabledSaveFinalQuote(): boolean {
    const orderValues = this.orderForm.value;
    return this.thereChanges && orderValues.finalQuote && orderValues.originCountry && orderValues.deliveryTime;
  }

   /**
   * Validates if the generate final quote button has to be enabled.
   *
   * @returns { boolean } true if the user can save the final quote, false otherwise.
   */
  get enabledGenerateFinalQuote(): boolean {
    return this.order.finalQuote && this.order.originCountry && this.order.deliveryTime;
  }

  /**
   * Check if the request samples must be showed.
   *
   * @return { boolean } true if the request samples button can be enabled, false otherwise.
   */
  get enabledRequestSamplesButton(): boolean {
    return this.order.products.some(product => product.features.some(feature => feature.sampleQuantity > 0));
  }

  /**
   * Determine if the order has an address.
   *
   * @return { boolean } true if the order has an address.
   */
  get orderHasAddress(): boolean {
    return this.order && !!Object.keys(this.order.shippingAddresses).length;
  }

  /**
   * Order state for the order.
   *
   * @return { State } The order state.
   */
  get orderState(): State {
    return this.order.state;
  }

  /**
   * Gets the first payment for an order.
   *
   * @return { number } The first payment for the order.
   */
  get firstPayment(): number {
    return Math.ceil(this.order.finalQuote / 2);
  }

  /**
   * Gets the last payment for an order.
   *
   * @return { number } The last payment for the order.
   */
  get lastPayment(): number {
    const payment: number = this.order.finalQuote - this.firstPayment;
    return Math.round(payment * 100) / 100;
  }
}
