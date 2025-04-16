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

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Supplier;

public final class SupportBuilder {
    private final Set<ValueGenerator<?>> generators;

    private SupportBuilder(Collection<ValueGenerator<?>> initialGenerators) {
        this.generators = new HashSet<>(initialGenerators);
    }

    public static SupportBuilder createSupport() {
        return new SupportBuilder(new HashSet<>());
    }

    public <T> Support<T> support(Class<T> superType) {
        return new Support<>(superType, this);
    }

    public <T, IMPL extends T> SupportBuilder supportSingle(Class<T> superType, Supplier<IMPL> implementation) {
        generators.add(new SupportGenerator<>(superType, new Implementation<>(superType, implementation)));
        return this;
    }

    public <T, IMPL extends T> SupportBuilder supportSingle(Class<T> superType,
                                                            Class<IMPL> implementationType,
                                                            Supplier<IMPL> implementation) {
        generators.add(new SupportGenerator<>(superType, new Implementation<>(implementationType, implementation)));
        return this;
    }

    public SupportBuilder support(ValueGenerator<?> supportGenerator) {
        generators.add(supportGenerator);
        return this;
    }

    public AggregatedValueGenerator build() {
        return new AggregatedSupportGenerator(generators);
    }

    private <T> void support(Class<T> superType, Collection<Implementation<? extends T>> implementations) {
        generators.add(new SupportGenerator<>(superType, implementations));
    }

    public static final class Support<T> {
        private final Class<T> superType;
        private final LinkedList<Implementation<? extends T>> implementations;
        private final SupportBuilder builder;

        public Support(Class<T> superType, SupportBuilder builder) {
            this.superType = superType;
            this.implementations = new LinkedList<>();
            this.builder = builder;
        }

        public <IMPL extends T> Support<T> withImplementation(Class<IMPL> implementationType,
                                                              Supplier<IMPL> implementation) {
            implementations.add(new Implementation<>(implementationType, implementation));

            return this;
        }

        public <NEW> Support<NEW> support(Class<NEW> superType) {
            if (!implementations.isEmpty()) {
                builder.support(this.superType, implementations);
            }

            return builder.support(superType);
        }

        public <NEW, IMPL extends NEW> SupportBuilder supportSingle(Class<NEW> superType,
                                                                    Supplier<IMPL> implementation) {
            if (!implementations.isEmpty()) {
                builder.support(this.superType, implementations);
            }

            return builder.supportSingle(superType, implementation);
        }

        public <NEW, IMPL extends NEW> SupportBuilder supportSingle(Class<NEW> superType,
                                                                    Class<IMPL> implementationType,
                                                                    Supplier<IMPL> implementation) {
            if (!implementations.isEmpty()) {
                builder.support(this.superType, implementations);
            }

            return builder.supportSingle(superType, implementationType, implementation);
        }

        public AggregatedValueGenerator build() {
            if (!implementations.isEmpty()) {
                builder.support(superType, implementations);
            }

            return builder.build();
        }
    }
}
