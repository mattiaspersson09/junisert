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

import java.util.List;
import java.util.Optional;

/**
 * Tests that a {@link Unit} overrides {@link Object#toString()} and that it's well implemented.
 */
public class ImplementsToString implements UnitTest {
    private static final Logger LOGGER = Logger.getLogger("Implements ToString");

    private final ValueService valueService;
    private final InstanceCreator instanceCreator;

    /**
     * Creates a new toString test with needed resources.
     *
     * @param valueService    providing value support with potentially caching abilities
     * @param instanceCreator of units
     */
    public ImplementsToString(ValueService valueService, InstanceCreator instanceCreator) {
        this.valueService = valueService;
        this.instanceCreator = instanceCreator;
    }

    @Override
    public void test(Unit unit) {
        LOGGER.info("Testing unit: {0}", unit.getName());

        if (!unit.hasMethodMatching(Methods::isToStringMethod)) {
            LOGGER.fail(details(unit, "was nowhere to be found"), "to have a toString method", "it was not found");
            throw new UnitAssertionError(unit.getName() + " was expected to implement the toString method");
        }

        Object instance = instanceCreator.createInstance(unit);

        if (!containsUnitName(instance, unit)) {
            LOGGER.fail(details(unit, "fails unit name check"), "to contain unit name", "it did not");
            throw new UnitAssertionError("Was expected to contain '" + unit.getName() + "'");
        }

        List<Field> instanceFields = unit.findFieldsMatching(Field::isInstanceMember);

        for (Field field : instanceFields) {
            Object value = valueService.getValue(field.getType()).get();

            field.setValue(instance, value);

            if (!containsField(instance, field)) {
                String valueString = toString(value);

                LOGGER.fail(details(unit, "fails field check"),
                        "to contain '" + field.getName() + "=" + valueString + "'",
                        "it did not");
                throw new UnitAssertionError("Was expected to contain '" + field.getName() + "=" + valueString + "'");
            }
        }
    }

    private boolean containsUnitName(Object instance, Unit unit) {
        LOGGER.test("Unit name check -> instance.toString().contains(\"{0}\")", unit.getName());
        return toString(instance).contains(unit.getName());
    }

    private boolean containsField(Object instance, Field field) {
        Object value = field.getValue(instance);
        String valueString = toString(value);
        String toString = toString(instance);

        LOGGER.test("Field check -> instance.toString().contains(\"{0}={1}\")", field.getName(), valueString);

        return toString.contains(field.getName() + "=" + valueString)
                || toString.contains(field.getName() + "='" + valueString + "'");
    }

    private String toString(Object object) {
        return Optional.ofNullable(object)
                .map(obj -> {
                    // Would otherwise show hash variant of the array instead of actual values
                    // At this point, all arrays should be empty
                    // Arrays.toString((Object[]) obj) is not possible for primitive arrays because of class cast issue
                    if (obj.getClass().isArray()) {
                        return "[]";
                    }

                    return obj.toString();
                })
                .orElse("");
    }

    private String details(Unit unit, String detail) {
        return unit.getName() + ".toString() " + detail;
    }
}
