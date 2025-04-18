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

import java.util.Objects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {
    @Test
    void predefinedOrders_isLogicallyOrdered() {
        assertThat(Order.FIRST.value()).isLessThan(Order.SECOND.value());
        assertThat(Order.SECOND.value()).isLessThan(Order.THIRD.value());
        assertThat(Order.THIRD.value()).isLessThan(Order.DEFAULT.value());
        assertThat(Order.DEFAULT.value()).isLessThan(Order.THIRD_LAST.value());
        assertThat(Order.THIRD_LAST.value()).isLessThan(Order.SECOND_LAST.value());
        assertThat(Order.SECOND_LAST.value()).isLessThan(Order.LAST.value());
    }

    @Test
    void isBefore_whenOrderHasLowerValue_thenIsTrue() {
        assertThat(Order.FIRST.isBefore(Order.SECOND)).isTrue();
    }

    @Test
    void isBefore_whenOrderHasHigherValue_thenIsFalse() {
        assertThat(Order.SECOND.isBefore(Order.FIRST)).isFalse();
    }

    @Test
    void isAfter_whenOrderHasHigherValue_thenIsTrue() {
        assertThat(Order.LAST.isAfter(Order.SECOND_LAST)).isTrue();
    }

    @Test
    void isAfter_whenOrderHasLowerValue_thenIsFalse() {
        assertThat(Order.SECOND_LAST.isAfter(Order.LAST)).isFalse();
    }

    @Test
    void moveUp_whenOrderIsLower_thenMoveToHigher() {
        assertThat(Order.LAST.moveUp().isBefore(Order.LAST)).isTrue();
    }

    @Test
    void moveDown_whenOrderIsHigher_thenMoveToLower() {
        assertThat(Order.FIRST.moveDown().isAfter(Order.FIRST)).isTrue();
    }

    @Test
    void move_whenCantMoveMore_thenStayAtPeak() {
        assertThat(Order.FIRST.moveUp(10)).isEqualTo(Order.FIRST);
        assertThat(Order.FIRST.moveUp(-10)).isEqualTo(Order.FIRST);
        assertThat(Order.SECOND.moveUp(10)).isEqualTo(Order.FIRST);
        assertThat(Order.SECOND.moveUp(-10)).isEqualTo(Order.FIRST);
        assertThat(Order.THIRD.moveUp(10)).isEqualTo(Order.FIRST);
        assertThat(Order.THIRD.moveUp(-10)).isEqualTo(Order.FIRST);

        assertThat(Order.LAST.moveDown(10)).isEqualTo(Order.LAST);
        assertThat(Order.LAST.moveDown(-10)).isEqualTo(Order.LAST);
        assertThat(Order.SECOND_LAST.moveDown(10)).isEqualTo(Order.LAST);
        assertThat(Order.SECOND_LAST.moveDown(-10)).isEqualTo(Order.LAST);
        assertThat(Order.THIRD_LAST.moveDown(10)).isEqualTo(Order.LAST);
        assertThat(Order.THIRD_LAST.moveDown(-10)).isEqualTo(Order.LAST);
    }

    @Test
    void moveSteps() {
        assertThat(Order.DEFAULT.moveUp(10).value()).isEqualTo(-10);
        assertThat(Order.DEFAULT.moveUp(-10).value()).isEqualTo(-10);
        assertThat(Order.DEFAULT.moveDown(10).value()).isEqualTo(10);
        assertThat(Order.DEFAULT.moveDown(-10).value()).isEqualTo(10);
    }

    @Test
    void equals_whenSameOrderValue_thenIsEqual() {
        Order first = Order.SECOND;

        assertThat(first).isEqualTo(Order.SECOND);
        assertThat(first).isEqualTo(new Order(Order.SECOND.value()));
    }

    @Test
    void equals_whenIsReference_thenIsEqual() {
        Object first = Order.FIRST;
        assertThat(Order.FIRST).isEqualTo(first);
    }

    @Test
    void equals_whenNotTheSameOrderValue_thenIsNotEqual() {
        assertThat(Order.FIRST).isNotEqualTo(Order.SECOND);
    }

    @Test
    void equals_whenOtherIsNull_thenIsNotEqual() {
        assertThat(Order.FIRST).isNotEqualTo(null);
    }

    @Test
    void equals_whenOtherNotTheSameClass_thenIsNotEqual() {
        Object string = "";
        assertThat(Order.FIRST).isNotEqualTo(string);
    }

    @Test
    void hashCode_whenSameOrderValue_thenIsEqual() {
        Order first = Order.SECOND;

        assertThat(first.hashCode()).isEqualTo(Order.SECOND.hashCode());
        assertThat(first.hashCode()).isEqualTo(new Order(Order.SECOND.value()).hashCode());
    }

    @Test
    void hashCode_whenIsReference_thenIsEqual() {
        assertThat(Order.FIRST.hashCode()).isEqualTo(((Object) Order.FIRST).hashCode());
    }

    @Test
    void hashCode_whenNotTheSameOrderValue_thenIsNotEqual() {
        assertThat(Order.FIRST.hashCode()).isNotEqualTo(Order.SECOND.hashCode());
    }

    @Test
    void hashCode_whenOtherIsNull_thenIsNotEqual() {
        assertThat(Order.FIRST.hashCode()).isNotEqualTo(Objects.hashCode(null));
    }
}
