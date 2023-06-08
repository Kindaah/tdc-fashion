/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.model;

import static org.junit.Assert.assertEquals;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoClassFilter;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.rule.impl.BusinessKeyMustExistRule;
import com.openpojo.validation.rule.impl.EqualsAndHashCodeMatchRule;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.BusinessIdentityTester;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SerializableTester;
import com.openpojo.validation.test.impl.SetterTester;
import com.openpojo.validation.test.impl.ToStringTester;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for any model object.
 *
 * @author Edson Ruiz.
 */
public class GenericModelTest
{
    private static final String PACKAGE_EXPECTED = "com.thedressclub.tdc_backend.model";
    private static final int EXPECTED_COUNT = 13;
    private static final String MODEL_PACKAGE = GenericModel.getModelPackageName();

    private PojoClassFilter filterModels;

    @Before
    public void setUp()
    {
        filterModels = model ->
        {
            boolean isTestModels = model.getName().endsWith("Test");

            return !isTestModels;
        };
    }

    @Test
    public void testGetModelPackageName()
    {
        String packageName = GenericModel.getModelPackageName();
        assertEquals("The model package name is wrong: ", PACKAGE_EXPECTED, packageName);
    }

    @Test
    public void testEnsureExpectedModelCount()
    {
        List<PojoClass> models = PojoClassFactory.getPojoClasses(MODEL_PACKAGE, filterModels);
        Affirm.affirmEquals("Models count is wrong: ", EXPECTED_COUNT, models.size());
    }

    @Test
    public void testModelStructureAndBehavior()
    {
        Validator validator =
            ValidatorBuilder.create()
                .with(new GetterMustExistRule())
                .with(new SetterMustExistRule())
                .with(new BusinessKeyMustExistRule())
                .with(new EqualsAndHashCodeMatchRule())
                .with(new SerializableTester())
                .with(new SetterTester())
                .with(new GetterTester())
                .with(new ToStringTester())
                .with(new BusinessIdentityTester())
                .build();

        validator.validate(MODEL_PACKAGE, filterModels);
    }
}