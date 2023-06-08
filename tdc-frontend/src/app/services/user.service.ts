/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

import { Injectable } from '@angular/core';
import { User } from '../models/user';
import { GenericService } from './generic.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

/**
 * @author Daniel Mejia
 * @file user.service.ts
 * @description User service
 */
@Injectable({ providedIn: 'root' })
export class UserService extends GenericService<User> {

  // Constants
  private static resource = 'users';
  public static sharedKeyHeader = 'sharedKey';
  public static subjectHeader = 'subject';
  public static shareKeyValue = environment.auth.registerKey;
  public static subjectValue = environment.app;
  public static registerTokenHeader = 'registerToken';

  /**
   * Creates an instance of OrderService.
   *
   * @param httpClient The HttpClient to make request calls.
   */
  constructor(httpClient: HttpClient) {
    super(httpClient, UserService.resource);
  }

  /**
   * Get all users.
   *
   * @param { string[] } roles optional roles to filter the users.
   *
   * @returns { Observable<User[]> } the User observable array.
   */
  getAllUsers(roles?: string[]): Observable<User[]> {
    const rolesParams = roles ? { role: roles } : { };
    return this.httpClient
      .get<User[]>(this.apiUrl, { params: this.getHttpParams(rolesParams) });
  }

  /**
   * Gets the register token.
   *
   * @returns { Observable<any> } the typed observable.
   */
  getRegisterToken(): Observable<any> {
    const headers = new HttpHeaders({
      [UserService.sharedKeyHeader]: UserService.shareKeyValue,
      [UserService.subjectHeader]: UserService.subjectValue
    });
    return this.httpClient.get(`${this.apiUrl}/token`, { headers });
  }

  /**
   * Register and user in the api.
   *
   * @param { User } user the user to add.
   * @param { string } registerToken the register token.
   *
   * @returns { Observable<User> } the typed observable.
   */
  registerUser(user: User, registerToken: string): Observable<User> {
    const headers = new HttpHeaders({ [UserService.registerTokenHeader]: registerToken });
    return this.httpClient.post<User>(this.apiUrl, user, { headers });
  }

  /**
   * Find a user by authKey.
   *
   * @param { string } authKey user auth key.
   *
   * @returns { Observable<User> } the typed observable.
   */
  findByAuthKey(authKey: string): Observable<User> {
    const requestUrl = `${this.apiUrl}/authKey/${authKey}`;
    return this.httpClient.get<User>(requestUrl);
  }

  /**
   * Change the user password.
   *
   * @param { string } authKey user auth key.
   * @param { any } params the params map.
   *
   * @returns { Observable<any> } the typed observable.
   */
  changePassword(authKey: string, params: any): Observable<any> {
    const requestUrl = `${this.apiUrl}/${authKey}`;
    return this.updatePatch(0, params, requestUrl);
  }
}
