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
package io.github.mattiaspersson09.junisert.value.common;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.testunits.unit.enumeration.EmptyEnumUnit;
import io.github.mattiaspersson09.junisert.testunits.unit.enumeration.EnumUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EnumValueGeneratorTest {
    private EnumValueGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new EnumValueGenerator();
    }

    @Test
    void supports_whenTypeIsAnEnum_thenIsSupported() {
        assertThat(generator.supports(EnumUnit.class)).isTrue();
    }

    @Test
    void supports_whenTypeIsNotAnEnum_thenIsNotSupported() {
        assertThat(generator.supports(Class.class)).isFalse();
        assertThat(generator.supports(Interface.class)).isFalse();
    }

    @Test
    void generate_whenTypeIsAnEnum_thenGeneratesFromEnumConstant() {
        Value<?> constantValue = generator.generate(EnumUnit.class);

        assertThat(constantValue.get())
                .isNotNull()
                .isInstanceOf(EnumUnit.class)
                .isIn(EnumUnit.CONST, EnumUnit.OTHER_CONST);
    }

    @Test
    void generate_whenNotSupported_thenThrowsUnsupportedTypeError() {
        assertThatThrownBy(() -> generator.generate(Class.class))
                .isInstanceOf(UnsupportedTypeError.class);
    }

    @Test
    void generate_whenEnumHasNoConstants_thenThrowsUnsupportedConstructionError() {
        assertThatThrownBy(() -> generator.generate(EmptyEnumUnit.class).get())
                .isInstanceOf(UnsupportedConstructionError.class);
    }

    private static class Class {
    }

    private interface Interface {
    }
}
