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
 * Used to build aggregated support with {@code 1..N} number of implementations per supported type.
 */
public interface SupportBuilder {
    /**
     * Creates a new {@link SupportBuilder}.
     *
     * @return a new builder
     */
    static SupportBuilder createSupport() {
        return new SupportBuilderImpl();
    }

    /**
     * Build new support around a higher abstraction type, to support several polymorphic implementations.
     *
     * @param superType preferably higher abstraction to support a polymorphic chain
     * @param <T>       type to support, preferably super
     * @return a new {@link Support} to build implementations for given {@code superType}
     */
    <T> Support<T> support(Class<T> superType);

    /**
     * Builds support for specific given {@code type}.
     *
     * @param type           to support
     * @param implementation lazy construction of the supported type
     * @param <T>            type to support
     * @param <IMPL>         implementation of {@literal <T>}
     * @return this builder to continue building more support
     * @see #support(Class)
     */
    <T, IMPL extends T> SupportBuilder supportSingle(Class<T> type, Supplier<IMPL> implementation);

    /**
     * Builds single support for a specific polymorphic type. Wider range between {@code superType} and
     * {@code implementationType} will result in larger support span.
     * <p><br>
     * Example:
     * <pre>
     * // Will support Collection, List, AbstractList and ArrayList
     * supportSingle(Collection.class, ArrayList.class, ArrayList::new);
     * </pre>
     *
     * @param superType          with higher abstraction than {@code implementationType} to support polymorphic chain
     * @param implementationType of {@code superType}
     * @param implementation     lazy construction of {@code implementationType}
     * @param <T>                type to support
     * @param <IMPL>             implementation of {@literal <T>}
     * @return this builder to continue building more support
     * @see #support(Class)
     */
    <T, IMPL extends T> SupportBuilder supportSingle(Class<T> superType,
                                                     Class<IMPL> implementationType,
                                                     Supplier<IMPL> implementation);

    /**
     * Creates the built support aggregated in a {@link AggregatedValueGenerator}.
     *
     * @return new aggregated support
     * @throws UnsupportedOperationException if no support were actually built
     */
    AggregatedValueGenerator build() throws UnsupportedOperationException;

    /**
     * Builder for a type to support with implementations. Is treated as a {@code SupportBuilder} as well to be able
     * to return to the original builder when starting on a new support build.
     *
     * @param <T> type to support
     */
    interface Support<T> extends SupportBuilder {
        /**
         * Build a supported polymorphic implementation for this current support.
         *
         * @param implementationType of this support currently being built
         * @param implementation     lazy construction of {@code implementationType}
         * @param <IMPL>             implementation of this support
         * @return this support to build more implementations or start building a new support
         */
        <IMPL extends T> Support<T> withImplementation(Class<IMPL> implementationType, Supplier<IMPL> implementation);
    }
}
