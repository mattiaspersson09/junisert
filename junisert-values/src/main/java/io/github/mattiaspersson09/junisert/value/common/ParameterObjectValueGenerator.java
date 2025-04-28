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
package io.github.mattiaspersson09.junisert.value.common;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class ParameterObjectValueGenerator implements ValueGenerator<Object> {
    private final ValueGenerator<Object> argumentGenerator;
    private final boolean forceConstructorAccess;

    public ParameterObjectValueGenerator(ValueGenerator<Object> argumentGenerator) {
        this.argumentGenerator = argumentGenerator;
        this.forceConstructorAccess = false;
    }

    private ParameterObjectValueGenerator(ValueGenerator<Object> argumentGenerator, boolean forceConstructorAccess) {
        this.argumentGenerator = argumentGenerator;
        this.forceConstructorAccess = forceConstructorAccess;
    }

    public static ParameterObjectValueGenerator withForcedAccess(ValueGenerator<Object> argumentGenerator) {
        return new ParameterObjectValueGenerator(argumentGenerator, true);
    }

    @Override
    public Value<?> generate(Class<?> fromType) throws UnsupportedTypeError, UnsupportedConstructionError {
        Optional<Constructor<?>> argConstructor = findArgumentConstructorWithFewestParameters(fromType);

        if (argConstructor.isPresent()) {
            Constructor<?> constructor = argConstructor.get();

            try {
                if (forceConstructorAccess) {
                    constructor.setAccessible(true);
                }

                Object[] arguments = Stream.of(constructor.getParameters())
                        .map(Parameter::getType)
                        .map(argumentGenerator::generate)
                        .toArray();

                return Value.ofEager(constructor.newInstance(arguments));
            } catch (Exception e) {
                throw new UnsupportedConstructionError(fromType, e);
            }
        }

        throw new UnsupportedTypeError(fromType);
    }

    @Override
    public boolean supports(Class<?> type) {
        Optional<Constructor<?>> argConstructor = findArgumentConstructorWithFewestParameters(type);

        if (!argConstructor.isPresent()) {
            return false;
        }

        Constructor<?> constructor = argConstructor.get();

        if (!Modifier.isPublic(constructor.getModifiers()) && !forceConstructorAccess) {
            return false;
        }

        Parameter[] parameters = constructor.getParameters();

        for (Parameter parameter : parameters) {
            if (!argumentGenerator.supports(parameter.getType())) {
                return false;
            }
        }

        return true;
    }

    private Optional<Constructor<?>> findArgumentConstructorWithFewestParameters(Class<?> type) {
        return Stream.of(type.getDeclaredConstructors())
                .filter(constructor -> constructor.getParameterCount() > 0)
                .min(Comparator.comparingInt(Constructor::getParameterCount));
    }
}
