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
import io.github.mattiaspersson09.junisert.testunits.hashcode.PrimeHashCode;
import io.github.mattiaspersson09.junisert.testunits.hashcode.WellImplementedHashCode;
import io.github.mattiaspersson09.junisert.testunits.hashcode.WellImplementedHashCodeExtendingBase;
import io.github.mattiaspersson09.junisert.testunits.hashcode.broken.InconsistentSuperHashCode;
import io.github.mattiaspersson09.junisert.testunits.hashcode.broken.MissingHashCode;
import io.github.mattiaspersson09.junisert.testunits.hashcode.broken.ReturnsConstantPrimeHashCode;
import io.github.mattiaspersson09.junisert.testunits.hashcode.broken.ReturnsRandomHashCode;
import io.github.mattiaspersson09.junisert.testunits.hashcode.broken.ReturnsZeroHashCode;
import io.github.mattiaspersson09.junisert.value.common.ArrayValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.PrimitiveValueGenerator;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ImplementsHashCodeIntegrationTest {
    private static ValueService valueService;
    private ImplementsHashCode implementsHashCode;

    @BeforeAll
    static void beforeAll() {
        valueService = new TestValueService(Arrays.asList(
                new PrimitiveValueGenerator(),
                new ArrayValueGenerator(),
                new ObjectValueGenerator()
        ));
    }

    @BeforeEach
    void setUp() {
        implementsHashCode = new ImplementsHashCode(valueService);
    }

    @Test
    void givenUnit_whenHasWellImplementedHashCodeMethod_thenPassesTest() {
        implementsHashCode.test(Unit.of(PrimeHashCode.class));
        implementsHashCode.test(Unit.of(WellImplementedHashCode.class));
        implementsHashCode.test(Unit.of(WellImplementedHashCodeExtendingBase.class));
    }

    @Test
    void givenUnit_whenNotDeclaringImplementationOfHashCodeMethod_thenFailsTest() {
        assertThatThrownBy(() -> implementsHashCode.test(Unit.of(MissingHashCode.class)))
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("was expected to implement")
                .hasMessageContaining("hashCode");
    }

    @Test
    void givenUnit_whenHasBrokenHashCodeImplementation_thenFailsTest() {
        assertThatThrownBy(() -> implementsHashCode.test(Unit.of(InconsistentSuperHashCode.class)))
                .isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> implementsHashCode.test(Unit.of(ReturnsRandomHashCode.class)))
                .isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> implementsHashCode.test(Unit.of(ReturnsZeroHashCode.class)))
                .isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> implementsHashCode.test(Unit.of(ReturnsConstantPrimeHashCode.class)))
                .isInstanceOf(UnitAssertionError.class);
    }
}
