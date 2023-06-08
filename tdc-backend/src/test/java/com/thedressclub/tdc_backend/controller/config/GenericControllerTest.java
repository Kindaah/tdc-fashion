/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thedressclub.tdc_backend.model.GenericModel;
import com.thedressclub.tdc_backend.service.GenericService;
import java.util.List;
import java.util.Map;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for {@link GenericController}.
 *
 * @author Edson Ruiz Ramirez.
 * @author Daniel Mejia.
 */
public abstract class GenericControllerTest<T extends GenericModel>
{
    protected static final Long OBJECT_ID = 1L;
    protected static final String SLASH = "/";

    private String endpointBase;

    protected MockMvc mockMvc;

    private T object;

    protected RestException addException;

    protected RestException updatedException;

    protected RestException noFoundException;

    @Mock
    protected GenericService<T> mockGenericService;

    protected void init(String endpointBase, T object, GenericController<T> genericController)
    {
        this.endpointBase = endpointBase;
        this.object = object;
        mockMvc = MockMvcBuilders.standaloneSetup(genericController).build();
        addException = new RestException(GenericController.NOT_CREATED_KEY);
        updatedException = new RestException(GenericController.NOT_UPDATED_KEY);
        noFoundException = new RestException(GenericController.NOT_FOUND_KEY);
    }

    protected ResultActions performAddRequest(T objectToReturn)
    throws Exception
    {
        when(mockGenericService.add(any())).thenReturn(objectToReturn);

        return mockMvc.perform(post(endpointBase)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(object)));
    }

    protected ResultActions performGetAllRequest(List<T> listToReturn)
    throws Exception
    {
        when(mockGenericService.getAll()).thenReturn(listToReturn);

        return performGetRequest(endpointBase);
    }

    protected ResultActions performFindByIdRequest(T objectToReturn)
    throws Exception
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(objectToReturn);

        return mockMvc.perform(get(endpointBase + SLASH + OBJECT_ID)
            .contentType(MediaType.APPLICATION_JSON));
    }

    protected ResultActions performUpdateRequest(T objectToReturn, T objectAfterUpdate)
    throws Exception
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(objectToReturn);
        when(mockGenericService.update(objectToReturn)).thenReturn(objectAfterUpdate);

        return mockMvc.perform(put(endpointBase + SLASH + OBJECT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(object)));
    }

    protected ResultActions performDeleteRequest(T objectToReturn)
    throws Exception
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(objectToReturn);

        return mockMvc.perform(delete(endpointBase + SLASH + OBJECT_ID)
            .contentType(MediaType.APPLICATION_JSON));
    }

    protected ResultActions performGetRequest(String URL)
    throws Exception
    {
        return mockMvc.perform(get(URL)
            .contentType(MediaType.APPLICATION_JSON));
    }

    protected ResultActions performFormRequest(String URL, Map<String, String> params)
    throws Exception
    {
        MockHttpServletRequestBuilder requestBuilder =
            patch(URL).contentType(MediaType.APPLICATION_FORM_URLENCODED);

        params.forEach(requestBuilder::param);

        return mockMvc.perform(requestBuilder);
    }

    protected ResultActions performFormRequestPost(String URL, Map<String, String> params)
    throws Exception
    {
        MockHttpServletRequestBuilder requestBuilder =
            post(URL).contentType(MediaType.APPLICATION_FORM_URLENCODED);

        params.forEach(requestBuilder::param);

        return mockMvc.perform(requestBuilder);
    }

    protected ResultActions performMultipartRequest(String URL, List<MockMultipartFile> params)
    throws Exception
    {
        MockMultipartHttpServletRequestBuilder requestBuilder = multipart(URL);
        params.forEach(requestBuilder::file);

        return mockMvc.perform(requestBuilder);
    }

    protected ResultActions performPatchRequest(String URL)
    throws Exception
    {
        return mockMvc.perform(patch(URL)
            .contentType(MediaType.APPLICATION_JSON));
    }

    protected ResultActions performPutRequest(String URL, T object)
    throws Exception
    {
        return mockMvc.perform(put(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(object)));
    }

    protected String asJsonString(final Object obj)
    throws Exception
    {
        return new ObjectMapper().writeValueAsString(obj);
    }
}