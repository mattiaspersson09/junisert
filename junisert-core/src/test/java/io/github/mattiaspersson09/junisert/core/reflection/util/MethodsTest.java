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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MethodsTest {
    @Mock
    Method method;
    @Mock
    Field field;

    @Test
    void methodIsSetterForField_whenSetterIsBeanStyle_thenIsTrue() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("setField");
        doReturn(Object.class).when(field).getType();
        when(method.hasParameterCount(1)).thenReturn(true);
        when(method.hasParameterTo(Object.class)).thenReturn(true);

        assertThat(Methods.isSetterForField(method, field)).isTrue();
    }

    @Test
    void methodIsSetterForField_whenSetterIsBuilderStyle_thenIsTrue() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("field");
        doReturn(Object.class).when(field).getType();
        when(method.hasParameterCount(1)).thenReturn(true);
        when(method.hasParameterTo(Object.class)).thenReturn(true);

        assertThat(Methods.isSetterForField(method, field)).isTrue();
    }

    @Test
    void methodIsSetterForField_whenMethodHasMoreOrLessThanOneParameter_thenIsFalse() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("setField");
        when(method.hasParameterCount(1)).thenReturn(false);

        assertThat(Methods.isSetterForField(method, field)).isFalse();
    }

    @Test
    void methodIsGetterForField_whenGetterIsBeanStyle_thenIsTrue() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("getField");
        doReturn(Object.class).when(field).getType();
        when(method.isProducing(Object.class)).thenReturn(true);

        assertThat(Methods.isGetterForField(method, field)).isTrue();
    }

    @Test
    void methodIsGetterForField_whenSetterIsRecordStyle_thenIsTrue() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("field");
        doReturn(Object.class).when(field).getType();
        when(method.isProducing(Object.class)).thenReturn(true);

        assertThat(Methods.isGetterForField(method, field)).isTrue();
    }

    @Test
    void methodIsGetterForField_whenMethodProducesOtherType_thenIsFalse() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("field");
        doReturn(Object.class).when(field).getType();
        when(method.isProducing(Object.class)).thenReturn(false);

        assertThat(Methods.isGetterForField(method, field)).isFalse();
    }

    @Test
    void givenPrimitiveBooleanField_withIsPrefix_whenMethodName_getIsField_thenIsGetter() {
        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("getIsField");
        doReturn(boolean.class).when(field).getType();
        when(method.isProducing(boolean.class)).thenReturn(true);

        assertThat(Methods.isGetterForField(method, field)).isTrue();
    }

    @Test
    void givenPrimitiveBooleanField_withIsPrefix_whenMethodName_isField_thenIsGetter() {
        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("isField");
        doReturn(boolean.class).when(field).getType();
        when(method.isProducing(boolean.class)).thenReturn(true);

        assertThat(Methods.isGetterForField(method, field)).isTrue();
    }

    @Test
    void givenPrimitiveBooleanField_withIsPrefix_whenMethodName_getField_andIsProducingPrimitiveBoolean_thenIsGetter() {
        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("getField");
        when(field.isTypeOf(boolean.class)).thenReturn(true);
        doReturn(boolean.class).when(field).getType();
        when(method.isProducing(boolean.class)).thenReturn(true);

        assertThat(Methods.isGetterForField(method, field)).isTrue();
    }

    @Test
    void givenPrimitiveBooleanField_withIsPrefix_whenMethodName_setIsField_andHasParameterPrimitiveBoolean_thenIsSetter() {
        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("setIsField");
        doReturn(boolean.class).when(field).getType();
        when(method.hasParameterCount(1)).thenReturn(true);
        when(method.hasParameterTo(boolean.class)).thenReturn(true);

        assertThat(Methods.isSetterForField(method, field)).isTrue();
    }

    @Test
    void givenPrimitiveBooleanField_withIsPrefix_whenMethodName_setField_andParameterIsPrimitiveBoolean_thenIsSetter() {
        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("setField");
        doReturn(boolean.class).when(field).getType();
        when(field.isTypeOf(boolean.class)).thenReturn(true);
        when(method.hasParameterCount(1)).thenReturn(true);
        when(method.hasParameterTo(boolean.class)).thenReturn(true);

        assertThat(Methods.isSetterForField(method, field)).isTrue();
    }

    @Test
    void givenPrimitiveBooleanField_withIsPrefix_whenMethodName_setField_andOverloadingMethod_thenOnlyBooleanSetterIsSetter() {
        Method overloadingNonBooleanMethod = Mockito.mock(Method.class);

        when(field.getName()).thenReturn("isField");
        when(method.getName()).thenReturn("setField");
        doReturn(boolean.class).when(field).getType();
        when(field.isTypeOf(boolean.class)).thenReturn(true);
        when(method.hasParameterCount(1)).thenReturn(true);
        when(method.hasParameterTo(boolean.class)).thenReturn(true);

        when(overloadingNonBooleanMethod.getName()).thenReturn("setField");
        when(overloadingNonBooleanMethod.hasParameterCount(1)).thenReturn(true);
        when(overloadingNonBooleanMethod.hasParameterTo(boolean.class)).thenReturn(false);

        assertThat(Methods.isSetterForField(method, field)).isTrue();
        assertThat(Methods.isSetterForField(overloadingNonBooleanMethod, field)).isFalse();
    }

    @Test
    void isEqualsMethod_whenMethodNameIs_equals_andIsFunctionOf_inputObject_resultPrimitiveBoolean_thenIsTrue() {
        when(method.getName()).thenReturn("equals");
        when(method.isFunctionOf(Object.class, boolean.class)).thenReturn(true);

        assertThat(Methods.isEqualsMethod(method)).isTrue();
    }

    @Test
    void isEqualsMethod_whenMethodNameIs_equals_butIsNotFunctionOf_inputObject_resultPrimitiveBoolean_thenIsFalse() {
        when(method.getName()).thenReturn("equals");
        when(method.isFunctionOf(Object.class, boolean.class)).thenReturn(false);

        assertThat(Methods.isEqualsMethod(method)).isFalse();
    }

    @Test
    void isHashCodeMethod_whenMethodNameIs_hashCode_andMethodOnlyProducePrimitiveInt_thenIsTrue() {
        when(method.getName()).thenReturn("hashCode");
        when(method.isProducing(int.class)).thenReturn(true);

        assertThat(Methods.isHashCodeMethod(method)).isTrue();
    }

    @Test
    void isHashCodeMethod_whenMethodNameIs_hashCode_butMethodIsNotProducingPrimitiveInt_thenIsFalse() {
        when(method.getName()).thenReturn("hashCode");
        when(method.isProducing(int.class)).thenReturn(false);

        assertThat(Methods.isHashCodeMethod(method)).isFalse();
    }

    @Test
    void isToStringMethod_whenMethodNameIs_toString_andMethodOnlyProduceString_thenIsTrue() {
        when(method.getName()).thenReturn("toString");
        when(method.isProducing(String.class)).thenReturn(true);

        assertThat(Methods.isToStringMethod(method)).isTrue();
    }

    @Test
    void isToStringMethod_whenMethodNameIs_toString_butNotProducingString_thenIsFalse() {
        when(method.getName()).thenReturn("toString");
        when(method.isProducing(String.class)).thenReturn(false);

        assertThat(Methods.isToStringMethod(method)).isFalse();
    }
}
