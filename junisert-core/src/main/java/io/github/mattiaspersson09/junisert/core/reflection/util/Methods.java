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

import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class Methods {
    private static final Predicate<String> ONE_LETTER_PREFIX_FIELD;

    static {
        ONE_LETTER_PREFIX_FIELD = Pattern.compile("^[a-z_$][A-Z0-9_$]").asPredicate();
    }

    private Methods() {
    }

    public static boolean isGetterForField(Method method, Field field) {
        /*
            Can have different styled getters:
            1. Java bean compliant style: get<property name>
            2. Java bean compliant style, get<property name>, removing is-prefix from boolean field name if found
            3. Java bean compliant style: is<property name>
            4. Builder/record style: just <property name>
         */
        return (isJavaBeanCompliantGetter(method, field) || isRecordStyle(method, field))
                && method.isProducing(field.getType());
    }

    public static boolean isSetterForField(Method method, Field field) {
        /*
            Can have different styled setters:
            1. Java bean compliant style: set<property name>
            2. Java bean compliant style: set<property name>, removing is-prefix from boolean field name if found
            3. Builder/record style
         */
        return (isJavaBeanCompliantSetter(method, field) || isRecordStyle(method, field))
                && method.hasParameterCount(1)
                && method.hasParameterTo(field.getType());
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

    static boolean isJavaBeanCompliantGetter(Method method, Field field) {
        String propertyName = toMethodPropertyName(field);

        return method.getName().equals("get" + propertyName)
                || method.getName().equals("is" + propertyName)
                || method.getName().equals("get" + toBooleanMethodPropertyName(field));
    }

    static boolean isJavaBeanCompliantSetter(Method method, Field field) {
        String propertyName = toMethodPropertyName(field);

        return method.getName().equals("set" + propertyName)
                || method.getName().equals("set" + toBooleanMethodPropertyName(field));
    }

    static boolean isRecordStyle(Method method, Field field) {
        return method.getName().equals(field.getName());
    }

    static String toMethodPropertyName(Field field) {
        String name = field.getName();

        if (ONE_LETTER_PREFIX_FIELD.test(name)) {
            return name;
        }

        return toUpperCaseFirstLetter(name);
    }

    static String toBooleanMethodPropertyName(Field field) {
        if (field.isTypeOf(boolean.class) && field.getName().startsWith("is")) {
            return toUpperCaseFirstLetter(field.getName().substring(2));
        }

        return toMethodPropertyName(field);
    }

    static String toUpperCaseFirstLetter(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
