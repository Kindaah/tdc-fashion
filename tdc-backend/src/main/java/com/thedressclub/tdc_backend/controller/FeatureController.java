/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import com.thedressclub.tdc_backend.business.ProductBusiness;
import com.thedressclub.tdc_backend.controller.config.GenericController;
import com.thedressclub.tdc_backend.model.Feature;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Rest API controller for {@link Feature}.
 *
 * @author Daniel Mejia.
 */
@RestController
public class FeatureController extends GenericController<Feature>
{
    static final String FEATURES_URL = "/features";
    static final String UPLOAD_PATTERNS_URL = "uploadPatterns";
    static final String PATTERNS_FILE_KEY = "patternsFiles";

    @Autowired
    private ProductBusiness productBussines;

    /**
     * @api {post} /features/{id}/uploadPatterns Upload files patterns
     * @apiName UploadFeatures
     * @apiGroup Features
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id The id of the feature.
     * @apiParam {File} [patternsFiles] The patterns files.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *      {
     *          "id": 7,
     *          "createdAt": "2018-11-27@20:07:14",
     *          "updatedAt": "2018-11-27@16:07:41",
     *          "color": "rgb(83,42,42)",
     *          "size": "M",
     *          "quantity": 11,
     *          "sampleQuantity": 0,
     *          "patternsUrls": "orders/order_47/product_6/features/feature_7/patternsFiles/Dress Club REST API.postman_collection.json"
     *      }
     * @apiError NOT_FOUND The resource does not exist or was not found.
     * @apiError BAD_REQUEST Maximum upload size exceeded.
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
     *              "Maximum upload size exceeded; nested exception is java.lang.IllegalStateException: org.apache.tomcat.util.http.fileupload.FileUploadBase$FileSizeLimitExceededException: The field sketchFiles exceeds its maximum permitted size of 1048576 bytes."
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Monday, August 27, 2018 2:13:03 PM COT"
     *      }
     */
    @PostMapping(value = FEATURES_URL + "/{id}/" + UPLOAD_PATTERNS_URL, headers = HEADER_TYPE)
    public ResponseEntity<Feature> uploadPatterns(@PathVariable(ID_KEY) long idFeature,
        @RequestParam(value = PATTERNS_FILE_KEY, required = false) MultipartFile[] patternsFiles)
    {
        Feature storedFeature = getStoredObject(idFeature);
        Function<String, String> getPrefix = productBussines.getBasePrefix(storedFeature);

        String basePatternsPrefix  = getPrefix.apply(PATTERNS_FILE_KEY);
        String patternsFileUrls =
            productBussines.storeFiles(basePatternsPrefix, patternsFiles, storedFeature.getPatternsUrls());

        storedFeature.setPatternsUrls(patternsFileUrls);

        return update(idFeature, storedFeature);
    }
}
