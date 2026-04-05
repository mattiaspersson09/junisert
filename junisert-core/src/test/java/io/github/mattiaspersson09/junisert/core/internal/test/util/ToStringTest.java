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

import io.github.mattiaspersson09.junisert.core.internal.reflection.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ToStringTest {
    @Mock
    Object instance;
    @Mock
    Field field;

    private ToString toString;

    @BeforeEach
    void setUp() {
        toString = new ToString(instance);
    }

    @Test
    void contains_givenValue_whenValueExists_thenTrue() {
        when(instance.toString()).thenReturn("Object{field=value}");

        assertThat(toString.contains("field=value")).isTrue();
    }

    @Test
    void contains_givenValue_whenValueDoesNotExist_thenFalse() {
        when(instance.toString()).thenReturn("Object{field=value}");

        assertThat(toString.contains("field2=value2")).isFalse();
    }

    @Test
    void contains_givenFieldAndValue_whenBothExistTogether_thenTrue() {
        when(instance.toString()).thenReturn("Object{field=value}");
        when(field.getName()).thenReturn("field");

        assertThat(toString.contains(field, "value")).isTrue();
    }

    @Test
    void contains_givenFieldAndValue_whenTheyDoNotExistTogether_thenFalse() {
        when(instance.toString()).thenReturn("Object{field=value, field2=value2}");
        when(field.getName()).thenReturn("field");

        assertThat(toString.contains(field, "value2")).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "=",
            " =",
            "= ",
            " = ",
            ":",
            " :",
            ": ",
            " : ",
    })
    void contains_givenFieldAndValue_whenValidOperator_thenTrue(String operator) {
        when(instance.toString()).thenReturn("Object{field" + operator + "value}");
        when(field.getName()).thenReturn("field");

        assertThat(toString.contains(field, "value")).isTrue();
    }

    @Test
    void contains_givenFieldAndValue_whenValueIsNull_thenItsHandledAsEmptyValue_andIsTrue() {
        when(instance.toString()).thenReturn("Object{field=}");
        when(field.getName()).thenReturn("field");

        assertThat(toString.contains(field, null)).isTrue();
    }

    @Test
    void contains_givenFieldAndArrayValue_whenValueIsEmptyArray_thenContainsBrackets_andIsTrue() {
        when(instance.toString()).thenReturn("Object{field=[]}");
        when(field.getName()).thenReturn("field");

        assertThat(toString.contains(field, new String[0])).isTrue();
    }

    @Test
    void contains_givenFieldAndStringValue_whenStringIsEmpty_thenItsHandledAsEmptyValue_andIsTrue() {
        when(instance.toString()).thenReturn("Object{field=}");
        when(field.getName()).thenReturn("field");

        assertThat(toString.contains(field, "")).isTrue();
    }

    @Test
    void contains_givenFieldAndStringValue_whenStringHasSingleQuoteValue_thenTrue() {
        when(instance.toString()).thenReturn("Object{field='value'}");
        when(field.getName()).thenReturn("field");

        assertThat(toString.contains(field, "value")).isTrue();
        assertThat(toString.contains(field, "value")).isTrue();
    }

    @Test
    void contains_givenFieldAndStringValue_whenStringHasDoubleQuoteValue_thenTrue() {
        when(instance.toString()).thenReturn("Object{field=\"value\"}");
        when(field.getName()).thenReturn("field");

        assertThat(toString.contains(field, "value")).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "=",
            " =",
            "= ",
            " = ",
            ":",
            " :",
            ": ",
            " : ",
    })
    void contains_givenFieldAndStringValue_whenStringHasSingleQuoteValueAndDifferentPadding_thenTrue(String operator) {
        when(instance.toString()).thenReturn("Object{field" + operator + "'value'}");
        when(field.getName()).thenReturn("field");

        assertThat(toString.contains(field, "value")).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "=",
            " =",
            "= ",
            " = ",
            ":",
            " :",
            ": ",
            " : ",
    })
    void contains_givenFieldAndStringValue_whenStringHasDoubleQuoteValueAndDifferentPadding_thenTrue(String operator) {
        when(instance.toString()).thenReturn("Object{field" + operator + "\"value\"}");
        when(field.getName()).thenReturn("field");

        assertThat(toString.contains(field, "value")).isTrue();
    }

    @Test
    void toString_returnsTextualRepresentation() {
        when(instance.toString()).thenReturn("Object{field=\"value\"}");

        assertThat(toString.toString()).isEqualTo("Object{field=\"value\"}");
    }

    @Test
    void fieldValuePair_equalIsDefaultOperator() {
        when(field.getName()).thenReturn("field");

        ToString.FieldValuePair defaultOperator = new ToString.FieldValuePair(field, "value");
        ToString.FieldValuePair fieldValue = new ToString.FieldValuePair(field, "value", '=');

        assertThat(defaultOperator.toString()).isEqualTo(fieldValue.toString());
    }
}
