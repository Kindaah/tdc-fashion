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

// Services
import { FeatureService } from '../feature.service';

// Models
import { Feature } from 'src/app/models/feature';

/**
 * @author Edson Ruiz Ramirez
 * @file feature.service.spec.ts
 * @description Feature Service test class
 */
describe('Service: ProductService', () => {
  let service: FeatureService;
  let httpMock: HttpTestingController;
  const featureId = 1;
  const testFeature: Feature = {
    id: featureId
  } as Feature;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [FeatureService]
    });
    service = TestBed.get(FeatureService);
    httpMock = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('Constructor: Should be created', () => {
    expect(service).toBeTruthy();
  });

  it('uploadFiles: Should upload files', () => {
    service.uploadPatterns(featureId, { file: 'testFile' })
      .subscribe(response => expect(response).toBe(testFeature));

    const testRequest = httpMock.expectOne(`${service.apiUrl}/${featureId}/uploadPatterns`);
    expect(testRequest.request.method).toBe('POST');
    testRequest.flush(testFeature);
  });
});
