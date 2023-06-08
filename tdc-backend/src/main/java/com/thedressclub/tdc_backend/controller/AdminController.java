/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static com.thedressclub.tdc_backend.controller.UserController.USER_ROLE_PARAM;
import static com.thedressclub.tdc_backend.controller.config.GenericController.NOT_CREATED_KEY;

import com.thedressclub.tdc_backend.controller.config.RestException;
import com.thedressclub.tdc_backend.model.Role;
import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.RoleService;
import com.thedressclub.tdc_backend.service.UserService;
import com.thedressclub.tdc_backend.service.external.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest API controller for Admin process.
 *
 * @author Daniel Mejia.
 */
@RestController
@RequestMapping("admin")
public class AdminController
{
    public static final String ADMIN_URL = "/admin";
    static final String TEAM_URL = "/team";
    static final String ROLE_WRONG_REQUEST = "role_wrong_request";

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private IAuthService authService;

    /**
     * @api {post} /admin/team Create a team member.
     * @apiName Add team user.
     * @apiGroup Admin
     * @apiVersion 1.0.0
     *
     * @apiParam {String} role The user role to create the team member.
     * @apiParamExample {json} Request-Example:
     *     {
     * 	        "firstName": "Customer",
     * 	        "companyEmail": "fashion@tdc.com",
     *      }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 201 Created
     *     {
     *          "id": 1,
     *          "createdAt": "2018-09-26@20:02:55",
     *          "updatedAt": "2018-09-26@20:02:55",
     *          "authKey": "5ba92cffdsc032fd6721f7055f7a",
     *          "firstName": "Admin",
     *          "companyEmail": "fashion@tdc.com",
     *          "role": {
     *              "id": 1,
     *              "createdAt": "2018-09-26@19:56:58",
     *              "updatedAt": "2018-09-26@19:56:58",
     *              "name": "ADMIN"
     *          }
     *      }
     *
     * @apiError BAD_REQUEST The role is wrong, please verify your request.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "The role is wrong, please verify your request."
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Friday, August 24, 2018 10:00:21 AM COT"
     *      }
     */
    @PostMapping(value = TEAM_URL)
    public ResponseEntity<User> addUserTeam(
        @RequestParam(USER_ROLE_PARAM) String roleName,
        @RequestBody User user)
    {
        Role userRole = roleService.findBy(roleName.toUpperCase());

        if (userRole == null)
        {
            throw new RestException(ROLE_WRONG_REQUEST, HttpStatus.BAD_REQUEST);
        }

        String authKey = authService.addUser(user.getCompanyEmail());

        if (authKey == null)
        {
            throw new RestException(NOT_CREATED_KEY, HttpStatus.BAD_REQUEST);
        }

        user.setAuthKey(authKey);
        user.setRole(userRole);
        User teamMember = userService.add(user);

        if (teamMember == null)
        {
            throw new RestException(NOT_CREATED_KEY, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(teamMember, HttpStatus.OK);
    }
}
