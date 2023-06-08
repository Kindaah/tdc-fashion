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
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

// Services
import { UserService } from '../user.service';

// Models
import { User } from 'src/app/models/user';

/**
 * @author Daniel Mejia
 * @file user.service.spec.ts
 * @description User Service test class
 */
describe('Service: UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;
  const testAuthKey = '3432';
  const testFilterRole = 'CUSTOMER';
  const testRegisterToken = {
    registerToken: 'testToken'
  };
  const testUser: User = {
    id: 1
  } as User;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
    });
    service = TestBed.get(UserService);
    httpMock = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('Constructor: Should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getAllUsers: Should get all users', () => {
    service.getAllUsers()
      .subscribe(response => expect(response).toEqual([testUser]));

    const testRequest = httpMock.expectOne(service.apiUrl);
    expect(testRequest.request.method).toBe('GET');
    testRequest.flush([testUser]);
  });

  it('getAllUsers: Should get all users filtered by role', () => {
    service.getAllUsers([testFilterRole])
      .subscribe(response => expect(response).toEqual([testUser]));

    const testRequest = httpMock.expectOne(`${service.apiUrl}?role=${testFilterRole}`);
    expect(testRequest.request.method).toBe('GET');
    testRequest.flush([testUser]);
  });

  it('getRegisterToken: Should get the register token', () => {
    service.getRegisterToken()
      .subscribe(response => expect(response).toBe(testRegisterToken));

    const testRequest = httpMock.expectOne(`${service.apiUrl}/token`);
    expect(testRequest.request.headers.get(UserService.sharedKeyHeader)).toBe(UserService.shareKeyValue);
    expect(testRequest.request.headers.get(UserService.subjectHeader)).toBe(UserService.subjectValue);
    expect(testRequest.request.method).toBe('GET');
    testRequest.flush(testRegisterToken);
  });

  it('registerUser: Should register the user', () => {
    service.registerUser(testUser, testRegisterToken.registerToken)
      .subscribe(response => expect(response).toBe(testUser));

    const testRequest = httpMock.expectOne(`${service.apiUrl}`);
    expect(testRequest.request.headers.get(UserService.registerTokenHeader)).toBe(testRegisterToken.registerToken);
    expect(testRequest.request.method).toBe('POST');
    testRequest.flush(testUser);
  });

  it('findByAuthKey: Should get the user by auth key', () => {
    service.findByAuthKey(testAuthKey)
      .subscribe(response => expect(response).toBe(testUser));

    const testRequest = httpMock.expectOne(`${service.apiUrl}/authKey/${testAuthKey}`);
    expect(testRequest.request.method).toBe('GET');
    testRequest.flush(testUser);
  });

  it('changePassword: Should update the user password', () => {
    const authKey = 'testAuthKey';
    const params = { test: 'test' };
    spyOn(service, 'updatePatch').and.returnValue(of(testUser));
    service.changePassword(authKey, params)
      .subscribe(response => expect(response).toBe(testUser));
  });
});
