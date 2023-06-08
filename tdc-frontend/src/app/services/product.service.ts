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
import { environment } from '../../environments/environment';

// Services
import { GenericService } from './generic.service';

// Models
import { Product } from '../models/product';
import { Order } from '../models/order';

/**
 * @author Daniel Mejia
 * @file product.service.ts
 * @description Product service
 */
@Injectable({ providedIn: 'root' })
export class ProductService extends GenericService<Product> {

  // Constants
  private static resource = 'products';

  /**
   * Creates an instance of ProductService.
   *
   * @param { HttpClient } httpClient The HttpClient to make request calls.
   */
  constructor(httpClient: HttpClient) {
    super(httpClient, ProductService.resource);
  }

  /**
   * Add a product.
   *
   * @param { Order } order The order of the add.
   * @param { Product } product the product to add.
   *
   * @returns { Observable<Product> } the Product observable.
   */
  addProduct(order: Order, product: Product): Observable<Product> {
    const requestUrl = `${environment.api}/orders/${order.id}/products`;
    return this.add(product, requestUrl);
  }

  /**
   * Updates a product.
   *
   * @param { number } productId The product id to update.
   * @param { Order } order The order of the product.
   * @param { Product } product the product to update.
   *
   * @returns { Observable<Product> } the Product observable.
   */
  updateProduct(productId: number, order: Order, product: Product): Observable<Product> {
    const requestUrl = `${environment.api}/orders/${order.id}/products/${productId}`;
    return this.update(productId, product, requestUrl);
  }

  /**
   * Upload a files to a product.
   *
   * @param { number } productId the product id to upload the files.
   * @param { object } params the object params with the key and files.
   *
   * @returns { Observable<Product> } An observable with the product updated.
   */
  uploadFiles(productId: number, params: object): Observable<Product> {
    const requestUrl = `${this.apiUrl}/${productId}/uploadFiles`;
    const formData = new FormData();
    Object.keys(params).forEach(key =>
      Array.from(params[key]).forEach(param => formData.append(key, param as File))
    );

    return this.httpClient.post<Product>(requestUrl, formData);
  }

  /**
   * Gets the download files url.
   *
   * @param { string } fileName the file name to download.
   *
   * @returns { Observable<any> } The observable with the url to donwload the file.
   */
  donwloadFileUrl(fileName: string): Observable<any> {
    const requestUrl = `${this.apiUrl}/fileDownloadUrl?fileName=${fileName}`;
    return this.httpClient.get<any>(requestUrl);
  }
}
