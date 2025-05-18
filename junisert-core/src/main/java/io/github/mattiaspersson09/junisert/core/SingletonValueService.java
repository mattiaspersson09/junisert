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
package io.github.mattiaspersson09.junisert.core;

import io.github.mattiaspersson09.junisert.api.internal.service.ValueService;
import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.value.common.DependencyObjectValueGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

final class SingletonValueService implements ValueService {
    private static final Logger LOGGER = Logger.getLogger(SingletonValueService.class);
    private static volatile SingletonValueService INSTANCE;

    private final Object mutex = new Object();
    private final List<ValueGenerator<?>> generators;
    private final ValueCache valueCache;

    SingletonValueService(ValueCache valueCache) {
        LOGGER.config("Initializing default value support");
        this.generators = new ArrayList<>();
        this.valueCache = valueCache;

        AggregatedValueGenerator defaultSupport = Junisert.aggregatedDefaultValueSupport();
        DependencyObjectValueGenerator dependencyObjectValueGenerator = DependencyObjectValueGenerator
                .buildDependencySupport(defaultSupport)
                .withForcedAccess()
                .withMaxDependencyDepth(Junisert.INSTANCE_DEPENDENCY_DEPTH)
                .build();

        this.generators.addAll(defaultSupport.aggregated());
        this.generators.add(dependencyObjectValueGenerator);
    }

    static synchronized SingletonValueService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SingletonValueService(Junisert.valueCache());
        }

        return INSTANCE;
    }

    @Override
    public void registerSupport(ValueGenerator<?> generator) {
        Objects.requireNonNull(generator);

        synchronized (mutex) {
            generators.add(generator);
        }

        LOGGER.config("Registered support: {0}", generator);
    }

    @Override
    public void registerNamedSupport(ValueGenerator<?> generator, String supportName) {
        Objects.requireNonNull(generator);

        synchronized (mutex) {
            generators.add(generator);
        }

        LOGGER.config("Registered named support: {0}", supportName);
    }

    @Override
    public Value<?> getValue(Class<?> type) {
        if (valueCache.contains(type)) {
            return valueCache.get(type);
        }

        synchronized (mutex) {
            for (ValueGenerator<?> generator : generators) {
                if (generator.supports(type)) {
                    return valueCache.save(type, generator.generate(type));
                }
            }
        }

        throw new UnsupportedTypeError(type);
    }

    int supportSize() {
        return generators.size();
    }

    void clear() {
        generators.clear();
    }

    List<ValueGenerator<?>> getRegisteredSupport() {
        return Collections.unmodifiableList(generators);
    }
}
