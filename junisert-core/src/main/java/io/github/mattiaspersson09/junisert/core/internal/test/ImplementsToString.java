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
import io.github.mattiaspersson09.junisert.core.internal.test.util.ToString;

import java.util.List;

/**
 * Tests that a {@link Unit} overrides {@link Object#toString()} and that it's well implemented.
 */
public class ImplementsToString extends AbstractUnitTest {
    private static final Logger LOGGER = Logger.getLogger("Implements ToString");

    /**
     * Creates a new toString test with needed resources.
     *
     * @param valueService    providing value support with potentially caching abilities
     * @param instanceCreator of units
     */
    public ImplementsToString(ValueService valueService, InstanceCreator instanceCreator) {
        super(valueService, instanceCreator);
    }

    @Override
    public void test(Unit unit) {
        LOGGER.info("Testing unit: {0}", unit.getName());

        if (!unit.hasMethodMatching(Methods::isToStringMethod)) {
            LOGGER.fail(details(unit, "was nowhere to be found"), "to have a toString method", "it was not found");
            throw new UnitAssertionError(unit.getName() + " was expected to implement the toString method");
        }

        Object instance = instanceCreator.createInstance(unit);
        ToString unitToString = new ToString(instance);

        LOGGER.test("Unit name check -> instance.toString() contains: {0}", unit.getName());

        if (!unitToString.contains(unit.getName())) {
            LOGGER.fail(details(unit, "fails unit name check"), "to contain unit name", "it did not");
            throw new UnitAssertionError("Was expected to contain '" + unit.getName() + "'");
        }

        List<Field> instanceFields = unit.findFieldsMatching(Field::isInstanceMember);

        for (Field field : instanceFields) {
            Object value = valueService.getValue(field.getType()).get();

            if (!unit.isImmutable()) {
                field.setValue(instance, value);
            }

            LOGGER.test("Field check -> instance.toString() contains with value: {0}", field.getName());

            if (!unitToString.contains(field, value)) {
                String valueString = ToString.valueOf(value);

                LOGGER.fail(details(unit, "fails field check"),
                        "to contain field '" + field.getName() + "' with value '" + valueString + "'",
                        "it did not");
                throw new UnitAssertionError("toString were expected to contain (together): field " + field.getName()
                        + ", any operator of (=, :) and value " + valueString);
            }
        }
    }

    private String details(Unit unit, String detail) {
        return unit.getName() + ".toString() " + detail;
    }
}
