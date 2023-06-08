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
import { AdminService } from '../admin.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { User } from 'src/app/models/user';

/**
 * @author Daniel Mejia
 * @file admin.service.spec.ts
 * @description Admin Service test class
 */
describe('Service: AdminService', () => {
  let service: AdminService;
  let httpMock: HttpTestingController;
  const testRole = 'TEST';
  const mockObject = {
    id: 1,
    companyEmail: 'testEmail'
  } as User;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AdminService,
      ]
    });

    service = TestBed.get(AdminService);
    httpMock = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('Constructor: Should be created', () => {
    expect(service).toBeTruthy();
  });

  it('addUserTeam: Should be add a team member observable<User>', () => {
    service.addUserTeam(mockObject, testRole)
      .subscribe(member => expect(member).toBe(mockObject));
    const testRequest = httpMock.expectOne(`${service.apiUrl}/team?role=${testRole}`);
    expect(testRequest.request.method).toBe('POST');
    testRequest.flush(mockObject);
  });
});
