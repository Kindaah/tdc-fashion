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

// Services
import { GenericService } from './generic.service';

// Models
import { Feature } from '../models/feature';

/**
 * @author Edson Ruiz Ramirez
 * @file feature.service.ts
 * @description Feature service
 */
@Injectable({ providedIn: 'root' })
export class FeatureService extends GenericService<Feature> {

  // Constants
  private static resource = 'features';

  /**
   * Creates an instance of FeatureService.
   *
   * @param { HttpClient } httpClient The HttpClient to make request calls.
   */
  constructor(httpClient: HttpClient) {
    super(httpClient, FeatureService.resource);
  }

  /**
   * Upload a patterns files to a feature.
   *
   * @param { number } featureId the feature id to upload the files.
   * @param { object } params the object params with the key and files.
   *
   * @returns { Observable<Feature> } An observable with the feature updated.
   */
  uploadPatterns(featureId: number, params: object): Observable<Feature> {
    const requestUrl = `${this.apiUrl}/${featureId}/uploadPatterns`;
    const formData = new FormData();
    Object.keys(params).forEach(key =>
      Array.from(params[key]).forEach(param => formData.append(key, param as File))
    );

    return this.httpClient.post<Feature>(requestUrl, formData);
  }
}
