/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import com.thedressclub.tdc_backend.dao.GenericDAO;
import com.thedressclub.tdc_backend.model.GenericModel;
import java.util.List;
import java.util.Map;

/**
 * Interface for any object Service
 *
 * @param <T> the type of the object.
 *
 * @author Edson Ruiz Ramirez.
 */
public interface IGenericService<T extends GenericModel>
{
    /**
     * Add one object.
     *
     * @param object the object to be added.
     *
     * @return the object added.
     */
    T add(T object);

    /**
     * Finds one object by id.
     *
     * @param id the id of the object to look for.
     *
     * @return an object.
     */
    T findById(Long id);

    /**
     * Gets a list of the {@link T extends GenericModel}.
     *
     * @param equalFilters The filters that the list is going to be filtered by.
     * Use the keys as string by columns in model separated by dot in nested params.
     * Use the value as object or list of object.
     *
     * @return list of {@link T extends GenericModel}.
     */
    List<T> filterBy(Map<String, Object> equalFilters);

    /**
     * Gets a list of all objects.
     *
     * @return a list with all the objects
     */
    List<T> getAll();

    /**
     * Updates one object.
     *
     * @param object the object to be updated.
     *
     * @return the object updated.
     */
    T update(T object);

    /**
     * Deletes an object.
     *
     * @param object the object to be deleted.
     */
    void delete(T object);

    /**
     * Sets the new genericDAO.
     *
     * @param genericDAO the new genericDAO.
     */
    void setGenericDAO(GenericDAO<T> genericDAO);

    /**
     * Gets the genericDAO.
     */
    GenericDAO<T> getGenericDAO();
}
