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
import { NotificationUtil } from '../notificationUtil';
import { SnotifyService } from 'ng-snotify';

/**
 * @author Edson Ruiz Ramirez
 * @file notificationUtil.spec.ts
 * @description Notification util test class.
 */
describe('Utilities: NotificationUtil', () => {
  const testBody = 'body test';
  const mockSnotifyService: jasmine.SpyObj<SnotifyService> =
    jasmine.createSpyObj<SnotifyService>('SnotifyService', ['success', 'error', 'setDefaults']);
  let notificationUtil: NotificationUtil;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        NotificationUtil,
        { provide: SnotifyService, useValue: mockSnotifyService },
      ]
    });
    notificationUtil = TestBed.get(NotificationUtil);
  });

  it('Constructor: Should be created', () => {
    expect(notificationUtil).toBeTruthy();
  });

  it('onSuccess: Should get the function success', () => {
    notificationUtil.onSuccess(testBody);
    expect(mockSnotifyService.success).toHaveBeenCalled();
  });

  it('onError: Should open the bank modal', () => {
    notificationUtil.onError(testBody);
    expect(mockSnotifyService.error).toHaveBeenCalled();
  });
});
