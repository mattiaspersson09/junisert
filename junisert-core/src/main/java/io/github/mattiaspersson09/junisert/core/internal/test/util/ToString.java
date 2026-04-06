/*
 * Copyright (c) 2025-2026 Mattias Persson
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

import java.util.Optional;

/**
 * Tests that toString-method of instances contains textual representation of its object.
 */
public final class ToString {
    private static final Logger LOGGER = Logger.getLogger("ToString");
    /**
     * Possible operators for field and value presentation in toString.
     */
    public static final char[] FIELD_VALUE_OPERATORS = new char[]{'=', ':'};

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
        return toString.contains(value);
    }

    /**
     * Checks if this toString contains given field and it's value.
     * This checks if {@code field}, any of {@link ToString#FIELD_VALUE_OPERATORS} and {@code value} is presented
     * together.
     *
     * @param field that should be present
     * @param value of the field
     * @return true if field and value is presented
     */
    public boolean contains(Field field, Object value) {
        for (char operator : FIELD_VALUE_OPERATORS) {
            FieldValuePair fieldValue = new FieldValuePair(field, value, operator);

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

        return toString.contains(fieldValue.toString())
                || toString.contains(fieldValue.toRecordString())
                || toString.contains(fieldValue.surroundValue('\"').toString())
                || toString.contains(fieldValue.surroundValue('\'').toString())
                || toString.contains(fieldValue.surroundValue('\"').toRecordString())
                || toString.contains(fieldValue.surroundValue('\'').toRecordString())
                // Check with padding
                || containsWithPadding(fieldValue.reset())
                || containsWithPaddingRecordArray(fieldValue)
                || containsWithPadding(fieldValue.surroundValue('\"'))
                || containsWithPadding(fieldValue.surroundValue('\''));
    }

    private boolean containsWithPadding(FieldValuePair fieldValue) {
        String toString = valueOf(instance);

        return toString.contains(fieldValue.padding().toString())
                || toString.contains(fieldValue.padLeft().toString())
                || toString.contains(fieldValue.padRight().toString());
    }

    private boolean containsWithPaddingRecordArray(FieldValuePair fieldValue) {
        String toString = valueOf(instance);

        return toString.contains(fieldValue.padding().toRecordString())
                || toString.contains(fieldValue.padLeft().toRecordString())
                || toString.contains(fieldValue.padRight().toRecordString());
    }

    /**
     * Converts {@code object} to its textual representation.
     *
     * @param object to convert
     * @return string variant of given object
     */
    public static String valueOf(Object object) {
        return Optional.ofNullable(object)
                .map(obj -> {
                    // Would otherwise show hash variant of the array instead of actual values
                    // At this point, all arrays should be empty
                    // Arrays.toString((Object[]) obj) is not possible for primitive arrays because of class cast issue
                    if (obj.getClass().isArray()) {
                        return "[]";
                    }

                    return obj.toString();
                })
                .orElse("");

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
        private final char operator;
        private Character valueSurrounder;
        private boolean leftPadding;
        private boolean rightPadding;

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
            this.field = field.getName();
            this.value = value;
            this.operator = operator;
        }

        private FieldValuePair padding() {
            leftPadding = rightPadding = true;
            return this;
        }

        private FieldValuePair padLeft() {
            leftPadding = true;
            rightPadding = false;
            return this;
        }

        private FieldValuePair padRight() {
            leftPadding = false;
            rightPadding = true;
            return this;
        }

        private FieldValuePair surroundValue(char character) {
            this.valueSurrounder = character;
            return this;
        }

        private FieldValuePair reset() {
            valueSurrounder = null;
            leftPadding = rightPadding = false;
            return this;
        }

        private String operator() {
            return (leftPadding ? " " : "") + operator + (rightPadding ? " " : "");
        }

        private String valueString() {
            return valueSurrounder == null
                    ? ToString.valueOf(value)
                    : valueSurrounder + ToString.valueOf(value) + valueSurrounder;
        }

        @Override
        public String toString() {
            return field + operator() + valueString();
        }

        /**
         * If value is potentially an array and instance is a Java record.
         *
         * @return string fix for record instances
         */
        String toRecordString() {
            if (valueSurrounder == null) {
                return field + operator() + (value.getClass().isArray() ? value.toString() : ToString.valueOf(value));
            }

            return field + operator()
                    + valueSurrounder
                    + (value.getClass().isArray() ? value.toString() : ToString.valueOf(value))
                    + valueSurrounder;
        }
    }
}
