/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { Component, OnInit, OnDestroy, AfterViewChecked, Input, Output, EventEmitter } from '@angular/core';
import { Validators, FormBuilder, FormGroup, FormArray } from '@angular/forms';
import { Observable, Subscription, forkJoin } from 'rxjs';
import { UploadEvent, FileSystemFileEntry } from 'ngx-file-drop';
import * as ColorConverter from 'color-convert';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { map } from 'rxjs/operators';

// Services
import { ProductService } from '../../services/product.service';
import { FeatureService } from '../../services/feature.service';

// Components
import { LayoutComponent } from '../layout/layout.component';

// Helpers
import * as UserHelper from '../../helpers/userHelper';
import { FormValidatorHelper } from '../../helpers/form/formValidatorHelper';

// Models
import { Order } from '../../models/order';
import { Product } from '../../models/product';
import { Feature } from '../../models/feature';

/**
 * @author Daniel Mejia
 * @file product.component.ts
 * @description Product component
 */

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})

export class ProductComponent implements OnInit, OnDestroy, AfterViewChecked {

  /**
   * Product form.
   *
   * @type { FormGroup }
   */
  productForm: FormGroup;

  /**
   * Files names for a product.
   */
  filesNames = {
    sketchFiles: [],
    technicalSheetFiles: [],
    modelFiles: []
  };

  /**
   * Files list for a product.
   */
  filesProduct = {
    sketchFiles: [],
    technicalSheetFiles: [],
    modelFiles: []
  };

  /**
   * Order object from parent.
   *
   * @type { Order }
   */
  @Input() order: Order;

  /**
   * Product object.
   *
   * @type { Product }
   */
  @Input() product: Product;

  /**
   * product index in the order.
   *
   * @type { number }
   */
  @Input() index: number;

  /**
   * Flag to check if the forms has to be disabled.
   */
  @Input() formDisabled: boolean;

  /**
   * Subscription to onSaveButton$ observable.
   *
   * @type { Subscription }
   */
  private onSaveButtonSubscription: Subscription;

  /**
   * Observable to check when the order save button is pressed.
   *
   * @type { Observable<string> }
   */
  @Input() onSaveButton$: Observable<string>;

  /**
   * Emiiter to notify the order when the product was saved.
   *
   * @type { EventEmitter<any> }
   */
  @Output() notifiyProductSaved: EventEmitter<any> = new EventEmitter<any>();

  /**
   * Emiiter to notify the order when the product was saved.
   *
   * @type { EventEmitter<boolean> }
   */
  @Output() notifyStatus: EventEmitter<boolean> = new EventEmitter<boolean>();

  /**
   * The form validator helper to validates the FormGroup.
   *
   * @type { FormValidatorHelper<FormGroup> }
   */
  formValidatorHelper: FormValidatorHelper<FormGroup>;

  /**
   * The flag that indicates if the drag and drop zone should be show.
   *
   * @type { boolean }
   */
  showDragZone = false;

  /**
   * The flag that indicates if the item should be able to download.
   *
   * @type { boolean }
   */
  permitDownload = false;

  /**
   * Creates an instance of ProductComponent.
   *
   * @param { FormBuilder } formBuilder the form builder.
   * @param { ProductService } productService the product service.
   */
  constructor(
    private formBuilder: FormBuilder,
    private productService: ProductService,
    private featureService: FeatureService,
    private modalService: NgbModal,
    private layout: LayoutComponent) {
    this.productForm = this.formBuilder.group({
      description: ['', Validators.required],
      features: this.formBuilder.array([], Validators.required)
    });

    this.formValidatorHelper = new FormValidatorHelper(this.productForm);
  }

  /**
   * Callback on init component.
   */
  ngOnInit() {
    if (this.product) {
      this.setProduct(this.product);
      this.product.features.forEach(this.setFeature);
      this.setPatterns();
      this.onSaveButtonSubscription = this.onSaveButton$.subscribe(this.updateProduct);
    } else {
      this.setFeature({} as Feature);
      this.onSaveButtonSubscription = this.onSaveButton$.subscribe(this.saveProduct);
    }
  }

  /**
   * Callback after the view was checked.
   */
  ngAfterViewChecked(): void {
    this.productForm.statusChanges.subscribe(this.onFormStatusChange);
  }

  /**
   * Sets the product to the current view.
   *
   * @param { Product } product The new product to set.
   */
  setProduct = (product: Product): void => {
    this.product = product;
    this.productForm.patchValue(product);
    this.filesNames['sketchFiles'] =
      product.sketchFileUrls ? product.sketchFileUrls.split(',') : [];
    this.filesNames['technicalSheetFiles'] =
      product.technicalSheetFileUrls ? product.technicalSheetFileUrls.split(',') : [];
    this.filesNames['modelFiles'] =
      product.modelFileUrls ? product.modelFileUrls.split(',') : [];
    this.setPatterns();
  }

  /**
   * Sets a feature to produc form.
   *
   * @param { Feature } feature the feature object to be set.
   */
  setFeature = (feature: Feature): void => {
    const featureForm = this.createFeatureForm();
    featureForm.patchValue(feature);
    this.featuresForm.push(featureForm);
  }

  /**
   * Saves the product.
   */
  saveProduct = (): void => {
    const onSavedProduct = (savedProduct): void => {
      if (this.filesProduct.sketchFiles.length || this.filesProduct.technicalSheetFiles.length) {
        this.productService.uploadFiles(savedProduct.id, this.filesProduct)
          .subscribe(this.uploadPatterns, this.manageError);
      } else {
        this.uploadPatterns(savedProduct);
      }
    };
    this.productService.addProduct(this.order, this.productForm.value)
      .subscribe(onSavedProduct, this.manageError);
  }

  /**
   * Updates a product.
   *
   * @param { string } action The action to updating.
   */
  updateProduct = (action: string): void => {
    if (action === 'uploadFiles') {
      this.upload3DModels();
    } else {
      this.product = Object.assign(this.product, this.productForm.value);
      this.productService.updateProduct(this.product.id, this.order, this.product)
        .subscribe(this.onUpdatedProduct, this.manageError);
    }
  }

  /**
   * Callback to execute when the product is saved.
   *
   * @param { Product } product The product updated.
   */
  onUpdatedProduct = (product: Product): void => {
    if (this.filesProduct.sketchFiles.length || this.filesProduct.technicalSheetFiles.length) {
      this.productService.uploadFiles(this.product.id, this.filesProduct)
        .subscribe(this.uploadPatterns, this.manageError);
    } else {
      this.uploadPatterns(product);
    }
  }

  /**
   * Upload the patterns for each feature.
   *
   * @param { Product } product The product to upload the patterns.
   */
  uploadPatterns = (product: Product): void => {
    product.features.forEach(feature => this.featuresForm.controls[feature.orderInProduct].patchValue(feature));
    const featureRequests = this.featuresForm.value
      .filter(feature => feature.id && feature.patterns.length)
      .map(feature => this.featureService.uploadPatterns(feature.id, { patternsFiles: feature.patterns }));

    const manageUpdated = storedProduct => {
        this.setProduct(storedProduct);
        this.permitDownload = false;
        this.notifiyProductSaved.emit();
    };

    forkJoin(featureRequests)
      .subscribe((features: Feature[]) => product.features = features, this.manageError, () => manageUpdated(product));
  }

  /**
   * Set the patterns to the feature form.
   */
  setPatterns(): void {
    this.featuresForm.controls.forEach((featureControl: FormGroup) => {
      const patternsUrls = featureControl.value.patternsUrls;
      const patternsUrlsList = patternsUrls ? patternsUrls.split(',') : [];
      const images$ = forkJoin(this.downloadPatterns(patternsUrlsList));
      this.setPatternsControl(featureControl, 'patternsToShow', images$);
      images$.subscribe(patternsFiles => this.setPatternsControl(featureControl, 'patterns', patternsFiles));
    });
  }

  /**
   * Uploads the 3D models.
   */
  upload3DModels = (): void => {
    const onUpload3DFiles = (storedProduct): void => {
      this.layout.spinner.hide();
      this.setProduct(storedProduct);
      this.notifiyProductSaved.emit({ modelsUpload: true });
    };

    this.layout.spinner.show();
    this.productService.uploadFiles(this.product.id, { modelFiles: this.filesProduct.modelFiles })
      .subscribe(onUpload3DFiles, this.manageError);
  }

  /**
   * Gets the download files url.
   *
   * @param fileName the file name to download.
   *
   * @returns The url to donwload the file.
   */
  downloadFile(fileName: string): void {
    const manageResponse = (response: any): void => {
      this.layout.spinner.hide();
      window.location.assign(response.fileDownloadUrl);
    };

    this.layout.spinner.show();
    this.productService.donwloadFileUrl(fileName)
      .subscribe(manageResponse, this.manageError);
  }

  /**
   * Gets the download files url.
   *
   * @param { string[] } fileName the file name to download.
   *
   * @returns The url to donwload the file.
   */
  downloadPatterns(patternsUrls: string[]): Observable<any>[] {
    return patternsUrls.map(patternUrl =>
      this.productService
        .donwloadFileUrl(patternUrl)
        .pipe(map(response => response.fileDownloadUrl)));
  }

  /**
   * Sets the files to the input.
   *
   * @param { string } fileKey the form file key.
   * @param { File[] } files the files to set.
   */
  onFileInputChange(fileKey: string, files: File[]): void {
    this.filesNames[fileKey] = Array.from(files).map(file => file.name);
    this.filesProduct[fileKey] = files;
    this.permitDownload = true;
    this.notifyStatus.emit(this.productForm.status === 'VALID');
  }

  /**
   * Sets the files to the input.
   *
   * @param { File[] } files the files to set.
   * @param { FormGroup } featureControl the files to set.
   */
  onPatternsInputChange(files: File[], featureControl: FormGroup): void {
    const filesList = Array.from(files).filter(file => file.type.startsWith('image/'));
    const listObservables = filesList
      .map(file =>
        Observable.create(observer => {
          const reader = new FileReader();
          reader.onload = (event: any) => observer.next(event.target.result);
          reader.onloadend = () => observer.complete();
          reader.readAsDataURL(file);
      }));

    this.setPatternsControl(featureControl, 'patternsToShow', forkJoin(listObservables));
    this.setPatternsControl(featureControl, 'patterns', filesList, true);
  }

  /**
   * Callback to execute when the product form status changes.
   *
   * @param { string } status the new status for the product form.
   */
  onFormStatusChange = (status: string) => {
    this.notifyStatus.emit(status === 'VALID');
  }

  /**
   * Manage when a error happens.
   *
   * @param { any } error the error.
   */
  manageError = (error: any): void => {
    this.notifiyProductSaved.emit({ error });
  }

  /**
   * Gets the file name from a file to show.
   *
   * @param { string } fileName the filename.
   */
  getFileNameToShow(fileName: string) {
    const index = fileName.lastIndexOf('/');
    return fileName.substring(index + 1);
  }

  /**
   * Gets the list of colors of the feature.
   *
   * @param { string } featureColor the colors of the feature.
   * @returns { string[] } the list of colors.
   */
  getColorList(featureColor: string): string[] {
    const list = featureColor.split('/');
    return list.toString() ? list : ['#000000'];
  }

  /**
   * Event drop file.
   *
   * @param { UploadEvent } event the event of a file been dropped.
   * @param { string } fileKey the form file key.
   *
   * @returns { Promise<void> } returns a void promise
   */
  onFileDropped = async (event: UploadEvent, fileKey: string): Promise<void> => {
    const filesPromises = event.files
      .filter(droppedFile => droppedFile.fileEntry.isFile)
      .map(droppedFile => {
        const fileEntry = droppedFile.fileEntry as FileSystemFileEntry;
        return new Promise<File>(resolve => fileEntry.file((file: File) => resolve(file)));
      });

    const files = await Promise.all(filesPromises);
    this.onFileInputChange(fileKey, files);
  }

  /**
   * Open the modal to add a comment when the order will be reject.
   *
   * @param { any } content The content to open the modal.
   */
  openModal = (content: any): void => {
    this.modalService.open(content, { windowClass: 'technical-modal' });
  }

  /**
   * Calculates the differents sizes in the features list.
   *
   * @return { string[] } A sizes list.
   */
  calculateSizes = (): string[] => {
    const seen = new Set();
    return this.featuresForm.value
      .map(feature => feature.size)
      .filter(size =>  !size || seen.has(size) ? false : seen.add(size));
  }

  /**
   * Get the CYMK color by the RGB color given.
   *
   * @param { string } color the RGB color.
   *
   * @returns { string } returns the CYMK color.
   */
  getColorCYMK(color: string): string {
    const rgb = color
      .slice(4, color.length - 1)
      .split(',');
    return `CYMK: ${ColorConverter.rgb.cmyk([+rgb[0], +rgb[1], +rgb[2]])}`;
  }

  /**
   * Sets the value to control in the feature form.
   *
   * @param { FormGroup } featureForm The feature form to set the control.
   * @param { string } controlKey The control key of the feature control.
   * @param { File[] | Observable<any[]> } valueToSet The new value to set.
   */
  setPatternsControl(
    featureForm: FormGroup,
    controlKey: string,
    valueToSet: any,
    emitEvent: boolean = false): void {
    featureForm.get(controlKey).reset(valueToSet, { emitEvent });
  }

  /**
   * Creates a feature form.
   *
   * @param { number } orderInProduct The order for feature in the product form.
   *
   * @returns { FormGroup } The feature form group created.
   */
  createFeatureForm(orderInProduct?: number): FormGroup {
    return this.formBuilder.group({
      id: [''],
      size: ['', Validators.required],
      color: ['', Validators.required],
      patternsUrls: [''],
      patterns: ['', Validators.maxLength(5)],
      patternsToShow: [''],
      orderInProduct: [orderInProduct || 0],
      quantity: [
        '',
        [ Validators.required, Validators.min(10), Validators.max(500) ],
      ]
    });
  }

  /**
   * Adds a features from product.
   */
  addFeatureToForm(): void {
    this.featuresForm.push(this.createFeatureForm(this.featuresForm.length));
  }

  /**
   * Removes a features from product.
   *
   * @param { number } index the feature index to remove.
   */
  removeFeatureToForm(index: number): void {
    this.featuresForm.removeAt(index);
    this.featuresForm.controls
      .forEach((control, formIndex) => control.get('orderInProduct').patchValue(formIndex));
  }

  /**
   * Gets the features form of product.
   */
  get featuresForm(): FormArray {
    return this.productForm.get('features') as FormArray;
  }

  /**
   * Validates if the input files has to be disabled.
   *
   * @returns { boolean } if the input should be disabled.
   */
  get inputFilesDisabled(): boolean {
    return !(this.orderInCreation);
  }

  /**
   * Validates if the order is in creation
   *
   * @returns true if the order is in creation, false otherwise.
   */
  get orderInCreation(): boolean {
    return this.order && this.order.state.id === 1;
  }

   /**
   * Validates if the upload files can be showed.
   */
  get showUpload3DModel(): boolean {
    return this.order && this.order.state.id > 3;
  }

  /**
   * Validates if the upload files buttom can be showed.
   */
  get uploadFilesButtonEnabled(): boolean {
    return this.order.state.id === 4 && UserHelper.sessionUserIs('DESIGNER');
  }

  /**
   * Validates if the technical pack button should be showed.
   *
   * @returns { boolean } true if the button should be showed, false otherwise.
   */
  get shouldShowTechnicalPackButton(): boolean {
    return this.orderInCreation || (!this.orderInCreation && !!this.product.technicalSheetRows.length);
  }

  /**
   * Validates if the technical sheet button should be showed.
   *
   * @returns { boolean } true if the button should be showed, false otherwise.
   */
  get shouldShowTechnicalSheetButton(): boolean {
    return this.orderInCreation || (!this.orderInCreation && !!this.product.technicalSheetFileUrls);
  }

  /**
   * Callback when the component is destroyed.
   */
  ngOnDestroy(): void {
    this.onSaveButtonSubscription.unsubscribe();
  }
}
