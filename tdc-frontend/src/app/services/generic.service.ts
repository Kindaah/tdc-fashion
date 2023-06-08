/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Inject } from '@angular/core';

/**
 * @author Daniel Mejia
 * @file generic.service.ts
 * @description Generic service
 */
export class GenericService<T> {

  /**
   * Api url for request calls.
   *
   * @type { string }
   */
  apiUrl: string;

  /**
   * Creates an instance of GenericService
   *
   * @param { HttpClient } httpClient The HttpClient to make request calls.
   * @param { string } resourceUrl The resource url to each model.
   */
  constructor(protected httpClient: HttpClient, @Inject('RESOURCE_URL') resourceUrl: string) {
    this.apiUrl = `${environment.api}/${resourceUrl}`;
  }

  /**
   * Gets all objects.
   *
   * @param { string } url optional url to override default.
   *
   * @returns { Observable<T[]> } the typed observable array.
   */
  getAll(url: string = this.apiUrl): Observable<T[]> {
    return this.httpClient.get<T[]>(url);
  }

  /**
   * Gets an order by id or by overrided URL.
   *
   * @param { number | string } param If type is number then order id otherwise overrided URL.
   *
   * @returns { Observable<T> } the typed observable.
   */
  findBy(param: number | string): Observable<T> {
    const requestUrl = typeof param === 'number' ? `${this.apiUrl}/${param}` : param;
    return this.httpClient.get<T>(requestUrl);
  }

  /**
   * Add an object.
   *
   * @param { T } object the object to add.
   * @param { string } url optional url to override default.
   *
   * @returns { Observable<T> } the typed observable.
   */
  add(object: T, url: string = this.apiUrl): Observable<T> {
    return this.httpClient.post<T>(url, object);
  }

  /**
   * Updates an object.
   *
   * @param { number } objectId the objec id.
   * @param { T } object the object to update.
   * @param { string } url optional url to override default.
   *
   * @returns { Observable<T> } the typed observable.
   */
  update(objectId: number, object: T, url: string = `${this.apiUrl}/${objectId}`): Observable<T> {
    return this.httpClient.put<T>(url, object);
  }

  /**
   * Updates an object with patch method passing http params.
   *
   * @param { number } objectId the objec id.
   * @param { object } params the params map.
   * @param { string } url optional url to override default.
   *
   * @returns { Observable<T> } the typed observable.
   */
  updatePatch(objectId: number, params: object, url: string = `${this.apiUrl}/${objectId}`): Observable<T> {
    return this.httpClient.patch<T>(url, this.getHttpParams(params));
  }

  /**
   * Gets the HttpParams for a request.
   *
   * @param { object } params The params object.
   *
   * @returns { HttpParams } The http params.
   */
  getHttpParams(params: object): HttpParams {
    let httpParams = new HttpParams();
    Object.keys(params)
      .forEach(key => httpParams = httpParams.append(key, params[key]));

    return httpParams;
  }
}
