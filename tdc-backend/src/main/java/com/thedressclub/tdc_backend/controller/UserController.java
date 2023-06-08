/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static java.util.Base64.getDecoder;
import static java.util.Collections.singletonMap;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;

import com.thedressclub.tdc_backend.controller.config.GenericController;
import com.thedressclub.tdc_backend.controller.config.RestException;
import com.thedressclub.tdc_backend.model.Role;
import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.OrderService;
import com.thedressclub.tdc_backend.service.RoleService;
import com.thedressclub.tdc_backend.service.UserService;
import com.thedressclub.tdc_backend.service.external.IAuthService;
import com.thedressclub.tdc_backend.service.external.IMailSenderService;
import com.thedressclub.tdc_backend.util.AesUtil;
import freemarker.template.TemplateException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest API controller for {@link User}.
 *
 * @author Edson Ruiz Ramirez.
 */
@RestController
@RequestMapping("users")
public class UserController extends GenericController<User>
{
    public static final String USERS_URL = "/users";

    static final String SPLIT_PARAM_PASS = "::";

    static final String PASS_CHANGE_ERROR_KEY = "password_change_error_messages";
    static final String REGISTER_KEY = "register.key";
    static final String APP_ALLOWED = "app.domain";
    static final String REGISTER_TOKEN_KEY = "registerToken";
    static final String AUTH_KEY_PARAM = "authKey";
    static final String USER_ROLE_PARAM = "role";
    static final String PASS_KEY_PARAM = "passToken";

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private Environment environment;

    @Autowired
    private IMailSenderService mailSenderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private IAuthService authService;

    @Autowired
    private AesUtil aesUtil;

    private JwtParser jwtParser = Jwts.parser();

    private JwtBuilder jwtBuilder = Jwts.builder();

    /**
     * @api {get} /users/token Get register token
     * @apiName Get register token
     * @apiGroup Users
     * @apiVersion 1.0.0
     * @apiHeaderExample {json} Header-Example:
     *     {
     *       "sharedKey": "Shared key to generate token"
     *       "subject": "Subject to generate the token"
     *     }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "registerToken": "token generated"
     *     }
     */
    @GetMapping(value = "/token", headers = HEADER_TYPE)
    public ResponseEntity<Map> getRegisterToken(
        @RequestHeader String sharedKey, @RequestHeader String subject)
    {
        SecretKey key = Keys.hmacShaKeyFor(sharedKey.getBytes(StandardCharsets.UTF_8));

        String jws =
            jwtBuilder
            .setSubject(subject)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return new ResponseEntity<>(singletonMap(REGISTER_TOKEN_KEY, jws), HttpStatus.OK);
    }

    /**
     * @api {post} /users Create an user
     * @apiName AddUser
     * @apiName AddUser
     * @apiGroup Users
     * @apiVersion 1.0.0
     * @apiHeaderExample {json} Header-Example:
     *     {
     *       "registerToken": "token for register"
     *     }
     * @apiParamExample {json} Request-Example:
     *     {
     * 	        "authKey":"5ba92cffdsc032fd6721f7055f7a",
     * 	        "firstName": "Customer",
     * 	        "lastName": "Customer",
     * 	        "phoneNumber": "33333",
     * 	        "companyEmail": "fashion@tdc.com",
     * 	        "companyName": "Tdc Fashion"
     *      }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 201 Created
     *     {
     *          "id": 1,
     *          "createdAt": "2018-09-26@20:02:55",
     *          "updatedAt": "2018-09-26@20:02:55",
     *          "authKey": "5ba92cffdsc032fd6721f7055f7a",
     *          "firstName": "Customer",
     *          "lastName": "Customer",
     *          "companyEmail": "fashion@tdc.com",
     *          "companyName": "Tdc Fashion",
     *          "phoneNumber": "33333",
     *          "role": {
     *              "id": 2,
     *              "createdAt": "2018-09-26@19:56:58",
     *              "updatedAt": "2018-09-26@19:56:58",
     *              "name": "CUSTOMER"
     *          }
     *      }
     *
     * @apiError BAD_REQUEST The full name cannot be blank, please add a value.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "The full name cannot be blank, please add a value."
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Friday, August 24, 2018 10:00:21 AM COT"
     *      }
     */
    @PostMapping(headers = HEADER_TYPE)
    public ResponseEntity<User> addUser(
        @RequestHeader(required = false) String registerToken,
        @Valid @RequestBody User user)
    throws IOException, TemplateException
    {
        String registerKey = environment.getProperty(REGISTER_KEY);
        String appAllowed = environment.getProperty(APP_ALLOWED);

        Claims decodeToken =
            jwtParser
                .setSigningKey(requireNonNull(registerKey).getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(registerToken)
                .getBody();

        if (requireNonNull(appAllowed).equals(decodeToken.getSubject()))
        {
            Role customerRole = roleService.findById(Role.CUSTOMER_ROLE);
            user.setRole(customerRole);
            mailSenderService.sendCompleteUserEmail(user);

            return add(user);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * @api {get} /users Get all users
     * @apiName GetAllUsers
     * @apiGroup Users
     * @apiVersion 1.0.0
     *
     * @apiParam {String} role The user role list to filter the users
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     [
     *          {
     *              "id": 3,
     *              "createdAt": "2018-09-16@16:38:46",
     *              "updatedAt": "2018-09-16@16:38:46",
     *              "authKey": "5b9bc8db72d4bb47f9a77813",
     *              "firstName": "admin",
     *              "lastName": "admin",
     *              "companyEmail": "admin@tdc.com",
     *              "title": "Admin",
     *              "phoneNumber": "33333",
     *          }
     *      ]
     */
    @GetMapping(headers = HEADER_TYPE)
    public ResponseEntity<List<User>> getAllUsers(
        @RequestParam(value = USER_ROLE_PARAM, defaultValue = "") List<String> userRoles)
    {
        return userRoles.isEmpty() ? getAll() : new ResponseEntity<>(userService.filterBy(userRoles), HttpStatus.OK);
    }

    /**
     * @api {get} /users/{id} Find an user by id
     * @apiName FindByIdUser
     * @apiGroup Users
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id The id of the user that will be searched.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 1,
     *          "createdAt": "2018-09-16@16:38:46",
     *          "updatedAt": "2018-09-16@16:38:46",
     *          "authKey": "5b9bc8db72d4bb47f9a77813",
     *          "firstName": "admin",
     *          "lastName": "admin",
     *          "companyEmail": "admin@tdc.com",
     *          "title": "Admin",
     *          "phoneNumber": "33333",
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
    public ResponseEntity<User> findByIdUser(@PathVariable(ID_KEY) long idUser)
    {
        return findById(idUser);
    }

    /**
     * @api {get} /users/authKey/{authKey} Find an user by id
     * @apiName FindByAuthKey
     * @apiGroup Users
     * @apiVersion 1.0.0
     *
     * @apiParam {String} authKey The authKey of the user that will be searched.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 3,
     *          "createdAt": "2018-09-16@16:38:46",
     *          "updatedAt": "2018-09-16@16:38:46",
     *          "authKey": "5b9bc8db72d4bb47f9a77813",
     *          "firstName": "admin",
     *          "lastName": "admin",
     *          "companyEmail": "admin@tdc.com",
     *          "title": "Admin",
     *          "phoneNumber": "33333",
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
    @GetMapping(value = "/" + AUTH_KEY_PARAM + "/{" + AUTH_KEY_PARAM + "}", headers = HEADER_TYPE)
    public ResponseEntity<User> findByAuthKey(@PathVariable(AUTH_KEY_PARAM) String authKey)
    {
        User storedUser = userService.findByAuthKey(authKey);

        if (storedUser == null)
        {
            throw new RestException(NOT_FOUND_KEY, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(storedUser, HttpStatus.OK);
    }

    /**
     * @api {put} /users/{id} Update the full name of an user
     * @apiName UpdateUser
     * @apiGroup Users
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id The id of the user that will be updated.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 3,
     *          "createdAt": "2018-09-16@16:38:46",
     *          "updatedAt": "2018-09-16@16:38:46",
     *          "authKey": "5b9bc8db72d4bb47f9a77813",
     *          "firstName": "admin",
     *          "lastName": "admin",
     *          "companyEmail": "admin@tdc.com",
     *          "title": "Admin",
     *          "phoneNumber": "33333",
     *      }
     *
     * @apiError BAD_REQUEST The full name cannot be blank, please add a value.
     * @apiError NOT_FOUND The resource does not exist or was not found.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "The full name cannot be blank, please add a value."
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Friday, August 24, 2018 11:39:26 AM COT"
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
    @PutMapping(value = "/{id}", headers = HEADER_TYPE)
    public ResponseEntity<User> updateUser(
        @PathVariable(ID_KEY) long idUser, @Valid @RequestBody User user)
    {
        return update(idUser, user);
    }

    /**
     * @api {delete} /users/{id} Delete an user
     * @apiName DeleteUser
     * @apiGroup Users
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id The id of the user that will be deleted.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 3,
     *          "createdAt": "2018-09-16@16:38:46",
     *          "updatedAt": "2018-09-16@16:38:46",
     *          "authKey": "5b9bc8db72d4bb47f9a77813",
     *          "firstName": "admin",
     *          "lastName": "admin",
     *          "companyEmail": "admin@tdc.com",
     *          "title": "Admin",
     *          "phoneNumber": "33333",
     *      }
     *
     * @apiError NOT_FOUND The resource does not exist or was not found.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 NOT_FOUND
     *     {
     *          "errorMessages": [
     *              "The resource does not exist or was not found."
     *          ],
     *          "status": "NOT_FOUND",
     *          "statusCode": 404,
     *          "timestamp": "Friday, August 24, 2018 11:41:46 AM COT"
     *      }
     */
    @DeleteMapping(value = "/{id}", headers = HEADER_TYPE)
    public ResponseEntity<User> deleteUser(@PathVariable(ID_KEY) long idUser)
    {
        return delete(idUser);
    }

    /**
     * @api {patch} /users/{authKey} Change the password of a user.
     * @apiName ChangePassword
     * @apiGroup Users
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id The id of the user that will be deleted.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 3,
     *          "createdAt": "2018-09-16@16:38:46",
     *          "updatedAt": "2018-09-16@16:38:46",
     *          "authKey": "5b9bc8db72d4bb47f9a77813",
     *          "firstName": "admin",
     *          "lastName": "admin",
     *          "companyEmail": "admin@tdc.com",
     *          "title": "Admin",
     *          "phoneNumber": "33333",
     *      }
     *
     * @apiError NOT_FOUND The resource does not exist or was not found.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 NOT_FOUND
     *     {
     *          "errorMessages": [
     *              "The resource does not exist or was not found."
     *          ],
     *          "status": "NOT_FOUND",
     *          "statusCode": 404,
     *          "timestamp": "Friday, August 24, 2018 11:41:46 AM COT"
     *      }
     */
    @PatchMapping(value = "/{"+AUTH_KEY_PARAM+"}", headers = HEADER_TYPE)
    public ResponseEntity<User> changePassword(
        @PathVariable(AUTH_KEY_PARAM) String authKey,
        @RequestParam(PASS_KEY_PARAM) String newPassword)
    {

        String decryptedPassword =  new String(getDecoder().decode(newPassword));
        String[] decryptedPasswordList = decryptedPassword.split(SPLIT_PARAM_PASS);

        if (decryptedPasswordList.length == 3)
        {
            String password = aesUtil.decrypt(decryptedPasswordList[0], decryptedPasswordList[1], decryptedPasswordList[2]);
            boolean passwordChanged = authService.changePassword(authKey, password);

            if (passwordChanged)
            {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        throw new RestException(PASS_CHANGE_ERROR_KEY, EXPECTATION_FAILED);
    }
}