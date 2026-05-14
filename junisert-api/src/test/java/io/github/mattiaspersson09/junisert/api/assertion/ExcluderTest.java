/*
 * Copyright (c) 2026 Mattias Persson
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
package io.github.mattiaspersson09.junisert.api.assertion;

import io.github.mattiaspersson09.junisert.common.reflection.Field;
import io.github.mattiaspersson09.junisert.common.reflection.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExcluderTest {

    @Test
    void defaults_addsExclusionFilters() {
        DefaultExcluder excluder = new DefaultExcluder();

        excluder.excludingField("field")
                .excludingField("field2")
                .excludingMethod("method", Object.class)
                .excludingMethod("method");

        assertThat(excluder.filters).hasSize(4);
    }

    private static class DefaultExcluder implements Excluder<DefaultExcluder> {
        private final List<Predicate<?>> filters = new ArrayList<>();

        @Override
        public DefaultExcluder excludingField(Predicate<Field> filter) {
            filters.add(filter);
            return this;
        }

        @Override
        public DefaultExcluder excludingMethod(Predicate<Method> filter) {
            filters.add(filter);
            return this;
        }

        @Override
        public DefaultExcluder excluding(Exclusion exclusion) {
            return this;
        }
    }
}
