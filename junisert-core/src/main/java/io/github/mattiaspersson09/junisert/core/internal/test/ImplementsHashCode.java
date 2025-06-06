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
package io.github.mattiaspersson09.junisert.core.internal.test;

import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.ValueService;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Field;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Unit;
import io.github.mattiaspersson09.junisert.core.internal.reflection.util.Methods;

import java.util.Objects;

/**
 * Tests that a {@link Unit} overrides {@link Object#hashCode()} and that it's well implemented.
 */
public class ImplementsHashCode implements UnitTest {
    private static final Logger LOGGER = Logger.getLogger("Implements HashCode");

    private final ValueService valueService;
    private final InstanceCreator instanceCreator;

    /**
     * Creates a new hashCode test with needed resources.
     *
     * @param valueService    providing value support with potentially caching abilities
     * @param instanceCreator of units
     */
    public ImplementsHashCode(ValueService valueService, InstanceCreator instanceCreator) {
        this.valueService = valueService;
        this.instanceCreator = instanceCreator;
    }

    @Override
    public void test(Unit unit) {
        LOGGER.info("Testing unit: {0}", unit.getName());

        if (!unit.hasMethodMatching(Methods::isHashCodeMethod)) {
            LOGGER.fail(details(unit, "was nowhere to be found"), "to have a hashCode method", "it was not found");
            throw new UnitAssertionError(unit.getName() + " was expected to implement the hashCode method");
        }

        Object instance = instanceCreator.createInstance(unit);
        Object instance2 = instanceCreator.createInstance(unit);

        LOGGER.info("Setting up fields for hash code comparison");

        for (Field field : unit.getFields()) {
            if (!field.isInstanceMember()) {
                continue;
            }

            Object value = valueService.getValue(field.getType()).get();

            field.setValue(instance, value);
            field.setValue(instance2, value);
        }

        if (!isPassingEmptyHashCodeCheck(instance)) {
            LOGGER.fail(details(unit, "fails empty hash code check"),
                    "to not equal empty hash codes",
                    "it did");
            throw new UnitAssertionError("Was expected to not return empty hash code");
        }

        if (!isPassingConsistencyCheck(instance)) {
            LOGGER.fail(details(unit, "fails consistency check"),
                    "to consistently return the same hash code",
                    "it did not");
            throw new UnitAssertionError("Was expected to have consistent hash code");
        }

        if (!isPassingUniqueHashCodeCheck(instance, instance2)) {
            LOGGER.fail(details(unit, "fails uniqueness check"),
                    "to have the same unique hash code for equal instances",
                    "it did not");
            throw new UnitAssertionError("Was expected to have equal unique hash code");
        }

        LOGGER.info("Resetting fields for an instance");

        for (Field field : unit.getFields()) {
            if (!field.isInstanceMember()) {
                continue;
            }

            Object value = valueService.getValue(field.getType()).asEmpty();
            field.setValue(instance2, value);
        }

        if (isPassingUniqueHashCodeCheck(instance, instance2)) {
            LOGGER.fail(details(unit, "fails uniqueness check"),
                    "to have unique hash code for instances with different values",
                    "it did not");
            throw new UnitAssertionError("Was expected to have unique hash code");
        }
    }

    private boolean isPassingConsistencyCheck(Object instance) {
        LOGGER.test("Consistency check -> instance.hashCode() == instance.hashCode()");
        return instance.hashCode() == instance.hashCode();
    }

    private boolean isPassingEmptyHashCodeCheck(Object instance) {
        LOGGER.test("Empty hash code check -> instance.hashCode() != Objects.hashCode(null)");
        return instance.hashCode() != Objects.hashCode(null);
    }

    private boolean isPassingUniqueHashCodeCheck(Object instance, Object instance2) {
        LOGGER.test("Unique hash code check -> instance.hashCode() == otherInstance.hashCode()");
        return instance.hashCode() == instance2.hashCode();
    }

    private String details(Unit unit, String detail) {
        return unit.getName() + ".hashCode() " + detail;
    }
}
