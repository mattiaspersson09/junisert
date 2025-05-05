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
package io.github.mattiaspersson09.junisert.core.test;

import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.api.internal.service.ValueService;
import io.github.mattiaspersson09.junisert.core.reflection.Unit;
import io.github.mattiaspersson09.junisert.testunits.tostring.CustomToString;
import io.github.mattiaspersson09.junisert.testunits.tostring.EmptyToString;
import io.github.mattiaspersson09.junisert.testunits.tostring.MissingToString;
import io.github.mattiaspersson09.junisert.testunits.tostring.NoInstanceFieldsToString;
import io.github.mattiaspersson09.junisert.testunits.tostring.NotAllFieldsToString;
import io.github.mattiaspersson09.junisert.testunits.tostring.NullToString;
import io.github.mattiaspersson09.junisert.testunits.tostring.OnlyClassNameToString;
import io.github.mattiaspersson09.junisert.testunits.tostring.StrictStandardToString;
import io.github.mattiaspersson09.junisert.value.common.ArrayValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.PrimitiveValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.WrapperPrimitiveValueGenerator;
import io.github.mattiaspersson09.junisert.value.java.JavaInternals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ImplementsToStringIntegrationTest {
    private static ValueService valueService;
    private ImplementsToString implementsToString;

    @BeforeAll
    static void beforeAll() {
        valueService = new TestValueService(Arrays.asList(
                new PrimitiveValueGenerator(),
                new WrapperPrimitiveValueGenerator(),
                new ArrayValueGenerator(),
                JavaInternals.getSupported(),
                new ObjectValueGenerator()
        ));
    }

    @BeforeEach
    void setUp() {
        implementsToString = new ImplementsToString(valueService);
    }

    @Test
    void givenUnit_whenToStringIsWellImplemented_thenPassesTest() {
        implementsToString.test(Unit.of(StrictStandardToString.class));
    }

    @Test
    void givenUnit_whenHaveNoInstanceFields_andToStringReturnsAtLeastClassName_thenPassesTest() {
        implementsToString.test(Unit.of(NoInstanceFieldsToString.class));
    }

    @Test
    void givenUnit_whenHaveInstanceFields_butToStringDoesNotShowAll_thenFailsTest() {
        assertThatThrownBy(() -> implementsToString.test(Unit.of(NotAllFieldsToString.class)))
                .isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> implementsToString.test(Unit.of(OnlyClassNameToString.class)))
                .isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void givenUnit_whenUnsupportedToStringConvention_thenFailsTest() {
        assertThatThrownBy(() -> implementsToString.test(Unit.of(CustomToString.class)))
                .isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void givenUnit_whenHasNotDeclaredToStringMethod_thenFailsTest() {
        assertThatThrownBy(() -> implementsToString.test(Unit.of(MissingToString.class)))
                .isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void givenUnit_whenToStringReturnsUnusableString_thenFailsTest() {
        assertThatThrownBy(() -> implementsToString.test(Unit.of(NullToString.class)))
                .isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> implementsToString.test(Unit.of(EmptyToString.class)))
                .isInstanceOf(UnitAssertionError.class);
    }
}
