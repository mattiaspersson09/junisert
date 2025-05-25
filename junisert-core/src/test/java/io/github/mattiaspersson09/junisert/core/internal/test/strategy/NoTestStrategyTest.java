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
package io.github.mattiaspersson09.junisert.core.internal.test.strategy;

import io.github.mattiaspersson09.junisert.core.internal.reflection.Field;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Method;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NoTestStrategyTest {
    @Mock
    Method method;
    @Mock
    Field field;
    private NoTestStrategy convention;

    @BeforeEach
    void setUp() {
        convention = new NoTestStrategy();
    }

    @Test
    void setterConvention_whenMethodIsBuilderStyle_thenIsAccepted() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("field");
        doReturn(Object.class).when(field).getType();
        when(method.hasParameterCount(1)).thenReturn(true);
        when(method.hasParameterOf(Object.class)).thenReturn(true);

        assertThat(convention.isSetterForField(field)).accepts(method);
    }

    @Test
    void setterConvention_whenMethodIsBuilderStyle_andHasMoreOrLessParameters_thenIsRejected() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("field");
        when(method.hasParameterCount(1)).thenReturn(false);

        assertThat(convention.isSetterForField(field)).rejects(method);
    }

    @Test
    void setterConvention_whenMethodIsBuilderStyle_andAcceptsOtherArgumentType_thenIsRejected() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("field");
        doReturn(Super.class).when(field).getType();
        when(method.hasParameterCount(1)).thenReturn(true);

        assertThat(convention.isSetterForField(field)).rejects(method);
    }

    @Test
    void getterConvention_whenGetterIsRecordStyle_thenIsAccepted() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("field");
        doReturn(Object.class).when(field).getType();
        when(method.isProducing(Object.class)).thenReturn(true);

        assertThat(convention.isGetterForField(field)).accepts(method);
    }

    @Test
    void getterConvention_whenMethodProducesOtherType_thenIsRejected() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("field");
        doReturn(Object.class).when(field).getType();
        when(method.isProducing(Object.class)).thenReturn(false);

        assertThat(convention.isGetterForField(field)).rejects(method);
    }

    @Test
    void givenPrimitiveBooleanField_withIsPrefix_whenMethodName_isField_thenIsGetter() {
        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("isField");
        doReturn(boolean.class).when(field).getType();
        when(method.isProducing(boolean.class)).thenReturn(true);

        assertThat(convention.isGetterForField(field)).accepts(method);
    }

    @Test
    void givenWrapperBooleanField_withIsPrefix_whenMethodName_isField_thenIsGetter() {
        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("isField");
        doReturn(Boolean.class).when(field).getType();
        when(method.isProducing(Boolean.class)).thenReturn(true);

        assertThat(convention.isGetterForField(field)).accepts(method);
    }
}
