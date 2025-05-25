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

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Direct implementation of {@link SupportBuilder} API.
 */
final class SupportBuilderImpl implements SupportBuilder {
    private final Set<ValueGenerator<?>> generators;

    SupportBuilderImpl() {
        this.generators = new HashSet<>();
    }

    /**
     * Only used by {@link SupportImpl} to add last built support and return to this builder.
     */
    void support(SupportGenerator<?> support) {
        generators.add(support);
    }

    @Override
    public <T> SupportBuilder.Support<T> support(Class<T> superType) {
        return new SupportImpl<>(superType, this);
    }

    @Override
    public <T, IMPL extends T> SupportBuilder supportSingle(Class<T> type, Supplier<IMPL> implementation) {
        generators.add(new SupportGenerator<>(type, new Implementation<>(type, implementation)));
        return this;
    }

    @Override
    public <T, IMPL extends T> SupportBuilder supportSingle(Class<T> superType,
                                                            Class<IMPL> implementationType,
                                                            Supplier<IMPL> implementation) {
        generators.add(new SupportGenerator<>(superType, new Implementation<>(implementationType, implementation)));
        return this;
    }

    @Override
    public AggregatedValueGenerator build() throws UnsupportedOperationException {
        if (generators.isEmpty()) {
            throw new UnsupportedOperationException("Not allowed to build an empty support");
        }

        return new AggregatedSupportGenerator(generators);
    }

    /**
     * Direct implementation of {@link SupportBuilder.Support} API.
     */
    final static class SupportImpl<T> implements SupportBuilder.Support<T> {
        private final SupportGenerator<T> supportGenerator;
        private final SupportBuilderImpl originalBuilder;

        SupportImpl(Class<T> superType, SupportBuilderImpl originalBuilder) {
            this.supportGenerator = new SupportGenerator<>(superType);
            this.originalBuilder = originalBuilder;
        }

        @Override
        public <IMPL extends T> SupportImpl<T> withImplementation(Class<IMPL> implementationType,
                                                                  Supplier<IMPL> implementation) {
            supportGenerator.addSupport(new Implementation<>(implementationType, implementation));
            return this;
        }

        @Override
        public <NEW> SupportBuilder.Support<NEW> support(Class<NEW> superType) {
            if (supportGenerator.hasSupport()) {
                originalBuilder.support(supportGenerator);
            }

            return originalBuilder.support(superType);
        }

        @Override
        public <NEW, IMPL extends NEW> SupportBuilder supportSingle(Class<NEW> type, Supplier<IMPL> implementation) {
            if (supportGenerator.hasSupport()) {
                originalBuilder.support(supportGenerator);
            }

            return originalBuilder.supportSingle(type, implementation);
        }

        @Override
        public <NEW, IMPL extends NEW> SupportBuilder supportSingle(Class<NEW> superType,
                                                                    Class<IMPL> implementationType,
                                                                    Supplier<IMPL> implementation) {
            if (supportGenerator.hasSupport()) {
                originalBuilder.support(supportGenerator);
            }

            return originalBuilder.supportSingle(superType, implementationType, implementation);
        }

        @Override
        public AggregatedValueGenerator build() throws UnsupportedOperationException {
            if (supportGenerator.hasSupport()) {
                originalBuilder.support(supportGenerator);
            }

            return originalBuilder.build();
        }
    }
}
