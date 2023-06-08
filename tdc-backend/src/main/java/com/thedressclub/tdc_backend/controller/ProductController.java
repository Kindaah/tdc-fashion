/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.thedressclub.tdc_backend.model.Product.MODEL_FILE_KEY;
import static com.thedressclub.tdc_backend.model.Product.SKETCH_FILE_KEY;
import static com.thedressclub.tdc_backend.model.Product.TECH_SHEET_FILE_KEY;

import com.thedressclub.tdc_backend.business.ProductBusiness;
import com.thedressclub.tdc_backend.controller.config.GenericController;
import com.thedressclub.tdc_backend.controller.config.RestException;
import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.Product;
import com.thedressclub.tdc_backend.service.IAzureStorageService;
import com.thedressclub.tdc_backend.service.OrderService;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Rest API controller for {@link Product}.
 *
 * @author Daniel Mejia.
 */
@RestController
public class ProductController extends GenericController<Product>
{
    static final String PRODUCTS_URL = "/products";
    static final String FILE_URL = "fileDownloadUrl";
    static final String FILE_NAME_KEY = "fileName";

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductBusiness productBussines;

    @Autowired
    private IAzureStorageService azureStorageService;

    /**
     * @api {post} /orders/{orderId}/products Create a product
     * @apiName AddProduct
     * @apiGroup Products
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} orderId The id of the order that will contain the product.
     *
     * @apiParamExample {json} Request-Example:
     *     {
     *          "description":"description",
     *          "features":[
     *              {
     *                  "color":"color",
     *                  "size":"size",
     *                  "quantity":10
     *              }
     *          ]
     *      }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 201 Created
     *     {
     *          "id": 1,
     *          "createdAt": "2018-08-23@22:05:14",
     *          "updatedAt": "2018-08-23@22:05:14",
     *          "description": "description",
     *          "quote": 0,
     *          "features": [
     *              {
     *                  "id": 1,
     *                  "createdAt": "2018-08-23@22:05:14",
     *                  "updatedAt": "2018-08-23@22:05:14",
     *                  "color": "color",
     *                  "size": "size",
     *                  "quantity": 10,
     *                  "sampleQuantity": 0
     *              }
     *          ]
     *      }
     *
     * @apiError BAD_REQUEST The product description cannot be blank, please add a value.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "The product description cannot be blank, please add a value."
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Thursday, August 23, 2018 5:05:58 PM COT"
     *      }
     */
    @PostMapping(value = "orders/{orderId}" + PRODUCTS_URL, headers = HEADER_TYPE)
    public ResponseEntity<Product> addProduct(
        @PathVariable("orderId") long orderId, @Valid @RequestBody Product product)
    {
        setDefaultValues(product, orderId);

        return add(product);
    }

    /**
     * @api {put} /orders/{orderId}/products/{id} Update a product
     * @apiName UpdateProduct
     * @apiGroup Products
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} orderId The id of the order that will contain the product.
     * @apiParam {Number} id The id of the product.
     *
     * @apiParamExample {json} Request-Example:
     *     {
     *          "id": 1,
     *          "description": "producto",
     *          "quote": 500,
     *          "features": [
     *              {
     *                  "id": 7,
     *                  "color": "dacxzs",
     *                  "size": "sizdsadse",
     *                  "quantity": 54,
     *                  "sampleQuantity": 0
     *              },
     *              {
     *                  "id": 8,
     *                  "color": "color",
     *                  "size": "size",
     *                  "quantity": 10,
     *                  "sampleQuantity": 0
     *              }
     *          ]
     *      }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 1,
     *          "updatedAt": "2018-08-23@22:11:31",
     *          "description": "producto",
     *          "quote": 500,
     *          "features": [
     *              {
     *                  "id": 2,
     *                  "createdAt": "2018-08-23@22:11:31",
     *                  "updatedAt": "2018-08-23@22:11:31",
     *                  "color": "dacxzs",
     *                  "size": "sizdsadse",
     *                  "quantity": 54,
     *                  "sampleQuantity": 0
     *              },
     *              {
     *                  "id": 3,
     *                  "createdAt": "2018-08-23@22:11:31",
     *                  "updatedAt": "2018-08-23@22:11:31",
     *                  "color": "color",
     *                  "size": "size",
     *                  "quantity": 10,
     *                  "sampleQuantity": 0
     *              }
     *          ]
     *      }
     *
     * @apiError BAD_REQUEST The color in a feature cannot be blank, please add a value.
     * @apiError NOT_FOUND The resource does not exist or was not found.
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 BAD_REQUEST
     *     {
     *          "errorMessages": [
     *              "The color in a feature cannot be blank, please add a value."
     *          ],
     *          "status": "BAD_REQUEST",
     *          "statusCode": 400,
     *          "timestamp": "Thursday, August 23, 2018 5:13:12 PM COT"
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
    @PutMapping(value = "orders/{orderId}" + PRODUCTS_URL + "/{id}", headers = HEADER_TYPE)
    public ResponseEntity<Product> updateProduct(
        @PathVariable("orderId") long orderId,
        @PathVariable(ID_KEY) long idProduct,
        @Valid @RequestBody Product product)
    {
        Product storedProduct = genericService.findById(idProduct);

        if (storedProduct == null)
        {
            throw new RestException(NOT_FOUND_KEY, HttpStatus.NOT_FOUND);
        }

        product.setSketchFileUrls(storedProduct.getSketchFileUrls());
        product.setTechnicalSheetFileUrls(storedProduct.getTechnicalSheetFileUrls());
        product.setModelFileUrls(storedProduct.getModelFileUrls());
        setDefaultValues(product, orderId);

        return update(idProduct, product);
    }

    /**
     * @api {post} /products/{id}/uploadFiles Upload files
     * @apiName UploadFiles
     * @apiGroup Products
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id The id of the product.
     * @apiParam {File} [sketchFiles] The sketch file.
     * @apiParam {File} [technicalSheetFiles] The technical Sheet file.
     * @apiParam {File} [modelFiles] The model 3D file.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *          "id": 1,
     *          "createdAt": "2018-08-23@22:05:14",
     *          "updatedAt": "2018-08-23@22:18:36",
     *          "description": "producto",
     *          "quote": 500,
     *          "features": [
     *              {
     *                  "id": 2,
     *                  "createdAt": "2018-08-23@22:11:31",
     *                  "updatedAt": "2018-08-23@22:11:31",
     *                  "color": "dacxzs",
     *                  "size": "sizdsadse",
     *                  "quantity": 54,
     *                  "sampleQuantity": 0
     *              },
     *              {
     *                  "id": 3,
     *                  "createdAt": "2018-08-23@22:11:31",
     *                  "updatedAt": "2018-08-23@22:11:31",
     *                  "color": "color",
     *                  "size": "size",
     *                  "quantity": 10,
     *                  "sampleQuantity": 0
     *              },
     *              {
     *                  "id": 1,
     *                  "createdAt": "2018-08-23@22:05:14",
     *                  "updatedAt": "2018-08-23@22:05:14",
     *                  "color": "color",
     *                  "size": "size",
     *                  "quantity": 10,
     *                  "sampleQuantity": 0
     *              }
     *          ],
     *          "sketchFileUrls": "orders/order_1/product_1/sketchFiles/sketchExample",
     *          "technicalSheetFileUrls":
     *          "orders/order_1/product_1/technicalSheetFiles/technicalSheetExample",
     *          "modelFileUrls": "orders/order_1/product_1/modelFiles/modelExample"
     *      }
     *
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
    @PostMapping(value = "products/{id}/uploadFiles", headers = HEADER_TYPE)
    public ResponseEntity<Product> uploadFiles(
        @PathVariable(ID_KEY) long idProduct,
        @RequestParam(value = SKETCH_FILE_KEY, required = false) MultipartFile[] sketchFiles,
        @RequestParam(value = TECH_SHEET_FILE_KEY, required = false) MultipartFile[] techSheetFiles,
        @RequestParam(value = MODEL_FILE_KEY, required = false) MultipartFile[] modelFiles)
    {
        Product product = getStoredObject(idProduct);
        Function<String, String> getPrefix = productBussines.getBasePrefix(product);

        String baseSketchPrefix = getPrefix.apply(SKETCH_FILE_KEY);
        String sketchFilesUrls =
            productBussines.storeFiles(baseSketchPrefix, sketchFiles, product.getSketchFileUrls());
        product.setSketchFileUrls(sketchFilesUrls);

        String baseTechSheetPrefix = getPrefix.apply(TECH_SHEET_FILE_KEY);
        String techSheetFilesUrls =
            productBussines.storeFiles(baseTechSheetPrefix, techSheetFiles, product.getTechnicalSheetFileUrls());
        product.setTechnicalSheetFileUrls(techSheetFilesUrls);

        String baseModelPrefix = getPrefix.apply(MODEL_FILE_KEY);
        String modelFilesUrls =
            productBussines.storeFiles(baseModelPrefix, modelFiles, product.getModelFileUrls());
        product.setModelFileUrls(modelFilesUrls);

        return update(idProduct, product);
    }

    /**
     * @api {get} /products/fileDownloadUrl Get File Download URL
     * @apiName Get File Download URL
     * @apiGroup Products
     * @apiVersion 1.0.0
     *
     * @apiParam {String} fileName The file name.
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     [
     *          {
     *              "fileDownloadUrl": "orders/order_1/product_1/modelFiles/fileName.jpg?sig=GJqjV08riLznXGRQ8A%3D&st=2018-08-27T14%9%3A50Z&se=2018-08-27T15%3AA50Z&sv=2018-03-28&si=DownloadPolicy&sr=b"
     *          }
     *     ]
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
    @GetMapping(value = "products/" + FILE_URL, headers = HEADER_TYPE)
    public ResponseEntity<Map> getDownloadFileUrl(@RequestParam(value = FILE_NAME_KEY) String fileName)
    {
        String downloadUrl = azureStorageService.getDownloadUrl(fileName);

        if (isNullOrEmpty(downloadUrl))
        {
            throw new RestException(NOT_FOUND_KEY, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(Collections.singletonMap(FILE_URL, downloadUrl), HttpStatus.OK);
    }

    private void setDefaultValues(Product product, long orderId)
    {
        Order order = orderService.findById(orderId);
        product.setOrder(order);
        product.getFeatures().forEach(feature -> feature.setProduct(product));
        product.getTechnicalSheetRows().forEach(technicalSheetRow -> technicalSheetRow.setProduct(product));
    }
}