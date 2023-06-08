/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.dao;

import com.thedressclub.tdc_backend.model.GenericModel;
import java.util.List;

/**
 * Interface for any Type DAO.
 *
 * @param <T> the object type.
 *
 * @author Edson Ruiz.
 */
public interface IGenericDAO<T extends GenericModel>
{
    /**
     * Saves the specified object.
     *
     * @param object the object to be saved.
     */
    void save(T object);

    /**
     * Find a object by id.
     *
     * @param id the object id.
     */
    T findBy(Long id);

    /**
     * Updates the specified object.
     *
     * @param object the object to be updated.
     *
     * @return the updated entity.
     */
    T update(T object);

    /**
     * Gets a list of the objects stored on this object.
     *
     * @return the list of objects.
     */
    List<T> list();

    /**
     * Deletes the specified object.
     *
     * @param object to be deleted.
     */
    void delete(T object);
}
