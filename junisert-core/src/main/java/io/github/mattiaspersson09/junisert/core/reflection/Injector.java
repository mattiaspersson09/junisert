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

import io.github.mattiaspersson09.junisert.api.internal.service.ValueService;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

public class Injector {
    private final ValueService valueService;

    public Injector(ValueService valueService) {
        this.valueService = valueService;
    }

    // TODO: Gör någonting med detta
//    public Value injectUnspported(Class<?> fromType) {
//        Constructor<?> con = Stream.of(fromType.getDeclaredConstructors())
//                .filter(constructor -> constructor.getParameterCount() > 0)
//                .findFirst()
//                .orElseThrow(
//                        () -> new RuntimeException(
//                                "Unable to create value for type: " + fromType.getTypeName()));
//
//        try {
//            con.setAccessible(true);
//            Parameter[] parameters = con.getParameters();
//            List<Object> injectionValues = new ArrayList<>();
//
//            for (Parameter parameter : parameters) {
//                // ignore recursive value
//                if (parameter.getType().equals(fromType)) {
//                    injectionValues.add(null);
//                    continue;
//                }
//
//                Object injectionValue = valueService.findValue(parameter.getType())
//                        .orElseThrow(
//                                () -> new RuntimeException(
//                                        "Unable to create value for type: " + fromType.getTypeName()))
//                        .get();
//                injectionValues.add(injectionValue);
//            }
//
//            return Value.ofEager(con.newInstance(injectionValues.toArray()));
//        } catch (Exception e) {
//            throw new RuntimeException("Unable to create value for type: " + fromType.getTypeName(), e);
//        }
//    }

    public static Object[] injectionFor(Constructor<?> constructor) {
        Parameter[] parameters = constructor.getParameters();

        return null;
    }
}
