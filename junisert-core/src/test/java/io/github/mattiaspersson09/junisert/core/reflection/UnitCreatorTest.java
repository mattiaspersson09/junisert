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
package io.github.mattiaspersson09.junisert.core.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UnitCreatorTest {
    @Mock
    Method method;
    @Mock
    Field field;
    @Mock
    Parameter parameter;

    @Test
    void methodIsSetterForField_whenSetterIsBeanStyle_thenIsTrue() {
        when(field.getName()).thenReturn("field");
        doReturn(FieldType.class).when(field).getType();
        when(method.getName()).thenReturn("setField");
        when(method.getParameters()).thenReturn(new Parameter[]{parameter});
        doReturn(FieldType.class).when(parameter).getType();

        assertThat(UnitCreator.methodIsSetterForField(method, field)).isTrue();
    }

    @Test
    void methodIsSetterForField_whenSetterIsBuilderStyle_thenIsTrue() {
        when(field.getName()).thenReturn("field");
        doReturn(FieldType.class).when(field).getType();
        when(method.getName()).thenReturn("field");
        when(method.getParameters()).thenReturn(new Parameter[]{parameter});
        doReturn(FieldType.class).when(parameter).getType();

        assertThat(UnitCreator.methodIsSetterForField(method, field)).isTrue();
    }

    @Test
    void methodIsSetterForField_whenMethodParameterIsSubType_thenIsTrue() {
        when(field.getName()).thenReturn("field");
        doReturn(FieldType.class).when(field).getType();
        when(method.getName()).thenReturn("setField");
        when(method.getParameters()).thenReturn(new Parameter[]{parameter});
        doReturn(PolymorphicParameterType.class).when(parameter).getType();

        assertThat(UnitCreator.methodIsSetterForField(method, field)).isTrue();
    }

    @Test
    void methodIsSetterForField_whenMethodParameterIsOtherType_thenIsFalse() {
        when(field.getName()).thenReturn("field");
        doReturn(FieldType.class).when(field).getType();
        when(method.getName()).thenReturn("setField");
        when(method.getParameters()).thenReturn(new Parameter[]{parameter});
        doReturn(WrongFieldType.class).when(parameter).getType();

        assertThat(UnitCreator.methodIsSetterForField(method, field)).isFalse();
    }

    @Test
    void methodIsSetterForField_whenMethodHasNoParameters_thenIsFalse() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("setField");
        when(method.getParameters()).thenReturn(new Parameter[]{});

        assertThat(UnitCreator.methodIsSetterForField(method, field)).isFalse();
    }

    @Test
    void methodIsSetterForField_whenMethodHasMoreParameters_thenIsFalse() {
        when(field.getName()).thenReturn("field");
        when(method.getName()).thenReturn("setField");
        when(method.getParameters()).thenReturn(new Parameter[]{parameter, parameter});

        assertThat(UnitCreator.methodIsSetterForField(method, field)).isFalse();
    }

    private static class FieldType {
    }

    private static class PolymorphicParameterType extends FieldType {
    }

    private static class WrongFieldType {
    }
}
