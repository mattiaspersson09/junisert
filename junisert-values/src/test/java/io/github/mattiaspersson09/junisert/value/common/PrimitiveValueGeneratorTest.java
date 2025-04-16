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
import io.github.mattiaspersson09.junisert.api.value.Value;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PrimitiveValueGeneratorTest {
    private PrimitiveValueGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new PrimitiveValueGenerator();
    }

    @ParameterizedTest
    @ValueSource(classes = {boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class,
            double.class})
    void primitives(Class<?> type) {
        Value<?> value = generator.generate(type);

        assertThat(generator.supports(type)).isTrue();
        assertThat(value.get()).isNotNull();
        assertThat(value.asEmpty()).isNotNull();
    }

    @Test
    void generate_notPrimitive_throwsUnsupportedTypeException() {
        assertThatThrownBy(() -> generator.generate(Integer.class)).isInstanceOf(UnsupportedTypeError.class);
    }
}
