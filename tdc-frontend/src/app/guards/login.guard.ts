/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import * as UserHelper from '../helpers/userHelper';

/**
 * @author Daniel Mejia
 * @file login.guard.ts
 * @description Login Guard
 */

@Injectable()
export class LoginGuard implements CanActivate {

  /**
   * Creates an instance of LoginGuard.
   *
   * @param { Router } router The router.
   */
  constructor(private router: Router) {}

  /**
   * Can activate route.
   *
   * @type { ActivatedRouteSnapshot } next - ActivatedRouteSnapshot instance.
   * @type { RouterStateSnapshot } state - RouterStateSnapshot instance.
   *
   * @returns { (Observable<boolean> | Promise<boolean> | boolean) } is authenticated.
   */
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean  {

    if (UserHelper.isSessionValid()) {
        return true;
    }

    this.router.navigate(['login'], { queryParams: { returnUrl: state.url }});
    return false;
  }
}
