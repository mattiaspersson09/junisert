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

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.sort.Order;
import io.github.mattiaspersson09.junisert.common.sort.Sortable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <strong>INTERNAL DISCLAIMER:</strong>
 * <p>
 * Internal API and not considered stable for direct usage by external users of this API,
 * can be modified, become invisible, moved, renamed or removed without proper notice.
 * This class is visible because of support for Java version 8 and lack of modularity
 * and when support is dropping for version 8 this will lose visibility.
 * </p><br>
 * <p>
 * Internal {@code ValueGenerator} for polymorphic support for a value type, supporting {@code 1..N} number of
 * implementations that can be generated. This generator is mutable and supporting that more implementations can be
 * added after creation.
 *
 * @param <T> polymorphic type this generator is supporting and generate values for.
 */
public final class SupportGenerator<T> implements ValueGenerator<T>, Sortable {
    private final Class<T> type;
    private final List<Implementation<? extends T>> implementations;
    private Order order;

    SupportGenerator(Class<T> type) {
        this(type, Collections.emptyList());
    }

    /**
     * Creates a new mutable support generator with given {@code implementation} as initial supported value
     * for {@code type}.
     *
     * @param type           to support
     * @param implementation as support value and lazy construction of {@code type}
     */
    public SupportGenerator(Class<T> type, Implementation<? extends T> implementation) {
        this(type, Collections.singleton(implementation));
    }

    /**
     * Creates a new mutable support generator with given {@code implementations} as initial supported values
     * for {@code type}.
     *
     * @param type            to support
     * @param implementations as support value and lazy constructions of {@code type}
     */
    public SupportGenerator(Class<T> type, Collection<Implementation<? extends T>> implementations) {
        this.order = Order.DEFAULT;
        this.type = Objects.requireNonNull(type);
        this.implementations = new ArrayList<>(Objects.requireNonNull(implementations));
    }

    /**
     * Adds given {@code implementation} as a lazy support of this generator's supported type.
     *
     * @param implementation as support value and lazy construction of this generator's supported type
     * @return this support generator
     */
    public SupportGenerator<T> addSupport(Implementation<? extends T> implementation) {
        implementations.add(implementation);
        return this;
    }

    /**
     * Re-order this generator with given {@code order}.
     *
     * @param order non-null new order, else current order will be kept
     * @return this support generator
     */
    public SupportGenerator<T> order(Order order) {
        if (order != null) {
            this.order = order;
        }

        return this;
    }

    int size() {
        return implementations.size();
    }

    @Override
    public Value<? extends T> generate(Class<?> fromType) throws UnsupportedTypeError {
        return implementations.stream()
                .filter(impl -> impl.isImplementationOf(fromType))
                .sorted()
                .findFirst()
                .orElseThrow(() -> new UnsupportedTypeError(fromType));
    }

    @Override
    public boolean supports(Class<?> type) {
        // 1. This generator's supported type should be equal to or a super type of the type
        // to generate value for, else we break support for polymorphic values.
        // 2. We need to prevent class casting problems however, by checking that the value to generate is
        // also an actual implementation of the type to generate value for.
        // To follow rule of polymorphism, BUT prevent trying to do narrowing conversion
        // specified in Java Language Specification > Assignment Contexts
        return this.type.isAssignableFrom(type) && implementations.stream()
                .anyMatch(impl -> impl.isImplementationOf(type));
    }

    @Override
    public Order order() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupportGenerator<?> that = (SupportGenerator<?>) o;
        return Objects.equals(type, that.type) && Objects.equals(implementations, that.implementations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, implementations);
    }
}
