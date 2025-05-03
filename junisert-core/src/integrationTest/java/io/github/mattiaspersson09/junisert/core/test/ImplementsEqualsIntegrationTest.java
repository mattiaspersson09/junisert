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
import io.github.mattiaspersson09.junisert.testunits.equals.WellImplementedEquals;
import io.github.mattiaspersson09.junisert.testunits.equals.WellImplementedEqualsExtendingBase;
import io.github.mattiaspersson09.junisert.testunits.equals.broken.AlwaysTrueEquals;
import io.github.mattiaspersson09.junisert.testunits.equals.broken.InconsistentEquals;
import io.github.mattiaspersson09.junisert.testunits.equals.broken.MissingEquals;
import io.github.mattiaspersson09.junisert.testunits.equals.broken.NoTypeCheckEquals;
import io.github.mattiaspersson09.junisert.testunits.equals.broken.OnlyReferenceEquals;
import io.github.mattiaspersson09.junisert.testunits.equals.broken.OnlyTypeCheckEquals;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ImplementsEqualsIntegrationTest {
    private static ValueService valueService;
    private ImplementsEquals implementsEquals;

    @BeforeAll
    static void beforeAll() {
        valueService = new TestValueService(new ObjectValueGenerator());
    }

    @BeforeEach
    void setUp() {
        implementsEquals = new ImplementsEquals(valueService);
    }

    @Test
    void givenUnit_whenHasWellImplementedEqualsMethod_thenPassesTest() {
        implementsEquals.test(Unit.of(WellImplementedEquals.class));
        implementsEquals.test(Unit.of(WellImplementedEqualsExtendingBase.class));
    }

    @Test
    void givenUnit_whenNotDeclaringImplementationOfEqualsMethod_thenFailsTest() {
        assertThatThrownBy(() -> implementsEquals.test(Unit.of(MissingEquals.class)))
                .isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void givenUnit_whenHasBrokenEqualsImplementation_thenFailsTest() {
        assertThatThrownBy(() -> implementsEquals.test(Unit.of(AlwaysTrueEquals.class)))
                .isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> implementsEquals.test(Unit.of(OnlyReferenceEquals.class)))
                .isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> implementsEquals.test(Unit.of(NoTypeCheckEquals.class)))
                .isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> implementsEquals.test(Unit.of(OnlyTypeCheckEquals.class)))
                .isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> implementsEquals.test(Unit.of(InconsistentEquals.class)))
                .isInstanceOf(UnitAssertionError.class);
    }
}
