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
package io.github.mattiaspersson09.junisert.core.reflection;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class UnitCreator {
    private UnitCreator() {
    }

    public static Unit createFrom(Class<?> origin) {
        Unit unit = new Unit(origin);

        Stream.of(origin.getDeclaredConstructors())
                .map(Constructor::new)
                .forEach(unit::addConstructor);

        List<Method> methods = new ArrayList<>();

        for (java.lang.reflect.Field field : origin.getDeclaredFields()) {
            Field unitField = new Field(field);

            for (java.lang.reflect.Method method : origin.getDeclaredMethods()) {
                if (methodIsSetterForField(method, unitField)) {
                    Setter setter = new Setter(method, map(method.getParameters()), unitField);
                    methods.add(setter);
                    unitField.addSetter(setter);
                } else if (methodIsGetterForField(method, unitField)) {
                    Getter getter = new Getter(method, map(method.getParameters()), unitField);
                    methods.add(getter);
                    unitField.addGetter(getter);
                } else {
                    methods.add(new Method(method, map(method.getParameters())));
                }
            }

            unit.addField(unitField);
        }

        methods.forEach(unit::addMethod);
        unit.setInstanceSupplier(() -> createObjectFromDefaultConstructor(unit));

        return unit;
    }

    private static Object createObjectFromDefaultConstructor(Unit unit) throws UnsupportedConstructionError {
        // throws UnsupportedConstructionError on reflection failure
        return ObjectValueGenerator.withForcedAccess()
                .generate(unit.getType())
                .get();
    }

    private static List<Parameter> map(java.lang.reflect.Parameter[] parameters) {
        return Stream.of(parameters)
                .map(Parameter::new)
                .collect(Collectors.toList());
    }

    static boolean methodIsSetterForField(java.lang.reflect.Method method, Field field) {
        return (method.getName().substring("set".length()).equalsIgnoreCase(field.getName())
                || method.getName().equalsIgnoreCase(field.getName()))
                && method.getParameters().length == 1
                && field.getType().isAssignableFrom(method.getParameters()[0].getType());
    }

    static boolean methodIsGetterForField(java.lang.reflect.Method method, Field field) {
        return (method.getName().substring("get".length()).equalsIgnoreCase(field.getName())
                || method.getName().substring("is".length()).equalsIgnoreCase(field.getName())
                || method.getName().equalsIgnoreCase(field.getName()))
                && method.getParameters().length == 0
                && method.getReturnType().isAssignableFrom(field.getType());
    }
}
