/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { Injectable } from '@angular/core';
import * as auth0 from 'auth0-js';
import { environment } from '../../environments/environment';
import * as UserHelper from '../helpers/userHelper';

// Services
import { UserService } from './user.service';
import { JwtHelperService } from '@auth0/angular-jwt';

// Models
import { User } from '../models/user';

/**
 * @author Daniel Mejia
 * @file auth.service.ts
 * @description Authentication service
 */

@Injectable({ providedIn: 'root' })
export class AuthService {

  /**
   * Autho0 web service.
   *
   * @type { WebAuth }
   */
  webAuth = new auth0.WebAuth({
    domain: environment.auth.domain,
    clientID: environment.auth.clientID,
    audience: environment.auth.apiAudience,
    responseType: 'token id_token',
    scope: 'openid'
  });

  /**
   * Auth0 client.
   *
   * @type { any }
   */
  authClient: any;

  /**
   * Creates an instance of AuthService.
   *
   * @param { UserService } userService The user service.
   * @param { JwtHelperService } jwtHelperService The jwt service helper.
   */
  constructor(
    public userService: UserService,
    public jwtHelperService: JwtHelperService) {
    this.authClient = this.webAuth.client;
  }

  /**
   * Authenticates with auth0 using username and password.
   *
   * @param { any } userData the user data.
   *
   * @returns { Promise<{boolean, data?}> } - authentication result.
   */
  login(userData: any): Promise<{}> {
    const { username, password } = userData;
    const authData = {
      realm: environment.auth.connection,
      username,
      password
    };
    return this.authenticateUser(authData)
      .then(this.getUserInfo);
  }

  /**
   * Authenticates with auth0 using the authData object.
   *
   * @param {any} authData the authentication data.
   *
   * @returns {Promise<{boolean, data?}>} - authentication result.
   */
  authenticateUser(authData: any): Promise<{}> {
    return new Promise((resolve, reject) => {
      const loginCallback = (errorAuth, authResponse) => {
        if (authResponse && authResponse.accessToken) {
          UserHelper.setSessionData(authResponse);
          resolve(authResponse.accessToken);
        } else {
          reject(errorAuth.description);
        }
      };

      this.authClient.login(authData, loginCallback);
    });
  }

  /**
   * Gets the user information form auth0.
   *
   * @param {any} accessToken the auth0 access token.
   *
   * @returns {Promise<{boolean, data?}>} - user result.
   */
  getUserInfo = (accessToken: any): Promise<{}> => {
    return new Promise((resolve, reject) => {
      const manageUserInfo = (userStored) => {
        UserHelper.setUserData(userStored);
        resolve(true);
      };
      const decodedToken = this.jwtHelperService.decodeToken(accessToken);
      const authKey = decodedToken.sub.split('|')[1];
      this.userService.findByAuthKey(authKey)
        .subscribe(manageUserInfo, error => reject(error));
    });
  }

  /**
   * Send an email to recover password.
   *
   * @param userEmail the user email.
   *
   * @returns {Promise<{boolean, data?}>} - the response.
   */
  sendRecoverPassword(userEmail: string): Promise<{}> {
    return new Promise((resolve, reject) => {
      const config = {
        connection: environment.auth.connection,
        email: userEmail
      };
      const onEmailSent = (error, response) => {
        if (error) {
          reject(error);
        } else {
          resolve(response);
        }
      };
      this.webAuth.changePassword(config, onEmailSent);
    });
  }

  /**
   * Sign up an user in auth0 and the api.
   *
   * @param { User } userData the user data.
   *
   * @returns { Promise<{boolean, data?}> } - the response.
   */
  signUp(userData: User, password: string): Promise<{}> {
    const configuration = {
      connection: environment.auth.connection,
      email: userData.companyEmail,
      password
    };
    return this.registerAuth0(configuration)
      .then(userAuthKey => {
        userData.authKey = `${userAuthKey}`;
        return this.getRegisterToken();
      })
      .then(registerToken => this.registerApi(userData, registerToken));
  }

  /**
   * Register the user in Auth0 platform.
   *
   * @param { object } configuration the configuration for the register.
   *
   * @returns { Promise<any> } - the response with the auth key.
   */
  registerAuth0(configuration: object): Promise<any> {
    return new Promise((resolve, reject) => {
      const onUserSignedUp = (error, auth0Response) => {
        if (error) {
          reject(error);
        } else {
          resolve(auth0Response['Id']);
        }
      };
      this.webAuth.signup(configuration, onUserSignedUp);
    });
  }

  /**
   * Gets the register token.
   *
   * @returns { Promise<{boolean, data?}> } - the response.
   */
  getRegisterToken = (): Promise<any> => {
    return new Promise((resolve, reject) => {
      this.userService.getRegisterToken()
        .subscribe(apiResponse => resolve(apiResponse.registerToken), apiError => reject(apiError));
    });
  }

  /**
   * Register an user in the API.
   *
   * @param { any } userData the user data to register the user.
   * @param { string } registerToken the register token.
   *
   * @returns { Promise<{ boolean, data? }> } - the response.
   */
  registerApi = (userData: any, registerToken: string): Promise<{}> => {
    return new Promise((resolve, reject) => {
      this.userService.registerUser(userData, registerToken)
        .subscribe((_user: User) => resolve(true), apiError => reject(apiError));
    });
  }

  /**
   * Logout the current user from the session.
   */
  logout(): void {
    UserHelper.cleanLocalStorage();
    this.webAuth.logout({
      clientID: environment.auth.clientID,
      returnTo: environment.app
    });
  }
}
