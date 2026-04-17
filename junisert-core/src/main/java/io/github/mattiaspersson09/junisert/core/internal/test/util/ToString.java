/*
 * Copyright (c) 2026 Mattias Persson
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
package io.github.mattiaspersson09.junisert.core.internal.test.util;

import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Field;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Tests that toString-method of instances contains textual representation of its object.
 */
public final class ToString {
    private static final Logger LOGGER = Logger.getLogger("ToString");
    private static final String[] FIELD_VALUE_PAIRS = new String[]{
            "%s=%s",
            "%s = %s",
            "%s:%s",
            "%s: %s",
            "%s : %s",
    };
    private static final char[] VALUE_POTENTIALLY_WRAPPED_CHARS = new char[]{'"', '\''};

    private final Object instance;

    /**
     * Creates a new {@code ToString} from given instance.
     *
     * @param instance to check toString for
     */
    public ToString(Object instance) {
        this.instance = instance;
    }

    /**
     * Checks if this toString contains given value.
     *
     * @param value this toString might contain
     * @return true if it contains the value
     */
    public boolean contains(String value) {
        String toString = valueOf(instance);

        // Because if instance.toString() itself returns null
        // would result in NPE when using toString.contains(), double valueOf would waste unnecessary time
        if (toString == null) {
            return false;
        }

        return toString.contains(value);
    }

    /**
     * Checks if this toString contains given field and it's value.
     * This checks if {@code field}, an assignment operator (=, :) and {@code value} is presented together.
     *
     * @param field that should be present
     * @param value of the field
     * @return true if field and value is presented
     */
    public boolean contains(Field field, Object value) {
        for (String fieldValueFormat : FIELD_VALUE_PAIRS) {
            FieldValuePair fieldValue = new FieldValuePair(field, value, fieldValueFormat);

            if (contains(fieldValue)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if this toString contains given field and it's value.
     * This checks if {@code fieldValue} pair is presented together with its operator between.
     *
     * @param fieldValue to check for
     * @return true if field and value is presented
     */
    public boolean contains(FieldValuePair fieldValue) {
        String toString = valueOf(instance);

        // Because if instance.toString() itself returns null
        // would result in NPE when using toString.contains(), double valueOf would waste unnecessary time
        if (toString == null) {
            return false;
        }

        // Standard checks
        if (toString.contains(fieldValue.toString())
                || toString.contains(fieldValue.toRecordString())
                || toString.contains(fieldValue.toJsonString())) {
            return true;
        }

        // Potentially wrapped value (typically strings)
        for (char wrap : VALUE_POTENTIALLY_WRAPPED_CHARS) {
            fieldValue.wrapValue(wrap);

            if (toString.contains(fieldValue.toString())
                    || toString.contains(fieldValue.toRecordString())
                    || toString.contains(fieldValue.toJsonString())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Converts {@code object} to its textual representation.
     *
     * @param object to convert
     * @return string variant of given object
     */
    public static String valueOf(Object object) {
        // Would otherwise show hash variant of the array instead of actual values
        // Arrays.toString((Object[]) object) is not possible for primitive arrays because of class cast issue
        return Optional.ofNullable(object)
                .filter(obj -> obj.getClass().isArray())
                .map(ToString::valueOfArray)
                .orElseGet(() -> String.valueOf(object));

    }

    /**
     * Converts an array to its textual representation using {@code Arrays.toString} or {@code Arrays.deepToString}.
     * If {@code array} object is not an actual array, the result will be an empty array string {@code "[]"}.<br>
     * If there's uncertainty if the object is an array or not, {@link ToString#valueOf(Object)} should be used instead.
     * <p><br>
     * Example results:
     * <pre>
     * // []
     * ToString.valueOfArray(new byte[0]);
     * // [1, 2, 3]
     * ToString.valueOfArray(new byte[]{1, 2, 3});
     * // [[1], [2], [3]]
     * ToString.valueOfArray(new byte[][]{{1}, {2}, {3}});
     * // [[1], [a], [null]]
     * ToString.valueOfArray(new Object[][]{{1}, {'a'}, {null}});
     * // null
     * ToString.valueOfArray(null);
     * </pre>
     *
     * @param array to convert to string value
     * @return array values presented in a string
     * @see #valueOf(Object)
     */
    public static String valueOfArray(Object array) {
        if (array == null) {
            return Objects.toString(null);
        }

        Class<?> arrayClass = array.getClass();

        if (!arrayClass.isArray()) {
            return "[]";
        }

        // One dimensional primitive arrays
        if (byte[].class.equals(arrayClass)) {
            return Arrays.toString((byte[]) array);
        } else if (short[].class.equals(arrayClass)) {
            return Arrays.toString((short[]) array);
        } else if (int[].class.equals(arrayClass)) {
            return Arrays.toString((int[]) array);
        } else if (long[].class.equals(arrayClass)) {
            return Arrays.toString((long[]) array);
        } else if (float[].class.equals(arrayClass)) {
            return Arrays.toString((float[]) array);
        } else if (double[].class.equals(arrayClass)) {
            return Arrays.toString((double[]) array);
        } else if (char[].class.equals(arrayClass)) {
            return Arrays.toString((char[]) array);
        } else if (boolean[].class.equals(arrayClass)) {
            return Arrays.toString((boolean[]) array);
        }

        // Primitive two-dimensional and higher arrays is treated as Object[] and safe to cast
        // deep is needed to show higher dimensional values
        return Arrays.deepToString((Object[]) array);
    }

    private static String recordValueOf(Object object) {
        return String.valueOf(object);
    }

    @Override
    public String toString() {
        return valueOf(instance);
    }

    /**
     * Used with {@link ToString} to check its textual representation in an instance object.
     */
    public static class FieldValuePair {
        private final String field;
        private final Object value;
        private final String format;
        private Character valueWrapper;

        /**
         * Creates a new field and value pair to find together in {@code toString}.
         *
         * @param field to find
         * @param value together with field to find
         * @see #FieldValuePair(Field, Object, char)
         */
        public FieldValuePair(Field field, Object value) {
            this(field, value, '=');
        }

        /**
         * Creates a new field and value pair to find together in {@code toString}.
         *
         * @param field    to find
         * @param value    together with field to find
         * @param operator operator to find in between field and value pair to match
         */
        public FieldValuePair(Field field, Object value, char operator) {
            this(field, value, "%s" + operator + "%s");
        }

        /**
         * Creates a new field and value pair to find together in {@code toString}.
         *
         * @param field  to find
         * @param value  together with field to find
         * @param format to find for field and value pair match
         */
        public FieldValuePair(Field field, Object value, String format) {
            this.field = field.getName();
            this.value = value;
            this.format = format;
        }

        private void wrapValue(char character) {
            this.valueWrapper = character;
        }

        private String valueString() {
            return valueWrapper == null
                    ? ToString.valueOf(value)
                    : valueWrapper + ToString.valueOf(value) + valueWrapper;
        }

        @Override
        public String toString() {
            return String.format(format, field, valueString());
        }

        /**
         * If value is potentially an array and instance might be a Java record. Records doesn't show array values
         * like {@code Arrays.toString()} does.
         * <p><br>
         * Standard record toString:
         * <pre>
         * field=value, byteArrayField=[B@5038d0b5
         * </pre>
         *
         * @return string fix for record instances
         */
        String toRecordString() {
            if (valueWrapper == null) {
                return String.format(format, field, ToString.recordValueOf(value));
            }

            return String.format(format, field, valueWrapper + ToString.recordValueOf(value) + valueWrapper);
        }

        /**
         * If field should be quoted like JSON and value potentially wrapped with quotes.
         *
         * @return string with quoted field and potentially wrapped value
         */
        String toJsonString() {
            return String.format(format, "\"" + field + "\"", valueString());
        }
    }
}
