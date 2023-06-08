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

// Services
import { OrderService } from '../../services/order.service';

// Components
import { LayoutComponent } from '../../components/layout/layout.component';
import { LoginComponent } from '../login/login.component';

// Helpers
import * as UserHelper from '../../helpers/userHelper';

// Models
import { CountDTO } from '../../dto/countDTO';

/**
 * @author Daniel Mejia
 * @file dashboard.component.ts
 * @description Dashboard component
 */

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit {

  /**
   * An array of countDTO
   *
   *  @type { CountDTO[] }
   */
  countDTOs: CountDTO[];

  /**
   * The error message to show.
   *
   * @type { any }
   */
  errorMessage: any;

  /**
   * User helper class instance.
   */
  userHelper = UserHelper;

  /**
   * The icons per state.
   *
   * @type { object }
   */
  icons = {
   'assets/dashboard/Incomplete-enable.png': [1],
   'assets/dashboard/Quote-enable.png': [2, 3],
   'assets/dashboard/Model-enable.png': [4, 5, 6, 7],
   'assets/dashboard/Samples-enable.png': [8, 9, 10],
   'assets/dashboard/Production-enable.png': [11, 12, 13, 16],
   'assets/dashboard/Complete-enable.png': [14, 15]
  };

  /**
   * Creates an instance of Dashboard component.
   *
   * @param { LoginComponent } loginComponent Provider from the LoginComponent
   * @param { OrderService } orderService The order service to process.
   * @param { LayoutComponent } layout Provider from the LayoutComponent
   */
  constructor(
      private loginComponent: LoginComponent,
      private orderService: OrderService,
      private layout: LayoutComponent
    ) {}

  /**
   * Callback on init component.
   */
  ngOnInit(): void {
    this.loginComponent.spinner.hide();
    this.getDashboard();
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
   * Sets the countDTOs for the dashborad.
   *
   * @param { CountDTO[] } countDTOs the countDTOs.
   */
  setCountDTO = (countDTOs: CountDTO[]): void => {
    this.countDTOs =
      countDTOs
        .filter(countDTO => countDTO.count > 0)
        .sort((a, b) => a.entity.id - b.entity.id);
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
   * Manage when an error happens.
   *
   * @param { any } error the error.
   */
  manageError = (error: any): void => {
    this.errorMessage = error;
    this.layout.spinner.hide();
  }
}
