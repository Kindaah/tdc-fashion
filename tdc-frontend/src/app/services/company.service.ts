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
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Enviroments
import { environment } from '../../environments/environment';

// Services
import { GenericService } from './generic.service';

// Models
import { Company } from '../models/company';

/**
 * @author Edson Ruiz Ramirez
 * @file company.service.ts
 * @description Company service
 */

@Injectable({ providedIn: 'root' })
export class CompanyService extends GenericService<Company> {

  // Constants
  private static resource = 'company';

  /**
   * Creates an instance of OrderService.
   *
   * @param { HttpClient } httpClient The HttpClient to make request calls.
   */
  constructor(httpClient: HttpClient) {
    super(httpClient, CompanyService.resource);
  }

  /**
   * Add a company.
   *
   * @param { number } userId The User of the Company.
   * @param { Company } company The company to add.
   *
   * @returns { Observable<Company> } The company info observable.
   */
  addCompany(userId: number, company: Company): Observable<Company> {
    const requestUrl = `${environment.api}/users/${userId}/company`;
    return this.add(company, requestUrl);
  }

  /**
   * Update a company.
   *
   * @param { number } companyId The Company id.
   * @param { Company } company The Company to update.
   *
   * @returns { Observable<Company> } The company observable.
   */
  updateCompany(companyId: number, company: Company): Observable<Company> {
    const requestUrl = `${environment.api}/users/company/${companyId}`;
    return this.update(companyId, company, requestUrl);
  }

  /**
   * Send an email to the Admin requesting that the Company Info be filled.
   *
   * @returns { Observable<object> } The object observable.
   */
  sendRequest(): Observable<object> {
    const requestUrl = `${environment.api}/users/companyRequest`;
    return this.httpClient.get(requestUrl);
  }
}
