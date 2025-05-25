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

import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedSupportGenerator;
import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.core.internal.ValueService;
import io.github.mattiaspersson09.junisert.value.common.ArrayValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.DependencyObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.EnumValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.InterfaceValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.PrimitiveValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.WrapperPrimitiveValueGenerator;
import io.github.mattiaspersson09.junisert.value.java.JavaInternals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class NoCacheTestValueService implements ValueService {
    private final List<ValueGenerator<?>> generators;

    public NoCacheTestValueService(ValueGenerator<?> generator) {
        this(Collections.singletonList(generator));
    }

    public NoCacheTestValueService(List<ValueGenerator<?>> generators) {
        this.generators = generators;
    }

    public static ValueService withAllValueGenerators() {
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

        return new NoCacheTestValueService(Arrays.asList(
                new PrimitiveValueGenerator(),
                new WrapperPrimitiveValueGenerator(),
                new ArrayValueGenerator(),
                new EnumValueGenerator(),
                javaInternals,
                new InterfaceValueGenerator(),
                ObjectValueGenerator.withForcedAccess(),
                DependencyObjectValueGenerator.withForcedAccess(aggregatedArgumentGenerator)
        ));
    }

    @Override
    public void registerSupport(ValueGenerator<?> generator) {
        // no-op
    }

    @Override
    public void registerNamedSupport(ValueGenerator<?> generator, String supportName) {
        // no-op
    }

    @Override
    public Value<?> getValue(Class<?> type) throws UnsupportedTypeError {
        for (ValueGenerator<?> generator : generators) {
            if (generator.supports(type)) {
                return generator.generate(type);
            }
        }

        throw new UnsupportedTypeError(type);
    }
}
