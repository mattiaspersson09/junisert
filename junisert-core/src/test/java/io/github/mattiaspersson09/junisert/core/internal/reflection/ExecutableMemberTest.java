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

import io.github.mattiaspersson09.junisert.testunits.polymorphism.Base;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

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
public class ExecutableMemberTest {
    @Mock
    Method executable;
    @Mock
    Parameter parameter;
    private ExecutableMember member;

    @BeforeEach
    void setUp() {
        when(executable.getParameters()).thenReturn(new Parameter[]{parameter});

        member = new ExecutableUnitMember(executable);
    }

    @Test
    void hasParameters() {
        Executable method = Mockito.mock(Method.class);
        when(method.getParameters()).thenReturn(new Parameter[]{});
        ExecutableMember parameterlessMember = new ExecutableUnitMember(method);

        assertThat(member.hasParameters()).isTrue();
        assertThat(parameterlessMember.hasParameters()).isFalse();
    }

    @Test
    void hasNoParameters() {
        Executable method = Mockito.mock(Method.class);
        when(method.getParameters()).thenReturn(new Parameter[]{});
        ExecutableMember parameterlessMember = new ExecutableUnitMember(method);

        assertThat(member.hasNoParameters()).isFalse();
        assertThat(parameterlessMember.hasNoParameters()).isTrue();
    }

    @Test
    void hasParameterCount() {
        Executable method = Mockito.mock(Method.class);
        when(method.getParameters()).thenReturn(new Parameter[]{});
        ExecutableMember parameterlessMember = new ExecutableUnitMember(method);

        assertThat(member.hasParameterCount(1)).isTrue();
        assertThat(member.hasParameterCount(0)).isFalse();
        assertThat(parameterlessMember.hasParameterCount(0)).isTrue();
    }

    @Test
    void hasParameterType_whenExactType_thenIsTrue() {
        doReturn(Base.class).when(parameter).getType();

        assertThat(member.hasParameterType(Base.class)).isTrue();
    }

    @Test
    void hasParameterType_whenPolymorphicType_thenIsFalse() {
        doReturn(Base.class).when(parameter).getType();

        assertThat(member.hasParameterType(Impl.class)).isFalse();
        assertThat(member.hasParameterType(Super.class)).isFalse();
    }

    @Test
    void hasParameterAssignableFrom_whenExactType_orSubtype_thenIsTrue() {
        doReturn(Base.class).when(parameter).getType();

        assertThat(member.hasParameterFrom(Base.class)).isTrue();
        assertThat(member.hasParameterFrom(Impl.class)).isTrue();
    }

    @Test
    void hasParameterAssignableFrom_whenSuperType_thenIsFalse() {
        doReturn(Base.class).when(parameter).getType();

        assertThat(member.hasParameterFrom(Super.class)).isFalse();
    }

    @Test
    void hasParameterAssignableTo_whenExactType_orSuperType_thenIsTrue() {
        doReturn(Base.class).when(parameter).getType();

        assertThat(member.hasParameterOf(Base.class)).isTrue();
        assertThat(member.hasParameterOf(Super.class)).isTrue();
    }

    @Test
    void hasParameterAssignableTo_whenSubtype_thenIsFalse() {
        doReturn(Base.class).when(parameter).getType();

        assertThat(member.hasParameterOf(Impl.class)).isFalse();
    }

    @Test
    void getParameterTypes_whenHasParameters_thenReturnsTypes() {
        doReturn(Super.class).when(parameter).getType();

        assertThat(member.getParameterTypes())
                .hasSize(1)
                .allMatch(parameter -> parameter.equals(Super.class));
    }

    @Test
    void getParameterTypes_whenHasParameters_thenReturnsTypesInDeclarationOrder() {
        Executable method = Mockito.mock(Method.class);
        Parameter objectParameter = Mockito.mock(Parameter.class);
        Parameter stringParameter = Mockito.mock(Parameter.class);
        Parameter integerParameter = Mockito.mock(Parameter.class);

        when(method.getParameters()).thenReturn(new Parameter[]{objectParameter, stringParameter, integerParameter});
        doReturn(Object.class).when(objectParameter).getType();
        doReturn(String.class).when(stringParameter).getType();
        doReturn(Integer.class).when(integerParameter).getType();

        ExecutableMember member = new ExecutableUnitMember(method);

        assertThat(member.getParameterTypes()).hasSize(3);
        assertThat(member.getParameterTypes().get(0)).isEqualTo(Object.class);
        assertThat(member.getParameterTypes().get(1)).isEqualTo(String.class);
        assertThat(member.getParameterTypes().get(2)).isEqualTo(Integer.class);
    }

    @Test
    void getParameterTypes_whenHasNoParameters_thenReturnsEmptyList() {
        Executable method = Mockito.mock(Method.class);
        when(method.getParameters()).thenReturn(new Parameter[]{});

        ExecutableMember member = new ExecutableUnitMember(method);

        assertThat(member.getParameterTypes()).isEmpty();
    }

    @Test
    void equalsTest() {
        Executable method = Mockito.mock(Method.class);
        when(method.getParameters()).thenReturn(new Parameter[]{});

        assertThat(member).isEqualTo((Object) member);
        assertThat(member).isEqualTo(new ExecutableUnitMember(executable));
        assertThat(member).isNotEqualTo(new ExecutableUnitMember(method));
        assertThat(member).isNotEqualTo(new Object());
        assertThat(member).isNotEqualTo(null);
    }

    @Test
    void hashCodeTest() {
        Executable method = Mockito.mock(Method.class);
        when(method.getParameters()).thenReturn(new Parameter[]{});

        assertThat(member.hashCode()).isEqualTo(((Object) member).hashCode());
        assertThat(member.hashCode()).isEqualTo(new ExecutableUnitMember(executable).hashCode());
        assertThat(member.hashCode()).isNotEqualTo(new ExecutableUnitMember(method).hashCode());
        assertThat(member.hashCode()).isNotEqualTo(new Object().hashCode());
        assertThat(member.hashCode()).isNotEqualTo(Objects.hashCode(null));
    }

    @Test
    void toStringTest() {
        when(executable.getName()).thenReturn("methodName");
        doReturn(Object.class).when(parameter).getType();

        assertThat(member.toString()).isEqualTo("methodName([class java.lang.Object])");
    }

    private static class ExecutableUnitMember extends ExecutableMember {
        protected ExecutableUnitMember(Executable origin) {
            super(origin);
        }

        @Override
        public Class<?> getType() {
            return null;
        }

        @Override
        public boolean equals(Object object) {
            return super.equals(object);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
