/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

import { TestBed } from '@angular/core/testing';
import { GenericService } from '../generic.service';
import { Generic } from '../../models/generic';
import { HttpClientTestingModule, HttpTestingController, TestRequest } from '@angular/common/http/testing';
import { environment } from '../../../environments/environment';
import { HttpParams } from '@angular/common/http';
import { forkJoin } from 'rxjs';

/**
 * @author Daniel Mejia
 * @file generic.service.spec.ts
 * @description Generic Service test class
 */
describe('Service: GenericService', () => {
  const overridedUrl = '/overrided';
  const testResource = 'testResource';
  const testId = 1;
  const testUrl = `${environment.api}/${testResource}`;

  let service: GenericService<Generic>;
  let httpMock: HttpTestingController;
  let mockObject: Generic;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        GenericService,
        { provide: 'RESOURCE_URL', useValue: testResource }
      ]
    });

    service = TestBed.get(GenericService);
    httpMock = TestBed.get(HttpTestingController);
    mockObject = {
      id: testId,
      createdAt: 'created_At',
      updatedAt: 'updated_at'
    };
  });

  afterEach(() => {
    httpMock.verify();
  });

  const verifyRequest = (url: string, method: string, returnValue: any): TestRequest => {
    const testRequest = httpMock.expectOne(url);
    expect(testRequest.request.method).toBe(method);
    testRequest.flush(returnValue);

    return testRequest;
  };

  const checkResults = results =>
    results.forEach(object => expect(object).toEqual(mockObject));

  it('Constructor: Should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getAll: Should be get observable<T[]>', () => {
    const default$ = service.getAll();
    const overrided$ = service.getAll(overridedUrl);
    forkJoin(default$, overrided$)
      .subscribe(results =>
        results.forEach(object => expect(object).toEqual([mockObject]))
      );

    verifyRequest(`${testUrl}`, 'GET', [mockObject]);
    verifyRequest(overridedUrl, 'GET', [mockObject]);
  });

  it('findBy: Should be get observable<T>', () => {
    const default$ = service.findBy(testId);
    const overrided$ = service.findBy(overridedUrl);
    forkJoin(default$, overrided$)
      .subscribe(checkResults);

    verifyRequest(`${testUrl}/${testId}`, 'GET', mockObject);
    verifyRequest(overridedUrl, 'GET', mockObject);
  });

  it('add: Should be add object and get observable<T>', () => {
    const default$ = service.add(mockObject);
    const overrided$ = service.add(mockObject, overridedUrl);
    forkJoin(default$, overrided$)
      .subscribe(checkResults);

    verifyRequest(`${testUrl}`, 'POST', mockObject);
    verifyRequest(overridedUrl, 'POST', mockObject);
  });

  it('update: Should be updates and object and get observable<T>', () => {
    const default$ = service.update(testId, mockObject);
    const overrided$ = service.update(testId, mockObject, overridedUrl);
    forkJoin(default$, overrided$)
      .subscribe(checkResults);

    verifyRequest(`${testUrl}/${testId}`, 'PUT', mockObject);
    verifyRequest(overridedUrl, 'PUT', mockObject);
  });

  it('updatePatch: Should be updates patch and object and get observable<T>', () => {
    const testParam = 'testParam';
    const mimeType = 'application/x-www-form-urlencoded;charset=UTF-8';
    const default$ = service.updatePatch(testId, { testKey: testParam });
    const overrided$ = service.updatePatch(testId, { testKey: testParam }, overridedUrl);
    forkJoin(default$, overrided$)
      .subscribe(checkResults);

    const requestDefault = verifyRequest(`${testUrl}/${testId}`, 'PATCH', mockObject);
    expect(requestDefault.request.body.get('testKey')).toBe(testParam);
    expect(requestDefault.request.detectContentTypeHeader()).toBe(mimeType);

    const requetOverrided = verifyRequest(overridedUrl, 'PATCH', mockObject);
    expect(requetOverrided.request.body.get('testKey')).toBe(testParam);
    expect(requetOverrided.request.detectContentTypeHeader()).toBe(mimeType);
  });

  it('getHttpParams: Should be get httpParams', () => {
    const testValue = 'testValue';
    const paramsObject = { testKey: testValue };
    const httpParams: HttpParams = service.getHttpParams(paramsObject);

    expect(httpParams.get('testKey')).toBe(testValue);
  });
});
