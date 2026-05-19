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
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertion;
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.common.logging.Logger;

import java.util.List;
import java.util.function.Predicate;

/**
 * Direct implementation of {@link ConstructorAssertion} API.
 *
 * @param <T> unit type owning the constructors under assertion
 */
class MultipleConstructorAssertion<T> extends AbstractAssertion<ConstructorAssertion<T>> implements ConstructorAssertion<T> {
    private static final Logger LOGGER = Logger.getLogger("Multiple Constructor Assertion");

    private final List<ConstructorAssertionImpl<T>> assertions;

    MultipleConstructorAssertion(List<ConstructorAssertionImpl<T>> assertions, AssertionResource assertionResource) {
        super(assertionResource);
        this.assertions = assertions;
    }

    @Override
    public ConstructorAssertion<T> hasAssignedProperty(String propertyName) throws UnitAssertionError {
        for (ConstructorAssertionImpl<T> assertion : assertions) {
            LOGGER.info("Asserting constructor: {0}", assertion.getConstructor());
            assertion.hasAssignedProperty(propertyName);
        }

        return this;
    }

    @Override
    public ConstructorAssertion<T> hasAssignedProperties(String... propertyNames) {
        for (ConstructorAssertionImpl<T> assertion : assertions) {
            LOGGER.info("Asserting constructor: {0}", assertion.getConstructor());
            assertion.hasAssignedProperties(propertyNames);
        }

        return this;
    }

    @Override
    public ConstructorAssertion<T> hasAssignedAllProperties() throws UnitAssertionError {
        for (ConstructorAssertionImpl<T> assertion : assertions) {
            LOGGER.info("Asserting constructor: {0}", assertion.getConstructor());
            assertion.hasAssignedAllProperties();
        }

        return this;
    }

    @Override
    public ConstructorAssertion<T> hasState(Predicate<T> state) throws UnitAssertionError {
        for (ConstructorAssertionImpl<T> assertion : assertions) {
            LOGGER.info("Asserting constructor: {0}", assertion.getConstructor());
            assertion.hasState(state);
        }

        return this;
    }

    @Override
    public ConstructorAssertion<T> hasState(Predicate<T> state, String description) throws UnitAssertionError {
        for (ConstructorAssertionImpl<T> assertion : assertions) {
            LOGGER.info("Asserting constructor: {0}", assertion.getConstructor());
            assertion.hasState(state, description);
        }

        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public UnitAssertion<T> assertThatUnit() {
        return new UnitAssertionImpl<>((Class<T>) getUnit().getType(), getAssertionResource());
    }
}
