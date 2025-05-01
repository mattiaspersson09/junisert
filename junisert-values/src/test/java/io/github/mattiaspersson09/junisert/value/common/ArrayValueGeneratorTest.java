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

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ArrayValueGeneratorTest {
    private ArrayValueGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new ArrayValueGenerator();
    }

    @Test
    void supports_whenTypeIsArray_thenIsSupported() {
        Class[] array = new Class[]{};
        assertThat(generator.supports(Class[].class)).isTrue();
        assertThat(generator.supports(array.getClass())).isTrue();
    }

    @Test
    void supports_whenTypeIsNotAnArray_thenIsNotSupported() {
        assertThat(generator.supports(Class.class)).isFalse();
    }

    @Test
    void generate_whenTypeIsAnArray_thenReturnsEmptyArrayValue() {
        assertThat((Class[]) generator.generate(Class[].class).get())
                .isNotNull()
                .hasSize(0);
    }

    @Test
    void generate_whenNotAnArray_thenThrowsUnsupportedTypeError() {
        assertThatThrownBy(() -> generator.generate(Class.class)).isInstanceOf(UnsupportedTypeError.class);
    }

    private static class Class {
    }
}
