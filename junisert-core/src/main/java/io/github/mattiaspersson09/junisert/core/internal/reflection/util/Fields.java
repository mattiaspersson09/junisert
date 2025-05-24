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

/**
 * Utility class with helper methods for acting on reflected {@link Field}'s.
 */
public final class Fields {
    private static final String SINGLE_PREFIX_REGEX = "^[a-z_$][A-Z0-9_$]";
    private static final Predicate<String> ONE_LETTER_PREFIX_FIELD;

    static {
        ONE_LETTER_PREFIX_FIELD = Pattern.compile(SINGLE_PREFIX_REGEX).asPredicate();
    }

    private Fields() {
    }

    /**
     * Checks if given {@code field} is considered an instance field, being a non-synthetic instance member.
     *
     * @param field to check
     * @return true if given field is a non-synthetic instance member
     */
    public static boolean isInstanceField(Field field) {
        return !field.isSynthetic() && field.isInstanceMember();
    }

    /**
     * Gets property name for given {@code field} with first letter in upper case.
     * If the property name is prefixed with a single letter or symbol according to:
     * {@value #SINGLE_PREFIX_REGEX} - then property name is not capitalized, but returned as is.
     *
     * @param field to convert to capitalized property name
     * @return property name capitalized or property as is if prefixed with a single letter
     */
    public static String toCapitalizedPropertyName(Field field) {
        String name = field.getName();

        if (ONE_LETTER_PREFIX_FIELD.test(name)) {
            return name;
        }

        return capitalize(name);
    }

    /**
     * Gets property name of given {@code field} with first letter in upper case.
     * This will also strip <em>"is"</em> from the property name, if the field is a
     * {@code boolean} and prefixed with it.
     * This is to comply with boilerplate {@code boolean} getters formed as {@code "isPropertyName"}
     * and setters formed as {@code "setPropertyName"} without is-prefix.
     * <p><br>
     * <p>
     * Example pseudo:
     * <pre>
     * // Given that field is formed inside a unit as: boolean isBooleanField
     * String propertyName = toBooleanCapitalizedPropertyName(field)
     *
     * // Given that method is formed inside a unit as: boolean isBooleanField()
     * method.name().equals("is" + propertyName)
     * // Given that method is formed inside a unit as: boolean setBooleanField(boolean booleanField)
     * method.name().equals("set" + propertyName)
     * </pre>
     *
     * @param field to convert to capitalized property name, potentially boolean with is-prefix
     * @return property name with first letter in upper case, without is-prefix if field is a boolean
     * @see #toCapitalizedPropertyName(Field)
     */
    public static String toBooleanCapitalizedPropertyName(Field field) {
        if (field.isBoolean() && field.getName().startsWith("is")) {
            return capitalize(field.getName().substring(2));
        }

        return toCapitalizedPropertyName(field);
    }

    private static String capitalize(String propertyName) {
        return Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
    }
}
