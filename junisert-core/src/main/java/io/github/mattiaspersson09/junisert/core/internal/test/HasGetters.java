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
import io.github.mattiaspersson09.junisert.api.internal.service.ValueService;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Field;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Member;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Method;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Unit;
import io.github.mattiaspersson09.junisert.core.internal.reflection.util.Fields;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public final class HasGetters extends AbstractUnitTest {
    private static final Logger LOGGER = Logger.getLogger("Has Getters");

    HasGetters(ValueService valueService) {
        super(valueService);
    }

    public HasGetters(ValueService valueService, InstanceCreator instanceCreator) {
        super(valueService, instanceCreator);
    }

    @Override
    public void test(Unit unit) {
        LOGGER.info("Active convention: {0}", activeConvention.name());
        LOGGER.info("Testing unit: {0}", unit.getName());

        for (Field field : unit.getFields()) {
            if (!Fields.isInstanceField(field)) {
                continue;
            }

            LOGGER.info("Checking field: {0}", field);

            List<Method> getters = unit.findMethodsMatching(activeConvention.getterConvention(field));

            if (getters.isEmpty()) {
                throw new UnitAssertionError(String.format("%s was expected to have getter for field: %s, "
                        + "but none was found", unit.getName(), field.getName()));
            }

            for (Method method : getters) {
                if (method.isSynthetic()) {
                    continue;
                }

                Value<?> argument = valueService.getValue(field.getType());
                Object value = argument.get();

                UnaryOperator<Object> getterGetValue = (instance) -> {
                    try {
                        return method.invoke(instance);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new UnitAssertionError("Failed to invoke getter", e);
                    }
                };

                Injection injection = new Injection(method, instanceCreator);
                injection.setup(instance -> field.setValue(instance, value));
                injection.shouldResultIn(instance -> Objects.equals(value, getterGetValue.apply(instance)));
                injection.onInjectionFail(() -> new UnitAssertionError("Failed to invoke getter"));

                if (!injection.inject()) {
                    LOGGER.fail("Expected method to get value from field but it did not",
                            concatName(unit, method) + " to get value from " + concatName(unit, field),
                            "it did not");
                    throw new UnitAssertionError(
                            String.format("Found getter: %s, but it was not getting value from field: %s",
                                    method.getName(), field.getName()));
                }
            }
        }
    }

    private String concatName(Unit unit, Member member) {
        return unit.getName() + "." + member;
    }
}
