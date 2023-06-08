/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.util;

import static java.lang.reflect.Modifier.isPrivate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Constructor;
import org.junit.Test;

/**
 * Test class for {@link Utils}.
 *
 * @author Daniel Mejia.
 */
public class UtilsTest
{
    private static final String TEST_KEY = "test_message";
    private static final String TEST_MESSAGE = "Test message";

    @Test
    public void testPrivateConstructor()
    throws NoSuchMethodException
    {
        Constructor<Utils> constructor = Utils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        boolean isPrivate = isPrivate(constructor.getModifiers());

        assertThat(isPrivate).isTrue();
        assertThatThrownBy(constructor::newInstance)
            .hasCauseInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void getMessage()
    {
        String response = Utils.getMessage(TEST_KEY);
        assertThat(response)
            .as("The message is wrong: ")
            .isEqualTo(TEST_MESSAGE);
    }
}