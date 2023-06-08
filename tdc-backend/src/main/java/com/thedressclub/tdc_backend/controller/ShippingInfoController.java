/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import com.thedressclub.tdc_backend.controller.config.GenericController;
import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.ShippingInfo;
import com.thedressclub.tdc_backend.service.OrderService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest API controller for {@link ShippingInfo}.
 *
 * @author Edson Ruiz Ramirez.
 */
@RestController
public class ShippingInfoController extends GenericController<ShippingInfo>
{
    @Autowired
    private OrderService orderService;

    /**
     * @api {post} /orders/{orderId}/shippingInfo Create a shipping info.
     * @apiName AddShippingInfo
     * @apiGroup ShippingInfo
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} orderId The id of the order related with the shipping info.
     *
     * @apiParamExample {json} Request-Example:
     *     {
     *          "company": "example",
     *          "trackingId": "12345abc",
     *          "details": "example"
     *      }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 201 Created
     *     {
     *          "id": 1,
     *          "createdAt": "2018-09-04@16:57:40",
     *          "updatedAt": "2018-09-04@16:57:40",
     *          "company": "example",
     *          "trackingId": "12345abc",
     *          "details": "example"
     *      }
     *
     * @apiError NOT_FOUND The resource does not exist or was not found.
     * @apiError BAD_REQUEST The company cannot be blank, please add a value.
     * @apiError BAD_REQUEST The tracking id cannot be blank, please add a value.
     * @apiError BAD_REQUEST The details cannot be blank, please add a value.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 404 NOT_FOUND
     *     {
     *          "errorMessages": [
     *              "The resource does not exist or was not found."
     *          ],
     *          "status": "NOT_FOUND",
     *          "statusCode": 404,
     *          "timestamp": "Thursday, August 23, 2018 4:07:23 PM COT"
     *      }
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "The company cannot be blank, please add a value."
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Tuesday, September 4, 2018 5:01:23 PM COT"
     *      }
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "The tracking id cannot be blank, please add a value."
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Tuesday, September 4, 2018 5:01:23 PM COT"
     *      }
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "The details cannot be blank, please add a value."
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Tuesday, September 4, 2018 5:01:23 PM COT"
     *      }
     */
    @PostMapping(value = "orders/{orderId}/shippingInfo", headers = HEADER_TYPE)
    public ResponseEntity<ShippingInfo> addShippingInfo(
        @PathVariable("orderId") long orderId, @Valid @RequestBody ShippingInfo shippingInfo)
    {
        Order order = orderService.findById(orderId);
        shippingInfo.setOrder(order);
        order.setShippingInfo(shippingInfo);

        add(shippingInfo);
        orderService.update(order);

        return new ResponseEntity<>(shippingInfo, HttpStatus.CREATED);
    }

    /**
     * @api {post} /orders/shippingInfo/{id} Update a shipping info.
     * @apiName UpdateShippingInfo
     * @apiGroup ShippingInfo
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id The id of the shipping info.
     *
     * @apiParamExample {json} Request-Example:
     *     {
     *          "id":
     *          "company": "example",
     *          "trackingId": "12345abc",
     *          "details": "example"
     *      }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 9,
     *          "updatedAt": "2018-09-04@22:12:56",
     *          "company": "exampleUpdate",
     *          "trackingId": "abc12345",
     *          "details": "exampleUpdate"
     *      }
     *
     * @apiError NOT_FOUND The resource does not exist or was not found.
     * @apiError BAD_REQUEST The company cannot be blank, please add a value.
     * @apiError BAD_REQUEST The tracking id cannot be blank, please add a value.
     * @apiError BAD_REQUEST The details cannot be blank, please add a value.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 404 NOT_FOUND
     *     {
     *          "errorMessages": [
     *              "The resource does not exist or was not found."
     *          ],
     *          "status": "NOT_FOUND",
     *          "statusCode": 404,
     *          "timestamp": "Thursday, August 23, 2018 4:07:23 PM COT"
     *      }
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "The company cannot be blank, please add a value."
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Tuesday, September 4, 2018 5:01:23 PM COT"
     *      }
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "The tracking id cannot be blank, please add a value."
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Tuesday, September 4, 2018 5:01:23 PM COT"
     *      }
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "The details cannot be blank, please add a value."
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Tuesday, September 4, 2018 5:01:23 PM COT"
     *      }
     */
    @PutMapping(value = "orders/shippingInfo/{id}", headers = HEADER_TYPE)
    public ResponseEntity<ShippingInfo> updateShippingInfo(
        @PathVariable(ID_KEY) long idShippingInfo, @Valid @RequestBody ShippingInfo shippingInfo)
    {
        return update(idShippingInfo, shippingInfo);
    }
}