/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller.config;

import com.thedressclub.tdc_backend.model.GenericModel;
import com.thedressclub.tdc_backend.service.GenericService;
import com.thedressclub.tdc_backend.util.Utils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Contains the basic logic for REST API controller.
 *
 * @author Edson Ruiz Ramirez.
 */
public abstract class GenericController<T extends GenericModel> implements IGenericController<T>
{
    private static final Logger LOGGER = Logger.getLogger(GenericController.class.getName());
    public static final String SLASH = "/";

    private static final String TO_ADD_KEY = "resource_to_add";
    private static final String TO_GET_ALL_KEY = "resource_to_get_all";
    private static final String TO_FIND_BY_ID_KEY = "resource_to_find_by_id";
    private static final String TO_UPDATE_KEY = "resource_to_update";
    private static final String TO_DELETE_KEY = "resource_to_delete";
    private static final String TO_GET_STORAGE_OBJECT_KEY = "resource_to_get_storage_object";

    protected static final String NOT_FOUND_KEY = "not_found";
    public static final String NOT_CREATED_KEY = "not_created";
    static final String NOT_UPDATED_KEY = "not_updated";

    protected static final String ID_KEY = "id";
    protected static final String HEADER_TYPE = "Accept=application/json";

    @Autowired
    protected GenericService<T> genericService;

    /** (non-Javadoc) @see {@link IGenericController} */
    @Override
    public ResponseEntity<T> add(T modelObject)
    {
        T modelObjectAdded = genericService.add(modelObject);

        LOGGER.log(Level.INFO, Utils.getMessage(TO_ADD_KEY), modelObject.getClass().getName());

        if (modelObjectAdded == null)
        {
            throw new RestException(NOT_CREATED_KEY, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(modelObjectAdded, HttpStatus.CREATED);
    }

    /** (non-Javadoc) @see {@link IGenericController} */
    @Override
    public ResponseEntity<List<T>> getAll()
    {
        LOGGER.log(Level.INFO, Utils.getMessage(TO_GET_ALL_KEY), T.getModelPackageName());

        List<T> modelObjects = genericService.getAll();

        return new ResponseEntity<>(modelObjects, HttpStatus.OK);
    }

    /** (non-Javadoc) @see {@link IGenericController} */
    @Override
    public ResponseEntity<T> findById(long objectId)
    {
        LOGGER.log(Level.INFO, Utils.getMessage(TO_FIND_BY_ID_KEY), T.getModelPackageName());

        T storedObject = getStoredObject(objectId);

        return new ResponseEntity<>(storedObject, HttpStatus.OK);
    }

    /** (non-Javadoc) @see {@link IGenericController} */
    @Override
    public ResponseEntity<T> update(long objectId, T modelObject)
    {
        LOGGER.log(Level.INFO, Utils.getMessage(TO_UPDATE_KEY), modelObject.getClass().getName());

        getStoredObject(objectId);
        modelObject.setId(objectId);
        T modelObjectUpdated = genericService.update(modelObject);

        if (modelObjectUpdated == null)
        {
            throw new RestException(NOT_UPDATED_KEY, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(modelObjectUpdated, HttpStatus.OK);
    }

    /** (non-Javadoc) @see {@link IGenericController} */
    @Override
    public ResponseEntity<T> delete(long idObject)
    {
        LOGGER.log(Level.INFO, Utils.getMessage(TO_DELETE_KEY), T.getModelPackageName());

        T objectToDelete = getStoredObject(idObject);
        genericService.delete(objectToDelete);

        return new ResponseEntity<>(objectToDelete, HttpStatus.OK);
    }

    /** (non-Javadoc) @see {@link IGenericController} */
    @Override
    public void setGenericService(GenericService<T> genericService)
    {
        this.genericService = genericService;
    }

    /** (non-Javadoc) @see {@link IGenericController} */
    @Override
    public T getStoredObject(long objectId)
    {
        LOGGER.log(Level.INFO, Utils.getMessage(TO_GET_STORAGE_OBJECT_KEY), T.getModelPackageName());

        T storedObject = genericService.findById(objectId);

        if (storedObject == null)
        {
            throw new RestException(NOT_FOUND_KEY, HttpStatus.NOT_FOUND);
        }

        return storedObject;
    }
}