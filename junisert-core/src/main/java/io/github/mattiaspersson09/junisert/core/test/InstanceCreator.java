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
package io.github.mattiaspersson09.junisert.core.test;

import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedSupportGenerator;
import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.core.reflection.Unit;
import io.github.mattiaspersson09.junisert.value.common.ArrayValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.EnumValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.InterfaceValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ParameterObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.PrimitiveValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.WrapperPrimitiveValueGenerator;
import io.github.mattiaspersson09.junisert.value.java.JavaInternals;

import java.util.Arrays;
import java.util.Optional;

/**
 * Helps create unit instances for tests. Using supplied {@link ValueGenerator} or default object generators,
 * which constructs objects through constructors.<br>
 * <br>
 * This class or similar class only for instances is needed to construct instances without a {@link ValueCache}.
 * Using {@link SingletonValueService} with a cache would produce error-prone tests.
 * Unit instances needs to be <em>unique</em>, caching instances will force references to be used
 * and updating a field in one instance will affect other instances of the same type.
 */
class InstanceCreator {
    private static final Logger LOGGER = Logger.getLogger(InstanceCreator.class);

    private final ValueGenerator<Object> instanceGenerator;

    InstanceCreator() {
        this(null);
    }

    InstanceCreator(ValueGenerator<Object> instanceGenerator) {
        this.instanceGenerator = Optional.ofNullable(instanceGenerator)
                .orElseGet(this::getDefaultObjectGenerators);
    }

    private AggregatedValueGenerator getDefaultObjectGenerators() {
        LOGGER.info("Using default common generators to supply arguments for unit constructor");

        ValueGenerator<?> argumentGenerator = new AggregatedSupportGenerator(Arrays.asList(
                new PrimitiveValueGenerator(),
                new WrapperPrimitiveValueGenerator(),
                new ArrayValueGenerator(),
                new EnumValueGenerator(),
                JavaInternals.getSupported(),
                new InterfaceValueGenerator(),
                ObjectValueGenerator.withForcedAccess()
        ));

        ValueGenerator<?> objectGenerator = ObjectValueGenerator.withForcedAccess();
        ValueGenerator<?> parameterObjectGenerator = ParameterObjectValueGenerator.withForcedAccess(argumentGenerator);

        return new AggregatedSupportGenerator(Arrays.asList(objectGenerator, parameterObjectGenerator));
    }

    Object createInstance(Unit unit) {
        return createInstance(unit.getType());
    }

    Object createInstance(Class<?> unitClass) {
        if (!instanceGenerator.supports(unitClass)) {
            throw new UnsupportedTypeError(unitClass);
        }

        return instanceGenerator.generate(unitClass).get();
    }
}
