/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static com.thedressclub.tdc_backend.controller.ShippingAddressController.SHIPPING_ADDRESS_URL;
import static com.thedressclub.tdc_backend.util.UserUtils.getCurrentUser;

import com.thedressclub.tdc_backend.business.OrderBusiness;
import com.thedressclub.tdc_backend.controller.config.GenericController;
import com.thedressclub.tdc_backend.controller.config.RestException;
import com.thedressclub.tdc_backend.dto.CountDTO;
import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.ShippingAddress;
import com.thedressclub.tdc_backend.model.State;
import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.OrderService;
import com.thedressclub.tdc_backend.service.ScheduledService;
import com.thedressclub.tdc_backend.service.ShippingAddressService;
import com.thedressclub.tdc_backend.service.StateService;
import com.thedressclub.tdc_backend.service.UserService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest API controller for {@link Order}.
 *
 * @author Edson Ruiz Ramirez.
 * @author Daniel Mejia.
 */
@RestController
@RequestMapping("orders")
public class OrderController extends GenericController<Order>
{
    public static final String ORDERS_URL = "/orders";
    static final String INITIAL_QUOTE_URL = "/quote";
    static final String COMMENT_URL = "/comment";
    static final String STATE_URL = "/state";
    static final String DASHBOARD_URL = "/dashboard";
    static final String FINAL_QUOTE_URL = "/finalQuote";

    static final String SHIPPING_TYPE = "shippingType";
    private static final String ID_SHIPPING_KEY = "shippingId";

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StateService stateService;

    @Autowired
    private OrderBusiness orderBusiness;

    @Autowired
    private ScheduledService scheduledService;

    @Autowired
    private ShippingAddressService shippingAddressService;

    /**
     * @api {post} /orders Create an order
     * @apiName AddOrder
     * @apiGroup Orders
     * @apiVersion 1.0.0
     *
     * @apiParamExample {json} Request-Example:
     *     {
     *       "referencePo":"example"
     *     }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 201 Created
     *     {
     *          "id": 1,
     *          "createdAt": "2018-08-30@21:44:58",
     *          "updatedAt": "2018-08-30@21:44:58",
     *          "referencePo": "12345",
     *          "initialQuote": 0,
     *          "finalQuote": 0,
     *          "state": {
     *              "id": 1,
     *              "createdAt": "2018-08-30@05:00:00",
     *              "updatedAt": "2018-08-30@05:00:00",
     *              "name": "Incomplete"
     *          }
     *      }
     *
     * @apiError BAD_REQUEST The reference po cannot be blank, please add a value.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "The reference po cannot be blank, please add a value."
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Thursday, August 23, 2018 2:56:26 PM COT"
     *      }
     */
    @PostMapping(headers = HEADER_TYPE)
    public ResponseEntity<Order> addOrder(@Valid @RequestBody Order order)
    {
        User currentUser = getCurrentUser(userService);
        State defaultState = stateService.findById(Order.DEFAULT_STATE_ID);

        order.setUser(currentUser);
        order.setState(defaultState);
        order.setDeliveryTime(null);

        return add(order);
    }

    /**
     * @api {get} /orders/dashboard Dashboard
     * @apiName GetDashboard
     * @apiGroup Orders
     * @apiVersion 1.0.0
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     [
     *          {
     *              "entity": {
     *                  "id": 1,
     *                  "createdAt": "2018-08-22@05:00:00",
     *                  "updatedAt": "2018-08-22@05:00:00",
     *                  "name": "Incomplete"
     *              },
     *              "count": 1
     *          }
     *     ]
     */
    @GetMapping(value = DASHBOARD_URL)
    public ResponseEntity<List<CountDTO>> getDashboard()
    {
        User currentUser = getCurrentUser(userService);
        List<State> statesFiltered = stateService.filterByRole(currentUser);
        List<CountDTO> states = orderBusiness.getListCountDTO(statesFiltered, currentUser);

        return new ResponseEntity<>(states, HttpStatus.OK);
    }

    /**
     * @api {get} /orders Get all orders
     * @apiName GetAllOrders
     * @apiGroup Orders
     * @apiVersion 1.0.0
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     [
     *          {
     *              "entity": {
     *                  "id": 1,
     *                  "createdAt": "2018-08-31@13:06:40",
     *                  "updatedAt": "2018-08-31@13:06:40",
     *                  "referencePo": "12345",
     *                  "initialQuote": 0,
     *                  "finalQuote": 0,
     *                  "shippingInfo": {
     *                      "id": 1,
     *                      "createdAt": "2018-09-04@20:36:38",
     *                      "updatedAt": "2018-09-04@20:36:38",
     *                      "company": "example",
     *                      "trackingId": "12345abc",
     *                      "details": "example"
     *                  },
     *                  "state": {
     *                      "id": 1,
     *                      "createdAt": "2018-08-31@05:00:00",
     *                      "updatedAt": "2018-08-31@05:00:00",
     *                      "name": "Incomplete"
     *                  }
     *              },
     *              "count": 1
     *          }
     *      ]
     */
    @GetMapping(headers = HEADER_TYPE)
    public ResponseEntity<List<CountDTO>> getAllOrders()
    {
        User currentUser = getCurrentUser(userService);
        List<Order> ordersFiltered = orderService.filterByRole(currentUser);
        List<CountDTO> listCounts = orderBusiness.getListCountDTO(ordersFiltered);

        return new ResponseEntity<>(listCounts, HttpStatus.OK);
    }

    /**
     * @api {get} /orders/{id} Find an order by id
     * @apiName FindByIdOrder
     * @apiGroup Orders
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id The id of the order that will be searched.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 1,
     *          "createdAt": "2018-08-31@13:06:40",
     *          "updatedAt": "2018-08-31@13:06:40",
     *          "referencePo": "12345",
     *          "initialQuote": 0,
     *          "finalQuote": 0,
     *          "shippingInfo": {
     *              "id": 1,
     *              "createdAt": "2018-09-04@20:36:38",
     *              "updatedAt": "2018-09-04@20:36:38",
     *              "company": "example",
     *              "trackingId": "12345abc",
     *              "details": "example"
     *          },
     *          "state": {
     *              "id": 1,
     *              "createdAt": "2018-08-31@05:00:00",
     *              "updatedAt": "2018-08-31@05:00:00",
     *              "name": "Incomplete"
     *          },
     *          "products": [
     *              {
     *                  "id": 1,
     *                  "createdAt": "2018-08-31@13:07:50",
     *                  "updatedAt": "2018-08-31@13:07:50",
     *                  "description": "description",
     *                  "quote": 0,
     *                  "features": [
     *                      {
     *                          "id": 1,
     *                          "createdAt": "2018-08-31@13:07:50",
     *                          "updatedAt": "2018-08-31@13:07:50",
     *                          "color": "color",
     *                          "size": "size",
     *                          "quantity": 10,
     *                          "sampleQuantity": 0
     *                      }
     *                  ]
     *              }
     *          ]
     *      }
     *
     * @apiError NOT_FOUND The resource does not exist or was not found.
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
     */
    @GetMapping(value = "/{id}", headers = HEADER_TYPE)
    public ResponseEntity<Order> findByIdOrder(@PathVariable(ID_KEY) long idOrder)
    {
        return findById(idOrder);
    }

    /**
     * @api {patch} /orders/{id} Update the referencePO of an order
     * @apiName UpdateOrder
     * @apiGroup Orders
     * @apiVersion 1.0.0
     * @apiHeaderExample {json} Header-Example:
     *     {
     *       "Content-Type": "application/x-www-form-urlencoded"
     *     }
     *
     * @apiParam {Number} id The id of the order that will be updated.
     * @apiParam {String} referencePO The new referencePO.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 1,
     *          "createdAt": "2018-08-22@20:09:58",
     *          "updatedAt": "2018-08-23@21:18:41",
     *          "referencePo": "new reference po",
     *          "products": [],
     *          "state": {
     *              "id": 2,
     *              "createdAt": "2018-08-22@05:00:00",
     *              "updatedAt": "2018-08-22@05:00:00",
     *              "name": "Pending Initial Quote"
     *          },
     *          "initialQuote": 456468.25,
     *          "finalQuote": 0
     *      }
     *
     * @apiError BAD_REQUEST An error occurred and the resource cannot be updated.
     * @apiError NOT_FOUND The resource does not exist or was not found.
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
     */
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Order> updateOrder(
        @PathVariable(ID_KEY) long idOrder, @RequestParam(value = "referencePo") String referencePo)
    {
        Order storedOrder = getStoredObject(idOrder);
        storedOrder.setReferencePo(referencePo);

        return update(idOrder, storedOrder);
    }

    /**
     * @api {patch} /orders/{id}/state/{idState} Update the state of an order
     * @apiName UpdateOrderState
     * @apiGroup Orders
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id The id of the order that will be updated.
     * @apiParam {Number} idState The id of the next state that will be set to the order.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 1,
     *          "createdAt": "2018-08-22@20:09:58",
     *          "updatedAt": "2018-08-23@21:18:41",
     *          "referencePo": "new reference po",
     *          "products": [],
     *          "shippingInfo": {
     *              "id": 1,
     *              "createdAt": "2018-09-04@20:36:38",
     *              "updatedAt": "2018-09-04@20:36:38",
     *              "company": "example",
     *              "trackingId": "12345abc",
     *              "details": "example"
     *          },
     *          "state": {
     *              "id": 2,
     *              "createdAt": "2018-08-22@05:00:00",
     *              "updatedAt": "2018-08-22@05:00:00",
     *              "name": "Pending Initial Quote"
     *          },
     *          "initialQuote": 456468.25,
     *          "finalQuote": 0
     *      }
     *
     * @apiError BAD_REQUEST An error occurred and the resource cannot be updated.
     * @apiError NOT_FOUND The resource does not exist or was not found.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "An error occurred and the resource cannot be updated"
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Friday, August 24, 2018 4:54:36 PM COT"
     *      }
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 404 NOT_FOUND
     *     {
     *          "errorMessages": [
     *              "The resource does not exist or was not found."
     *          ],
     *          "status": "NOT_FOUND",
     *          "statusCode": 404,
     *          "timestamp": "Thursday, August 23, 2018 4:25:32 PM COT"
     *      }
     */
    @PatchMapping(value = "/{id}" + STATE_URL + "/{idState}", headers = HEADER_TYPE)
    public ResponseEntity<Order> updateOrderState(
        @PathVariable(ID_KEY) long idOrder,
        @PathVariable("idState") long idState)
    {
        Order storedOrder = getStoredObject(idOrder);
        State nextState = stateService.findById(idState);
        storedOrder.setState(nextState);

        if(storedOrder.getState().getId() == Order.PENDING_INITIAL_QUOTE_APPROVAL_STATE_ID)
        {
            scheduledService.expireOrder(storedOrder.getId());
        }

        return update(idOrder, storedOrder);
    }

    /**
     * @api {patch} /orders/{id}/quote Update the initial quote of an order
     * @apiName UpdateOrderInitialQuote
     * @apiGroup Orders
     * @apiVersion 1.0.0
     * @apiHeaderExample {json} Header-Example:
     *     {
     *       "Content-Type": "application/x-www-form-urlencoded"
     *     }
     *
     * @apiParam {Number} id The id of the order that will be updated.
     * @apiParam {Number} initialQuote The new initial quote.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 1,
     *          "createdAt": "2018-08-22@20:09:58",
     *          "updatedAt": "2018-08-23@21:29:16",
     *          "referencePo": "new reference po",
     *          "products": [],
     *          "state": {
     *              "id": 2,
     *              "createdAt": "2018-08-22@05:00:00",
     *              "updatedAt": "2018-08-22@05:00:00",
     *              "name": "Pending Initial Quote"
     *          },
     *          "initialQuote": 500,
     *          "finalQuote": 0
     *      }
     *
     * @apiError BAD_REQUEST An error occurred and the resource cannot be updated.
     * @apiError NOT_FOUND The resource does not exist or was not found.
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
     */
    @PatchMapping(value = "/{id}" + INITIAL_QUOTE_URL, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Order> updateOrderInitialQuote(
        @PathVariable(ID_KEY) long idOrder,
        @RequestParam(value = "initialQuote") double initialQuote)
    {
        Order storedOrder = getStoredObject(idOrder);
        storedOrder.setInitialQuote(initialQuote);

        return update(idOrder, storedOrder);
    }

    /**
     * @api {patch} /orders/{id}/finalQuote Update the final quote, origin country and deliveryTime
     * @apiName Update final quote
     * @apiGroup Orders
     * @apiVersion 1.0.0
     * @apiHeaderExample {json} Header-Example:
     *     {
     *       "Content-Type": "application/x-www-form-urlencoded"
     *     }
     *
     * @apiParam {Number} id The id of the order that will be updated.
     * @apiParam {Number} finalQuote The new final quote.
     * @apiParam {String} originCountry The order origin country.
     * @apiParam {Number} deliveryTime The number of days since today till delivery.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 1,
     *          "createdAt": "2018-08-22@20:09:58",
     *          "updatedAt": "2018-08-23@21:29:16",
     *          "referencePo": "new reference po",
     *          "products": [],
     *          "state": {
     *              "id": 2,
     *              "createdAt": "2018-08-22@05:00:00",
     *              "updatedAt": "2018-08-22@05:00:00",
     *              "name": "Pending Initial Quote"
     *          },
     *          "initialQuote": 500,
     *          "finalQuote": 600,
     *          "originCountry": "United States",
     *          "deliveryTime": "2018-08-29@05:00:00"
     *      }
     *
     * @apiError BAD_REQUEST An error occurred and the resource cannot be updated.
     * @apiError NOT_FOUND The resource does not exist or was not found.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "An error occurred and the resource cannot be updated"
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Thursday, August 27, 2018 4:19:24 PM COT"
     *      }
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 404 NOT_FOUND
     *     {
     *          "errorMessages": [
     *              "The resource does not exist or was not found."
     *          ],
     *          "status": "NOT_FOUND",
     *          "statusCode": 404,
     *          "timestamp": "Thursday, August 27, 2018 4:07:23 PM COT"
     *      }
     */
    @PatchMapping(value = "/{id}" + FINAL_QUOTE_URL, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Order> updateOrderFinalQuote(
        @PathVariable(ID_KEY) long idOrder,
        @RequestParam(value = "finalQuote") double finalQuote,
        @RequestParam(value = "originCountry") String originCountry,
        @RequestParam(value = "deliveryTime") int deliveryDays)
    {
        Order storedOrder = getStoredObject(idOrder);

        LocalDate now = LocalDate.now();
        now = now.plusDays(deliveryDays);
        Date deliveryTime = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());

        storedOrder.setFinalQuote(finalQuote);
        storedOrder.setOriginCountry(originCountry);
        storedOrder.setDeliveryTime(deliveryTime);

        return update(idOrder, storedOrder);
    }

    /**
     * @api {patch} /orders/{id}/comment Update the comment of an order
     * @apiName Update Order Comment
     * @apiGroup Orders
     * @apiVersion 1.0.0
     * @apiHeaderExample {json} Header-Example:
     *     {
     *       "Content-Type": "application/x-www-form-urlencoded"
     *     }
     *
     * @apiParam {Number} id The id of the order that will be updated.
     * @apiParam {String} comment The new comment.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 1,
     *          "createdAt": "2018-08-22@20:09:58",
     *          "updatedAt": "2018-08-23@21:30:38",
     *          "referencePo": "new reference po",
     *          "products": [],
     *          "state": {
     *              "id": 2,
     *              "createdAt": "2018-08-22@05:00:00",
     *              "updatedAt": "2018-08-22@05:00:00",
     *              "name": "Pending Initial Quote"
     *          },
     *          "initialQuote": 500,
     *          "finalQuote": 0,
     *          "comment": "I reject this order because I don't like the quote."
     *      }
     *
     * @apiError BAD_REQUEST An error occurred and the resource cannot be updated.
     * @apiError NOT_FOUND The resource does not exist or was not found..
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
     */
    @PatchMapping(value = "/{id}" + COMMENT_URL, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Order> updateOrderComment(
        @PathVariable(ID_KEY) long idOrder,
        @RequestParam(value = "comment") String comment)
    {
        Order storedOrder = getStoredObject(idOrder);
        storedOrder.setComment(comment);

        return update(idOrder, storedOrder);
    }

    /**
     * @api {post} orders/{id}/shippingAddress/{shippingId} Add a shipping address to order.
     * @apiName addOrderShippingAddress
     * @apiGroup ShippingAddress
     * @apiVersion 1.0.0
     * @apiHeaderExample {json} Header-Example:
     *     {
     *       "Content-Type": "application/x-www-form-urlencoded"
     *     }
     *
     * @apiParam {Number} id The id of the order.
     * @apiParam {Number} shippingId The id of the shipping address.
     * @apiParam {String} shippingType The shipping type for save the shipping address.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 1,
     *          "createdAt": "2018-08-22@20:09:58",
     *          "updatedAt": "2018-08-23@21:30:38",
     *          "referencePo": "new reference po",
     *          "products": [],
     *          "state": {
     *              "id": 2,
     *              "createdAt": "2018-08-22@05:00:00",
     *              "updatedAt": "2018-08-22@05:00:00",
     *              "name": "Pending Initial Quote"
     *          },
     *          "shippingAddresses": { "Samples": {
     *                  "id": 1,
     *                  "createdAt": "2018-09-04@16:57:40",
     *                  "updatedAt": "2018-09-04@16:57:40",
     *                  "contactName": "example",
     *                  "country": "example",
     *                  "streetAddress": "example",
     *                  "streetAddressAddInfo": "example",
     *                  "state": "example",
     *                  "city": "example",
     *                  "postalCode": "12345",
     *                  "countryCode": "123",
     *                  "phoneNumber": "1234567"
     *              }
     *          },
     *          "initialQuote": 500,
     *          "finalQuote": 0,
     *          "comment": "I reject this order because I don't like the quote."
     *      }
     *
     * @apiError BAD_REQUEST An error occurred and the resource cannot be updated.
     * @apiError NOT_FOUND The resource does not exist or was not found.
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
     */
    @PatchMapping(value = "/{" + ID_KEY + "}" + SHIPPING_ADDRESS_URL + "/{" + ID_SHIPPING_KEY + "}",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Order> addOrderShippingAddress(
        @PathVariable(ID_KEY) long orderId,
        @PathVariable(ID_SHIPPING_KEY) long idShippingAddress,
        @RequestParam(value = SHIPPING_TYPE) String type)
    {
        Order storedOrder = getStoredObject(orderId);
        ShippingAddress storedShippingAddress = shippingAddressService.findById(idShippingAddress);

        if (storedShippingAddress == null)
        {
            throw new RestException(NOT_CREATED_KEY, HttpStatus.BAD_REQUEST);
        }

        storedOrder.getShippingAddresses().put(type, storedShippingAddress);
        orderService.update(storedOrder);

        return new ResponseEntity<>(storedOrder, HttpStatus.OK);
    }
}