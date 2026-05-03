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
package io.github.mattiaspersson09.junisert.api.internal.support;

import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;

import java.util.function.Supplier;

/**
 * A chainable support user that needs temporary support when performing operations.
 *
 * @param <U> temporary support user
 */
public interface SupportUser<U> {
    /**
     * Registers temporary support to be used during operations. Values created by a temporary support
     * will be handled differently, to prevent side effects and disruption of other operations.
     *
     * @param support that is needed temporarily
     * @return this support user
     * @see ValueGenerator
     * @see #withSupport(Class, Class, Value)
     * @see #withSupport(Class, Value)
     */
    default U withSupport(ValueGenerator<?> support) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Registers temporary support to be used during operations. Values created by a temporary support
     * will be handled differently, to prevent side effects and disruption of other operations.<br>
     * <br>
     * <strong>OBS!</strong> {@code implementation} should not construct a negative (null or primitive zero/false)
     * value, it will affect value comparison and might produce error-prone results.
     * It's empty representation however is allowed to be negative, see
     * {@link Value#of(Supplier, Supplier) lazy value} and {@link Value#ofEager(Object, Object) eager value}
     * for more details.<br>
     * <br>
     * Will register custom support for a polymorphic type, where you can support a hierarchy of values.
     * During assertions if any type between {@code superType} and {@code implementationType} appears,
     * this support can generate values for it.<br>
     * <br>
     * <p>Example support for {@code CharSequence}, {@code AbstractStringBuilder} and {@code StringBuffer}:
     * <pre>
     * withTemporarySupport(CharSequence.class, StringBuffer.class, StringBuffer::new);
     * </pre>
     * Example with the same support but non-null empty value:
     * <pre>
     * withTemporarySupport(CharSequence.class, StringBuffer.class, Value.of(() -> new StringBuffer("Junisert"), StringBuffer::new));
     * </pre>
     *
     * @param superType          to support
     * @param implementationType of {@code superType} to support
     * @param implementation     of {@code implementationType} to lazily construct
     * @param <T>                type for super type
     * @param <I>                type for implementation
     * @return this support user
     * @see #withSupport(Class, Value)
     */
    default <T, I extends T> U withSupport(Class<T> superType, Class<I> implementationType, Value<I> implementation) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Registers temporary support to be used during operations. Values created by a temporary support
     * will be handled differently, to prevent side effects and disruption of other operations.<br>
     * <br>
     * <strong>OBS!</strong> {@code implementation} should not construct a negative (null or primitive zero/false)
     * value, it will affect value comparison and might produce error-prone results.
     * It's empty representation however is allowed to be negative, see
     * {@link Value#of(Supplier, Supplier) lazy value} and {@link Value#ofEager(Object, Object) eager value}
     * for more details.<br>
     * <br>
     * Will temporarily register custom support for a specific type, this will only support a single type and not an
     * entire polymorphic hierarchy. To support a hierarchy {@link #withSupport(Class, Class, Value)}
     * should be used instead.<br>
     * <br>
     * <p>Example support for {@code StringBuffer}:
     * <pre>
     * withTemporarySupport(StringBuffer.class, StringBuffer::new);
     * </pre>
     * Example with the same support but non-null empty value:
     * <pre>
     * withTemporarySupport(StringBuffer.class, Value.of(() -> new StringBuffer("Junisert"), StringBuffer::new));
     * </pre>
     *
     * @param implementationType to support
     * @param implementation     of {@code implementationType} to lazily construct, not a negative value
     * @param <T>                type of implementation
     * @return this support user
     */
    default <T> U withSupport(Class<T> implementationType, Value<T> implementation) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
