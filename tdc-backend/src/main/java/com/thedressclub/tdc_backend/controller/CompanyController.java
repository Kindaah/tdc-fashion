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
import static com.thedressclub.tdc_backend.util.UserUtils.getCurrentUser;
import static com.thedressclub.tdc_backend.util.Utils.getMessage;
import static java.util.Collections.singletonMap;

import com.thedressclub.tdc_backend.controller.config.GenericController;
import com.thedressclub.tdc_backend.controller.config.RestException;
import com.thedressclub.tdc_backend.model.Company;
import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.CompanyService;
import com.thedressclub.tdc_backend.service.UserService;
import com.thedressclub.tdc_backend.service.external.IMailSenderService;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest API controller for {@link Company}.
 *
 * @author Daniel Mejia.
 */
@RestController
public class CompanyController extends GenericController<Company>
{
    private static final Logger LOGGER = Logger.getLogger(CompanyController.class.getName());

    private static final String USER_ID_PARAM = "userId";
    public static final String COMPANY_URL = "/company";
    public static final String REQUEST = "Request";

    static final String FAILED_TO_SEND_EMAIL = "failed_to_sent_email";
    static final String MESSAGE_KEY = "message";
    static final String COMPLETE_USER_EMAIL_SENT = "user_complete_email_sent";

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private IMailSenderService mailSenderService;

    /**
     * @api {post} /users/{userId}/company Create a company.
     * @apiName AddCompany
     * @apiGroup Company
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} userId The id of the user related with the company.
     *
     * @apiParamExample {json} Request-Example:
     *      {
     *          "id": 3,
     *          "createdAt": "2018-11-20@19:55:39",
     *          "updatedAt": "2018-11-20@19:55:39",
     *          "name": "TDC Fashion",
     *          "country": "USA",
     *          "state": "Texas",
     *          "city": "Houston",
     *          "streetAddress": "Avenida Siempreviva 742",
     *          "addressAdditionalInfo": "Avenida Siempreviva 742",
     *          "postalCode": "12345",
     *          "website": "https://tdc.fashion.com",
     *          "trademarkName": "Trademark",
     *          "trademarkRegistrationNumber": "Tradermark registration",
     *          "salesTaxPermit": "None",
     *          "dunNumber": "123456789",
     *          "einNumber": "123-456789",
     *          "nearestAirport": "Houston airport",
     *          "secondNearestAirport": "Las vegas airport",
     *          "lineBusiness": "12345"
     *      }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 201 Created
     *      {
     *          "id": 3,
     *          "createdAt": "2018-11-20@19:55:39",
     *          "updatedAt": "2018-11-20@19:55:39",
     *          "name": "TDC Fashion",
     *          "country": "USA",
     *          "state": "Texas",
     *          "city": "Houston",
     *          "streetAddress": "Avenida Siempreviva 742",
     *          "addressAdditionalInfo": "Avenida Siempreviva 742",
     *          "postalCode": "12345",
     *          "website": "https://tdc.fashion.com",
     *          "trademarkName": "Trademark",
     *          "trademarkRegistrationNumber": "Tradermark registration",
     *          "salesTaxPermit": "None",
     *          "dunNumber": "123456789",
     *          "einNumber": "123-456789",
     *          "nearestAirport": "Houston airport",
     *          "secondNearestAirport": "Las vegas airport",
     *          "lineBusiness": "12345"
     *      }
     *
     * @apiError NOT_FOUND The resource does not exist or was not found.
     * @apiError BAD_REQUEST The company name cannot be blank, please add a value.
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
     *              "The company name cannot be blank, please add a value."
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Tuesday, September 4, 2018 5:01:23 PM COT"
     *      }
     */
    @PostMapping(value = USERS_URL + SLASH + "{" + USER_ID_PARAM + "}" + COMPANY_URL, headers = HEADER_TYPE)
    public ResponseEntity<Company> addCompany(
        @PathVariable(USER_ID_PARAM) long userId, @Valid @RequestBody Company company)
    {
        User user = userService.findById(userId);
        company.setUser(user);
        user.setCompany(company);
        add(company);

        return new ResponseEntity<>(company, HttpStatus.CREATED);
    }

    /**
     * @api {post} /users/company/{id} Update a company.
     * @apiName UpdateCompany
     * @apiGroup Company
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id The id of the company.
     *
     * @apiParamExample {json} Request-Example:
     *     {
     *          "name": "TDC Fashion",
     *          "country": "USA",
     *          "state": "Texas",
     *          "city": "Houston",
     *          "streetAddress": "Avenida Siempreviva 742",
     *          "addressAdditionalInfo": "Avenida Siempreviva 742",
     *          "postalCode": "12345",
     *          "website": "https://tdc.fashion.com",
     *          "trademarkName": "Trademark",
     *          "trademarkRegistrationNumber": "Tradermark registration",
     *          "salesTaxPermit": "None",
     *          "dunNumber": "123456789",
     *          "einNumber": "123-456789",
     *          "nearestAirport": "Houston airport",
     *          "secondNearestAirport": "Las vegas airport",
     *          "lineBusiness": "12345"
     *      }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *       {
     *          "id": 3,
     *          "createdAt": "2018-11-20@19:55:39",
     *          "updatedAt": "2018-11-20@19:55:39",
     *          "name": "TDC Fashion",
     *          "country": "USA",
     *          "state": "Texas",
     *          "city": "Houston",
     *          "streetAddress": "Avenida Siempreviva 742",
     *          "addressAdditionalInfo": "Avenida Siempreviva 742",
     *          "postalCode": "12345",
     *          "website": "https://tdc.fashion.com",
     *          "trademarkName": "Trademark",
     *          "trademarkRegistrationNumber": "Tradermark registration",
     *          "salesTaxPermit": "None",
     *          "dunNumber": "123456789",
     *          "einNumber": "123-456789",
     *          "nearestAirport": "Houston airport",
     *          "secondNearestAirport": "Las vegas airport",
     *          "lineBusiness": "12345"
     *      }
     *
     * @apiError NOT_FOUND The resource does not exist or was not found.
     * @apiError BAD_REQUEST The company name cannot be blank, please add a value.
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
     *              "The company name cannot be blank, please add a value."
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Tuesday, September 4, 2018 5:01:23 PM COT"
     *      }
     */
    @PutMapping(value = USERS_URL + COMPANY_URL + SLASH + "{id}", headers = HEADER_TYPE)
    public ResponseEntity<Company> updateCompany(
        @PathVariable(ID_KEY) long idCompany, @Valid @RequestBody Company company)
    {
        return update(idCompany, company);
    }

    /**
     * @api {get} /users/companyRequest Send an email to notify TDC to complete company information
     * @apiName Request current user company information.
     * @apiGroup Company
     * @apiVersion 1.0.0
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     [
     *          {
     *              "message": "New request to complete company information"
     *          }
     *     ]
     */
    @GetMapping(value = USERS_URL + COMPANY_URL + REQUEST, headers = HEADER_TYPE)
    public ResponseEntity<Map> getRequestUserCompany()
    {
        User storedUser = getCurrentUser(userService);

        try
        {
            mailSenderService.sendCompleteUserEmail(storedUser);
        }
        catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, e.getMessage());

            throw new RestException(FAILED_TO_SEND_EMAIL, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(singletonMap(MESSAGE_KEY, getMessage(COMPLETE_USER_EMAIL_SENT)), HttpStatus.OK);
    }
}
