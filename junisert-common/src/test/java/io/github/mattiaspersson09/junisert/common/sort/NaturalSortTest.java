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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NaturalSortTest {
    @Test
    void whenDirectlyInheritingFromNaturalSort_andDeclareItselfGenerically_thenIsNaturallySortable() {
        Sorted sorted = new Sorted();
        GenericBaseSorted<?> genericSorted = new GenericBaseSorted<>();

        assertThat(sorted.ordered(Order.FIRST).getClass()).isEqualTo(sorted.getClass());
        assertThat(genericSorted.ordered(Order.FIRST).getClass()).isEqualTo(genericSorted.getClass());
    }

    @Test
    void whenDirectlyInheritingFromNaturalSort_butDeclaresOtherSortable_thenThrowsUnsupportedOperationException() {
        assertThatThrownBy(DeclaresOtherSorted::new)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("expected: " + DeclaresOtherSorted.class)
                .hasMessageContaining("but found: " + Sorted.class);
    }

    @Test
    void whenExtendingNaturallySortable_andNotDeclaringItself_thenThrowsUnsupportedOperationException() {
        assertThatThrownBy(ExtendingBaseSorted::new)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("expected: " + ExtendingBaseSorted.class)
                .hasMessageContaining("but found: " + GenericBaseSorted.class);

        assertThatThrownBy(ExtendingSorted::new)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("expected: " + ExtendingSorted.class)
                .hasMessageContaining("but found: " + Sorted.class);
    }

    @Test
    void reorder_givenNewOrder_thenChangesOrder() {
        Sorted sorted = new Sorted().ordered(Order.FIRST);

        assertThat(sorted.order()).isEqualTo(Order.FIRST);
        assertThat(sorted.ordered(Order.SECOND).order()).isEqualTo(Order.SECOND);
    }

    @Test
    void reorder_whenNewOrderIsMissing_thenKeepsLastOrder() {
        Sorted sorted = new Sorted().ordered(Order.FIRST);

        assertThat(sorted.order()).isEqualTo(Order.FIRST);
        assertThat(sorted.ordered(null).order()).isEqualTo(Order.FIRST);
    }

    private static class GenericBaseSorted<T> extends NaturalSort<GenericBaseSorted<T>> {
        public GenericBaseSorted() {
            super(Order.DEFAULT);
        }
    }

    private static class ExtendingBaseSorted extends GenericBaseSorted<Object> {
        public ExtendingBaseSorted() {
            super();
        }
    }

    private static class DeclaresOtherSorted extends NaturalSort<Sorted> {
        public DeclaresOtherSorted() {
            super(Order.DEFAULT);
        }
    }

    private static class Sorted extends NaturalSort<Sorted> {
        public Sorted() {
            super(Order.DEFAULT);
        }
    }

    private static class ExtendingSorted extends Sorted {
        public ExtendingSorted() {
            super();
        }
    }
}
