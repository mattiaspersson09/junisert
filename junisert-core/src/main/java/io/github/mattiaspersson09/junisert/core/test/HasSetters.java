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
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.core.reflection.Field;
import io.github.mattiaspersson09.junisert.core.reflection.Method;
import io.github.mattiaspersson09.junisert.core.reflection.Unit;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class HasSetters implements UnitTest {
    private static final Logger LOGGER = Logger.getLogger(HasSetters.class);

    private final ValueService valueService;

    public HasSetters(ValueService valueService) {
        this.valueService = valueService;
    }

    @Override
    public boolean test(Unit unit) {
        for (Field field : unit.getFields()) {
            // Ignores autogenerated synthetic fields (i.e. JaCoCo fields for data and such)
            if (field.isSynthetic()) {
                continue;
            }

            if (field.getSetters().isEmpty()) {
                throw new UnitAssertionError(String.format("%s was expected to have setter for field: %s, "
                        + "but none was found", unit.getName(), field.getName()));
            }

            LOGGER.info("Checking field: {0} -> {1}", unit.getName(), field.getName());

            if (!anySetterSetsValue(unit, field)) {
                throw new UnitAssertionError(String.format("Found setter(s): %s  - but none were working for field: %s",
                        getSetterNames(field), field.getName()));
            }
        }

        return true;
    }

    private boolean anySetterSetsValue(Unit unit, Field field) {
        for (Method method : field.getSetters()) {
            // Ignores autogenerated synthetic methods (i.e. JaCoCo methods and such)
            if (method.isSynthetic()) {
                continue;
            }

            Value<?> argument = valueService.getValue(field.getType());
            Injection injection = new Injection(unit, method);

            Object value = argument.get();
            Object empty = argument.asEmpty();

            injection.setup(instance -> field.setValue(instance, empty));
            injection.shouldResultIn(instance -> !Objects.equals(empty,
                    field.getValueOrElse(instance, empty)));
            injection.onInjectionError(() -> new UnitAssertionError("Failed to invoke setter"));

            if (injection.inject(value)) {
                return true;
            }

            LOGGER.fail("Expected method to set value for field but it did not",
                    concatName(unit, method) + " to set value for " + concatName(unit, field),
                    "it did not");
        }

        return false;
    }

    private Collection<String> getSetterNames(Field field) {
        return field.getSetters()
                .stream()
                .map(Method::getName)
                .collect(Collectors.toList());
    }

    private String concatName(Unit unit, Method method) {
        return unit.getName() + "." + method.getName() + "(...)";
    }

    private String concatName(Unit unit, Field field) {
        return unit.getName() + "." + field.getName();
    }
}
