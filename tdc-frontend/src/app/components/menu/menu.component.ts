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
import { AuthService } from '../../services/auth.service';

// Components
import { LayoutComponent } from '../layout/layout.component';

// Helpers
import * as UserHelper from '../../helpers/userHelper';

/**
 * @author Daniel Mejia
 * @file menu.component.ts
 * @description Menu component
 */

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})

export class MenuComponent implements OnInit {

  /**
   * Flag to set if menu is show visible or hidden.
   */
  public isCollapsed = true;

  /**
   * Creates an instance of MenuComponent.
   */
  constructor(private layout: LayoutComponent, private authService: AuthService) { }

  /**
   * Callback on init component.
   */
  ngOnInit() {
  }

  /**
   * Logout the user and redirec to login view.
   */
  logout() {
    this.layout.spinner.show();
    this.authService.logout();
  }

  /**
   * Validates if the user is an Admin.
   *
   * @returns { boolean } indicates if th user is an Admin.
   */
  get userIsAdmin(): boolean {
    return UserHelper.sessionUserIs('ADMIN');
  }

  /**
   * Validates if the user is a Designer.
   *
   * @returns { boolean } indicates if th user is a Designer.
   */
  get userIsDesigner(): boolean {
    return UserHelper.sessionUserIs('DESIGNER');
  }

  /**
   * Checks if the order is on the state and the current user has role.
   *
   * @param { string } role the user role to check.
   *
   * @returns { boolean } true if the order has the state and the user has the role.
   */
  mustBeShowed(role: string): boolean {
    return UserHelper.sessionUserIs(role);
  }

}
