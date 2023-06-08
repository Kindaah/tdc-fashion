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
import { SnotifyService, SnotifyPosition, SnotifyToastConfig } from 'ng-snotify';

/**
 * @author Edson Ruiz Ramirez
 * @file notificationUtil.ts
 * @description Notification Util.
 */

@Injectable({ providedIn: 'root' })
export class NotificationUtil {

  /**
   * The success title.
   *
   * @type { string }
   */
  successTitle = 'SUCCESS';

  /**
   * The error title.
   *
   * @type { string }
   */
  errorTitle = 'ERROR';

  /**
   * The configuration object.
   *
   * @type { Object }
   */
  config = {
    style: 'material',
    timeout: 5000,
    position: <SnotifyPosition> SnotifyPosition.rightTop,
    progressBar: true,
    closeClick: true,
    newTop: true,
    filterDuplicates: false,
    backdrop: -1,
    dockMax: 8,
    blockMax: 6,
    pauseHover: true
  };

  /**
   * Creates an instance of OrderComponent.
   *
   * @param { SnotifyService } snotifyService dependencie for SnotifyService
   */
  constructor(private snotifyService: SnotifyService) { }

  /**
   * Get the configuration for the notification.
   *
   * @returns { SnotifyToastConfig } the configuration.
   */
  get notificationConfig(): SnotifyToastConfig {
    this.snotifyService.setDefaults({
      global: {
        newOnTop: this.config.newTop,
        maxAtPosition: this.config.blockMax,
        maxOnScreen: this.config.dockMax
      }
    });
    return {
      backdrop: this.config.backdrop,
      position: this.config.position,
      timeout: this.config.timeout,
      showProgressBar: this.config.progressBar,
      closeOnClick: this.config.closeClick,
      pauseOnHover: this.config.pauseHover
    };
  }

  /**
   * Success notification.
   *
   * @param { string } body The body message.
   */
  onSuccess(body: string): void  {
    const icon = `assets/notification/done-final.png`;

    this.snotifyService.success(body, this.successTitle, {
      ...this.notificationConfig,
      icon
    });
  }

  /**
   * Error notification.
   *
   * @param { string } body The body message.
   */
  onError(body: string): void {
    const icon = `assets/notification/warning-final.png`;

    this.snotifyService.error(body, this.errorTitle, {
      ...this.notificationConfig,
      icon
    });
  }
}
