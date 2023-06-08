/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { LoginGuard } from './login.guard';
import * as UserHelper from '../helpers/userHelper';
import { TestBed } from '@angular/core/testing';
import { Router, RouterStateSnapshot } from '@angular/router';

/**
 * @author Daniel Mejia
 * @file login.guard.spec.ts
 * @description Login guard testing class
 */
describe('Guard: Login',  () => {
  const testToken = 'token';
  const testUrl = 'url test';
  let guard: LoginGuard;
  const mockRouter: jasmine.SpyObj<Router> =
    jasmine.createSpyObj<Router>('Router', ['navigate']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        LoginGuard,
        { provide: Router, useValue: mockRouter },
      ]
    });
    guard = TestBed.get(LoginGuard);
    UserHelper.cleanLocalStorage();
  });

  it('canActivate: Should check if session is wrong', () => {
    expect(guard.canActivate(null, { url: testUrl} as RouterStateSnapshot )).toBeFalsy();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['login'], { queryParams: { returnUrl: testUrl }});
  });

  it('canActivate: Should check if session is okay', () => {
    UserHelper.setSessionData(testToken);
    expect(guard.canActivate(null, null)).toBeTruthy();
  });
});
