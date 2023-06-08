/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

// Dependencies
import { TestBed} from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { environment } from 'src/environments/environment';

// Services
import { ShippingAddressService } from '../shippingAddress.service';

// Models
import { ShippingAddress } from '../../models/shippingAddress';

/**
 * @author Edson Ruiz Ramirez
 * @file shippingAddress.service.spec.ts
 * @description Shipping Address Service test class
 */
describe('Service: ShippingAddressService', () => {
  let service: ShippingAddressService;
  let httpMock: HttpTestingController;
  const testUserId = 1;
  const testShippingId = 2;
  const testShippingAddress: ShippingAddress = {
    id: testShippingId
  } as ShippingAddress;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ShippingAddressService]
    });
    service = TestBed.get(ShippingAddressService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('Constructor: Should be created', () => {
    expect(service).toBeTruthy();
  });

  it('addShippingAddress: Should be create the shipping address', () => {
    spyOn(service, 'add').and.returnValue(of(testShippingAddress));
    service.addShippingAddress(testUserId, testShippingAddress)
      .subscribe(response => expect(response).toBe(testShippingAddress));
  });

  it('updateShippingAddress: Should be update the shipping address', () => {
    spyOn(service, 'update').and.returnValue(of(testShippingAddress));
    service.updateShippingAddress(testShippingId, testShippingAddress)
      .subscribe(response => expect(response).toBe(testShippingAddress));
  });

  it('deleteShippingAddress: Should deletes a shipping address', () => {
    service.deleteShippingAddress(testShippingId)
      .subscribe(response => expect(response).toBe(testShippingAddress));

    const expectedUrl = `${environment.api}/users/${ShippingAddressService.resource}/${testShippingId}`;
    const testRequest = httpMock.expectOne(expectedUrl);
    expect(testRequest.request.method).toBe('DELETE');
    testRequest.flush(testShippingAddress);
  });
});
