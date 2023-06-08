/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.service;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.thedressclub.tdc_backend.dao.GenericDAO;
import com.thedressclub.tdc_backend.model.GenericModel;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for All Service Tests.
 *
 * @author Edson Ruiz Ramirez.
 */
public class GenericServiceTest<T extends GenericModel>
{
    private static final Long OBJECT_ID = 1L;

    @Mock
    protected GenericDAO<T> mockGenericDAO;

    @Mock
    private T mockObject;

    @InjectMocks
    private GenericService<T> instance;

    @Before
    public void setUp()
    {
        instance = new GenericService<>();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAdd()
    {
        T response = instance.add(mockObject);

        assertThat(response).as("The object is wrong: ").isEqualTo(mockObject);
        verify(mockGenericDAO).save(mockObject);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddIsException()
    {
        instance.setGenericDAO(null);

        assertThat(instance.add(mockObject)).as("The object is not null").isNull();
    }

    @Test
    public void testFindObjectById()
    {
        when(mockGenericDAO.findBy(OBJECT_ID)).thenReturn(mockObject);

        T objectFounded = instance.findById(OBJECT_ID);

        assertThat(objectFounded).as("The objected founded is wrong: ").isEqualTo(mockObject);
    }

    @Test
    public void testGetAll()
    {
        when(mockGenericDAO.list()).thenReturn(generateList());
        List<T> response = instance.getAll();

        assertThat(response.size()).as("The list size is incorrect").isEqualTo(generateList()
            .size());
    }

    @Test
    public void testFilterBy()
    {
        List<T> generatedList = generateList();
        when(mockGenericDAO.list(anyMap())).thenReturn(generatedList);

        List<T> response = instance.filterBy(anyMap());

        assertThat(response.size()).as("The size wrong value").isEqualTo(generatedList.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testUpdate()
    {
        instance.update(mockObject);

        verify(mockGenericDAO).update(mockObject);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testUpdateIsException()
    {
        instance.setGenericDAO(null);

        assertThat(instance.update(mockObject)).as("The object is not null").isNull();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDelete()
    {
        instance.delete(mockObject);

        verify(mockGenericDAO, times(1)).delete(mockObject);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSetGenericDAO()
    {
        instance.setGenericDAO(mockGenericDAO);

        assertThat(instance.getGenericDAO()).as("The generic DAO is wrong: ").isEqualTo(
            mockGenericDAO);
    }

    @SuppressWarnings("unchecked")
    private List<T> generateList()
    {
        return Arrays.asList((T) new GenericModel(), (T) new GenericModel(), (T) new GenericModel());
    }
}