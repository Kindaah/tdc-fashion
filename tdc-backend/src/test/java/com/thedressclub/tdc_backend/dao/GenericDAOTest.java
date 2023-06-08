/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.dao;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.thedressclub.tdc_backend.model.GenericModel;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for All DAO Tests.
 *
 * @author Edson Ruiz.
 */
public class GenericDAOTest<T extends GenericModel>
{
    private static final Long ID_ENTITY = 2L;
    private static final String COLUMN_NAME = "columnName";
    private static final String ANOTHER_COLUMN = "anotherColumn";
    private static final Object VALUE = "value";
    private static final String NESTED_COLUMN = COLUMN_NAME + "." + ANOTHER_COLUMN;

    private Class<T> entityClass;

    @Mock
    private EntityManager mockEntityManager;

    @Mock
    private CriteriaBuilder mockCriteriaBuilder;

    @Mock
    private CriteriaQuery<T> mockCriteriaQuery;

    @Mock
    private TypedQuery<T> mockTypedQuery;

    @Mock
    private T mockEntity;

    @Mock
    private Root<T> mockRoot;

    @Mock
    private Predicate mockPredicate;

    @Mock
    private Path<Object> mockPath;

    @InjectMocks
    private GenericDAO<T> instance;

    @Before
    public void setUp()
    {
        instance = new GenericDAO<>(entityClass);
        MockitoAnnotations.initMocks(this);

        when(mockEntityManager.find(entityClass, ID_ENTITY)).thenReturn(mockEntity);
        when(mockEntityManager.merge(mockEntity)).thenReturn(mockEntity);
        when(mockEntityManager.getCriteriaBuilder()).thenReturn(mockCriteriaBuilder);
        when(mockCriteriaBuilder.createQuery(entityClass)).thenReturn(mockCriteriaQuery);
        when(mockCriteriaQuery.from(entityClass)).thenReturn(mockRoot);
        when(mockEntityManager.createQuery(mockCriteriaQuery)).thenReturn(mockTypedQuery);
        when(mockTypedQuery.getResultList()).thenReturn(getEntityList());
    }

    @Test
    public void testSave()
    {
        instance.save(mockEntity);

        verify(mockEntityManager).persist(mockEntity);
    }

    @Test
    public void testFindBy()
    {
        T response = instance.findBy(ID_ENTITY);

        assertThat(response).as("The entity is wrong: ").isEqualTo(mockEntity);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testUpdate()
    {
        instance.update(mockEntity);

        verify(mockEntityManager).merge(mockEntity);
    }

    @Test
    public void testList()
    {
        List<T> responseList = instance.list();
        List<T> expectedList = getEntityList();

        assertThat(responseList.size()).as("List size is wrong").isEqualTo(expectedList.size());
    }

    @Test
    public void testListIsEqualFilter()
    {
        when(mockRoot.get(COLUMN_NAME)).thenReturn(mockPath);
        when(mockPath.in(VALUE)).thenReturn(mockPredicate);

        List<T> responseList = instance.list(singletonMap(COLUMN_NAME, VALUE));
        List<T> expectedList = getEntityList();

        assertThat(responseList.size()).as("List size is wrong").isEqualTo(expectedList.size());
        verify(mockCriteriaQuery).select(mockRoot);
        verify(mockCriteriaQuery).where(mockPredicate);
    }

    @Test
    public void testListIsEqualFilterNestedColumn()
    {
        when(mockRoot.get(COLUMN_NAME)).thenReturn(mockPath);
        when(mockPath.get(ANOTHER_COLUMN)).thenReturn(mockPath);
        when(mockPath.in((Object) singletonList(VALUE))).thenReturn(mockPredicate);

        List<T> responseList = instance.list(singletonMap(NESTED_COLUMN, singletonList(VALUE)));
        List<T> expectedList = getEntityList();

        assertThat(responseList.size()).as("List size is wrong").isEqualTo(expectedList.size());
        verify(mockCriteriaQuery).select(mockRoot);
        verify(mockCriteriaQuery).where(mockPredicate);
    }

    @Test
    public void testDelete()
    {
        instance.delete(mockEntity);

        verify(mockEntityManager).merge(mockEntity);
        verify(mockEntityManager).remove(mockEntity);
    }

    @SuppressWarnings("unchecked")
    private List<T> getEntityList()
    {
        return Arrays.asList((T) new GenericModel(), (T) new GenericModel(), (T) new GenericModel());
    }
}