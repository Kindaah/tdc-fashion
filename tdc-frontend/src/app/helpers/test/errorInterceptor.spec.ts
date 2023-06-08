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
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { ErrorInterceptor } from '../errorInterceptor';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { RouterTestingModule } from '@angular/router/testing';

// Services
import { AuthService } from 'src/app/services/auth.service';
import { GenericService } from 'src/app/services/generic.service';

// Helpers
import { NotificationUtil } from '../../utilities/notificationUtil';

// Models
import { Generic } from 'src/app/models/generic';

/**
 * @author Daniel Mejia
 * @file errorInterceptor.spec.ts
 * @description Test class for {@link ErrorInterceptor}
 */
describe('Interceptor: ErrorInterceptor', () => {
  const testResource = 'testResource';
  const mockAuthService: jasmine.SpyObj<AuthService> =
    jasmine.createSpyObj<AuthService>('AuthService', ['logout']);
  const mockNotificationUtil: jasmine.SpyObj<NotificationUtil> =
    jasmine.createSpyObj<NotificationUtil>('notificationUtil', ['onError']);
  const errorUnauthorized = { status: 401, statusText: 'Unauthorized' };
  const errorForbidden = { status: 403, statusText: 'Forbidden' };
  const errorBadRequest = { status: 400, statusText: 'Bad Request' };
  const errorDefault = { status: 500, statusText: 'Default error' };

  let service: GenericService<Generic>;
  let httpMock: HttpTestingController;
  let router: Router;
  let location: Location;
  let errorInterceptor: ErrorInterceptor;
  let notificationUtil: NotificationUtil;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
      ],
      providers: [
        GenericService,
        { provide: NotificationUtil, useValue: mockNotificationUtil },
        { provide: AuthService, useValue: mockAuthService },
        { provide: 'RESOURCE_URL', useValue: testResource },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: ErrorInterceptor,
          multi: true,
        },
      ],
    });

    router = TestBed.get(Router);
    service = TestBed.get(GenericService);
    httpMock = TestBed.get(HttpTestingController);
    errorInterceptor = TestBed.get(ErrorInterceptor);
    notificationUtil = TestBed.get(NotificationUtil);
    location = TestBed.get(Location);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('Unathorized: Should logout the user', () => {
    mockAuthService.logout.and.callFake(() => location.go('/login'));
    service.getAll()
      .subscribe(() => { }, _error => expect(location.path()).toBe('/login'));

    const testRequest = httpMock.expectOne(`${service.apiUrl}`);
    testRequest.flush({}, errorUnauthorized);
  });

  it('Forbidden: Should redirect to dashboard', () => {
    spyOn(router, 'navigate');
    service.getAll()
      .subscribe(() => { }, error => expect(error).toBe(errorForbidden));

    const testRequest = httpMock.expectOne(`${service.apiUrl}`);
    testRequest.flush(errorForbidden, errorForbidden);
  });

  it('RequestError: Should show notification with the error', () => {
    service.getAll()
      .subscribe(() => { }, _error => expect(notificationUtil.onError).toHaveBeenCalled());

    const testRequest = httpMock.expectOne(`${service.apiUrl}`);
    testRequest.flush(errorBadRequest.statusText, errorBadRequest);
  });

  it('DefaultError: Should show notification with default value', () => {
    const checkError = () =>
      expect(notificationUtil.onError)
        .toHaveBeenCalledWith(errorInterceptor.defaultMessage);

    service.getAll()
      .subscribe(() => { }, checkError);
    const testRequest = httpMock.expectOne(`${service.apiUrl}`);
    testRequest.flush(errorDefault.statusText, errorDefault);
  });
});
