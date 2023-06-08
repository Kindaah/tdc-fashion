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
import { Observable, Subject, forkJoin } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

// Components
import { LayoutComponent } from '../../components/layout/layout.component';

// Services
import { ProductService } from '../../services/product.service';

// Helpers
import * as UserHelper from '../../helpers/userHelper';

// Models
import { Product } from '../../models/product';
import { Order } from '../../models/order';
import { Feature } from '../../models/feature';

/**
 * @author Cristian Ricardo
 * @file samples.component.ts
 * @description Samples component
 */

@Component({
  selector: 'app-samples',
  templateUrl: './samples.component.html',
  styleUrls: ['./samples.component.css']
})

export class SamplesComponent implements OnInit {

  /**
   * The error message to show.
   *
   * @type { any }
   */
  errorMessage: any;

  /**
   * The flags that indicates if any feature were selected.
   *
   * @type { boolean[] }
   */
  featureSelected: boolean[] = [];

  /**
   * The flags that indicates if any product is collapse.
   *
   * @type { boolean[] }
   */
  productsCollapsed: boolean[] = [true];

  /**
   * The list of quote per sample for the features.
   *
   * @type { number[] }
   */
  quotePerSample: number[] = [];

  /**
   * Order object from parent.
   *
   * @type { Order }
   */
  @Input() order: Order;

  /**
   * List of Product object.
   *
   * @type { Product[] }
   */
  products: Product[];

  /**
   * Observable to check when the order save button is pressed.
   *
   * @type { Observable<string> }
   */
  @Input() onSaveButton$: Observable<string>;

  /**
   * Event for the samples total.
   *
   * @type { EventEmitter }
   */
  @Output() samplesTotalEvent = new EventEmitter();

  /**
   * Emiiter to notify the order when the product was saved.
   *
   * @type { EventEmitter<any> }
   */
  @Output() notifiySamples: EventEmitter<any> = new EventEmitter<any>();

  /**
   * Emiiter to notify if any feature has been selected.
   *
   * @type { EventEmitter<any> }
   */
  @Output() notifiyFeatureSelected: EventEmitter<any> = new EventEmitter<any>();

  /**
   * Default sample price.
   *
   * @type { number }
   */
  private defaultSamplePrice = 350;

  /**
   * Subject to check changes on quantity.
   *
   * @type { Subject<{ featureId: number, quantity: number }> }
   */
  samplesSubject = new Subject<{ featureId: number, quantity: number }>();

  /**
   * Samples total.
   *
   * @type { number }
   */
  samplesTotal = 0;

  /**
   * User helper class instance.
   */
  userHelper = UserHelper;

  /**
   * Creates an instance of SamplesComponent.
   */
  constructor(
    private layout: LayoutComponent,
    private productService: ProductService) { }

  /**
   * Callback on init component.
   */
  ngOnInit() {
    this.products = JSON.parse(JSON.stringify(this.order.products));
    const collapse = true;
    this.products.forEach(product => {
      const productId = product.id;
      this.productsCollapsed[productId] = collapse;
      product.features.forEach(feature => {
        const featureId = feature.id;
        this.featureSelected[featureId] = false;
        if (feature.sampleQuantity) {
          this.featureSelected[featureId] = true;
          this.setQuote({ featureId, quantity: feature.sampleQuantity });
        }
      });
    });
    this.onSaveButton$.subscribe(this.updateSampleQuantity);
    this.samplesSubject
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe(this.setQuote);
  }

  /**
   * Update the samples quantity for products.
   */
  updateSampleQuantity = (): void => {
    this.layout.spinner.show();
    const onProductsUpdated = (products: Product[]): void => {
      this.order.products = products;
      this.layout.spinner.hide();
      this.notifiySamples.emit({ featureUpdated: true });
    };

    const productsObservable =
      this.products.map(product => this.productService.updateProduct(product.id, this.order, product));

    forkJoin(productsObservable)
      .subscribe(onProductsUpdated, error => this.notifiySamples.emit({ error }));
  }

  /**
   * Clear the fields when a feature is unselected.
   *
   * @param { Feature } feature the feature object.
   */
  selectFeature = (feature: Feature): void => {
    this.notifiyFeatureSelected.emit(true);
    if (this.featureSelected[feature.id]) {
      this.samplesTotal -= this.quotePerSample[feature.id];
      this.quotePerSample[feature.id] = 0;
      feature.sampleQuantity = 0;
      this.samplesTotalEvent.emit(this.samplesTotal);
    }
  }

  /**
   * Logic to do when a quantity has been put.
   *
   * @param { { featureId: number, quantity: number } } param The param with the featureId and the quantity.
   */
  setQuote = (param: { featureId: number, quantity: number }): void => {
    const { featureId, quantity } = param;
    if (this.quotePerSample[featureId] !== undefined && this.quotePerSample[featureId] !== null) {
      this.samplesTotal -= this.quotePerSample[featureId];
      this.quotePerSample[featureId] = this.defaultSamplePrice * quantity;
      this.sampleTotalSum(this.quotePerSample[featureId]);
    } else {
      this.quotePerSample[featureId] = this.defaultSamplePrice * quantity;
      this.sampleTotalSum(this.quotePerSample[featureId]);
    }
  }

  /**
   * The sum of all the quotes
   *
   * @param { number } quotePerSample the quote Per Sample.
   */
  sampleTotalSum = (quotePerSample: number): void => {
    if (quotePerSample !== null) {
      this.samplesTotal += quotePerSample;
      this.samplesTotalEvent.emit(this.samplesTotal);
    }
  }

  /**
   * Gets the list of colors of the feature.
   *
   * @param { string } featureColor the colors of the feature.
   *
   * @returns { string[] } the list of colors.
   */
  getColorList(featureColor: string): string[] {
    return featureColor.split('/');
  }

  /**
   * Checks if the order is on the state and the current user has role.
   *
   * @param { number } state the order state id to check.
   * @param { string } role the user role to check.
   *
   * @returns { boolean } true if the order has the state and the user has the role.
   */
  mustBeShowed(state: number, role: string): boolean {
    return this.order && this.order.state.id === state && this.userHelper.sessionUserIs(role);
  }
}
