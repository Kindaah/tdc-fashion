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
import { FormBuilder, FormArray, FormGroup, Validators } from '@angular/forms';
import { FormValidatorHelper } from 'src/app/helpers/form/formValidatorHelper';
import { LayoutComponent } from '../layout/layout.component';
import { UniversalValidators } from 'ngx-validators';

// Services
import { ProductService } from '../../services/product.service';

// Models
import { Product } from '../../models/product';
import { Order } from '../../models/order';
import { TechnicalSheetRow } from '../../models/technicalSheetRow';

/**
 * @author Daniel Mejia
 * @file technical-sheet-form.component.ts
 * @description Technical Sheet FormC omponent
 */

@Component({
  selector: 'app-technical-sheet-form',
  templateUrl: './technical-sheet-form.component.html',
  styleUrls: ['./technical-sheet-form.component.css']
})
export class TechnicalSheetFormComponent implements OnInit {

  /**
   * The technical sheet form.
   *
   * @param { () => void }
   */
  @Input() callback: () => void;

  /**
   * The sizes list from the product features.
   *
   * @param { string[] }
   */
  @Input() sizes: string[];

  /**
   * The product object.
   *
   * @param { Product }
   */
  @Input() product: Product;

  /**
   * Flag to check if the forms has to be disabled.
   *
   * @param { boolean }
   */
  @Input() formDisabled: boolean;

  /**
   * The order object.
   *
   * @param { Order }
   */
  @Input() order: Order;

  /**
   * The emmiter to notify when the technical sheet was updated.
   *
   * @param { EventEmitter<any> }
   */
  @Output() technicalSheetUpdated: EventEmitter<any> = new EventEmitter<any>();

  /**
   * The emmiter to notify when an error was thrown.
   *
   * @param { EventEmitter<any> }
   */
  @Output() technicalSheetError: EventEmitter<any> = new EventEmitter<any>();

  /**
   * The technical sheet form.
   *
   * @param { FormGroup }
   */
  technicalSheetForm: FormGroup;

  /**
   * The options available for the tol selector.
   *
   * @param { object }
   */
  tolOptions: object = [0.5, 1, 1.5];

  /**
   * The form validator helper to validates the FormGroup.
   *
   * @type { FormValidatorHelper<FormGroup> }
   */
  formValidatorHelper: FormValidatorHelper<FormGroup>;

  /**
   * Constructor.
   *
   * @param { FormBuilder } formBuilder The form builder.
   * @param { ProductService } productService The product service.
   * @param { LayoutComponent } layout The layout component.
   */
  constructor(
    private formBuilder: FormBuilder,
    private productService: ProductService,
    private layout: LayoutComponent,
  ) {
    this.technicalSheetForm = this.formBuilder.group({
      technicalSheetRows: this.formBuilder.array([])
    });
    this.formValidatorHelper = new FormValidatorHelper(this.technicalSheetForm);
  }

  /**
   * Callback on init component.
   */
  ngOnInit() {
    if (this.product.technicalSheetRows.length) {
      this.product.technicalSheetRows.forEach(this.setTechnicalSheetRow);
    } else {
      this.setTechnicalSheetRow({} as TechnicalSheetRow);
    }
  }

  /**
   * Sets the techical sheet row to the form.
   *
   * @param { TechnicalSheetRow } technicalSheetRow The object to set.
   */
  setTechnicalSheetRow = (technicalSheetRow: TechnicalSheetRow): void => {
    const technicalSheetRowForm = this.createTechnicalSheetRow();
    technicalSheetRowForm.patchValue(technicalSheetRow);
    this.technicalSheetRowsFormArray.push(technicalSheetRowForm);
  }

  /**
   * Creates and form group of technical sheet row.
   *
   * @return { FormGroup } The form with the fields for the technical sheet row.
   */
  createTechnicalSheetRow(letter?: string): FormGroup {
    return this.formBuilder.group({
      id: [''],
      letter: [letter || this.getLetter(1)],
      description: ['', Validators.required],
      tol: ['', [Validators.required, UniversalValidators.isNumber]],
      twelve: [''],
      sizes: this.createSizeForm(this.sizes)
    });
  }

  /**
   * Creates and form group of technical sheet row sizes.
   *
   * @param { string[] } sizes The sizes list to create the form.
   *
   * @return { FormGroup } The form with the sizes params.
   */
  createSizeForm = (sizes: string[]): FormGroup => {
    const object = sizes.reduce((obj, item) => {
      obj[item] = ['', Validators.required];
      return obj;
    }, {});
    return this.formBuilder.group(object);
  }

  /**
   * Adds a new technical sheet row to the form.
   */
  addTechnicalSheetRow(): void {
    const letter = this.getLetter(this.technicalSheetRowsFormArray.length + 1);
    this.technicalSheetRowsFormArray.push(this.createTechnicalSheetRow(letter));
  }

  /**
   * Removes a technical sheet row from form.
   *
   * @param { number } index The technical sheet sheet index in the form.
   */
  removeTechnicalSheetRow(index: number): void {
    this.technicalSheetRowsFormArray.removeAt(index);
    this.technicalSheetRowsFormArray.controls
      .forEach((control, formIndex) => control.get('letter').patchValue(this.getLetter(formIndex + 1)));
  }

  /**
   * Save the technical sheet rows in the product.
   */
  saveTechnicalSheetRows() {
    this.layout.spinner.show();
    const onProductUpdated = (updatedProduct: Product) => {
      this.technicalSheetForm.markAsPristine();
      this.product = updatedProduct;
      this.technicalSheetUpdated.emit('Technical pack saved!');
    };

    const productToUpdate = { ...this.product };
    productToUpdate.technicalSheetRows = this.technicalSheetRowsFormArray.value;
    this.productService.updateProduct(productToUpdate.id, this.order, productToUpdate)
      .subscribe(onProductUpdated, error => this.technicalSheetError.emit(error));
  }

  /**
   * Takes a positive integer and returns the corresponding column name.
   *
   * @param { number }index The positive integer to convert to a column name.
   *
   * @return { string }  The column name.
   */
  getLetter(index: number): string {
    let letter = '';
    for (let a = 1, b = 26; (index -= a) >= 0; a = b, b *= 26) {
      letter = String.fromCharCode(Math.trunc((index % b) / a) + 65) + letter;
    }
    return letter;
  }

  /**
   * Gets the technical sheet rows form array
   *
   * @returns { FormArray } The technical sheet form array.
   */
  get technicalSheetRowsFormArray(): FormArray {
    return this.technicalSheetForm.get('technicalSheetRows') as FormArray;
  }

  /**
   * Gets the size form for a technical sheet row.
   *
   * @param { FormGroup } technicalSheetRow The technical sheet row form to get the sizes form.
   *
   * @returns { FormGroup } The sizes form.
   */
  getSizeForm(technicalSheetRow: FormGroup): FormGroup {
    return technicalSheetRow.get('sizes') as FormGroup;
  }

  /**
   * Gets the size form keys for a technical sheet row.
   *
   * @param { FormGroup } technicalSheetRow The technical sheet row form to get the sizes form keys.
   *
   * @returns { string[] } The sizes keys.
   */
  getSizeFormKeys(technicalSheetRow: FormGroup): string[] {
    return Object.keys(this.getSizeForm(technicalSheetRow).controls);
  }

  /**
   * Validates if should show a notification for sizes changes.
   *
   * @returns { boolean } true if should show the notification, false otherwise.
   */
  get shouldShowNotification(): boolean {
    const technicalRowSizes = this.product.technicalSheetRows.length ? Object.keys(this.product.technicalSheetRows[0].sizes) : [];
    return !(this.sizes.length === technicalRowSizes.length && this.sizes.every((value, index) => value === technicalRowSizes[index]));
  }
}
