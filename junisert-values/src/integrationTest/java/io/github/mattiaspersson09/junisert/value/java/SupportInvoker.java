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
package io.github.mattiaspersson09.junisert.value.java;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.logging.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Invokes support classes to ensure that they are functional and gets test coverage.
 */
class SupportInvoker {
    private static final Logger LOGGER = Logger.getLogger(SupportInvoker.class);

    private final Map<Class<?>, Object> usedArguments;
    private final Collection<ValueGenerator<?>> supportArgumentGenerators;

    SupportInvoker(Collection<ValueGenerator<?>> supportArgumentGenerators) {
        this.supportArgumentGenerators = supportArgumentGenerators;
        this.usedArguments = new HashMap<>();
    }

    void invoke(Class<?> support, Supplier<?> instance) {
        LOGGER.test("invoking: {0}", support.getSimpleName());
        Object object = instance.get();

        for (Method method : support.getMethods()) {
            if (Modifier.isStatic(method.getModifiers())) {
                LOGGER.info("Ignoring method: {0}", method.getName());
                continue;
            }

            LOGGER.test("-> {0}", method.getName());

            Object[] args = Arrays.stream(method.getParameters())
                    .map(this::getArgumentValue)
                    .toArray();

            try {
                method.invoke(object, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new AssertionError(e);
            }
        }
    }

    void invokeVisible(Class<?> support, Supplier<?> instance) {
        LOGGER.test("invoking: {0}", support.getSimpleName());
        Object object = instance.get();

        for (Method method : support.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers())
                    || !Modifier.isPublic(method.getModifiers())) {
                LOGGER.info("Ignoring method: {0}", method.getName());
                continue;
            }

            LOGGER.test("-> {0}", method.getName());

            Object[] args = Arrays.stream(method.getParameters())
                    .map(this::getArgumentValue)
                    .toArray();

            try {
                method.invoke(object, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new AssertionError(e);
            }
        }
    }

    private Object getArgumentValue(Class<?> argument) {
        if (usedArguments.containsKey(argument)) {
            return usedArguments.get(argument);
        }

        Object argumentValue = supportArgumentGenerators.stream()
                .filter(gen -> gen.supports(argument))
                .map(gen -> gen.generate(argument))
                .findAny()
                .orElseThrow(() -> new UnsupportedTypeError(argument))
                .get();

        usedArguments.put(argument, argumentValue);

        return argumentValue;
    }

    private Object getArgumentValue(Parameter parameter) {
        return getArgumentValue(parameter.getType());
    }
}
