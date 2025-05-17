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

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.value.common.DependencyObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;

import java.util.Arrays;
import java.util.List;

public final class TestInstanceCreator implements InstanceCreator {
    private final List<ValueGenerator<?>> instanceGenerators;

    public TestInstanceCreator() {
        ValueGenerator<?> dependencySupport = Junisert.aggregatedDefaultValueSupport();
        instanceGenerators = Arrays.asList(
                ObjectValueGenerator.withForcedAccess(),
                DependencyObjectValueGenerator.withForcedAccess(dependencySupport)
        );
    }

    @Override
    public Object createInstance(Class<?> unitClass) {
        for (ValueGenerator<?> generator : instanceGenerators) {
            if (generator.supports(unitClass)) {
                return generator.generate(unitClass).get();
            }
        }

        throw new UnsupportedTypeError(unitClass);
    }
}
