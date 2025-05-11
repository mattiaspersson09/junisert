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
package io.github.mattiaspersson09.junisert.core.internal.convention;

import io.github.mattiaspersson09.junisert.core.internal.reflection.Field;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Method;

import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConventionTest {
    @Test
    void defaultNameIsClassName() {
        assertThat(new TestConvention().name()).isEqualTo(TestConvention.class.getSimpleName());
    }

    private static class TestConvention implements Convention {
        @Override
        public Predicate<Method> getterConvention(Field field) {
            return null;
        }

        @Override
        public Predicate<Method> setterConvention(Field field) {
            return null;
        }
    }
}
