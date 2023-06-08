/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

/**
 * @author Daniel Mejia
 * @file auth.service.spec.ts
 * @description Authentication service test class
 */

// Dependencies
import { TestBed } from '@angular/core/testing';
import * as sinon from 'sinon';
import * as UserHelper from '../../helpers/userHelper';
import { JwtHelperService } from '@auth0/angular-jwt';
import { of, throwError } from 'rxjs';
import { JwtModule } from '@auth0/angular-jwt';
import { HttpClientModule } from '@angular/common/http';

// Services
import { AuthService } from '../auth.service';
import { UserService } from './../user.service';

// Models
import { User } from 'src/app/models/user';

describe('Service: AuthService', () => {
  const emailTest = 'test@tdc.com';
  const passTest = 'test';
  const testAccessToken = 'test_token';
  const testLoginError = 'Wrong password or email';
  const errorInfo = 'errorInfo';
  const testUserAuthKey = 'auth|12345';
  const resetMessageTest = 'sent';
  const registerToken = 'token_register';
  const apiError = 'apiError';
  const mockClientObject: any = {};
  const mockAuthWeb: any = {};
  let service: AuthService;
  let sandbox;
  let userService: UserService;
  const tokenDecoded = { sub: testUserAuthKey };
  const mockUser: User = {
    authKey: 'testKey',
    companyEmail: 'testEmail',
    role: {
      id: 1
    }
  } as User;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        JwtModule.forRoot({
          config: {
            tokenGetter: () => ''
          }
        })
      ],
      providers: [AuthService, JwtHelperService]
    });

    service = TestBed.get(AuthService);
    userService = TestBed.get(UserService);
    sandbox = sinon.sandbox.create();
  });

  afterEach(() => {
    sandbox.restore();
  });

  it('login: Should login user', (done) => {
    const authServiceMock = sandbox.mock(service);
    authServiceMock.expects('authenticateUser').withArgs()
      .returns(Promise.resolve(testAccessToken));

    authServiceMock.expects('getUserInfo').withArgs(testAccessToken)
      .returns(Promise.resolve(true));

    const testAuth = (responseLogin) => {
      expect(responseLogin).toBeTruthy();
      authServiceMock.verify();
      done();
    };

    const userData = {
      username: emailTest,
      password: passTest
    };

    service.login(userData)
      .then(testAuth)
      .catch(done);
  });

  it('authenticateUser: Should authenticated user error', (done) => {
    const testError = (errorExpected) => {
      expect(errorExpected).toBe(testLoginError);
      done();
    };

    const errorAuth = {
      description: testLoginError
    };

    mockClientObject.login = (a, callback) => callback(errorAuth);

    service.authClient = mockClientObject;
    service.authenticateUser({})
      .catch(testError);
  });

  it('authenticateUser: Should authenticated user success', (done) => {
    const testToken = (expectedToken) => {
      expect(expectedToken).toBe(testAccessToken);
      done();
    };

    const authResponse = {
      accessToken: testAccessToken
    };

    mockClientObject.login = (a, callback) => callback(false, authResponse);

    service.authClient = mockClientObject;
    service.authenticateUser({})
      .then(testToken)
      .catch(done);
  });

  it('getUserInfo: Should user info error', (done) => {
    const testError = (errorExpected) => {
      expect(errorExpected).toBe(errorInfo);
      done();
    };

    spyOn(service.jwtHelperService, 'decodeToken').and.returnValue(tokenDecoded);
    spyOn(service.userService, 'findByAuthKey').and.returnValues(throwError(errorInfo));
    service.getUserInfo(testAccessToken)
      .catch(testError);
  });

  it('getUserInfo: Should user info success', (done) => {
    const testSuccess = (response) => {
      expect(response).toBeTruthy();
      done();
    };

    spyOn(service.jwtHelperService, 'decodeToken').and.returnValue(tokenDecoded);
    spyOn(service.userService, 'findByAuthKey').and.returnValue(of(mockUser));
    service.getUserInfo(testAccessToken)
      .then(testSuccess);
  });

  it('sendRecoverPassword: Should send recover password fail', () => {
    mockAuthWeb.changePassword = (_config, callback) =>
      callback(true, null);
    service.webAuth = mockAuthWeb;
    service.sendRecoverPassword(emailTest)
      .catch(error => expect(error).toBeTruthy());
  });

  it('sendRecoverPassword: Should send recover password success', () => {
    mockAuthWeb.changePassword = (config, callback) =>
      callback(false, resetMessageTest);
    service.webAuth = mockAuthWeb;
    service.sendRecoverPassword(emailTest)
      .then(response => expect(response).toBe(resetMessageTest));
  });

  it('signUp: Should sign up', () => {
    spyOn(service, 'registerAuth0').and.returnValue(Promise.resolve(testUserAuthKey));
    spyOn(service, 'getRegisterToken').and.returnValue(Promise.resolve(registerToken));
    spyOn(service, 'registerApi').and.returnValue(Promise.resolve(true));
    service.signUp(mockUser, passTest)
      .then(response => expect(response).toBeTruthy());
  });

  it('registerAuth0: Should register in auth0 platform success', () => {
    mockAuthWeb.signup = (_config, callback) =>
      callback(false, { Id: testUserAuthKey });

    service.webAuth = mockAuthWeb;
    service.registerAuth0({})
      .then(response => expect(response).toBe(testUserAuthKey));
  });

  it('registerAuth0: Should register in auth0 platform fail', () => {
    mockAuthWeb.signup = (_config, callback) =>
      callback(errorInfo, null);

    service.webAuth = mockAuthWeb;
    service.registerAuth0({})
      .then(error => expect(error).toBe(errorInfo));
  });

  it('getRegisterToken: Should gets the register token', () => {
    spyOn(userService, 'getRegisterToken')
      .and
      .returnValues(of({ registerToken }), throwError(apiError));

    service.getRegisterToken()
      .then(response => expect(response).toBe(registerToken));
    service.getRegisterToken()
      .then(error => expect(error).toBe(apiError));
  });

  it('registerApi: Should register in the api', () => {
    spyOn(userService, 'registerUser')
      .and
      .returnValues(of(mockUser), throwError(apiError));

    service.registerApi(mockUser, registerToken)
      .then(response => expect(response).toBeTruthy());
    service.registerApi(mockUser, registerToken)
      .then(error => expect(error).toBe(apiError));
  });

  it('logout: Should logout success', () => {
    mockAuthWeb.logout = () => null;
    service.webAuth = mockAuthWeb;
    service.logout();
    expect(UserHelper.isSessionValid()).toBeFalsy();
  });
});
