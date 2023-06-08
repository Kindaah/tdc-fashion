/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as UserHelper from '../../helpers/userHelper';

// Services
import { OrderService } from './../../services/order.service';

// Components
import { LayoutComponent } from '../../components/layout/layout.component';

// Models
import { CountDTO } from '../../dto/countDTO';

/**
 * @author Daniel Mejia
 * @file orders.component.ts
 * @description Orders component
 */
@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})

export class OrdersComponent implements OnInit {

  /**
   * Variable for the Virtual Scroll plugin.
   *
   * @type { any }
   */
  viewPortItems: any;

  /**
   * An array of countDTO
   *
   *  @type { CountDTO[] }
   */
  countDTOs: CountDTO[];

  /**
   * An array of filtered countDTO
   *
   *  @type { CountDTO[] }
   */
  countDTOsFiltered: CountDTO[];

  /**
   * The list of states
   *
   *  @type { string[] }
   */
  states: string[] = ['All'];

  /**
   * The error message to show.
   *
   * @type { any }
   */
  errorMessage: any;

  /**
   * The flag that indicates if a button is active.
   *
   * @type { string }
   */
  flagButtonActive = 'All';

  /**
   * User helper class instance.
   */
  userHelper = UserHelper;

  /**
   * Creates an instance of OrdersComponent.
   *
   * @param { OrderService } orderService order service.
   * @param { LayoutComponent } layout component LayoutComponent
   * @param { ActivatedRoute } route dependencie to ActivatedRoute
   */
  constructor(
      private orderService: OrderService,
      private layout: LayoutComponent,
      private route: ActivatedRoute
    ) {}

  /**
   * Callback on init component.
   */
  ngOnInit(): void {
    this.route
      .queryParams
      .subscribe(params => this.flagButtonActive = params['filter'] || 'All');
    this.getAllOrders();
  }

  /**
   * Gets all the orders.
   *
   */
  getAllOrders(): void {
    this.layout.spinner.show();
    this.orderService.getAllDTO()
      .subscribe(this.setCountDTO, this.manageError);
  }

  /**
   * Sets the countDTOs.
   *
   * @param { CountDTO[] } countDTOs the countDTOs.
   */
  setCountDTO = (countDTOs: CountDTO[]): void => {
    this.countDTOs = countDTOs;
    this.countDTOsFiltered = this.countDTOs;

    this.countDTOs.forEach((count) => {
      if (!this.states.includes(count.entity.state.name)) {
        this.states.push(count.entity.state.name);
      }
    });
    this.filterOrders(this.flagButtonActive);
    this.layout.spinner.hide();
  }

  /**
   * Filter the orders by state.
   *
   * @param { string } state the state to filter by.
   */
  filterOrders = (state: string): void => {
    this.flagButtonActive = state;

    if (state === 'All') {
      this.countDTOsFiltered = this.countDTOs;
    } else {
      this.countDTOsFiltered =
        this.countDTOs.filter(countDTO => countDTO.entity.state.name.includes(state));
    }
  }

  /**
   * Manage when a error happens.
   *
   * @param { any } error the error.
   */
  manageError = (error: any): void => {
    this.errorMessage = error;
    this.layout.spinner.hide();
  }
}
