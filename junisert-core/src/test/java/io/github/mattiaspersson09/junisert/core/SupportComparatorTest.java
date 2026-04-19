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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SupportComparatorTest {
    private SupportComparator comparator;

    @BeforeEach
    void setUp() {
        comparator = new SupportComparator();
    }

    @Test
    void compare_givenNotSortable_whenSameOrder_thenReturnsZero() {
        assertThat(comparator.compare(new NotSortable(), new NotSortable2())).isEqualTo(0);
    }

    @Test
    void compare_givenSortable_whenLeftIsPrioritized_thenReturnsNegative() {
        assertThat(comparator.compare(new FirstOrder(), new LastOrder())).isEqualTo(-1);
    }

    @Test
    void compare_givenSortable_whenRightIsPrioritized_thenReturnsPositive() {
        assertThat(comparator.compare(new LastOrder(), new FirstOrder())).isEqualTo(1);
    }

    @Test
    void compare_givenSamePriority_whenComparingToRight_thenReturnsNegative() {
        assertThat(comparator.compare(new FirstOrder(), new FirstOrder2())).isEqualTo(-1);
    }

    private static class NotSortable implements ValueGenerator<Object> {
        @Override
        public Value<?> generate(Class<?> fromType) throws UnsupportedTypeError {
            return null;
        }

        @Override
        public boolean supports(Class<?> type) {
            return false;
        }
    }

    private static class NotSortable2 extends NotSortable {
    }

    private static class LastOrder implements ValueGenerator<Object>, Sortable {
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

    private static class FirstOrder implements ValueGenerator<Object>, Sortable {
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

    private static class FirstOrder2 extends FirstOrder {
    }
}
