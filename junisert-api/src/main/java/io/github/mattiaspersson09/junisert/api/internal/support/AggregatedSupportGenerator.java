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

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <strong>INTERNAL DISCLAIMER:</strong>
 * <p>
 * Internal API and not considered stable for direct usage by external users of this API,
 * can be modified, become invisible, moved, renamed or removed without proper notice.
 * This class is visible because of support for Java version 8 and lack of modularity
 * and when support is dropping for version 8 this will lose visibility.
 * </p>
 */
public class AggregatedSupportGenerator implements AggregatedValueGenerator {
    private final List<ValueGenerator<?>> generators;

    /**
     * Creates a new aggregated support generator with given {@link ValueGenerator}'s to be internally aggregated.
     *
     * @param generators aggregated generators
     */
    public AggregatedSupportGenerator(Collection<ValueGenerator<?>> generators) {
        this.generators = new ArrayList<>(generators);
    }

    @Override
    public Value<?> generate(Class<?> fromType) throws UnsupportedTypeError {
        for (ValueGenerator<?> generator : generators) {
            if (generator.supports(fromType)) {
                return generator.generate(fromType);
            }
        }

        throw new UnsupportedTypeError(fromType);
    }

    @Override
    public boolean supports(Class<?> type) {
        return generators.stream()
                .anyMatch(generator -> generator.supports(type));
    }

    @Override
    public Collection<ValueGenerator<?>> aggregated() {
        return Collections.unmodifiableCollection(generators);
    }

    @Override
    public AggregatedValueGenerator merge(ValueGenerator<?> generator) {
        List<ValueGenerator<?>> aggregated = new ArrayList<>(generators);

        if (generator instanceof AggregatedValueGenerator) {
            aggregated.addAll(((AggregatedValueGenerator) generator).aggregated());
        } else {
            aggregated.add(generator);
        }

        return new AggregatedSupportGenerator(aggregated);
    }

    @Override
    public AggregatedValueGenerator mergeFirst(ValueGenerator<?> generator) {
        List<ValueGenerator<?>> aggregated = new ArrayList<>();

        if (generator instanceof AggregatedValueGenerator) {
            aggregated.addAll(((AggregatedValueGenerator) generator).aggregated());
        } else {
            aggregated.add(generator);
        }

        aggregated.addAll(generators);

        return new AggregatedSupportGenerator(aggregated);
    }
}
