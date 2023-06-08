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
import com.thedressclub.tdc_backend.dao.IGenericDAO;
import com.thedressclub.tdc_backend.model.GenericModel;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Services generic class.
 *
 * @author Edson Ruiz Ramirez.
 */
public class GenericService<T extends GenericModel> implements IGenericService<T>
{
    private static final Logger LOGGER = Logger.getLogger(GenericService.class.getName());

    @Autowired
    private GenericDAO<T> genericDAO;

    /** (non-Javadoc) @see {@link IGenericService} */
    @Override
    public T add(T modelObject)
    {
        try
        {
            genericDAO.save(modelObject);

            return modelObject;
        }
        catch (Exception exception)
        {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);

            return null;
        }
    }

    /** (non-Javadoc) @see {@link IGenericService} */
    @Override
    public T findById(Long objectId)
    {
        return genericDAO.findBy(objectId);
    }

    /** (non-Javadoc) @see {@link IGenericService} */
    @Override
    public List<T> getAll()
    {
        return genericDAO.list();
    }

    /** (non-Javadoc) @see {@link IGenericService}*/
    @Override
    public List<T> filterBy(Map<String, Object> equalFilters)
    {
        return genericDAO.list(equalFilters);
    }

    /** (non-Javadoc) @see {@link IGenericService} */
    @Override
    public T update(T modelObject)
    {
        try
        {
            return genericDAO.update(modelObject);
        }
        catch (Exception exception)
        {
            LOGGER.log(Level.SEVERE, exception.toString(), exception);

            return null;
        }
    }

    /** (non-Javadoc) @see {@link IGenericService} */
    @Override
    public void delete(T object)
    {
        genericDAO.delete(object);
    }

    /** (non-Javadoc) @see {@link IGenericDAO} */
    @Override
    public void setGenericDAO(GenericDAO<T> genericDAO)
    {
        this.genericDAO = genericDAO;
    }

    /** (non-Javadoc) @see {@link IGenericDAO} */
    @Override
    public GenericDAO<T> getGenericDAO()
    {
        return genericDAO;
    }
}
