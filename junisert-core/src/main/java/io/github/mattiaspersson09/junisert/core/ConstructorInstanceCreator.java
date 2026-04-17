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
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.ValueService;
import io.github.mattiaspersson09.junisert.value.common.DependencyObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;

import java.util.Arrays;
import java.util.Objects;

/**
 * Helps create unit instances for tests. Using supplied {@link ValueGenerator} or default object generators,
 * which constructs objects through constructors.<br>
 * <br>
 * This class or similar class only for instances is needed to construct instances without a cache.
 * Using {@link ValueService} with a cache would produce error-prone tests.
 * Unit instances needs to be <em>unique</em>, caching instances will force references to be used
 * and updating a field in one instance will affect other instances of the same type.
 */
final class ConstructorInstanceCreator implements InstanceCreator {
    private final ValueGenerator<Object> instanceGenerator;
    private final AdaptableDependencyGenerator adaptableGenerator;

    ConstructorInstanceCreator(ValueGenerator<Object> instanceGenerator) {
        this.instanceGenerator = this.adaptableGenerator = new AdaptableDependencyGenerator(instanceGenerator);
    }

    ConstructorInstanceCreator(ValueGenerator<Object> dependencySupport, int dependencyDepth) {
        Objects.requireNonNull(dependencySupport);
        this.adaptableGenerator = new AdaptableDependencyGenerator(dependencySupport);
        this.instanceGenerator = new AggregatedSupportGenerator(Arrays.asList(
                ObjectValueGenerator.withForcedAccess(),
                DependencyObjectValueGenerator.buildDependencySupport(this.adaptableGenerator)
                        .withForcedAccess()
                        .withMaxDependencyDepth(dependencyDepth)
                        .build()
        ));
    }

    @Override
    public Object createInstance(Class<?> unitClass) {
        if (!instanceGenerator.supports(unitClass)) {
            throw new UnsupportedTypeError(unitClass);
        }

        adaptableGenerator.produceEmptyValues(false);

        return instanceGenerator.generate(unitClass).get();
    }

    @Override
    public Object createEmptyInstance(Class<?> unitClass) {
        adaptableGenerator.produceEmptyValues(true);

        return instanceGenerator.generate(unitClass).get();
    }
}
