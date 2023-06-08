/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static com.thedressclub.tdc_backend.controller.UserController.USERS_URL;

import com.thedressclub.tdc_backend.controller.config.GenericController;
import com.thedressclub.tdc_backend.controller.config.RestException;
import com.thedressclub.tdc_backend.model.ShippingAddress;
import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest API controller for {@link ShippingAddress}.
 *
 * @author Edson Ruiz Ramirez.
 */
@RestController
public class ShippingAddressController extends GenericController<ShippingAddress>
{
    public static final String SHIPPING_ADDRESS_URL = "/shippingAddress";

    @Autowired
    private UserService userService;

    /**
     * @api {post} /users/id/shippingAddress Create a shipping address for a specific user.
     * @apiName AddShippingAddress
     * @apiGroup ShippingAddress
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id The id of user.
     *
     * @apiParamExample {json} Request-Example:
     *     {
     *          "contactName": "example",
     *          "country": "example",
     *          "streetAddress": "example",
     *          "streetAddressAddInfo": "example",
     *          "state": "example",
     *          "city": "example",
     *          "postalCode": "12345",
     *          "countryCode": "123",
     *          "phoneNumber": "1234567"
     *      }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 201 Created
     *     {
     *          "id": 1,
     *          "createdAt": "2018-09-04@16:57:40",
     *          "updatedAt": "2018-09-04@16:57:40",
     *          "contactName": "example",
     *          "country": "example",
     *          "streetAddress": "example",
     *          "streetAddressAddInfo": "example",
     *          "state": "example",
     *          "city": "example",
     *          "postalCode": "12345",
     *          "countryCode": "123",
     *          "phoneNumber": "1234567"
     *      }
     *
     * @apiError BAD_REQUEST An error occurred and the resource cannot be created.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "An error occurred and the resource cannot be created"
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Thursday, August 23, 2018 4:19:24 PM COT"
     *      }
     */
    @PostMapping(value= USERS_URL + "/{" + ID_KEY + "}" + SHIPPING_ADDRESS_URL, headers = HEADER_TYPE)
    public ResponseEntity<ShippingAddress> addShippingAddress(
        @PathVariable(ID_KEY) long idUser,
        @Valid @RequestBody ShippingAddress shippingAddress)
    {
        User storedUser = userService.findById(idUser);

        if (storedUser == null)
        {
            throw new RestException(NOT_CREATED_KEY, HttpStatus.BAD_REQUEST);
        }

        shippingAddress.setUser(storedUser);
        return add(shippingAddress);
    }

    /**
     * @api {put} /users/shippingAddress/{id} Update a shipping address.
     * @apiName UpdateShippingAddress
     * @apiGroup ShippingAddress
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id The id of the shipping address.
     *
     * @apiParamExample {json} Request-Example:
     *     {
     *          "id": 1,
     *          "contactName": "example",
     *          "country": "example",
     *          "streetAddress": "example",
     *          "streetAddressAddInfo": "example",
     *          "state": "example",
     *          "city": "example",
     *          "postalCode": "12345",
     *          "countryCode": "123",
     *          "phoneNumber": "1234567"
     *      }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 Ok
     *     {
     *          "id": 1,
     *          "createdAt": "2018-09-04@16:57:40",
     *          "updatedAt": "2018-09-04@16:57:40",
     *          "contactName": "example",
     *          "country": "example",
     *          "streetAddress": "example",
     *          "streetAddressAddInfo": "example",
     *          "state": "example",
     *          "city": "example",
     *          "postalCode": "12345",
     *          "countryCode": "123",
     *          "phoneNumber": "1234567"
     *      }
     *
     * @apiError BAD_REQUEST An error occurred and the resource cannot be updated.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "An error occurred and the resource cannot be updated"
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Thursday, August 23, 2018 4:19:24 PM COT"
     *      }
     */
    @PutMapping(value = USERS_URL + SHIPPING_ADDRESS_URL + SLASH + "{id}", headers = HEADER_TYPE)
    public ResponseEntity<ShippingAddress> updateShippingAddress(
        @PathVariable(ID_KEY) long idShippingAddress, @Valid @RequestBody ShippingAddress shippingAddress)
    {
        return update(idShippingAddress, shippingAddress);
    }

    /**
     * @api {delete} /users/shippingAddress/{id} Change the shipping address to disabled.
     * @apiName DisableShippingAddress
     * @apiGroup ShippingAddress
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id The id of the shipping address.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 Ok
     *     {
     *          "id": 1,
     *          "createdAt": "2018-09-04@16:57:40",
     *          "updatedAt": "2018-09-04@16:57:40",
     *          "contactName": "example",
     *          "country": "example",
     *          "streetAddress": "example",
     *          "streetAddressAddInfo": "example",
     *          "state": "example",
     *          "city": "example",
     *          "postalCode": "12345",
     *          "countryCode": "123",
     *          "phoneNumber": "1234567"
     *          "enabled": false
     *      }
     *
     * @apiError BAD_REQUEST An error occurred and the resource cannot be updated.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "An error occurred and the resource cannot be updated"
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Thursday, August 23, 2018 4:19:24 PM COT"
     *      }
     */
    @DeleteMapping(value = USERS_URL + SHIPPING_ADDRESS_URL + SLASH + "{id}", headers = HEADER_TYPE)
    public ResponseEntity<ShippingAddress> deleteShippingAddress(
        @PathVariable(ID_KEY) long idShippingAddress)
    {
        ShippingAddress storedAddress = getStoredObject(idShippingAddress);
        storedAddress.setEnabled(false);
        return update(idShippingAddress, storedAddress);
    }
}