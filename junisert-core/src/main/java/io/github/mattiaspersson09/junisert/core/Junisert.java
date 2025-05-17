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

import io.github.mattiaspersson09.junisert.api.assertion.PlainObjectAssertion;
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertion;
import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedSupportGenerator;
import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.core.assertion.PlainObjectAssertionImpl;
import io.github.mattiaspersson09.junisert.core.assertion.UnitAssertionImpl;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.SharedResource;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Unit;
import io.github.mattiaspersson09.junisert.value.common.ArrayValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.EnumValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.InterfaceValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.PrimitiveValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.WrapperPrimitiveValueGenerator;
import io.github.mattiaspersson09.junisert.value.java.JavaInternals;

import java.util.Arrays;
import java.util.List;


public final class Junisert {
    static final int INSTANCE_DEPENDENCY_DEPTH = 3;
    private static volatile InstanceCreator defaultInstanceCreator;
    private static volatile ValueCache valueCache;

    private Junisert() {
    }

    public static UnitAssertion assertThatUnit(Class<?> unitClass) {
        return new UnitAssertionImpl(getDefaultTestResource(unitClass));
    }

    public static PlainObjectAssertion assertThatPojo(Class<?> pojoClass) {
        return new PlainObjectAssertionImpl(getDefaultTestResource(pojoClass));
    }

    static List<ValueGenerator<?>> defaultValueSupport() {
        return Arrays.asList(
                new PrimitiveValueGenerator(),
                new WrapperPrimitiveValueGenerator(),
                new ArrayValueGenerator(),
                new EnumValueGenerator(),
                JavaInternals.getSupported(),
                new InterfaceValueGenerator(),
                new ObjectValueGenerator(),
                ObjectValueGenerator.withForcedAccess()
        );
    }

    static AggregatedValueGenerator aggregatedDefaultValueSupport() {
        return new AggregatedSupportGenerator(defaultValueSupport());
    }

    static synchronized ValueCache valueCache() {
        if (valueCache == null) {
            valueCache = new ValueCache();
        }

        return valueCache;
    }

    static synchronized SharedResource getDefaultTestResource(Class<?> unitUnderAssertion) {
        if (defaultInstanceCreator == null) {
            ValueGenerator<Object> dependencyGenerator = new CachingDependencyGenerator(
                    aggregatedDefaultValueSupport(), valueCache());
            defaultInstanceCreator = new ConstructorInstanceCreator(dependencyGenerator, INSTANCE_DEPENDENCY_DEPTH);
        }

        return new SharedResource(
                Unit.of(unitUnderAssertion),
                defaultInstanceCreator,
                SingletonValueService.getInstance()
        );
    }
}
