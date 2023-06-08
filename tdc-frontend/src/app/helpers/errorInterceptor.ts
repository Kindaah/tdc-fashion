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
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError, empty } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

// Services
import { AuthService } from '../services/auth.service';

// Helper
import { NotificationUtil } from '../utilities/notificationUtil';

/**
 * @author Daniel Mejia
 * @file errorInterceptor.ts
 * @description ErrorInterceptor to manage generic errors.
 */
@Injectable({ providedIn: 'root' })
export class ErrorInterceptor implements HttpInterceptor {

  /**
   * The error message.
   *
   * @type { string }
   */
  errorMessage: string;

  /**
   * The default error message.
   *
   * @type { string }
   */
  defaultMessage = 'Something went wrong';

  /**
   * Creates a new error interceptor.
   *
   * @param { AuthService } authenticationService an authentication service.
   * @param { NotificationUtil } notificationUtil helper NotificationUtil.
   * @param { Router } router the angular router.
   */
  constructor(
    private authenticationService: AuthService,
    private notificationUtil: NotificationUtil,
    private router: Router) { }

  /**
   * Intercepts an error request.
   *
   * @param { HttpRequest<any> } request the HttpRequest object.
   * @param { HttpHandler } next  the next error handler.
   *
   * @returns { Observable<HttpEvent<any>> } the http event observable.
   */
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next
      .handle(request)
      .pipe(catchError(this.manageErrors));
  }

  /**
   * Manages the errors intercepted.
   *
   * @param { any } errorResponse the errors response.
   *
   * @returns { Observable<never> } the observable to next.
   */
  manageErrors = (errorResponse: any): Observable<never> => {
    const errorStatus = errorResponse.status;
    const error = errorResponse.error;
    const errorCallback = this.errorCallbacks[errorStatus] || this.manageDefault;
    this.getErrorMessage(error);

    if (errorStatus === 401) {
      this.authenticationService.logout();
      return empty();
    } else {
      errorCallback();
      return throwError(error);
    }
  }

  /**
   * Get the error message.
   *
   * @param { any } error The error response.
   */
  getErrorMessage = (error: any): void => {
    this.errorMessage = '';
    const messages = error.errorMessages || [this.defaultMessage];
    this.errorMessage = messages[0].split(';')[0];
  }

  /**
   * Manage the default errors.
   */
  manageDefault = (): void => {
    this.showNotification(this.defaultMessage);
  }

  /**
   * Manages the forbidden error.
   */
  manageForbidden = (): void => {
    this.router.navigate(['dashboard']);
  }

  /**
   * Manages the request error.
   */
  manageRequestError = (): void => {
    this.showNotification(this.errorMessage);
  }

  /**
   * Shows a notification with a message.
   *
   * @param { string } message the message to show.
   */
  showNotification(message: string): void {
    this.notificationUtil.onError(message);
  }

  /**
   * Gets the object of errors known.
   *
   * @returns { object } the errors object.
   */
  get errorCallbacks(): object {
    return {
      403: this.manageForbidden,
      400: this.manageRequestError,
      404: this.manageRequestError
    };
  }
}
