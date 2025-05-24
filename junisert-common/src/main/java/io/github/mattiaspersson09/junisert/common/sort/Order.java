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

/**
 * Used as value holder for sorting in a collection or stream, to sort objects based on their order value.
 * An object with an order of lower value should be evaluated before objects with higher value.
 *
 * @see Sortable
 */
public final class Order {
    private static final int MAX_VALUE = 100;
    /**
     * Order to be evaluated <em>first</em> among other ordered objects.
     */
    public static final Order FIRST = new Order(-MAX_VALUE);
    /**
     * Order to be evaluated <em>second</em> among other ordered objects.
     */
    public static final Order SECOND = new Order(-MAX_VALUE + 1);
    /**
     * Order to be evaluated <em>third</em> among other ordered objects.
     */
    public static final Order THIRD = new Order(-MAX_VALUE + 2);
    /**
     * Order without a defined order (neutral) among other ordered objects.
     */
    public static final Order DEFAULT = new Order(0);
    /**
     * Order to be evaluated <em>third to last</em> among other ordered objects.
     */
    public static final Order THIRD_LAST = new Order(MAX_VALUE - 2);
    /**
     * Order to be evaluated <em>second to last</em> among other ordered objects.
     */
    public static final Order SECOND_LAST = new Order(MAX_VALUE - 1);
    /**
     * Order to be evaluated <em>last</em> among other ordered objects.
     */
    public static final Order LAST = new Order(MAX_VALUE);

    private final int value;

    Order(int value) {
        this.value = value;
    }

    /**
     * Check if this order is in a position before {@code other}.
     *
     * @param other potentially after
     * @return true if this order is before
     */
    public boolean isBefore(Order other) {
        return value < other.value;
    }

    /**
     * Check if this order is in a position after {@code other}.
     *
     * @param other potentially before
     * @return true if this order is after
     */
    public boolean isAfter(Order other) {
        return value > other.value;
    }

    /**
     * Reorder closer to first position by 1 step.
     *
     * @return new order
     */
    public Order moveUp() {
        return moveUp(1);
    }

    /**
     * Reorder {@code steps} closer to first position. Steps are always considered as an absolute value,
     * meaning if supplied steps are negative it will be converted to positive.
     *
     * @param steps non-negative to move up
     * @return new order
     */
    public Order moveUp(int steps) {
        if (isReachingPeak(steps)) {
            return FIRST;
        }

        return new Order(value - Math.abs(steps));
    }

    /**
     * Reorder closer to last position by 1 step.
     *
     * @return new order
     */
    public Order moveDown() {
        return moveDown(1);
    }

    /**
     * Reorder {@code steps} closer to last position.
     *
     * @param steps non-negative to move down
     * @return new order
     */
    public Order moveDown(int steps) {
        if (isReachingPeak(steps)) {
            return LAST;
        }

        return new Order(value + Math.abs(steps));
    }

    /**
     * This order's value.
     *
     * @return order value
     */
    public int value() {
        return value;
    }

    private boolean isReachingPeak(int steps) {
        return (Math.abs(steps) >= Math.abs(MAX_VALUE - Math.abs(value)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return value == order.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
