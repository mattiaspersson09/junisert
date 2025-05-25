/*
 * Copyright (c) 2025-2025 Mattias Persson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.mattiaspersson09.junisert.core.internal.test;

import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.core.NoCacheTestValueService;
import io.github.mattiaspersson09.junisert.core.TestInstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.ValueService;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Unit;
import io.github.mattiaspersson09.junisert.core.internal.test.strategy.TestStrategy;
import io.github.mattiaspersson09.junisert.testunits.setter.BeanAndBuilderStyle;
import io.github.mattiaspersson09.junisert.testunits.setter.BeanStyle;
import io.github.mattiaspersson09.junisert.testunits.setter.BooleanCollisionBeanStyle;
import io.github.mattiaspersson09.junisert.testunits.setter.BooleanTwoAlternatives;
import io.github.mattiaspersson09.junisert.testunits.setter.BuilderStyle;
import io.github.mattiaspersson09.junisert.testunits.setter.MissingSetter;
import io.github.mattiaspersson09.junisert.testunits.setter.NotSettingField;
import io.github.mattiaspersson09.junisert.testunits.setter.PolymorphicSetter;
import io.github.mattiaspersson09.junisert.testunits.setter.RecordStyle;
import io.github.mattiaspersson09.junisert.testunits.setter.TwoButOnlyOneWorking;
import io.github.mattiaspersson09.junisert.testunits.setter.TwoLettersOrLessBeanStyle;
import io.github.mattiaspersson09.junisert.value.common.InterfaceValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.PrimitiveValueGenerator;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HasSettersIntegrationTest {
    private static ValueService valueService;
    private static InstanceCreator instanceCreator;
    private HasSetters hasSetters;

    @BeforeAll
    static void beforeAll() {
        valueService = new NoCacheTestValueService(Arrays.asList(
                new PrimitiveValueGenerator(),
                new InterfaceValueGenerator(),
                new ObjectValueGenerator()
        ));
        instanceCreator = new TestInstanceCreator();
    }

    @BeforeEach
    void setUp() {
        hasSetters = new HasSetters(valueService, instanceCreator);
    }

    @Test
    void givenUnit_whenAllFieldsHaveSetters_andIsAcceptableSetters_thenPassesTest() {
        hasSetters.test(Unit.of(BeanStyle.class));
        hasSetters.test(Unit.of(BuilderStyle.class));
        hasSetters.test(Unit.of(RecordStyle.class));
        hasSetters.test(Unit.of(BeanAndBuilderStyle.class));
        hasSetters.test(Unit.of(TwoLettersOrLessBeanStyle.class));
        hasSetters.test(Unit.of(PolymorphicSetter.class));
    }

    @Test
    void givenUnit_whenConventionIsSetToJavaBean_andSetterIsNotBeanCompliant_thenFailsTest() {
        hasSetters.setTestStrategy(TestStrategy.javaBeanCompliant());

        assertThatThrownBy(() -> hasSetters.test(Unit.of(BuilderStyle.class))).isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> hasSetters.test(Unit.of(RecordStyle.class))).isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void givenUnit_whenTwoSimilarFields_andOneIsBooleanWithIsPrefix_andOverloadingSetters_thenPassesTest() {
        hasSetters.test(Unit.of(BooleanCollisionBeanStyle.class));
    }

    @Test
    void givenUnit_whenTwoAlternativesForBooleanField_thenTestsBoth_andPassesTest() {
        hasSetters.test(Unit.of(BooleanTwoAlternatives.class));
    }

    @Test
    void givenUnit_whenAnyFieldIsMissingSetter_thenFailsTest() {
        assertThatThrownBy(() -> hasSetters.test(Unit.of(MissingSetter.class)))
                .isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> hasSetters.test(Unit.of(TwoButOnlyOneWorking.class)))
                .isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void givenUnit_whenAllFieldsHaveSetters_butSomeSetterIsNotWorking_thenFailsTest() {
        assertThatThrownBy(() -> hasSetters.test(Unit.of(NotSettingField.class)))
                .isInstanceOf(UnitAssertionError.class);
    }
}
