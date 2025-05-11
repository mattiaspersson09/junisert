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
import io.github.mattiaspersson09.junisert.api.internal.service.ValueService;
import io.github.mattiaspersson09.junisert.core.NoCacheTestValueService;
import io.github.mattiaspersson09.junisert.core.internal.convention.Convention;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Unit;
import io.github.mattiaspersson09.junisert.testunits.getter.BeanAndRecordStyle;
import io.github.mattiaspersson09.junisert.testunits.getter.BeanStyle;
import io.github.mattiaspersson09.junisert.testunits.getter.BooleanBeanStyle;
import io.github.mattiaspersson09.junisert.testunits.getter.BooleanRecordStyle;
import io.github.mattiaspersson09.junisert.testunits.getter.BooleanTwoAlternatives;
import io.github.mattiaspersson09.junisert.testunits.getter.MissingGetter;
import io.github.mattiaspersson09.junisert.testunits.getter.NotGettingField;
import io.github.mattiaspersson09.junisert.testunits.getter.PolymorphicGetter;
import io.github.mattiaspersson09.junisert.testunits.getter.RecordStyle;
import io.github.mattiaspersson09.junisert.testunits.getter.TwoButOnlyOneWorking;
import io.github.mattiaspersson09.junisert.testunits.getter.TwoLettersOrLessBeanStyle;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HasGettersIntegrationTest {
    private static ValueService valueService;
    private HasGetters hasGetters;

    @BeforeAll
    static void beforeAll() {
        valueService = NoCacheTestValueService.withAllValueGenerators();
    }

    @BeforeEach
    void setUp() {
        hasGetters = new HasGetters(valueService);
    }

    @Test
    void givenUnit_whenAllFieldsHaveGetters_andIsAcceptableGetters_thenPassesTest() {
        hasGetters.test(Unit.of(BeanStyle.class));
        hasGetters.test(Unit.of(RecordStyle.class));
        hasGetters.test(Unit.of(TwoLettersOrLessBeanStyle.class));
        hasGetters.test(Unit.of(BooleanBeanStyle.class));
        hasGetters.test(Unit.of(BooleanRecordStyle.class));
        hasGetters.test(Unit.of(BeanAndRecordStyle.class));
        hasGetters.test(Unit.of(PolymorphicGetter.class));
    }

    @Test
    void givenUnit_whenConventionIsSetToJavaBean_andGetterIsNotBeanCompliant_thenFailsTest() {
        hasGetters.setActiveConvention(Convention.javaBeanCompliant());

        assertThatThrownBy(() -> hasGetters.test(Unit.of(RecordStyle.class))).isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> hasGetters.test(Unit.of(BooleanRecordStyle.class))).isInstanceOf(
                UnitAssertionError.class);
    }

    @Test
    void givenUnit_whenTwoAlternativesForBooleanField_thenTestsBoth_andPassesTest() {
        hasGetters.test(Unit.of(BooleanTwoAlternatives.class));
    }

    @Test
    void givenUnit_whenAllFieldsHaveGetters_butSomeGetterIsNotWorking_thenFailsTest() {
        assertThatThrownBy(() -> hasGetters.test(Unit.of(NotGettingField.class)))
                .isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> hasGetters.test(Unit.of(TwoButOnlyOneWorking.class)))
                .isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void givenUnit_whenAnyFieldIsMissingGetter_thenFailsTest() {
        assertThatThrownBy(() -> hasGetters.test(Unit.of(MissingGetter.class)))
                .isInstanceOf(UnitAssertionError.class);
    }
}
