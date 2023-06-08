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
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';

// Models
import { User } from '../models/user';

/**
 * @author Daniel Mejia
 * @file admin.service.ts
 * @description Admin service
 */
@Injectable({ providedIn: 'root' })
export class AdminService {

  /**
   * Api url for request calls.
   *
   * @type { string }
   */
  apiUrl: string;

  /**
   * Creates an instance of OrderService.
   *
   * @param { HttpClient } httpClient The HttpClient to make request calls.
   */
  constructor(private httpClient: HttpClient) {
    this.apiUrl = `${environment.api}/admin`;
  }

  /**
   * Adds a team member to the application.
   *
   * @param { User } teamMember The new member.
   * @param { string } role The new member role.
   *
   * @returns { Observable<User> } The member added.
   */
  addUserTeam(teamMember: User, role: string): Observable<User> {
    const params = new HttpParams().append('role', role);
    return this.httpClient.post<User>(`${this.apiUrl}/team`, teamMember, { params });
  }
}
