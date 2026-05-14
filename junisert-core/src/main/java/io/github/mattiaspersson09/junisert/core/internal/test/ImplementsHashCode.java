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

import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.common.reflection.Field;
import io.github.mattiaspersson09.junisert.common.reflection.Unit;
import io.github.mattiaspersson09.junisert.common.reflection.util.Methods;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.ValueService;
import io.github.mattiaspersson09.junisert.core.internal.test.util.HashCode;

import java.util.List;


/**
 * Tests that a {@link Unit} overrides {@link Object#hashCode()} and that it's well implemented.
 */
public class ImplementsHashCode extends AbstractUnitTest<ImplementsHashCode> {
    private static final Logger LOGGER = Logger.getLogger("Implements HashCode");

    /**
     * Creates a new hashCode test with needed resources.
     *
     * @param valueService    providing value support with potentially caching abilities
     * @param instanceCreator of units
     */
    public ImplementsHashCode(ValueService valueService, InstanceCreator instanceCreator) {
        super(valueService, instanceCreator);
    }

    @Override
    public void test(Unit unit) {
        LOGGER.info("Testing unit: {0}", unit.getName());

        if (!unit.hasMethodMatching(Methods::isHashCodeMethod)) {
            LOGGER.fail(unit.getName() + ".hashCode() " + "was nowhere to be found",
                    "to have a hashCode method", "it was not found");
            throw new UnitAssertionError(unit.getName() + " was expected to implement the hashCode method");
        }

        Object instance = instanceCreator.createInstance(unit);
        Object instance2 = instanceCreator.createInstance(unit);

        LOGGER.info("Setting up fields for hash code comparison");

        List<Field> fields = unit.findFieldsMatching(exclusion::isNotExcluded);

        for (Field field : fields) {
            if (!unit.isImmutable()) {
                Object value = valueService.getValue(field.getType()).get();
                field.setValue(instance, value);
                field.setValue(instance2, value);
            }
        }

        HashCode.ofInstance(instance)
                .isConsistent()
                .isNotEmpty()
                .isEqualTo(instance2)
                .isNotEqualTo(() -> unit.isImmutable()
                        ? createEmptyImmutableInstance(unit)
                        : resetFieldsInInstance(fields, instance2));
    }

    private Object resetFieldsInInstance(List<Field> fields, Object instance) {
        for (Field field : fields) {
            Object value = valueService.getValue(field.getType()).asEmpty();
            field.setValue(instance, value);
        }

        return instance;
    }
}
