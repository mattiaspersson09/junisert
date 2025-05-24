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
package io.github.mattiaspersson09.junisert.api.internal.support;

import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.sort.Order;
import io.github.mattiaspersson09.junisert.common.sort.Sortable;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * <strong>INTERNAL DISCLAIMER:</strong>
 * <p>
 * Internal API and not considered stable for direct usage by external users of this API,
 * can be modified, become invisible, moved, renamed or removed without proper notice.
 * This class is visible because of support for Java version 8 and lack of modularity
 * and when support is dropping for version 8 this will lose visibility.
 * </p><br>
 * <p>
 * Should be used for supplying a concrete implementation of a super type to
 * support polymorphic values. Also used to mark a supported internal type's
 * implemented value, to prevent facing {@link ClassCastException} and narrowing
 * conversion during value lookup from a {@link ValueGenerator}.
 *
 * @param <T> concrete implementation in a polymorphic chain
 * @see Value
 */
public final class Implementation<T> implements Value<T>, Sortable {
    private final Class<T> implementationType;
    private final Supplier<? extends T> value;
    private Order order;

    /**
     * Creates a new support implementation.
     *
     * @param implementationType to support
     * @param value              lazy construction of the implementation
     */
    public Implementation(Class<T> implementationType, Supplier<? extends T> value) {
        this.order = Order.DEFAULT;
        this.implementationType = Objects.requireNonNull(implementationType, "Must have an implementation type");
        this.value = Objects.requireNonNull(value, "Can't construct a lazy value object without a value supplier");
    }

    /**
     * Check if this value is an implementation of {@code origin}. Origin should be
     * a super type of this implementation or be EXACTLY this implementation to
     * prevent narrowing conversion. According to Java Language Specification > Assignment Contexts
     * and rule of polymorphism.
     *
     * @param origin potential super type
     * @return true if this value is an implementation of origin
     */
    public boolean isImplementationOf(Class<?> origin) {
        if (origin == null) {
            return false;
        }

        return origin.isAssignableFrom(this.implementationType);
    }

    /**
     * Re-order this implementation with given {@code order}.
     *
     * @param order non-null new order, else current order will be kept
     * @return this implementation
     */
    public Implementation<T> order(Order order) {
        if (order != null) {
            this.order = order;
        }

        return this;
    }

    @Override
    public T get() {
        return value.get();
    }

    @Override
    public Order order() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Implementation<?> that = (Implementation<?>) o;
        return Objects.equals(implementationType, that.implementationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(implementationType);
    }
}
