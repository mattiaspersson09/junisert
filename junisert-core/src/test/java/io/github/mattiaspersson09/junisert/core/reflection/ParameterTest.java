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

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParameterTest {
    @Mock
    java.lang.reflect.Parameter origin;

    @Test
    void isSynthetic_whenSynthetic_thenIsTrue() {
        Parameter parameter = Parameter.of(origin);

        when(origin.isSynthetic()).thenReturn(true);

        assertThat(parameter.isSynthetic()).isTrue();
    }

    @Test
    void isSynthetic_whenNotSynthetic_thenIsFalse() {
        Parameter parameter = Parameter.of(origin);

        when(origin.isSynthetic()).thenReturn(false);

        assertThat(parameter.isSynthetic()).isFalse();
    }

    @Test
    void equalsTest() {
        Parameter parameter = Parameter.of(origin);

        assertThat(parameter).isEqualTo(Parameter.of(origin));
        assertThat(parameter).isEqualTo((Object) parameter);
        assertThat(parameter).isNotEqualTo(null);
        assertThat(parameter).isNotEqualTo(new Object());
    }

    @Test
    void equals_whenHasOtherOrigin_thenIsFalse() {
        Parameter parameter = Parameter.of(origin);

        assertThat(parameter).isNotEqualTo(Parameter.of(mock(java.lang.reflect.Parameter.class)));
    }

    @Test
    void hashCodeTest() {
        Parameter parameter = Parameter.of(origin);

        assertThat(parameter.hashCode()).isEqualTo(Parameter.of(origin).hashCode());
        assertThat(parameter.hashCode()).isEqualTo(((Object) parameter).hashCode());
        assertThat(parameter.hashCode()).isNotEqualTo(Objects.hashCode(null));
        assertThat(parameter.hashCode()).isNotEqualTo(new Object().hashCode());
    }

    @Test
    void hashCode_whenHasOtherOrigin_thenIsFalse() {
        Parameter parameter = Parameter.of(origin);

        assertThat(parameter.hashCode()).isNotEqualTo(Parameter.of(mock(java.lang.reflect.Parameter.class)).hashCode());
    }

    @Test
    void toStringTest() {
        Parameter parameter = Parameter.of(origin);

        doReturn(Object.class).when(origin).getType();
        when(origin.getName()).thenReturn("parameterName");

        assertThat(parameter.toString()).isEqualTo("class java.lang.Object parameterName");
    }
}
