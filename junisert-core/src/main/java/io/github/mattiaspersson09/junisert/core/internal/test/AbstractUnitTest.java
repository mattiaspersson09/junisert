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
package io.github.mattiaspersson09.junisert.core.internal.test;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.ValueService;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Constructor;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Unit;
import io.github.mattiaspersson09.junisert.core.internal.test.strategy.TestStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Base class for {@link UnitTest}'s, holding needed resources for performing tests.
 */
public abstract class AbstractUnitTest implements UnitTest {
    /**
     * Providing value support with potentially caching abilities.
     */
    protected final ValueService valueService;
    /**
     * Creates instances of {@link Unit}'s.
     */
    protected final InstanceCreator instanceCreator;
    /**
     * Active testing strategy for units.
     */
    protected TestStrategy testStrategy;

    /**
     * Creates a new abstract unit test with needed resources.
     *
     * @param valueService    providing value support with potentially caching abilities
     * @param instanceCreator of units
     */
    public AbstractUnitTest(ValueService valueService, InstanceCreator instanceCreator) {
        this.valueService = valueService;
        this.instanceCreator = instanceCreator;
        this.testStrategy = TestStrategy.none();
    }

    /**
     * Sets active testing strategy.
     *
     * @param testStrategy to be used during test
     */
    public final void setTestStrategy(TestStrategy testStrategy) {
        this.testStrategy = testStrategy;
    }

    /**
     * Creates en empty instance of an immutable unit from a constructor having parameters,
     * meaning only negative/empty values will be used as arguments.<br>
     * If given {@code unit} is not immutable this might produce unwanted results.
     *
     * @param unit to create instance of
     * @return instance with empty properties
     * @throws UnsupportedConstructionError if unable to find argument constructor
     */
    protected Object createEmptyImmutableInstance(Unit unit) throws UnsupportedConstructionError {
        Constructor constructor = unit.findConstructorsMatching(Constructor::hasParameters)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UnsupportedConstructionError(unit.getType()));

        Object[] argumentValues = constructor.getParameterTypes()
                .stream()
                .map(valueService::getValue)
                .map(Value::asEmpty)
                .toArray();

        return constructor.invoke(null, argumentValues);
    }

    /**
     * Creates every instance combination of an immutable unit from a constructor having parameters
     * (except all negatives/empty), starting with only positive values for every argument.<br>
     * If given {@code unit} is not immutable this might produce unwanted results.
     *
     * @param unit to create instances of
     * @return list of every possible instance combination
     * @throws UnsupportedConstructionError if unable to find any argument constructor
     */
    protected List<Object> createImmutableInstances(Unit unit) throws UnsupportedConstructionError {
        List<Object> instances = new ArrayList<>();

        Constructor constructor = unit.findConstructorsMatching(Constructor::hasParameters)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UnsupportedConstructionError(unit.getType()));

        List<Value<?>> parameterValues = constructor.getParameterTypes()
                .stream()
                .map(valueService::getValue)
                .collect(Collectors.toList());

        List<Object> values = parameterValues.stream()
                .map(Value::get)
                .collect(Collectors.toList());

        // Default instance with all parameters getting positive values
        instances.add(constructor.invoke(null, values.toArray()));

        // Create different instances with empty value for every constructor parameter
        Value<?> previousValue = null;
        for (int i = 0; i < parameterValues.size(); i++) {
            Value<?> value = parameterValues.get(i);

            values.set(i, value.asEmpty());
            instances.add(constructor.invoke(null, values.toArray()));

            if (previousValue != null) {
                values.set(i - 1, previousValue.get());
                instances.add(constructor.invoke(null, values.toArray()));
            }

            previousValue = value;
        }

        return instances;
    }
}
