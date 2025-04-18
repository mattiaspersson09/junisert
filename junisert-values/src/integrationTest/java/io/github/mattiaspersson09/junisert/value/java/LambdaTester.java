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

class LambdaTester {
    private static final Logger LOGGER = Logger.getLogger(LambdaTester.class);

    private final Map<Class<?>, Object> usedArguments;
    private final Collection<ValueGenerator<?>> functionalArgumentGenerators;

    LambdaTester(Collection<ValueGenerator<?>> functionalArgumentGenerators) {
        this.functionalArgumentGenerators = functionalArgumentGenerators;
        this.usedArguments = new HashMap<>();
    }

    public void invoke(Class<?> functional, Supplier<Object> lambdaObject) {
        if (!functional.isAnnotationPresent(FunctionalInterface.class)) {
            LOGGER.info("Not a functional interface, ignoring: {0}", functional);
            return;
        }

        LOGGER.test("invoking: {0}", functional.getSimpleName());
        Object lambda = lambdaObject.get();

        for (Method method : functional.getMethods()) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            LOGGER.test("-> {0}", method.getName());

            Object[] args = Arrays.stream(method.getParameters())
                    .map(this::getArgumentValue)
                    .toArray();

            try {
                method.invoke(lambda, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new AssertionError(e);
            }
        }
    }

    private Object getArgumentValue(Class<?> argument) {
        if (usedArguments.containsKey(argument)) {
            return usedArguments.get(argument);
        }

        Object argumentValue = functionalArgumentGenerators.stream()
                .filter(gen -> gen.supports(argument))
                .map(gen -> gen.generate(argument))
                .findAny()
                .orElseThrow(RuntimeException::new)
                .get();

        usedArguments.put(argument, argumentValue);

        return argumentValue;
    }

    private Object getArgumentValue(Parameter parameter) {
        return getArgumentValue(parameter.getType());
    }
}
