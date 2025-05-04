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

import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.api.internal.service.ValueService;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.core.reflection.Field;
import io.github.mattiaspersson09.junisert.core.reflection.Unit;
import io.github.mattiaspersson09.junisert.core.reflection.util.Methods;

import java.util.Objects;

public class ImplementsEquals implements UnitTest {
    private static final Logger LOGGER = Logger.getLogger("Implements Equals");

    private final ValueService valueService;

    public ImplementsEquals(ValueService valueService) {
        this.valueService = valueService;
    }

    @Override
    public void test(Unit unit) {
        if (!hasDeclaredEqualsMethod(unit)) {
            LOGGER.fail(details(unit, "was nowhere to be found"), "to have an equals method", "it was not found");
            throw new UnitAssertionError(unit.getName() + " was expected to implement the equals method");
        }

        InstanceCreator instanceCreator = new InstanceCreator();
        Object instance = instanceCreator.createInstance(unit);
        Object instance2 = instanceCreator.createInstance(unit);

        if (!isPassingNullCheck(instance)) {
            LOGGER.fail(details(unit, "fails null check"), "to not equal null objects", "it did");
            throw new UnitAssertionError("Was expected to NOT equal null objects");
        }

        // Boilerplate usually adds getClass() check, lowering code coverage without this check
        if (!isPassingTypeCheck(instance)) {
            LOGGER.fail(details(unit, "fails type check"), "to not equal objects of other types", "it did");
            throw new UnitAssertionError("Was expected to NOT equal other object types");
        }

        LOGGER.info("Setting up fields for equality comparison");

        for (Field field : unit.getFields()) {
            if (field.isSynthetic() || field.modifier().isStatic()) {
                continue;
            }

            Object value = valueService.getValue(field.getType()).get();

            if (!field.setValue(instance, value)) {
                LOGGER.warn("Failed to invoke field {0} with value of type {1}", field.getName(), value.getClass());
                throw new UnitAssertionError("Failed to setup field values");
            }

            if (!field.setValue(instance2, value)) {
                LOGGER.warn("Failed to invoke field {0} with value of type {1}", field.getName(), value.getClass());
                throw new UnitAssertionError("Failed to setup field values");
            }
        }

        if (!isPassingSelfCheck(instance, instance)) {
            LOGGER.fail(details(unit, "fails self check"), "to equal reference of itself", "it did not");
            throw new UnitAssertionError("Was expected to equal itself as reference");
        }

        if (!isPassingSymmetryCheck(instance, instance2)) {
            LOGGER.fail(details(unit, "fails symmetry check"), "to be symmetric", "it was not");
            throw new UnitAssertionError("Was expected to equal other instances with the same values");
        }

        if (!isPassingConsistencyCheck(instance, instance2)) {
            LOGGER.fail(details(unit, "fails consistency check"), "to be consistent", "it was not");
            throw new UnitAssertionError("Was expected to be return the same result every time");
        }

        // Passing other sanity checks, different field value instances should not be equal
        for (Field field : unit.getFields()) {
            if (field.isSynthetic() || field.modifier().isStatic()) {
                continue;
            }

            Object value = valueService.getValue(field.getType()).asEmpty();

            if (!field.setValue(instance2, value)) {
                LOGGER.warn("Failed to reset field {0}", field.getName());
                throw new UnitAssertionError("Failed to reset field values");
            }
        }

        if (instance.equals(instance2) || instance2.equals(instance)) {
            LOGGER.fail(details(unit, "fails negative check"), "to not equal instance with other values", "it did");
            throw new UnitAssertionError("Was expected to NOT equal another instance with different values");
        }
    }

    private boolean hasDeclaredEqualsMethod(Unit unit) {
        return unit.getMethods()
                .stream()
                .anyMatch(Methods::isEqualsMethod);
    }

    private boolean isPassingSelfCheck(Object instance, Object selfReference) {
        LOGGER.test("Self check -> instance.equals(instance)");
        return instance.equals(selfReference);
    }

    private boolean isPassingSymmetryCheck(Object instance, Object instance2) {
        LOGGER.test("Symmetry check -> instance.equals(otherInstance)");
        return instance.equals(instance2) && instance2.equals(instance);
    }

    private boolean isPassingConsistencyCheck(Object instance, Object instance2) {
        LOGGER.test("Consistency check -> instance.equals(otherInstance) x 2");
        return isPassingSymmetryCheck(instance, instance2) && isPassingSymmetryCheck(instance, instance2);
    }

    private boolean isPassingTypeCheck(Object instance) {
        LOGGER.test("Type check -> instance.equals(nonRelatedInstance)");
        return !instance.equals(new NonEqualClass());
    }

    private boolean isPassingNullCheck(Object instance) {
        LOGGER.test("Null check -> instance.equals(null)");
        return !Objects.equals(instance, null);
    }

    private String details(Unit unit, String detail) {
        return unit.getName() + ".equals(...) " + detail;
    }

    private static class NonEqualClass {
    }
}
