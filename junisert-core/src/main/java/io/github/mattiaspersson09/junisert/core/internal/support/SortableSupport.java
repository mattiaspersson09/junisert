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
package io.github.mattiaspersson09.junisert.core.internal.support;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.sort.Order;
import io.github.mattiaspersson09.junisert.common.sort.Sortable;

import java.util.Objects;

/**
 * Sortable adapter for a supported value generator, to be able to adjust sorting order for every support
 * that isn't directly sortable. This adapter should only be used if a support isn't already sortable.
 */
public final class SortableSupport implements ValueGenerator<Object>, Sortable {
    private final ValueGenerator<?> support;

    /**
     * Creates a new sortable adapter for {@code support}.
     *
     * @param support to wrap as sortable
     */
    public SortableSupport(ValueGenerator<?> support) {
        this.support = support;
    }

    @Override
    public Value<?> generate(Class<?> fromType) throws UnsupportedTypeError {
        return support.generate(fromType);
    }

    @Override
    public boolean supports(Class<?> type) {
        return support.supports(type);
    }

    @Override
    public Order order() {
        // Place right before earlier registered support
        return Order.DEFAULT.moveUp();
    }

    /**
     * Gets the support wrapped inside this sortable adapter.
     *
     * @return support
     */
    public ValueGenerator<?> getSupport() {
        return support;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SortableSupport that = (SortableSupport) object;
        return Objects.equals(support, that.support);
    }

    @Override
    public int hashCode() {
        return Objects.hash(support);
    }

    @Override
    public String toString() {
        return "SortableSupport{" +
                "support=" + support +
                '}';
    }
}
