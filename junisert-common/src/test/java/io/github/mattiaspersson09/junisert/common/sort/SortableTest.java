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
package io.github.mattiaspersson09.junisert.common.sort;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SortableTest {
    @Test
    void compare_whenFirstIsLower_andSecondIsHigher_thenNegative() {
        Sortable first = () -> Order.FIRST;
        Sortable second = () -> Order.DEFAULT;

        assertThat(first.compareTo(second)).isEqualTo(-1);
    }

    @Test
    void compare_whenFirstIsHigher_andSecondIsLower_thenPositive() {
        Sortable first = () -> Order.LAST;
        Sortable second = () -> Order.DEFAULT;

        assertThat(first.compareTo(second)).isEqualTo(1);
    }

    @Test
    void compare_whenFirstAndSecondHasSameOrder_thenZero() {
        Sortable first = () -> Order.DEFAULT;
        Sortable second = () -> Order.DEFAULT;

        assertThat(first.compareTo(second)).isEqualTo(0);
    }
}
