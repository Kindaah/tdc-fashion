/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.dao;

import static java.util.Arrays.stream;

import com.thedressclub.tdc_backend.model.GenericModel;
import com.thedressclub.tdc_backend.util.Utils;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contains the data access methods for any type object.
 *
 * @param <T> the object type.
 *
 * @author Edson Ruiz.
 */
@Transactional
public class GenericDAO<T extends GenericModel> implements IGenericDAO<T>
{
    private static final Logger LOGGER = Logger.getLogger(GenericDAO.class.getName());

    private static final String RESOURCE_SAVED_KEY = "resource_saved";
    private static final String RESOURCE_UPDATED_KEY = "resource_updated";
    private static final String RESOURCE_SEARCH_KEY = "resource_search";
    private static final String RESOURCE_SEARCH_ALL = "resource_search_all";
    private static final String RESOURCE_FILTERED = "resource_filtered";
    private static final String RESOURCE_DELETED_KEY = "resource_deleted";
    private static final String BREAK_POINT = "\\.";

    Class<T> entityClass;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Constructor
     *
     * @param entityClass for the specific model.
     */
    GenericDAO(Class<T> entityClass)
    {
        this.entityClass = entityClass;
    }

    /** (non-Javadoc) @see {@link IGenericDAO} */
    @Override
    public void save(T object)
    {
        LOGGER.log(Level.INFO, Utils.getMessage(RESOURCE_SAVED_KEY), entityClass);

        entityManager.persist(object);
    }

    /** (non-Javadoc) @see {@link IGenericDAO} */
    @Override
    public T findBy(Long objectId)
    {
        LOGGER.log(Level.INFO, Utils.getMessage(RESOURCE_SEARCH_KEY), entityClass);

        return entityManager.find(entityClass, objectId);
    }

    /** (non-Javadoc) @see {@link IGenericDAO} */
    @Override
    public T update(T object)
    {
        LOGGER.log(Level.INFO, Utils.getMessage(RESOURCE_UPDATED_KEY), entityClass);

        return entityManager.merge(object);
    }

    /** (non-Javadoc) @see {@link IGenericDAO} */
    @Override
    public void delete(T object)
    {
        LOGGER.log(Level.INFO, Utils.getMessage(RESOURCE_DELETED_KEY), object.getClass());

        entityManager.remove(entityManager.merge(object));
    }

    /** (non-Javadoc) @see {@link IGenericDAO} */
    @Override
    public List<T> list()
    {
        LOGGER.log(Level.INFO, Utils.getMessage(RESOURCE_SEARCH_ALL));

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }

    /**
     * Gets a list of the {@link T extends GenericModel} stored in the database.
     *
     * @param equalFilters The filters that the list is going to be filtered by.
     * Use the keys as string by columns in model separated by dot in nested params.
     * Use the value as object or list of object.
     *
     * @return list of {@link T extends GenericModel}.
     */
    public List<T> list(Map<String, Object> equalFilters)
    {
        LOGGER.log(Level.INFO, Utils.getMessage(RESOURCE_FILTERED));

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);
        equalFilters.forEach((columnDefinition, values) ->
        {
            Path<T> columnPath =
                stream(columnDefinition.split(BREAK_POINT))
                    .sequential()
                    .reduce((Path<T>) root, Path::get, (first, second) -> first);

            Predicate condition = columnPath.in(values);
            criteriaQuery.where(condition);
        });
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }
}