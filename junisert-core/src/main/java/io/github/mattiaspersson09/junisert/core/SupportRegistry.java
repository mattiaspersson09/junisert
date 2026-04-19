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
package io.github.mattiaspersson09.junisert.core;

import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedSupportGenerator;
import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.common.sort.Sortable;
import io.github.mattiaspersson09.junisert.core.internal.support.SortableSupport;
import io.github.mattiaspersson09.junisert.value.common.ArrayValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.EnumValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.InterfaceValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.PrimitiveValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.WrapperPrimitiveValueGenerator;
import io.github.mattiaspersson09.junisert.value.java.JavaInternals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class SupportRegistry {
    private static final Logger LOGGER = Logger.getLogger(SupportRegistry.class);
    private static volatile SupportRegistry instance;

    private final Object mutex = new Object();
    private final AggregatedValueGenerator defaultValueSupport;
    private final List<ValueGenerator<?>> registeredSupport;
    private final ValueCache valueCache;

    private SupportRegistry() {
        LOGGER.config("Initializing default value support");
        defaultValueSupport = new AggregatedSupportGenerator(Arrays.asList(
                new PrimitiveValueGenerator(),
                new WrapperPrimitiveValueGenerator(),
                new ArrayValueGenerator(),
                new EnumValueGenerator(),
                createJavaInternalSupport(),
                new InterfaceValueGenerator(),
                new ObjectValueGenerator(),
                ObjectValueGenerator.withForcedAccess()
        ));
        LOGGER.config("Initializing value cache");
        valueCache = new ValueCache();
        registeredSupport = new ArrayList<>();
    }

    static synchronized SupportRegistry get() {
        if (instance == null) {
            instance = new SupportRegistry();
        }

        return instance;
    }

    ValueCache cache() {
        return valueCache;
    }

    AggregatedValueGenerator defaultValueSupport() {
        return defaultValueSupport;
    }

    List<ValueGenerator<?>> registeredSupport() {
        synchronized (mutex) {
            return Collections.unmodifiableList(registeredSupport);
        }
    }

    SupportRegistry register(ValueGenerator<?> support) {
        synchronized (mutex) {
            registeredSupport.add(toSortableSupport(support));
            registeredSupport.sort(new SupportComparator());
        }
        LOGGER.config("Registered support: {0}", support);

        return this;
    }

    void clearRegisteredSupport() {
        synchronized (mutex) {
            registeredSupport.clear();
        }
    }

    void clearCache() {
        valueCache.clear();
    }

    private ValueGenerator<?> createJavaInternalSupport() {
        LOGGER.config("Initializing predefined Java internal support");
        return JavaInternals.getSupported();
    }

    private ValueGenerator<?> toSortableSupport(ValueGenerator<?> generator) {
        return (generator instanceof Sortable) ? generator : new SortableSupport(generator);
    }
}
