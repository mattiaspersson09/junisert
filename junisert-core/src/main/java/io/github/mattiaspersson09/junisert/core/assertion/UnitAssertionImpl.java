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
package io.github.mattiaspersson09.junisert.core.assertion;

import io.github.mattiaspersson09.junisert.api.assertion.ConstructorAssertion;
import io.github.mattiaspersson09.junisert.api.assertion.PlainObjectAssertion;
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertion;
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.common.reflection.Constructor;
import io.github.mattiaspersson09.junisert.common.reflection.Unit;
import io.github.mattiaspersson09.junisert.common.reflection.util.Methods;
import io.github.mattiaspersson09.junisert.core.internal.test.HasGetters;
import io.github.mattiaspersson09.junisert.core.internal.test.HasSetters;
import io.github.mattiaspersson09.junisert.core.internal.test.strategy.TestStrategy;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Direct implementation of {@link UnitAssertion} API.
 *
 * @param <T> unit type
 */
public class UnitAssertionImpl<T> extends AbstractAssertion<UnitAssertion<T>> implements UnitAssertion<T> {
    private static final Logger LOGGER = Logger.getLogger("Unit Assertion");
    private final Class<T> unitClass;

    /**
     * Creates a new implementation of {@link PlainObjectAssertion}.
     *
     * @param unitClass         unit under assertion, providing type safety
     * @param assertionResource needed for assertions
     */
    public UnitAssertionImpl(Class<T> unitClass, AssertionResource assertionResource) {
        super(assertionResource);
        this.unitClass = unitClass;
    }

    @Override
    public PlainObjectAssertion asPojo() {
        return new PlainObjectAssertionImpl(getAssertionResource());
    }

    @Override
    public UnitAssertion<T> isJavaBeanCompliant() throws UnitAssertionError {
        Unit unit = getUnit();

        if (unit.hasNoDefaultConstructor()) {
            throw new UnitAssertionError(unit.getName() + " were expected to have a default constructor");
        }

        if (unit.hasFieldMatching(field -> getExclusion().isNotExcluded(field) && !field.modifier().isPrivate())) {
            throw new UnitAssertionError(unit.getName() + " were expected to only have private properties");
        }

        TestStrategy beanTestStrategy = TestStrategy.javaBeanCompliant();

        createTest(HasGetters.class)
                .withTestStrategy(beanTestStrategy)
                .test(unit);

        createTest(HasSetters.class)
                .withTestStrategy(beanTestStrategy)
                .test(unit);

        if (!Serializable.class.isAssignableFrom(unit.getType())) {
            LOGGER.warn("{0} should implement {1}, it is not enforced but recommended to ensure serialization",
                    unit.getName(), Serializable.class.getName());
        }

        if (!unit.hasMethodMatching(Methods::isEqualsMethod)) {
            LOGGER.warn("{0} should override equals(Object)", unit.getName());
        }

        if (!unit.hasMethodMatching(Methods::isHashCodeMethod)) {
            LOGGER.warn("{0} should override hashCode()", unit.getName());
        }

        if (!unit.hasMethodMatching(Methods::isToStringMethod)) {
            LOGGER.warn("{0} should override toString()", unit.getName());
        }

        return this;
    }

    @Override
    public UnitAssertion<T> isImmutable() throws UnitAssertionError {
        Unit unit = getUnit();

        if (!unit.isImmutable()) {
            throw new UnitAssertionError(unit.getName() + " were expected to be immutable, but it wasn't");
        }

        return this;
    }

    @Override
    public ConstructorAssertion<T> whenCreatedFromConstructor(Class<?>... parameters) {
        Constructor constructor = getUnit().findConstructorsMatching(c -> c.hasParameters(parameters))
                .stream()
                .findFirst()
                .orElseThrow(() -> new UnitAssertionError(String.format("Couldn't find a matching constructor"
                        + "\nExpected unit: %s\nTo have a constructor with parameters: %s",
                        getUnit().getName(), Arrays.asList(parameters))));

        return new ConstructorAssertionImpl<>(constructor, getAssertionResource());
    }

    @Override
    public ConstructorAssertion<T> whenCreatedFromConstructor(Predicate<Constructor> filter) throws UnitAssertionError {
        List<Constructor> constructors = getUnit().findConstructorsMatching(filter);

        if (constructors.isEmpty()) {
            throw new UnitAssertionError(String.format("Couldn't find any matching constructor"
                    + "\nExpected unit: %s\nTo have any constructor matching given filter", getUnit().getName()));
        }

        AssertionResource assertionResource = getAssertionResource();

        if (constructors.size() == 1) {
            return new ConstructorAssertionImpl<>(constructors.get(0), assertionResource);
        }

        List<ConstructorAssertionImpl<T>> assertions = constructors.stream()
                .map(constructor -> new ConstructorAssertionImpl<T>(constructor, assertionResource))
                .collect(Collectors.toList());

        return new MultipleConstructorAssertion<>(assertions, assertionResource);
    }
}
