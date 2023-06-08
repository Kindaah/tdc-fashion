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
import { UserService } from './../../services/user.service';

// Components
import { LayoutComponent } from '../../components/layout/layout.component';

// Models
import { User } from '../../models/user';

/**
 * @author Edson Ruiz Ramirez
 * @file users.component.ts
 * @description Users component
 */

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  /**
   * Variable for the Virtual Scroll plugin.
   *
   * @type { any }
   */
  viewPortItems: any;

  /**
   * An array of User
   *
   *  @type { User[] }
   */
  users: User[];

  /**
   * An array of filtered User
   *
   *  @type { User[] }
   */
  usersFiltered: User[];

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
   * The company states for the users.
   *
   * @type { string[] }
   */
  companyStates: string[] = [];

  /**
   * Creates an instance of OrdersComponent.
   *
   * @param { UserService } userService service UserService
   * @param { LayoutComponent } layout component LayoutComponent
   */
  constructor(
    private userService: UserService,
    private layout: LayoutComponent,
  ) { }

  /**
   * Callback on init component.
   */
  ngOnInit(): void {
    this.getAllUsers();
  }

  /**
   * Gets all the users.
   *
   */
  getAllUsers(): void {
    this.layout.spinner.show();
    this.userService.getAllUsers(['CUSTOMER'])
      .subscribe(this.setUsers, this.manageError);
  }

  /**
   * Sets the users.
   *
   * @param { User[] } users the users.
   */
  setUsers = (users: User[]): void => {
    this.users = users;
    this.usersFiltered = this.users;
    this.companyStates.push('All');

    if (this.users.some(user => !!user.company)) {
      this.companyStates.push('Complete');
    }
    if (this.users.some(user => !!!user.company)) {
      this.companyStates.push('Incomplete');

      this.flagButtonActive = 'Incomplete';
      this.filterUsers(this.flagButtonActive);
    }

    this.layout.spinner.hide();
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

  /**
   * Determine is an user has a company.
   *
   * @param { User } user the user object.
   *
   * @returns { boolean } true if the user has a company.
   */
  hasCompany = (user: User): boolean => !!user.company;

  /**
   * Return the object with the status of the company.
   *
   * @returns { object } the status of the company for the user.
   */
  get status(): object {
    return {
      'All': (users) => users,
      'Complete': (users) => users.filter(user => !!user.company),
      'Incomplete': (users) => users.filter(user => !!!user.company)
    };
  }

  /**
   * Filter the users list.
   *
   * @param { string } companyState the company state of the user.
   */
  filterUsers = (companyState: string): void => {
    this.flagButtonActive = companyState;
    this.usersFiltered = this.status[companyState](this.users);
  }
}
