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
package io.github.mattiaspersson09.junisert.core.reflection.util;

import io.github.mattiaspersson09.junisert.core.reflection.Field;
import io.github.mattiaspersson09.junisert.core.reflection.Method;

public final class Methods {
    private Methods() {
    }

    public static boolean isSetterForField(java.lang.reflect.Method method, Field field) {
        return isSetterForField(Method.of(method), field);
    }

    public static boolean isGetterForField(java.lang.reflect.Method method, Field field) {
        return isGetterForField(Method.of(method), field);
    }

    public static boolean isSetterForField(Method method, Field field) {
        return (method.getName().substring("set".length()).equalsIgnoreCase(field.getName())
                || method.getName().equalsIgnoreCase(field.getName()))
                && method.hasParameterCount(1)
                && method.hasParameterAssignableTo(field.getType());
    }

    public static boolean isGetterForField(Method method, Field field) {
        return (method.getName().substring("get".length()).equalsIgnoreCase(field.getName())
                || method.getName().substring("is".length()).equalsIgnoreCase(field.getName())
                || method.getName().equalsIgnoreCase(field.getName()))
                && method.isProducing(field.getType());
    }

    public static boolean isEqualsMethod(Method method) {
        return "equals".equals(method.getName()) && method.isFunctionOf(Object.class, boolean.class);
    }

    public static boolean isHashCodeMethod(Method method) {
        return "hashCode".equals(method.getName()) && method.isProducing(int.class);
    }

    public static boolean isToStringMethod(Method method) {
        return "toString".equals(method.getName()) && method.isProducing(String.class);
    }
}
