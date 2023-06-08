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
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * Interface for any  Controller
 *
 * @param <T> the type of the Model.
 *
 * @author Edson Ruiz Ramirez.
 */
public interface IGenericController<T extends GenericModel>
{
    /**
     * Gets a list of all objects.
     *
     * @return response entity with a list with all the objects.
     */
    ResponseEntity<List<T>> getAll();

    /**
     * Gets an object by his id.
     *
     * @return response entity with the object founded.
     */
    ResponseEntity<T> findById(long id);

    /**
     * Add an object.
     *
     * @param modelObject to be added.
     *
     * @return response entity with the object added.
     */
    ResponseEntity<T> add(T modelObject);

    /**
     * Updates an object.
     *
     * @param objectId the objectId to be updated.
     * @param modelObject to be updated.
     *
     * @return response entity with the object updated.
     */
    ResponseEntity<T> update(long objectId, T modelObject);

    /**
     * Deletes an object.
     *
     * @param idObject to be deleted.
     *
     * @return response entity with the object deleted.
     */
    ResponseEntity<T> delete(long idObject);

    /**
     * Sets a generic service.
     *
     * @param genericService the service to be set.
     */
    void setGenericService(GenericService<T> genericService);

    /**
     * Get the stored object by his id.
     *
     * @param objectId the id to search the object.
     *
     * @return the stored object.
     * @throws RestException if the object is not founded.
     */
    T getStoredObject(long objectId);
}