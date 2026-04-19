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
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.ValueService;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Field;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Unit;
import io.github.mattiaspersson09.junisert.core.internal.reflection.util.Methods;
import io.github.mattiaspersson09.junisert.core.internal.test.util.Equals;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Tests that a {@link Unit} overrides {@link Object#equals(Object)} and that it's well implemented.
 */
public class ImplementsEquals extends AbstractUnitTest {
    private static final Logger LOGGER = Logger.getLogger("Implements Equals");
    private static final int TIMES_CONSISTENCY_CHECK = 3;

    /**
     * Creates a new equals test with needed resources.
     *
     * @param valueService    providing value support with potentially caching abilities
     * @param instanceCreator of units
     */
    public ImplementsEquals(ValueService valueService, InstanceCreator instanceCreator) {
        super(valueService, instanceCreator);
    }

    @Override
    public void test(Unit unit) {
        LOGGER.info("Testing unit: {0}", unit.getName());

        if (!unit.hasMethodMatching(Methods::isEqualsMethod)) {
            LOGGER.fail(unit.getName() + ".equals(Object) " + "was nowhere to be found",
                    "to have an equals method", "it was not found");
            throw new UnitAssertionError(unit.getName() + " was expected to implement the equals method");
        }

        Object instance = instanceCreator.createInstance(unit);
        Object instance2 = instanceCreator.createInstance(unit);
        Object instance3 = instanceCreator.createInstance(unit);

        LOGGER.info("Setting up fields for equality comparison");

        for (Field field : unit.getFields()) {
            if (!field.isInstanceMember()) {
                continue;
            }

            if (!unit.isImmutable()) {
                Object value = valueService.getValue(field.getType()).get();
                field.setValue(instance, value);
                field.setValue(instance2, value);
                field.setValue(instance3, value);
            }
        }

        Equals equals = Equals.ofInstance(instance)
                .loggingOn()
                .isReflexive()
                .isSymmetricWith(instance2)
                .isTransitiveWith(instance2, instance3)
                .isConsistentWith(instance2, TIMES_CONSISTENCY_CHECK)
                .isNotSymmetricWith((Object) null)
                .isNotSymmetricWith(new Object());

        if (unit.isImmutable()) {
            List<Object> instances = createImmutableInstances(unit).stream()
                    .skip(1)
                    .collect(Collectors.toList());

            for (Object otherInstance : instances) {
                equals.loggingOff()
                        .isNotSymmetricWith(otherInstance)
                        .loggingOn();
            }
            return;
        }

        Field previousField = null;
        for (Field field : unit.getFields()) {
            if (!field.isInstanceMember()) {
                continue;
            }

            Object value = valueService.getValue(field.getType()).asEmpty();
            field.setValue(instance2, value);
            equals.loggingOff()
                    .isNotSymmetricWith(instance2)
                    .loggingOn();

            if (previousField != null) {
                previousField.setValue(instance2, valueService.getValue(previousField.getType()).get());
                equals.loggingOff()
                        .isNotSymmetricWith(instance2)
                        .loggingOn();
            }

            previousField = field;
        }
    }
}
