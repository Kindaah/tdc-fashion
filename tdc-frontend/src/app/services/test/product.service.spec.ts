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
import { ProductService } from '../product.service';

// Models
import { Product } from 'src/app/models/product';
import { Order } from 'src/app/models/order';

/**
 * @author Daniel Mejia
 * @file product.service.spec.ts
 * @description Product Service test class
 */
describe('Service: ProductService', () => {
  let service: ProductService;
  let httpMock: HttpTestingController;
  const productId = 1;
  const testProduct: Product = {
    id: productId,
    description: 'testProduct'
  } as Product;
  const testOrder: Order = {
    id: 1
  } as Order;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProductService]
    });
    service = TestBed.get(ProductService);
    httpMock = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('Constructor: Should be created', () => {
    expect(service).toBeTruthy();
  });

  it('addProduct: Should add a product', () => {
    spyOn(service, 'add').and.returnValue(of(testProduct));
    service.addProduct(testOrder, testProduct)
      .subscribe(response => expect(response).toBe(testProduct));
  });

  it('updateProduct: Should updated a product', () => {
    spyOn(service, 'update').and.returnValue(of(testProduct));
    service.updateProduct(productId, testOrder, testProduct)
      .subscribe(response => expect(response).toBe(testProduct));
  });

  it('uploadFiles: Should upload files', () => {
    service.uploadFiles(productId, { file: 'testFile' })
      .subscribe(response => expect(response).toBe(testProduct));

    const testRequest = httpMock.expectOne(`${service.apiUrl}/${productId}/uploadFiles`);
    expect(testRequest.request.method).toBe('POST');
    testRequest.flush(testProduct);
  });

  it('donwloadFileUrl: Should download a file url', () => {
    const testFileName = 'testFile';
    const testUrl = { fileDownloadUrl: 'testImageUrl' };
    service.donwloadFileUrl(testFileName)
      .subscribe(response => expect(response).toBe(testUrl));

    const testRequest = httpMock.expectOne(`${service.apiUrl}/fileDownloadUrl?fileName=${testFileName}`);
    expect(testRequest.request.method).toBe('GET');
    testRequest.flush(testUrl);
  });
});
