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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JavaBeanTestStrategyTest {
    @Mock
    Method method;
    @Mock
    Field field;
    private JavaBeanTestStrategy convention;

    @BeforeEach
    void setUp() {
        convention = new JavaBeanTestStrategy();
    }

    @Test
    void setterConvention_givenField_whenMethodIsBeanStyle_thenIsAccepted() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("setField");
        doReturn(Object.class).when(field).getType();
        when(method.hasParameterCount(1)).thenReturn(true);
        when(method.hasParameterOf(Object.class)).thenReturn(true);

        assertThat(convention.isSetterForField(field)).accepts(method);
    }

    @Test
    void setterConvention_givenField_whenMethodIsJustPropertyName_thenIsRejected() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("field");

        assertThat(convention.isSetterForField(field)).rejects(method);
    }

    @Test
    void setterConvention_givenField_whenMethodHasMoreOrLessThanOneParameter_thenIsRejected() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("setField");
        when(method.hasParameterCount(1)).thenReturn(false);

        assertThat(convention.isSetterForField(field)).rejects(method);
    }

    @Test
    void getterConvention_givenField_whenGetterIsBeanStyle_thenIsAccepted() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("getField");
        doReturn(Object.class).when(field).getType();
        when(method.isProducing(Object.class)).thenReturn(true);

        assertThat(convention.isGetterForField(field)).accepts(method);
    }

    @Test
    void givenPrimitiveBooleanField_withIsPrefix_whenMethodName_getIsField_thenIsGetter() {
        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("getIsField");
        doReturn(boolean.class).when(field).getType();
        when(method.isProducing(boolean.class)).thenReturn(true);

        assertThat(convention.isGetterForField(field)).accepts(method);
    }

    @Test
    void givenWrapperBooleanField_withIsPrefix_whenMethodName_getIsField_thenIsGetter() {
        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("getIsField");
        doReturn(Boolean.class).when(field).getType();
        when(method.isProducing(Boolean.class)).thenReturn(true);

        assertThat(convention.isGetterForField(field)).accepts(method);
    }

    @Test
    void givenPrimitiveBooleanField_withIsPrefix_whenMethodName_getField_andIsProducingPrimitiveBoolean_thenIsGetter() {
        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("getField");
        when(field.isBoolean()).thenReturn(true);
        doReturn(boolean.class).when(field).getType();
        when(method.isProducing(boolean.class)).thenReturn(true);

        assertThat(convention.isGetterForField(field)).accepts(method);
    }

    @Test
    void givenWrapperBooleanField_withIsPrefix_whenMethodName_getField_andIsProducingWrapperBoolean_thenIsGetter() {
        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("getField");
        when(field.isBoolean()).thenReturn(true);
        doReturn(Boolean.class).when(field).getType();
        when(method.isProducing(Boolean.class)).thenReturn(true);

        assertThat(convention.isGetterForField(field)).accepts(method);
    }

    @Test
    void givenPrimitiveBooleanField_withIsPrefix_whenMethodName_setIsField_andHasParameterPrimitiveBoolean_thenIsSetter() {
        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("setIsField");
        doReturn(boolean.class).when(field).getType();
        when(method.hasParameterCount(1)).thenReturn(true);
        when(method.hasParameterOf(boolean.class)).thenReturn(true);

        assertThat(convention.isSetterForField(field)).accepts(method);
    }

    @Test
    void givenWrapperBooleanField_withIsPrefix_whenMethodName_setIsField_andHasParameterWrapperBoolean_thenIsSetter() {
        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("setIsField");
        doReturn(Boolean.class).when(field).getType();
        when(method.hasParameterCount(1)).thenReturn(true);
        when(method.hasParameterOf(Boolean.class)).thenReturn(true);

        assertThat(convention.isSetterForField(field)).accepts(method);
    }

    @Test
    void givenPrimitiveBooleanField_withIsPrefix_whenMethodName_setField_andParameterIsPrimitiveBoolean_thenIsSetter() {
        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("setField");
        doReturn(boolean.class).when(field).getType();
        when(field.isBoolean()).thenReturn(true);
        when(method.hasParameterCount(1)).thenReturn(true);
        when(method.hasParameterOf(boolean.class)).thenReturn(true);

        assertThat(convention.isSetterForField(field)).accepts(method);
    }

    @Test
    void givenWrapperBooleanField_withIsPrefix_whenMethodName_setField_andParameterIsWrapperBoolean_thenIsSetter() {
        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("setField");
        doReturn(Boolean.class).when(field).getType();
        when(field.isBoolean()).thenReturn(true);
        when(method.hasParameterCount(1)).thenReturn(true);
        when(method.hasParameterOf(Boolean.class)).thenReturn(true);

        assertThat(convention.isSetterForField(field)).accepts(method);
    }

    @Test
    void givenPrimitiveBooleanField_withIsPrefix_whenMethodName_setField_andOverloadingMethod_thenOnlyAcceptsBooleanSetter() {
        Method overloadingNonBooleanMethod = Mockito.mock(Method.class);

        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("setField");
        doReturn(boolean.class).when(field).getType();
        when(field.isBoolean()).thenReturn(true);
        when(method.hasParameterCount(1)).thenReturn(true);
        when(method.hasParameterOf(boolean.class)).thenReturn(true);

        when(overloadingNonBooleanMethod.getName()).thenReturn("setField");
        when(overloadingNonBooleanMethod.hasParameterCount(1)).thenReturn(true);
        when(overloadingNonBooleanMethod.hasParameterOf(boolean.class)).thenReturn(false);

        assertThat(convention.isSetterForField(field)).accepts(method);
        assertThat(convention.isSetterForField(field)).rejects(overloadingNonBooleanMethod);
    }

    @Test
    void givenWrapperBooleanField_withIsPrefix_whenMethodName_setField_andOverloadingMethod_thenOnlyAcceptsBooleanSetter() {
        Method overloadingNonBooleanMethod = Mockito.mock(Method.class);

        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("setField");
        doReturn(Boolean.class).when(field).getType();
        when(field.isBoolean()).thenReturn(true);
        when(method.hasParameterCount(1)).thenReturn(true);
        when(method.hasParameterOf(Boolean.class)).thenReturn(true);

        when(overloadingNonBooleanMethod.getName()).thenReturn("setField");
        when(overloadingNonBooleanMethod.hasParameterCount(1)).thenReturn(true);
        when(overloadingNonBooleanMethod.hasParameterOf(Boolean.class)).thenReturn(false);

        assertThat(convention.isSetterForField(field)).accepts(method);
        assertThat(convention.isSetterForField(field)).rejects(overloadingNonBooleanMethod);
    }
}
