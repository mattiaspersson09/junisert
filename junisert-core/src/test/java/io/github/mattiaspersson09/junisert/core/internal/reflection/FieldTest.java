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
package io.github.mattiaspersson09.junisert.core.internal.reflection;

import io.github.mattiaspersson09.junisert.testunits.unit.enumeration.EmptyEnumUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class FieldTest {
    @Mock
    java.lang.reflect.Field origin;
    private Field field;

    @BeforeEach
    void setUp() {
        field = new Field(origin);
    }

    @Test
    void isArray() {
        java.lang.reflect.Field nonArrayField = Mockito.mock(java.lang.reflect.Field.class);

        doReturn(int[].class).when(origin).getType();
        doReturn(int.class).when(nonArrayField).getType();

        assertTrue(field.isArray());
        assertFalse(new Field(nonArrayField).isArray());
    }

    @Test
    void isEnum() {
        java.lang.reflect.Field nonEnumField = Mockito.mock(java.lang.reflect.Field.class);

        doReturn(EmptyEnumUnit.class).when(origin).getType();
        doReturn(int.class).when(nonEnumField).getType();

        assertTrue(field.isEnum());
        assertFalse(new Field(nonEnumField).isEnum());
    }

    @Test
    void isPrimitive() {
        java.lang.reflect.Field nonPrimitiveField = Mockito.mock(java.lang.reflect.Field.class);

        doReturn(int.class).when(origin).getType();
        doReturn(Integer.class).when(nonPrimitiveField).getType();

        assertTrue(field.isPrimitive());
        assertFalse(new Field(nonPrimitiveField).isPrimitive());
    }

    @Test
    void isBoolean_whenHasPrimitiveBooleanType_thenIsTrue() {
        doReturn(boolean.class).when(origin).getType();

        assertTrue(field.isBoolean());
    }

    @Test
    void isBoolean_whenHasWrapperBooleanType_thenIsTrue() {
        doReturn(Boolean.class).when(origin).getType();

        assertTrue(field.isBoolean());
    }

    @Test
    void isBoolean_whenHasOtherType_thenIsFalse() {
        doReturn(Object.class).when(origin).getType();

        assertFalse(field.isBoolean());
    }
}
