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

import io.github.mattiaspersson09.junisert.core.internal.reflection.Method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MethodsTest {
    @Mock
    Method method;

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
