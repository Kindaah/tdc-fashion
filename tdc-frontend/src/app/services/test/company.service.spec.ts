/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { environment } from 'src/environments/environment';

// Services
import { CompanyService } from '../company.service';

// Models
import { Company } from '../../models/company';

/**
 * @author Edson Ruiz Ramirez
 * @file company.service.spec.ts
 * @description Company Service test class
 */
describe('Service: CompanyService', () => {
  let service: CompanyService;
  let httpMock: HttpTestingController;
  const testObject = { message: 'message' };
  const testUserId = 1;
  const testCompanyId = 2;
  const testCompany: Company = {
    id: testCompanyId
  } as Company;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CompanyService]
    });
    service = TestBed.get(CompanyService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('Constructor: Should be created', () => {
    expect(service).toBeTruthy();
  });

  it('addCompany: Should be create the company', () => {
    spyOn(service, 'add').and.returnValue(of(testCompany));
    service.addCompany(testUserId, testCompany)
      .subscribe(response => expect(response).toBe(testCompany));
  });

  it('updateCompany: Should be update the company', () => {
    spyOn(service, 'update').and.returnValue(of(testCompany));
    service.updateCompany(testCompanyId, testCompany)
      .subscribe(response => expect(response).toBe(testCompany));
  });

  it('sendRequest: Should send a request', () => {
     service.sendRequest()
      .subscribe(response => expect(response['message']).toBe(testObject['message']));

    const testRequest = httpMock.expectOne(`${environment.api}/users/companyRequest`);
    expect(testRequest.request.method).toBe('GET');
    testRequest.flush(testObject);
  });
});
