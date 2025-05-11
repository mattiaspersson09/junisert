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
package io.github.mattiaspersson09.junisert.core.internal.reflection.util;

import io.github.mattiaspersson09.junisert.core.internal.reflection.Field;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class Fields {
    private static final Predicate<String> ONE_LETTER_PREFIX_FIELD;

    static {
        ONE_LETTER_PREFIX_FIELD = Pattern.compile("^[a-z_$][A-Z0-9_$]").asPredicate();
    }

    private Fields() {
    }

    public static boolean isInstanceField(Field field) {
        return !field.isSynthetic() && field.isInstanceMember();
    }

    public static String toMethodPropertyName(Field field) {
        String name = field.getName();

        if (ONE_LETTER_PREFIX_FIELD.test(name)) {
            return name;
        }

        return toUpperCaseFirstLetter(name);
    }

    public static String toBooleanMethodPropertyName(Field field) {
        if (field.isBoolean() && field.getName().startsWith("is")) {
            return toUpperCaseFirstLetter(field.getName().substring(2));
        }

        return toMethodPropertyName(field);
    }

    private static String toUpperCaseFirstLetter(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
