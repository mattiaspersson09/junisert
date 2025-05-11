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
import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedSupportGenerator;
import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.value.common.ArrayValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.EnumValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.InterfaceValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ParameterObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.PrimitiveValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.WrapperPrimitiveValueGenerator;
import io.github.mattiaspersson09.junisert.value.java.JavaInternals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

final class SingletonValueService implements ValueService {
    private static final Logger LOGGER = Logger.getLogger("Junisert");
    private static volatile SingletonValueService INSTANCE;

    private final List<ValueGenerator<?>> generators;
    private final ValueCache valueCache;

    SingletonValueService(ValueCache valueCache) {
        LOGGER.info("Initializing default value support, this construction is expensive and should only happen once");
        this.generators = Collections.synchronizedList(new ArrayList<>());
        this.valueCache = valueCache;
        registerDefaultValueSupport();
    }

    private void registerDefaultValueSupport() {
        ValueGenerator<?> javaInternals = JavaInternals.getSupported();
        AggregatedValueGenerator aggregatedArgumentGenerator = new AggregatedSupportGenerator(Arrays.asList(
                new PrimitiveValueGenerator(),
                new WrapperPrimitiveValueGenerator(),
                new ArrayValueGenerator(),
                new EnumValueGenerator(),
                javaInternals,
                new InterfaceValueGenerator(),
                ObjectValueGenerator.withForcedAccess()
        ));

        registerNamedSupport(new PrimitiveValueGenerator(), "primitive support");
        registerNamedSupport(new WrapperPrimitiveValueGenerator(), "wrapper primitive support");
        registerNamedSupport(new ArrayValueGenerator(), "array support");
        registerNamedSupport(new EnumValueGenerator(), "enum support");
        registerNamedSupport(javaInternals, "predefined Java internals support");
        registerNamedSupport(new InterfaceValueGenerator(), "interface proxy support");
        registerNamedSupport(ObjectValueGenerator.withForcedAccess(), "object (from default constructor) support");
        registerNamedSupport(ParameterObjectValueGenerator.withForcedAccess(aggregatedArgumentGenerator),
                "object (from argument constructor) support");
    }

    static synchronized SingletonValueService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SingletonValueService(new ValueCache());
        }

        return INSTANCE;
    }

    @Override
    public void registerSupport(ValueGenerator<?> generator) {
        generators.add(Objects.requireNonNull(generator));
        LOGGER.config("Registered support: {0}", generator.getClass().getName());
    }

    @Override
    public void registerNamedSupport(ValueGenerator<?> generator, String supportName) {
        generators.add(Objects.requireNonNull(generator));
        LOGGER.config("Registered named support: {0}", supportName);
    }

    @Override
    public Value<?> getValue(Class<?> type) {
        if (valueCache.contains(type)) {
            return valueCache.get(type);
        }

        for (ValueGenerator<?> generator : generators) {
            if (generator.supports(type)) {
                Value<?> value = generator.generate(type);

                return valueCache.save(type, value);
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
}
