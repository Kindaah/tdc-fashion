/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.controller;

import static com.thedressclub.tdc_backend.controller.OrderController.COMMENT_URL;
import static com.thedressclub.tdc_backend.controller.OrderController.DASHBOARD_URL;
import static com.thedressclub.tdc_backend.controller.OrderController.FINAL_QUOTE_URL;
import static com.thedressclub.tdc_backend.controller.OrderController.INITIAL_QUOTE_URL;
import static com.thedressclub.tdc_backend.controller.OrderController.ORDERS_URL;
import static com.thedressclub.tdc_backend.controller.OrderController.SHIPPING_TYPE;
import static com.thedressclub.tdc_backend.controller.OrderController.STATE_URL;
import static com.thedressclub.tdc_backend.controller.ShippingAddressController.SHIPPING_ADDRESS_URL;
import static com.thedressclub.tdc_backend.model.Order.PENDING_INITIAL_QUOTE_APPROVAL_STATE_ID;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.thedressclub.tdc_backend.business.OrderBusiness;
import com.thedressclub.tdc_backend.controller.config.GenericControllerTest;
import com.thedressclub.tdc_backend.dto.CountDTO;
import com.thedressclub.tdc_backend.model.Order;
import com.thedressclub.tdc_backend.model.Role;
import com.thedressclub.tdc_backend.model.ShippingAddress;
import com.thedressclub.tdc_backend.model.State;
import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.OrderService;
import com.thedressclub.tdc_backend.service.ScheduledService;
import com.thedressclub.tdc_backend.service.ShippingAddressService;
import com.thedressclub.tdc_backend.service.StateService;
import com.thedressclub.tdc_backend.service.UserService;
import com.thedressclub.tdc_backend.util.UserUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Test class for {@link OrderController}.
 *
 * @author Daniel Mejia.
 */
public class OrderControllerTest extends GenericControllerTest<Order>
{
    private static final long SHIPPING_ADDRESS_ID_TEST = 1;
    private static final String REFERENCE_PO_KEY = "referencePo";
    private static final String INITIAL_QUOTE_KEY = "initialQuote";
    private static final String COMMENT_KEY = "comment";
    private static final String FINAL_QUOTE_KEY = "finalQuote";
    private static final String ORIGIN_COUNTRY_KEY = "originCountry";
    private static final String DELIVERY_TIME_KEY = "deliveryTime";
    private static final String REFERENCE_PO_VALUE = "value";
    private static final String INITIAL_QUOTE_VALUE = "123456789.12";
    private static final String COMMENT_VALUE = "comment";
    private static final String FINAL_QUOTE_VALUE = "987654321.98";
    private static final String ORIGIN_COUNTRY_VALUE = "value";
    private static final String DELIVERY_TIME_VALUE = "37";
    private static final String STATE_NAME = "state";
    private static final String PRINCIPAL = "auth|123545678";
    private static final String ROLE_NAME = "CUSTOMER";
    private static final String FIRST_NAME = "full_name";
    private static final String LAST_NAME = "last_name";
    private static final String AUTH_KEY = "auth_key";
    private static final String COMPANY_EMAIL = "company@email.com";
    private static final String PHONE_NUMBER = "33333333";
    private static final String TYPE_TEST_VALUE = "test shipping type";

    private static final String SET_INITIAL_QUOTE_URL = ORDERS_URL + SLASH + OBJECT_ID + INITIAL_QUOTE_URL;
    private static final String SET_COMMENT_URL = ORDERS_URL + SLASH + OBJECT_ID + COMMENT_URL;
    private static final String CHANGE_STATE_URL = ORDERS_URL + SLASH + OBJECT_ID + STATE_URL;
    private static final String SET_FINAL_QUOTE = ORDERS_URL + SLASH + OBJECT_ID + FINAL_QUOTE_URL;;
    private static final String ADD_ORDER_SHIPPING_ADDRESS_URL =
        ORDERS_URL + SLASH + OBJECT_ID  + SHIPPING_ADDRESS_URL + SLASH + SHIPPING_ADDRESS_ID_TEST;

    private Order order;

    private State state;

    private User user;

    private CountDTO countDTO;

    @Mock
    private OrderBusiness mockOrderBusiness;

    @Mock
    private SecurityContext mockSecurityContext;

    @Mock
    private UserService mockUserService;

    @Mock
    private Authentication mockAuthentication;

    @Mock
    private StateService mockStateService;

    @Mock
    private OrderService mockOrderService;

    @Mock
    private State mockState;

    @Mock
    private ScheduledService mockScheduledService;
    
    @Mock
    private ShippingAddressService mockShippingAddressService;

    @InjectMocks
    private OrderController orderController;

    @Before
    public void setUp()
    {
        Role role = new Role();
        role.setId(OBJECT_ID);
        role.setName(ROLE_NAME);
        role.setUsers(Collections.emptySet());

        user = new User();
        user.setId(OBJECT_ID);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setAuthKey(AUTH_KEY);
        user.setCompanyEmail(COMPANY_EMAIL);
        user.setPhoneNumber(PHONE_NUMBER);
        user.setRole(role);

        order = new Order();
        order.setId(OBJECT_ID);
        order.setReferencePo(REFERENCE_PO_VALUE);
        order.setProducts(Collections.emptySet());
        order.setState(null);

        state = new State();
        state.setId(OBJECT_ID);
        state.setName(STATE_NAME);
        state.setFilter(ROLE_NAME);
        state.setSequenceState(OBJECT_ID);
        state.setOrders(Collections.emptySet());

        order.setUser(user);
        order.setState(state);

        countDTO = new CountDTO();

        MockitoAnnotations.initMocks(this);
        init(ORDERS_URL, order, orderController);
        orderController.setGenericService(mockGenericService);

        when(mockAuthentication.getPrincipal()).thenReturn(PRINCIPAL);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockStateService.findById(Order.DEFAULT_STATE_ID)).thenReturn(mockState);
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(order);
        SecurityContextHolder.setContext(mockSecurityContext);
    }

    @After
    public void tearDown()
    {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testAddOrderSuccess()
    throws Exception
    {
        ResultActions result = performAddRequest(order);

        result.andExpect(status().isCreated());
    }

    @Test
    public void testAddOrderFail()
    {
        assertThatThrownBy(() -> performAddRequest(null))
            .hasCause(addException);
    }

    @Test
    public void testGetDashboard()
    throws Exception
    {
        when(UserUtils.getCurrentUser(mockUserService)).thenReturn(user);

        List<State> listToReturn = singletonList(SerializationUtils.clone(state));
        when(mockStateService.getAll()).thenReturn(listToReturn);
        when(mockStateService.filterByRole(user)).thenReturn(listToReturn);

        countDTO.setCount(listToReturn.size());
        countDTO.setEntity(state);
        List<CountDTO> listCounts = singletonList(countDTO);
        when(mockOrderBusiness.getListCountDTO(listToReturn, user)).thenReturn(singletonList(countDTO));

        String url = ORDERS_URL + DASHBOARD_URL;
        ResultActions result = performGetRequest(url);

        result
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(listCounts)));
    }

    @Test
    public void testGetAllOrders()
    throws Exception
    {
        when(UserUtils.getCurrentUser(mockUserService)).thenReturn(user);

        List<Order> listToReturn = singletonList(SerializationUtils.clone(order));
        when(mockOrderBusiness.filterOrdersByUser(listToReturn, user)).thenReturn(listToReturn);
        when(mockOrderService.filterByRole(user)).thenReturn(listToReturn);

        countDTO.setCount(listToReturn.size());
        countDTO.setEntity(order);
        List<CountDTO> listCounts = singletonList(countDTO);

        when(mockOrderBusiness.getListCountDTO(listToReturn)).thenReturn(singletonList(countDTO));
        ResultActions result = performGetAllRequest(listToReturn);

        result
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(listCounts)));
    }

    @Test
    public void testFindByIdOrderFound()
    throws Exception
    {
        ResultActions result = performFindByIdRequest(order);
        result
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(order)));
    }

    @Test
    public void testFindByIdOrderNotFound()
    {
        assertThatThrownBy(() -> performFindByIdRequest(null))
            .hasCause(noFoundException);
    }

    @Test
    public void testUpdateOrderSuccess()
    throws Exception
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(order);
        when(mockGenericService.update(order)).thenReturn(order);

        String url = ORDERS_URL + SLASH + OBJECT_ID;
        Map<String, String> params = singletonMap(REFERENCE_PO_KEY, REFERENCE_PO_VALUE);
        ResultActions result = performFormRequest(url, params);

        result
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(order)));
    }

    @Test
    public void testUpdateOrderNotFound()
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(null);

        String url = ORDERS_URL + SLASH + OBJECT_ID;
        Map<String, String> params = singletonMap(REFERENCE_PO_KEY, REFERENCE_PO_VALUE);

        assertThatThrownBy(() -> performFormRequest(url, params))
            .hasCause(noFoundException);
    }

    @Test
    public void testUpdateOrderBadRequest()
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(order);
        when(mockGenericService.update(order)).thenReturn(null);

        String url = ORDERS_URL + SLASH + OBJECT_ID;
        Map<String, String> params = singletonMap(REFERENCE_PO_KEY, REFERENCE_PO_VALUE);

        assertThatThrownBy(() -> performFormRequest(url, params))
            .hasCause(updatedException);
    }

    @Test
    public void testUpdateOrderInitialQuoteSuccess()
    throws Exception
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(order);
        when(mockGenericService.update(order)).thenReturn(order);

        Map<String, String> params =
            singletonMap(INITIAL_QUOTE_KEY, INITIAL_QUOTE_VALUE);
        ResultActions result = performFormRequest(SET_INITIAL_QUOTE_URL, params);

        result
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(order)));
    }

    @Test
    public void testUpdateOrderInitialQuoteNotFound()
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(null);

        Map<String, String> params =
            singletonMap(INITIAL_QUOTE_KEY, INITIAL_QUOTE_VALUE);

        assertThatThrownBy(() -> performFormRequest(SET_INITIAL_QUOTE_URL, params))
            .hasCause(noFoundException);
    }

    @Test
    public void testUpdateOrderInitialQuoteBadRequest()
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(order);
        when(mockGenericService.update(order)).thenReturn(null);

        Map<String, String> params =
            singletonMap(INITIAL_QUOTE_KEY, INITIAL_QUOTE_VALUE);

        assertThatThrownBy(() -> performFormRequest(SET_INITIAL_QUOTE_URL, params))
            .hasCause(updatedException);
    }

    @Test
    public void testUpdateOrderCommentSuccess()
    throws Exception
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(order);
        when(mockGenericService.update(order)).thenReturn(order);

        Map<String, String> params = singletonMap(COMMENT_KEY, COMMENT_VALUE);
        ResultActions result = performFormRequest(SET_COMMENT_URL, params);

        result
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(order)));
    }

    @Test
    public void testUpdateOrderCommentNotFound()
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(null);

        Map<String, String> params = singletonMap(COMMENT_KEY, COMMENT_VALUE);

        assertThatThrownBy(() -> performFormRequest(SET_COMMENT_URL, params))
            .hasCause(noFoundException);
    }

    @Test
    public void testUpdateOrderCommentBadRequest()
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(order);
        when(mockGenericService.update(order)).thenReturn(null);

        Map<String, String> params = singletonMap(COMMENT_KEY, COMMENT_VALUE);

        assertThatThrownBy(() -> performFormRequest(SET_COMMENT_URL, params))
            .hasCause(updatedException);
    }

    @Test
    public void testUpdateOrderStateSuccess()
    throws Exception
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(order);
        when(mockStateService.findById(OBJECT_ID)).thenReturn(state);
        order.setState(state);
        when(mockGenericService.update(order)).thenReturn(order);

        ResultActions result = performPatchRequest(CHANGE_STATE_URL + SLASH + OBJECT_ID);

        result
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(order)));
    }

    @Test
    public void testUpdateOrderStateToExpire()
    throws Exception
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(order);
        state.setId(PENDING_INITIAL_QUOTE_APPROVAL_STATE_ID);
        when(mockStateService.findById(PENDING_INITIAL_QUOTE_APPROVAL_STATE_ID)).thenReturn(state);
        order.setState(state);
        when(mockGenericService.update(order)).thenReturn(order);

        ResultActions result = performPatchRequest(CHANGE_STATE_URL + SLASH + PENDING_INITIAL_QUOTE_APPROVAL_STATE_ID);

        result
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(order)));

        verify(mockScheduledService).expireOrder(OBJECT_ID);
    }

    @Test
    public void testUpdateOrderStateNotFound()
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(null);

        assertThatThrownBy(() -> performPatchRequest(CHANGE_STATE_URL + SLASH + OBJECT_ID))
            .hasCause(noFoundException);
    }

    @Test
    public void testUpdateOrderStateBadRequest()
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(order);
        when(mockGenericService.update(order)).thenReturn(null);

        assertThatThrownBy(() -> performPatchRequest(CHANGE_STATE_URL + SLASH + OBJECT_ID))
            .hasCause(updatedException);
    }

    @Test
    public void testUpdateOrderFinalQuoteSuccess()
    throws Exception
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(order);
        when(mockGenericService.update(order)).thenReturn(order);

        Map<String, String> params = new HashMap<>();
        params.put(FINAL_QUOTE_KEY, FINAL_QUOTE_VALUE);
        params.put(ORIGIN_COUNTRY_KEY, ORIGIN_COUNTRY_VALUE);
        params.put(DELIVERY_TIME_KEY, DELIVERY_TIME_VALUE);
        ResultActions result = performFormRequest(SET_FINAL_QUOTE, params);

        result
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(order)));
    }

    @Test
    public void testUpdateOrderFinalQuoteNotFound()
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(null);

        Map<String, String> params = new HashMap<>();
        params.put(FINAL_QUOTE_KEY, FINAL_QUOTE_VALUE);
        params.put(ORIGIN_COUNTRY_KEY, ORIGIN_COUNTRY_VALUE);
        params.put(DELIVERY_TIME_KEY, DELIVERY_TIME_VALUE);

        assertThatThrownBy(() -> performFormRequest(SET_FINAL_QUOTE, params))
            .hasCause(noFoundException);
    }

    @Test
    public void testUpdateOrderFinalQuoteBadRequest()
    {
        when(mockGenericService.findById(OBJECT_ID)).thenReturn(order);
        when(mockGenericService.update(order)).thenReturn(null);

        Map<String, String> params = new HashMap<>();
        params.put(FINAL_QUOTE_KEY, FINAL_QUOTE_VALUE);
        params.put(ORIGIN_COUNTRY_KEY, ORIGIN_COUNTRY_VALUE);
        params.put(DELIVERY_TIME_KEY, DELIVERY_TIME_VALUE);

        assertThatThrownBy(() -> performFormRequest(SET_FINAL_QUOTE, params))
            .hasCause(updatedException);
    }

    @Test
    public void testAddOrderShippingAddressSuccess()
    throws Exception
    {
        ShippingAddress testShippingAddress = new ShippingAddress();
        testShippingAddress.setId(SHIPPING_ADDRESS_ID_TEST);

        Order copyOrder = SerializationUtils.clone(order);
        copyOrder.getShippingAddresses().put(TYPE_TEST_VALUE, testShippingAddress);

        when(mockShippingAddressService.findById(SHIPPING_ADDRESS_ID_TEST)).thenReturn(testShippingAddress);
        when(mockOrderService.update(order)).thenReturn(copyOrder);

        Map<String, String> params = singletonMap(SHIPPING_TYPE, TYPE_TEST_VALUE);
        ResultActions result = performFormRequest(ADD_ORDER_SHIPPING_ADDRESS_URL, params);

        result
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(copyOrder)));
    }

    @Test
    public void testUpdateRelationShippingAddressNotFound()
    {
        when(mockShippingAddressService.findById(SHIPPING_ADDRESS_ID_TEST)).thenReturn(null);

        Map<String, String> params = singletonMap(SHIPPING_TYPE, TYPE_TEST_VALUE);

        assertThatThrownBy(() -> performFormRequest(ADD_ORDER_SHIPPING_ADDRESS_URL, params))
            .hasCause(addException);
    }
}