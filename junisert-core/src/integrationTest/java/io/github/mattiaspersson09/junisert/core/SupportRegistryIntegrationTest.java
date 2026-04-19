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
package io.github.mattiaspersson09.junisert.core;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.sort.Order;
import io.github.mattiaspersson09.junisert.common.sort.Sortable;
import io.github.mattiaspersson09.junisert.core.internal.support.SortableSupport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SupportRegistryIntegrationTest {
    @BeforeAll
    static void beforeAll() {
        SupportRegistry.get().clearRegisteredSupport();
    }

    @AfterEach
    void tearDown() {
        SupportRegistry.get().clearRegisteredSupport();
    }

    @Test
    void register_whenSupportIsNotSortable_thenRegistersLatestBeforeOlder() {
        SupportRegistry.get()
                .register(new UserSupport())
                .register(new UserSupport2());

        assertThat(SupportRegistry.get().registeredSupport())
                .hasSize(2)
                .first()
                .extracting(s -> ((SortableSupport) s).getSupport())
                .isInstanceOf(UserSupport2.class);
        assertThat(SupportRegistry.get().registeredSupport())
                .last()
                .extracting(s -> ((SortableSupport) s).getSupport())
                .isInstanceOf(UserSupport.class);
    }

    @Test
    void register_whenSupportIsSortable_thenRegistersAtRequestedOrder() {
        SupportRegistry.get()
                .register(new UserSupport())
                .register(new UserSupport2())
                .register(new LeastPrioritySupport())
                .register(new HighPrioritySupport());

        assertThat(SupportRegistry.get().registeredSupport())
                .hasSize(4)
                .first()
                .isInstanceOf(HighPrioritySupport.class);
        assertThat(SupportRegistry.get().registeredSupport())
                .last()
                .isInstanceOf(LeastPrioritySupport.class);
    }

    private static class UserSupport implements ValueGenerator<Object> {
        @Override
        public Value<?> generate(Class<?> fromType) throws UnsupportedTypeError {
            return null;
        }

        @Override
        public boolean supports(Class<?> type) {
            return false;
        }
    }

    private static class UserSupport2 implements ValueGenerator<Object> {
        @Override
        public Value<?> generate(Class<?> fromType) throws UnsupportedTypeError {
            return null;
        }

        @Override
        public boolean supports(Class<?> type) {
            return false;
        }
    }

    private static class LeastPrioritySupport implements ValueGenerator<Object>, Sortable {
        @Override
        public Value<?> generate(Class<?> fromType) throws UnsupportedTypeError {
            return null;
        }

        @Override
        public boolean supports(Class<?> type) {
            return false;
        }

        @Override
        public Order order() {
            return Order.LAST;
        }
    }

    private static class HighPrioritySupport implements ValueGenerator<Object>, Sortable {
        @Override
        public Value<?> generate(Class<?> fromType) throws UnsupportedTypeError {
            return null;
        }

        @Override
        public boolean supports(Class<?> type) {
            return false;
        }

        @Override
        public Order order() {
            return Order.FIRST;
        }
    }
}
