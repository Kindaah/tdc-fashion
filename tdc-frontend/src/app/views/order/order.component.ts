/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subject } from 'rxjs';
import { StateMachine } from './state-machine';

// Services
import { OrderService } from '../../services/order.service';
import { UserService } from '../../services/user.service';
import { CompanyService } from '../../services/company.service';

// Components
import { LayoutComponent } from '../../components/layout/layout.component';
import { ShippingInfoModalComponent } from '../../components/shipping-info-modal/shipping-info-modal.component';
import { AddressFormModalComponent } from '../../components/address-form-modal/address-form-modal.component';

// Helpers
import { FormValidatorHelper } from '../../helpers/form/formValidatorHelper';
import * as UserHelper from '../../helpers/userHelper';
import { PaymentUtil } from '../../utilities/paymentUtil';
import { NotificationUtil } from '../../utilities/notificationUtil';

// Models
import { Order } from '../../models/order';
import { User } from '../../models/user';
import { Payment } from '../../models/payment';
import { CountDTO } from '../../dto/countDTO';

// Constants
const orderSamples = 8;
const orderManufacture = 11;
const orderRemainingPayment = 13;

/**
 * @author Daniel Mejia
 * @file order.component.ts
 * @description Order component
 */
@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})

export class OrderComponent extends StateMachine implements OnInit {

  /**
   * The payment modal reference.
   *
   * @type { TemplateRef<any> }
   */
  @ViewChild('paymentModal') paymentModalRef: TemplateRef<any>;

  /**
   * Notfy save button.
   */
  notifySaveButton: Subject<string> = new Subject<string>();

  /**
   * Flag that indicates if the save button must be enable.
   *
   * @type { boolean }
   */
  enableSaveFeatureButton = false;

  /**
   * The form validator helper to validates the FormGroup.
   *
   * @type { FormValidatorHelper<FormGroup> }
   */
  formValidatorHelper: FormValidatorHelper<FormGroup>;

  /**
   * An array of countDTO
   *
   *  @type { CountDTO[] }
   */
  countDTOs: CountDTO[];

  /**
   * An array of countDTO
   *
   *  @type { CountDTO[] }
   */
  countDTOsMedium: CountDTO[];

  /**
   * An array of countDTO
   *
   *  @type { CountDTO[] }
   */
  countDTOsSmall: CountDTO[];

  /**
   * The icons per state.
   *
   * @type { object }
   */
  icons = {
   'assets/stepBar/Incomplete.png': [1],
   'assets/stepBar/Quote.png': [2, 3],
   'assets/stepBar/Model.png': [4, 5, 6, 7],
   'assets/stepBar/Samples.png': [8, 9, 10],
   'assets/stepBar/Production.png': [11, 12, 13, 16],
   'assets/stepBar/Complete.png': [14, 15]
  };

  /**
   * Creates an instance of OrderComponent.
   *
   * @param { OrderService } orderService service OrderService
   * @param { LayoutComponent } layout component LayoutComponent
   * @param { Router } router dependencie Router
   * @param { userService } userService service UserService
   * @param { FormBuilder } formBuilder dependencie FormBuilder
   * @param { NgbModal } modalService dependencie NgbModal
   * @param { PaymentUtil } paymentUtil helper PaymentUtil
   * @param { NotificationUtil } notificationUtil helper NotificationUtil
   * @param { CompanyService } companyService service CompanyService
   */
  constructor(
    orderService: OrderService,
    layout: LayoutComponent,
    private router: Router,
    private route: ActivatedRoute,
    userService: UserService,
    private formBuilder: FormBuilder,
    private modalService: NgbModal,
    private paymentUtil: PaymentUtil,
    notificationUtil: NotificationUtil,
    private companyService: CompanyService
    ) {
    super(layout, orderService, notificationUtil, userService);
    this.orderForm = this.formBuilder.group(this.formFields);
    this.orderForm.valueChanges.subscribe(() => this.thereChanges = true);
    this.formValidatorHelper = new FormValidatorHelper(this.orderForm);
  }

  /**
   * Callback on init component.
   */
  ngOnInit() {
    this.getDashboard();
    const idRequest = +this.route.snapshot.paramMap.get('id');
    if (idRequest) {
      let addProduct;
      this.route
        .queryParams
        .subscribe(params => addProduct = params['action']);
      this.getOrder(idRequest, addProduct);
    }
  }

  /**
   * Gets the order by id.
   *
   * @param { number } orderId to search the order.
   * @param { any } addProduct flag to add product.
   * @param { Function } callback To execute after get order.
   */
  getOrder(orderId: number, addProduct?: any, callback: Function = () => {}): void {
    this.layout.spinner.show();
    const afterFind = (order: Order) => {
      this.setOrder(order);
      callback();
      if (addProduct) {
        this.addProduct();
      }
    };
    this.orderService.findBy(orderId)
      .subscribe(afterFind, this.manageError);

    this.formValidatorHelper = new FormValidatorHelper(this.orderForm);
  }

  /**
   * Save an order.
   *
   * @param { any } requestParams request params to navigate.
   */
  save(requestParams?: any): void {
    this.layout.spinner.show();
    const redirectTo = (storedOrder: Order): void => {
      this.notificationUtil.onSuccess('Order created!');
      this.router.navigate([`orders/${storedOrder.id}/`], { queryParams: requestParams });
    };
    this.orderService.add(this.orderForm.value)
      .subscribe(redirectTo, this.manageError);
  }

  /**
   * Updates current product and after the order.
   */
  update(): void {
    this.layout.spinner.show();
    if (this.order.products.length) {
      this.notifySaveButton.next();
    } else {
      this.orderService.updatePatch(this.order.id, this.orderForm.value)
        .subscribe((order: Order) => this.onOrderUpdated(order, 'Order saved!'), this.manageError);
    }
  }

  /**
   * Upload the 3D models.
   */
  upload3DModels(): void {
    this.notifySaveButton.next('uploadFiles');
  }

  /**
   * Sets the final quote and put in Pending3DModelandFinalQuoteApproval to the customer.
   */
  createFinalQuote(): void {
    this.layout.spinner.show();
    this.orderService.updateOrder('finalQuote', this.order, this.orderForm.value)
      .subscribe((order: Order) => this.onOrderUpdated(order, 'Order saved!'), this.manageError);
  }

  /**
   * Update the features with the samples quantity.
   */
  updateFeatures(): void {
    this.notifySaveButton.next('samples');
  }

  /**
   * Callback when the a notification is received.
   */
  onNotification(response: any): void {
    if (response && response.error) {
      this.manageError(response.error);
    } else if (response && response.modelsUpload) {
      this.getOrder(this.order.id);
      this.notificationUtil.onSuccess('Models uploaded');
    } else if (response && response.featureUpdated) {
      this.notificationUtil.onSuccess('Order saved!');
    } else if (response && response.refresh) {
      this.getOrder(this.order.id, false, response.callback);
      this.notificationUtil.onSuccess(response.message);
    } else {
      this.orderService.updatePatch(this.order.id, this.orderForm.value)
        .subscribe((order: Order) => this.onOrderUpdated(order, 'Order saved!'), this.manageError);
    }
  }

  /**
   * Callback when the a notification is received.
   */
  onFeatureSelected(response: any): void {
    this.enableSaveFeatureButton = response;
  }

  /**
   * Initialize the shipping address modal component.
   */
  openShippingAddressFormModal(): void {
    this.layout.spinner.show();
    const userData = UserHelper.getUserData();
    const onModalClose = (order: Order) => {
      this.setOrder(order);
      this.openModal(this.paymentModalRef);
    };
    const openModal = (user: User) => {
      this.layout.spinner.hide();
      const modalRef = this.modalService.open(AddressFormModalComponent).componentInstance;
      modalRef.order = this.order;
      modalRef.user = user;
      modalRef.shippingAddressType = this.paymentStatesActions[this.orderState.id].type;
      modalRef.closeEvent
        .subscribe(onModalClose);
    };

    this.userService.findByAuthKey(userData.authKey)
      .subscribe(openModal, this.manageError);
  }

  /**
   * Open a bank modal for make a payment.
   */
  addPayment(): void {
    const { amount, afterPayment } = this.paymentStatesActions[this.orderState.id];
    const addPayment = (bankReponse): Promise<Payment> => {
      this.layout.spinner.show();
      return this.paymentUtil.addPayment(bankReponse, this.order, amount);
    };

    this.paymentUtil.openBankModal()
      .then(addPayment)
      .then(() => afterPayment())
      .catch(this.manageError);
  }

  /**
   * Initialize the shipping info modal component.
   */
  openInfoModal(): void {
    this.layout.spinner.show();
    const modalRef = this.modalService.open(ShippingInfoModalComponent);
    modalRef.componentInstance.orderId = this.order.id;

    modalRef.componentInstance.changeStateEvent
      .subscribe(this.setOrder);

    modalRef.result
      .catch(this.manageError);
  }

  /**
   * Open the modal to add a comment when the order will be reject.
   */
  openModal = (content: any): void => {
    this.modalService.open(content, { size: 'lg' });
  }

  /**
   * Callback when the product status form changes.
   *
   * @param { boolean } productFormStatus the product form status.
   */
  onProductStatusChange(productFormStatus: boolean): void {
    this.productFormValid = productFormStatus;
    this.thereChanges = true;
  }

  /**
   * Adds a product to order view.
   */
  addProduct(): void {
    if (this.order) {
      this.order.products.push(null);
      this.currentTab = this.order.products.length - 1;
    } else {
      this.save({ action: 'openProduct' });
    }
  }

  /**
   * Removes a product to order view.
   */
  removeProduct(): void {
    this.order.products.pop();
    this.currentTab = this.order.products.length - 1;
  }

  /**
   * Gets the object for the payment according to the order state.
   *
   * @returns { object } The object with the information for the payments.
   */
  get paymentStatesActions(): object {
    return {
      [orderSamples]: {
        type: 'Samples',
        amount: this.orderForm.get('samplesTotal').value,
        afterPayment: this.samplesInProduction
      },
      [orderManufacture]: {
        type: 'Manufacture',
        amount: this.firstPayment,
        afterPayment: this.orderInProduction
      },
      [orderRemainingPayment]: {
        type: 'Remaining',
        amount: this.lastPayment,
        afterPayment: this.shippingOrder
      }
    };
  }

  /**
   * Send an email to the Admin requesting that the Company Info be filled.
   */
  sendRequest(): void {
    this.layout.spinner.show();
    const emailSent = response => {
      this.layout.spinner.hide();
      this.notificationUtil.onSuccess(response['message']);
    };
    this.companyService.sendRequest()
      .subscribe(emailSent, this.manageError);
  }

  /**
   * Validate if the current user has Company Info.
   *
   * @returns { boolean } True if the current user has Company Info.
   */
  get hasCompanyInformation(): boolean {
    return !!this.currentUser.company;
  }

  /**
   * Gets the dashboard values.
   */
  getDashboard(): void {
    this.layout.spinner.show();
    this.orderService.getDashboard()
      .subscribe(this.setCountDTO, this.manageError);
  }

  /**
   * Sets the countDTOs.
   *
   * @param { CountDTO[] } countDTOs the countDTOs.
   */
  setCountDTO = (countDTOs: CountDTO[]): void => {
    this.countDTOs =
      countDTOs
        .filter(countDTO => countDTO.entity.id !== 15)
        .sort((a, b) => a.entity.sequenceState - b.entity.sequenceState);

    const sequenceState = this.order ? this.order.state.sequenceState : 1;

    this.countDTOsMedium = this.countDTOs
      .filter(countDTO => countDTO.entity.sequenceState >= (sequenceState - 3)
              && countDTO.entity.sequenceState <= (sequenceState + 3))
      .sort((a, b) => a.entity.sequenceState - b.entity.sequenceState);

    this.countDTOsSmall = this.countDTOs
      .filter(countDTO => countDTO.entity.sequenceState === sequenceState
              || countDTO.entity.sequenceState === (sequenceState - 1)
              || countDTO.entity.sequenceState === (sequenceState + 1))
      .sort((a, b) => a.entity.sequenceState - b.entity.sequenceState);
    this.layout.spinner.hide();
  }

  /**
   * Get the icon by the state given.
   *
   * @param { number } state the state.
   *
   * @return { string[] } the icons values.
   */
  getIcon = (state: number): string[] => {
    return Object.keys(this.icons)
      .filter(key => this.icons[key].includes(state));
  }

  /**
   * Validates if the user is a Customer.
   *
   * @returns { boolean } true if the user is a Customer, false otherwise.
   */
  get userIsCustomer(): boolean {
    return UserHelper.sessionUserIs('CUSTOMER');
  }
}
