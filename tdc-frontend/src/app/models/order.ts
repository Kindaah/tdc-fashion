/**
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

import { State } from './state';
import { Generic } from './generic';
import { Product } from './product';
import { ShippingInfo } from './shippingInfo';
import { Payment } from './payment';
import { ShippingAddress } from './shippingAddress';

/**
 * @author Daniel Mejia
 * @file order.ts
 * @description Order model
 */
export interface Order extends Generic {

  /**
   * reference PO.
   *
   * @type { string }
   */
  referencePo: string;

  /**
   * initial quote.
   *
   * @type { number }
   */
  initialQuote: number;

  /**
   * final quote.
   *
   * @type { number }
   */
  finalQuote: number;

  /**
   * the order comment.
   *
   * @type { string }
   */
  comment: string;

  /**
   * the origin country.
   *
   * @type { string }
   */
  originCountry: string;

  /**
   * The order delivery time.
   *
   * @type { any }
   */
  deliveryTime: any;

  /**
   * the order state.
   *
   * @type { State }
   */
  state: State;

  /**
   * the ShippingInfo object.
   *
   * @type { ShippingInfo }
   */
  shippingInfo: ShippingInfo;

  /**
   * The order shipping address object.
   *
   * @type { { [shippingType: string]: ShippingAddress } }
   */
  shippingAddresses: { [shippingType: string]: ShippingAddress };

  /**
   * the products list.
   *
   * @type { Product[] }
   */
  products: Product[];

   /**
   * the payments list.
   *
   * @type { Payment[] }
   */
  payments: Payment[];

}
