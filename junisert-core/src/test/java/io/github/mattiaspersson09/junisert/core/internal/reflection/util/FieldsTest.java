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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FieldsTest {
    @Mock
    Field field;

    @Test
    void toCapitalizedPropertyName_whenFieldHasOneLetterPrefix_thenSamePropertyName() {
        when(field.getName()).thenReturn("mField");

        assertThat(Fields.toCapitalizedPropertyName(field)).isEqualTo("mField");
    }

    @Test
    void toCapitalizedPropertyName_whenFieldHasSymbolPrefix_thenSamePropertyName() {
        when(field.getName()).thenReturn("_field");

        assertThat(Fields.toCapitalizedPropertyName(field)).isEqualTo("_field");
    }

    @Test
    void toCapitalizedPropertyName_whenFieldHasMoreThanOneLetterPrefix_thenUpperCaseFirstLetter() {
        when(field.getName()).thenReturn("isField");

        assertThat(Fields.toCapitalizedPropertyName(field)).isEqualTo("IsField");
    }

    @Test
    void toBooleanCapitalizedPropertyName_whenFieldIsBoolean_andHasNoIsPrefix_thenSamePropertyName() {
        when(field.getName()).thenReturn("field");
        when(field.isBoolean()).thenReturn(true);

        assertThat(Fields.toBooleanCapitalizedPropertyName(field)).isEqualTo("Field");
    }

    @Test
    void toBooleanCapitalizedPropertyName_whenFieldIsBoolean_andHasIsPrefix_thenRemovesPrefix() {
        when(field.getName()).thenReturn("isField");
        when(field.isBoolean()).thenReturn(true);

        assertThat(Fields.toBooleanCapitalizedPropertyName(field)).isEqualTo("Field");
    }
}
