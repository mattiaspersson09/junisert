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

import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public final class SupportBuilder {
    private final Set<ValueGenerator<?>> generators;

    private SupportBuilder() {
        this.generators = new HashSet<>();
    }

    /**
     * Creates a new {@link SupportBuilder}.
     *
     * @return a new builder
     */
    public static SupportBuilder createSupport() {
        return new SupportBuilder();
    }

    /**
     * Build new support around a higher abstraction type, to support several polymorphic implementations.
     *
     * @param superType preferably higher abstraction to support a polymorphic chain
     * @param <T>       type to support, preferably super
     * @return a new {@link Support} to build implementations for given {@code superType}
     */
    public <T> Support<T> support(Class<T> superType) {
        return new Support<>(superType, this);
    }

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
    public <T, IMPL extends T> SupportBuilder supportSingle(Class<T> type, Supplier<IMPL> implementation) {
        generators.add(new SupportGenerator<>(type, new Implementation<>(type, implementation)));
        return this;
    }

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
    public <T, IMPL extends T> SupportBuilder supportSingle(Class<T> superType,
                                                            Class<IMPL> implementationType,
                                                            Supplier<IMPL> implementation) {
        generators.add(new SupportGenerator<>(superType, new Implementation<>(implementationType, implementation)));
        return this;
    }

    /**
     * Creates the built support aggregated in a {@link AggregatedValueGenerator}.
     *
     * @return new aggregated support
     * @throws UnsupportedOperationException if no support were actually built
     */
    public AggregatedValueGenerator build() throws UnsupportedOperationException {
        if (generators.isEmpty()) {
            throw new UnsupportedOperationException("Not allowed to build an empty support");
        }

        return new AggregatedSupportGenerator(generators);
    }

    private <T> void support(Class<T> superType, Collection<Implementation<? extends T>> implementations) {
        generators.add(new SupportGenerator<>(superType, implementations));
    }

    /**
     * Builder for a type to support with implementations. Is treated as a {@link SupportBuilder} as well to be able
     * to return to the original builder when wanting to start building on a new support.
     *
     * @param <T> type to support
     */
    public static final class Support<T> {
        private final Class<T> superType;
        private final List<Implementation<? extends T>> implementations;
        private final SupportBuilder builder;

        private Support(Class<T> superType, SupportBuilder builder) {
            this.superType = superType;
            this.implementations = new ArrayList<>();
            this.builder = builder;
        }

        /**
         * Build a supported polymorphic implementation for this current support.
         *
         * @param implementationType of this support currently being built
         * @param implementation     lazy construction of {@code implementationType}
         * @param <IMPL>             implementation of this support
         * @return this support to build more implementations or start building a new support
         */
        public <IMPL extends T> Support<T> withImplementation(Class<IMPL> implementationType,
                                                              Supplier<IMPL> implementation) {
            implementations.add(new Implementation<>(implementationType, implementation));

            return this;
        }

        /**
         * Finish building this support and start building a new support with given {@code superType}.
         * This current support will be added to the original builder before the new support is being built.
         *
         * @param superType preferably higher abstraction to support a polymorphic chain
         * @param <NEW>     type to start building support for
         * @return a new {@link Support} to start building new support
         * @see SupportBuilder#support(Class) SupportBuilder.support(Class)
         */
        public <NEW> Support<NEW> support(Class<NEW> superType) {
            if (!implementations.isEmpty()) {
                builder.support(this.superType, implementations);
            }

            return builder.support(superType);
        }

        /**
         * Finish building this support and start building a new specific support with given {@code type}.
         * This current support will be added to the original builder before the new support is being built.
         *
         * @param type           to support
         * @param implementation lazy construction of the supported type
         * @param <NEW>          type to support
         * @param <IMPL>         implementation of {@literal <NEW>}
         * @return the original {@code SupportBuilder} to build new support
         * @see SupportBuilder#supportSingle(Class, Supplier) SupportBuilder.supportSingle(Class, Supplier)
         */
        public <NEW, IMPL extends NEW> SupportBuilder supportSingle(Class<NEW> type,
                                                                    Supplier<IMPL> implementation) {
            if (!implementations.isEmpty()) {
                builder.support(this.superType, implementations);
            }

            return builder.supportSingle(type, implementation);
        }

        /**
         * Finish building this support and start building a new single polymorphic support with given {@code type}.
         * This current support will be added to the original builder before the new support is being built.
         * Wider range between {@code superType} and {@code implementationType} will result in larger support span.
         * <p><br>
         * Example:
         * <pre>
         * // Will support Collection, List, AbstractList and ArrayList
         * supportSingle(Collection.class, ArrayList.class, ArrayList::new);
         * </pre>
         *
         * @param superType          with higher abstraction than {@code implementationType} to support polymorphic
         *                           chain
         * @param implementationType of {@code superType}
         * @param implementation     lazy construction of {@code implementationType}
         * @param <NEW>              type to support
         * @param <IMPL>             implementation of {@literal <NEW>}
         * @return the original {@code SupportBuilder} to build new support
         * @see SupportBuilder#supportSingle(Class, Class, Supplier) SupportBuilder.supportSingle(Class, Class,
         *      Supplier)
         */
        public <NEW, IMPL extends NEW> SupportBuilder supportSingle(Class<NEW> superType,
                                                                    Class<IMPL> implementationType,
                                                                    Supplier<IMPL> implementation) {
            if (!implementations.isEmpty()) {
                builder.support(this.superType, implementations);
            }

            return builder.supportSingle(superType, implementationType, implementation);
        }

        /**
         * Finish building this support and create every built support from the original builder
         * aggregated in a {@link AggregatedValueGenerator}.
         *
         * @return new aggregated support
         * @see SupportBuilder#build() SupportBuilder.build()
         */
        public AggregatedValueGenerator build() {
            if (!implementations.isEmpty()) {
                builder.support(superType, implementations);
            }

            return builder.build();
        }
    }
}
