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
import { HttpClientModule } from '@angular/common/http';
import { of } from 'rxjs';

// Services
import { ShippingInfoService } from '../shippingInfo.service';

// Models
import { ShippingInfo } from '../../models/shippingInfo';

/**
 * @author Edson Ruiz Ramirez
 * @file shippingInfo.service.spec.ts
 * @description Shipping Info Service test class
 */
describe('Service: ShippingInfoService', () => {
  let service: ShippingInfoService;
  const testOrderId = 1;
  const testShippingId = 2;
  const testShippingInfo: ShippingInfo = {
    id: testShippingId
  } as ShippingInfo;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [ShippingInfoService]
    });
    service = TestBed.get(ShippingInfoService);
  });

  it('Constructor: Should be created', () => {
    expect(service).toBeTruthy();
  });

  it('addShippingInfo: Should be create the shipping info', () => {
    spyOn(service, 'add').and.returnValue(of(testShippingInfo));
    service.addShippingInfo(testOrderId, testShippingInfo)
      .subscribe(response => expect(response).toBe(testShippingInfo));
  });

  it('updateShippingInfo: Should be update the shipping info', () => {
    spyOn(service, 'update').and.returnValue(of(testShippingInfo));
    service.updateShippingInfo(testShippingId, testShippingInfo)
      .subscribe(response => expect(response).toBe(testShippingInfo));
  });
});
