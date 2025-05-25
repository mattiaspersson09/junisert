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
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.ValueService;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Field;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Method;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Unit;

import java.util.List;
import java.util.Objects;

/**
 * Tests that a {@link Unit} has a working setter for every non-synthetic instance field.
 */
public class HasSetters extends AbstractUnitTest {
    private static final Logger LOGGER = Logger.getLogger("Has Setters");

    /**
     * Creates a new setter test with needed resources.
     *
     * @param valueService    providing value support with potentially caching abilities
     * @param instanceCreator of units
     */
    public HasSetters(ValueService valueService, InstanceCreator instanceCreator) {
        super(valueService, instanceCreator);
    }

    @Override
    public void test(Unit unit) {
        LOGGER.info("Active test strategy: {0}", testStrategy.name());
        LOGGER.info("Testing unit: {0}", unit.getName());

        for (Field field : unit.getFields()) {
            if (!field.isInstanceMember()) {
                continue;
            }

            LOGGER.info("Checking field: {0}", field);

            List<Method> setters = unit.findMethodsMatching(testStrategy.isSetterForField(field)
                    .and(Method::isInstanceMember));

            if (setters.isEmpty()) {
                throw new UnitAssertionError(String.format("%s was expected to have setter for instance field: %s, "
                        + "but none was found", unit.getName(), field.getName()));
            }

            for (Method method : setters) {
                Value<?> fieldValue = valueService.getValue(field.getType());
                Object[] methodArguments = method.getParameterTypes()
                        .stream()
                        .map(valueService::getValue)
                        .map(Value::get)
                        .toArray();
                Object empty = fieldValue.asEmpty();

                Injection injection = new Injection(method, instanceCreator);
                injection.setup(instance -> field.setValue(instance, empty));
                injection.shouldResultIn(instance -> !Objects.equals(empty, field.getValue(instance)));

                if (!injection.inject(methodArguments)) {
                    LOGGER.fail("Expected method to set value for field but it did not",
                            method + " to set value for " + field,
                            "it did not");
                    throw new UnitAssertionError(String.format("Found setter: %s, but it was not setting for field: %s",
                            method.getName(), field.getName()));
                }
            }
        }
    }
}
