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
package io.github.mattiaspersson09.junisert.core.assertion;

import io.github.mattiaspersson09.junisert.api.assertion.PlainObjectAssertion;
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertion;
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.api.internal.service.ValueService;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.core.internal.AssertionResource;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Unit;
import io.github.mattiaspersson09.junisert.core.internal.reflection.util.Fields;
import io.github.mattiaspersson09.junisert.core.internal.reflection.util.Methods;
import io.github.mattiaspersson09.junisert.core.internal.test.HasGetters;
import io.github.mattiaspersson09.junisert.core.internal.test.HasSetters;
import io.github.mattiaspersson09.junisert.core.internal.test.strategy.TestStrategy;

import java.io.Serializable;

/**
 * Direct implementation of {@link UnitAssertion} API.
 */
public class UnitAssertionImpl implements UnitAssertion {
    private static final Logger LOGGER = Logger.getLogger("Unit assertion");

    private final AssertionResource assertionResource;

    /**
     * Creates a new implementation of {@link PlainObjectAssertion}.
     *
     * @param assertionResource needed for assertions
     */
    public UnitAssertionImpl(AssertionResource assertionResource) {
        this.assertionResource = assertionResource;
    }

    @Override
    public PlainObjectAssertion asPojo() {
        return new PlainObjectAssertionImpl(assertionResource);
    }

    @Override
    public UnitAssertion isJavaBeanCompliant() throws UnitAssertionError {
        Unit unit = assertionResource.getUnitUnderAssertion();
        ValueService valueService = assertionResource.getValueService();
        InstanceCreator instanceCreator = assertionResource.getInstanceCreator();

        if (unit.hasNoDefaultConstructor()) {
            throw new UnitAssertionError(unit.getName() + " were expected to have a default constructor");
        }

        if (unit.hasFieldMatching(field -> Fields.isInstanceField(field) && !field.modifier().isPrivate())) {
            throw new UnitAssertionError(unit.getName() + " were expected to only have private properties");
        }

        TestStrategy beanTestStrategy = TestStrategy.javaBeanCompliant();

        HasGetters hasGetters = new HasGetters(valueService, instanceCreator);
        hasGetters.setTestStrategy(beanTestStrategy);
        hasGetters.test(unit);

        HasSetters hasSetters = new HasSetters(valueService, instanceCreator);
        hasSetters.setTestStrategy(beanTestStrategy);
        hasSetters.test(unit);

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
}
