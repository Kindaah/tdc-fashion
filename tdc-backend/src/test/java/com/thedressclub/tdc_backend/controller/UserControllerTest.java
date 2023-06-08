/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club
 */

package com.thedressclub.tdc_backend.controller;

import static com.thedressclub.tdc_backend.controller.UserController.AUTH_KEY_PARAM;
import static com.thedressclub.tdc_backend.controller.UserController.PASS_CHANGE_ERROR_KEY;
import static com.thedressclub.tdc_backend.controller.UserController.PASS_KEY_PARAM;
import static com.thedressclub.tdc_backend.controller.UserController.REGISTER_TOKEN_KEY;
import static com.thedressclub.tdc_backend.controller.UserController.SPLIT_PARAM_PASS;
import static com.thedressclub.tdc_backend.controller.UserController.USERS_URL;
import static com.thedressclub.tdc_backend.controller.UserController.USER_ROLE_PARAM;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.thedressclub.tdc_backend.controller.config.GenericControllerTest;
import com.thedressclub.tdc_backend.controller.config.RestException;
import com.thedressclub.tdc_backend.model.Role;
import com.thedressclub.tdc_backend.model.User;
import com.thedressclub.tdc_backend.service.RoleService;
import com.thedressclub.tdc_backend.service.UserService;
import com.thedressclub.tdc_backend.service.external.IAuthService;
import com.thedressclub.tdc_backend.service.external.IMailSenderService;
import com.thedressclub.tdc_backend.util.AesUtil;
import freemarker.template.TemplateException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Test class for {@link UserController}.
 *
 * @author Daniel Mejia.
 */
public class UserControllerTest extends GenericControllerTest<User>
{
    private static final String TOKEN_URL = "/token";
    private static final String FIRST_NAME = "full_name";
    private static final String LAST_NAME = "last_name";
    private static final String AUTH_KEY = "auth_key";
    private static final String COMPANY_EMAIL = "company@email.com";
    private static final String PHONE_NUMBER = "33333333";
    private static final String TEST_APP = "APP";
    private static final String TEST_REGISTER_KEY = "register";
    private static final String REGISTER_HEADER = "registerToken";
    private static final String TEST_TOKEN = "testToken";
    private static final String TEST_ANOTHER_APP = "anotherApp";
    private static final String SHARED_KEY_HEADER = "sharedKey";
    private static final String SUBJECT_HEADER = "subject";
    private static final String TEST_SHARED_KEY = "dsaha2HfadLsdsSXypKdsaKFi+fdsfzNgAIGm7Ak5oHIvVShQG9AGY==";
    private static final String TEST_SUBJECT = "subject test";
    private static final String REQUEST_VALUE = "test values request";
    private static final String TEST_NEW_PASSWORD = "salt::iv::pass";
    private static final String WRONG_PASS = "cGFzc3dvcmQ=";
    private static final String TEST_PASS_DECRYPTED = "pass";

    private User user;

    @Mock
    private Role mockRole;

    @Mock
    private RoleService mockRoleService;

    @Mock
    private UserService mockUserService;

    @Mock
    private JwtParser mockJwtParser;

    @Mock
    private Jws<Claims> mockJwt;

    @Mock
    private Claims mockClaims;

    @Mock
    private Environment mockEnvironment;

    @Mock
    private JwtBuilder mockJwtBuilder;

    @Mock
    private IMailSenderService mockIEmailService;

    @Mock
    private AesUtil mockAesUtil;

    @Mock
    private IAuthService mockAuthService;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp()
    throws IOException, TemplateException
    {
        user = new User();
        user.setId(OBJECT_ID);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setAuthKey(AUTH_KEY);
        user.setCompanyEmail(COMPANY_EMAIL);
        user.setPhoneNumber(PHONE_NUMBER);
        user.setRole(null);

        MockitoAnnotations.initMocks(this);
        init(USERS_URL, user, userController);
        userController.setGenericService(mockGenericService);

        when(mockRoleService.findById(Role.CUSTOMER_ROLE)).thenReturn(mockRole);
        when(mockJwtParser.setSigningKey((byte[]) any())).thenReturn(mockJwtParser);
        when(mockJwtParser.parseClaimsJws(anyString())).thenReturn(mockJwt);
        when(mockJwt.getBody()).thenReturn(mockClaims);
        when(mockClaims.getSubject()).thenReturn(TEST_APP);
        when(mockEnvironment.getProperty(UserController.APP_ALLOWED)).thenReturn(TEST_APP);
        when(mockEnvironment.getProperty(UserController.REGISTER_KEY)).thenReturn(TEST_REGISTER_KEY);
        when(mockGenericService.add(any())).thenReturn(user);
        doNothing().when(mockIEmailService).sendCompleteUserEmail(user);
    }

    @Test
    public void testGetRegisterToken()
    throws Exception
    {
        SecretKey key = Keys.hmacShaKeyFor(TEST_SHARED_KEY.getBytes(StandardCharsets.UTF_8));

        when(mockJwtBuilder.setSubject(TEST_SUBJECT)).thenReturn(mockJwtBuilder);
        when(mockJwtBuilder.signWith(key, SignatureAlgorithm.HS256)).thenReturn(mockJwtBuilder);
        when(mockJwtBuilder.compact()).thenReturn(TEST_TOKEN);

        ResultActions result =
            mockMvc
                .perform(get(USERS_URL + TOKEN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(SHARED_KEY_HEADER, TEST_SHARED_KEY)
                .header(SUBJECT_HEADER, TEST_SUBJECT));

        Map<String, String> expectedResponse = Collections.singletonMap(REGISTER_TOKEN_KEY, TEST_TOKEN);
        result
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(expectedResponse)));
    }

    @Test
    public void testAddUserSuccess()
    throws Exception
    {
        ResultActions result =
            mockMvc
                .perform(post(USERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(REGISTER_HEADER, TEST_TOKEN)
                .content(asJsonString(user)));

        result
            .andExpect(status().isCreated())
            .andExpect(content().string(asJsonString(user)));

        verify(mockIEmailService).sendCompleteUserEmail(user);
    }

    @Test
    public void testAddUserUnauthorized()
    throws Exception
    {
        when(mockEnvironment.getProperty(UserController.APP_ALLOWED)).thenReturn(TEST_ANOTHER_APP);

        ResultActions result = mockMvc.perform(post(USERS_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .header(REGISTER_HEADER, TEST_TOKEN)
            .content(asJsonString(user)));

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetAllUsersWithFilters()
    throws Exception
    {
        List<User> listToReturn = singletonList(user);
        when(mockUserService.filterBy(singletonList(REQUEST_VALUE))).thenReturn(singletonList(user));

        ResultActions result =
            mockMvc
                .perform(get(USERS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param(USER_ROLE_PARAM, REQUEST_VALUE));

        result
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(listToReturn)));
    }

    @Test
    public void testGetAllUsersWithoutFilters()
    throws Exception
    {
        List<User> listToReturn = singletonList(user);

        ResultActions result = performGetAllRequest(listToReturn);

        result
            .andExpect(status().isOk())
            .andExpect(content().string(asJsonString(listToReturn)));
    }

    @Test
    public void testFindByIdUserFound()
    throws Exception
    {
        ResultActions result = performFindByIdRequest(user);
        result.andExpect(status().isOk()).andExpect(content().string(asJsonString(user)));
    }

    @Test
    public void testFindByIdUserNotFound()
    {
        assertThatThrownBy(() -> performFindByIdRequest(null))
            .hasCause(noFoundException);
    }

    @Test
    public void testFindByAuthKeyUserFound()
    throws Exception
    {
        when(mockUserService.findByAuthKey(AUTH_KEY)).thenReturn(user);

        ResultActions result = mockMvc.perform(get(USERS_URL + SLASH + AUTH_KEY_PARAM + SLASH + AUTH_KEY)
            .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk()).andExpect(content().string(asJsonString(user)));
    }

    @Test
    public void testFindByAuthKeyNotFound()
    {
        when(mockUserService.findByAuthKey(AUTH_KEY)).thenReturn(null);

        assertThatThrownBy(() -> mockMvc.perform(get(USERS_URL + SLASH + AUTH_KEY_PARAM + SLASH + AUTH_KEY)
            .contentType(MediaType.APPLICATION_JSON)))
            .hasCause(noFoundException);
    }

    @Test
    public void testUpdateUserSuccess()
    throws Exception
    {
        ResultActions result = performUpdateRequest(user, user);
        result.andExpect(status().isOk()).andExpect(content().string(asJsonString(user)));
    }

    @Test
    public void testUpdateUserNotFound()
    {
        assertThatThrownBy(() -> performUpdateRequest(null, null))
            .hasCause(noFoundException);
    }

    @Test
    public void testUpdateUserBadRequest()
    {
        assertThatThrownBy(() -> performUpdateRequest(user, null))
            .hasCause(updatedException);
    }

    @Test
    public void testDeleteUserSuccess()
    throws Exception
    {
        ResultActions result = performDeleteRequest(user);
        result.andExpect(status().isOk()).andExpect(content().string(asJsonString(user)));
    }

    @Test
    public void testDeleteUserFail()
    {
        assertThatThrownBy(() -> performDeleteRequest(null))
            .hasCause(noFoundException);
    }

    @Test
    public void testChangePasswordSuccess()
    throws Exception
    {
        byte[] passEncode = Base64.getEncoder().encode(TEST_NEW_PASSWORD.getBytes());
        String[] testPassDecode = TEST_NEW_PASSWORD.split(SPLIT_PARAM_PASS);

        when(mockUserService.findByAuthKey(AUTH_KEY)).thenReturn(user);
        when(mockAesUtil.decrypt(testPassDecode[0], testPassDecode[1], testPassDecode[2]))
            .thenReturn(TEST_PASS_DECRYPTED);
        when(mockAuthService.changePassword(AUTH_KEY, TEST_PASS_DECRYPTED)).thenReturn(true);

        ResultActions result = mockMvc.perform(patch(USERS_URL + SLASH + AUTH_KEY)
            .param(PASS_KEY_PARAM, new String(passEncode)));

        result
            .andExpect(status().isOk());
    }

    @Test
    public void testChangePasswordFailChanged()
    {
        byte[] passEncode = Base64.getEncoder().encode(TEST_NEW_PASSWORD.getBytes());
        String[] testPassDecode = TEST_NEW_PASSWORD.split(SPLIT_PARAM_PASS);

        when(mockUserService.findByAuthKey(AUTH_KEY)).thenReturn(user);
        when(mockAesUtil.decrypt(testPassDecode[0], testPassDecode[1], testPassDecode[2]))
            .thenReturn(TEST_PASS_DECRYPTED);
        when(mockAuthService.changePassword(AUTH_KEY, TEST_PASS_DECRYPTED)).thenReturn(false);

        assertThatThrownBy(() ->
            mockMvc.perform(patch(USERS_URL + SLASH + AUTH_KEY).param(PASS_KEY_PARAM, new String(passEncode))))
            .hasCause(new RestException(EXPECTATION_FAILED, PASS_CHANGE_ERROR_KEY));
    }

    @Test
    public void testChangePasswordFailRequest()
    {
        assertThatThrownBy(() ->
            mockMvc.perform(patch(USERS_URL + SLASH + AUTH_KEY).param(PASS_KEY_PARAM, WRONG_PASS)))
            .hasCause(new RestException(EXPECTATION_FAILED, PASS_CHANGE_ERROR_KEY));
    }
}