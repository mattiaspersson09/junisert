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
import io.github.mattiaspersson09.junisert.value.common.PrimitiveValueGenerator;

import java.util.Objects;

/**
 * Internal support when a user registers a support that's primitive, to prevent that
 * null values would be used when asserting.
 *
 * @param <T> type of support
 */
public final class PrimitiveSupport<T> implements ValueGenerator<T> {
    private final Value<T> implementation;
    private final Class<T> type;

    /**
     * Creates a new primitive support for {@code primitive} type.
     *
     * @param primitive      type to support
     * @param implementation that constructs primitive value
     */
    public PrimitiveSupport(Class<T> primitive, Value<T> implementation) {
        this.type = Objects.requireNonNull(primitive);

        if (!primitive.isPrimitive()) {
            throw new IllegalArgumentException("Type to support isn't primitive: " + primitive);
        }

        this.implementation = Value.ofEager(implementation.get(), getEmptyPrimitive(implementation.asEmpty()));
    }

    @SuppressWarnings("unchecked")
    private T getEmptyPrimitive(T wantedEmpty) {
        if (wantedEmpty != null) {
            return wantedEmpty;
        }

        return (T) new PrimitiveValueGenerator().generate(this.type).asEmpty();
    }

    @Override
    public Value<? extends T> generate(Class<?> fromType) throws UnsupportedTypeError {
        if (!supports(fromType)) {
            throw new UnsupportedTypeError(fromType);
        }

        return implementation;
    }

    @Override
    public boolean supports(Class<?> type) {
        return this.type.equals(type);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PrimitiveSupport<?> that = (PrimitiveSupport<?>) object;
        return Objects.equals(implementation, that.implementation) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(implementation, type);
    }

    @Override
    public String toString() {
        return "PrimitiveSupport{" +
                "implementation=" + implementation +
                ", type=" + type +
                '}';
    }
}
