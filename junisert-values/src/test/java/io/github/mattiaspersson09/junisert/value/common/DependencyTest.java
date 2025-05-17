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
package io.github.mattiaspersson09.junisert.value.common;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPrivateConstructor;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Base;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DependencyTest {
    @Mock
    ValueGenerator<?> valueSupport;
    @Mock
    Constructor<?> constructor;
    @Mock
    Parameter parameter;
    @Mock
    Function<Class<?>, Constructor<?>> extractDependencyConstructor;

    @Test
    void createInstance_whenForcingConstructorAccess_thenTriesToSetAccessible() throws InvocationTargetException,
                                                                                InstantiationException,
                                                                                IllegalAccessException {
        Dependency dependency = createDependencyWithForcedAccessAndDependencyDepth(true, 0);

        when(constructor.getParameters()).thenReturn(new Parameter[]{parameter});
        doReturn(new Object()).when(constructor).newInstance(any());
        doNothing().when(constructor).setAccessible(anyBoolean());
        doReturn(Object.class).when(parameter).getType();
        when(valueSupport.supports(any())).thenReturn(true);
        doReturn((Value<?>) Object::new).when(valueSupport).generate(any());

        dependency.createInstance();

        verify(constructor, times(1)).setAccessible(true);
    }

    @Test
    void createInstance_whenNotForcingConstructorAccess_thenDoesNotTryToSetAccessible() throws InvocationTargetException,
                                                                                        InstantiationException,
                                                                                        IllegalAccessException {
        Dependency dependency = createDependencyWithForcedAccessAndDependencyDepth(false, 0);

        when(constructor.getParameters()).thenReturn(new Parameter[]{parameter});
        doReturn(new Object()).when(constructor).newInstance(any());
        doReturn(Object.class).when(parameter).getType();
        when(valueSupport.supports(any())).thenReturn(true);
        doReturn((Value<?>) Object::new).when(valueSupport).generate(any());

        dependency.createInstance();

        verify(constructor, never()).setAccessible(anyBoolean());
    }

    @Test
    void createInstance_whenUnableToCreateInstance_thenThrowsUnsupportedConstructionError() throws InvocationTargetException,
                                                                                            InstantiationException,
                                                                                            IllegalAccessException {
        Dependency dependency = createDependencyWithDependencyDepth(0);

        when(constructor.getParameters()).thenReturn(new Parameter[]{parameter});
        when(constructor.newInstance(any())).thenThrow(RuntimeException.class);
        doReturn(Impl.class).when(constructor).getDeclaringClass();
        doReturn(Object.class).when(parameter).getType();
        when(valueSupport.supports(any())).thenReturn(true);
        doReturn((Value<?>) Object::new).when(valueSupport).generate(any());

        assertThatThrownBy(dependency::createInstance).isInstanceOf(UnsupportedConstructionError.class);
    }

    @Test
    void createInstance_whenDependencyConstructorIsDefault_thenDoesNotTryToHandleDependencyValues() throws InvocationTargetException,
                                                                                                    InstantiationException,
                                                                                                    IllegalAccessException {
        Dependency dependency = createDependencyWithDependencyDepth(0);

        when(constructor.getParameters()).thenReturn(new Parameter[]{});
        doReturn(new Object()).when(constructor).newInstance();

        dependency.createInstance();

        verify(valueSupport, never()).supports(any());
        verify(valueSupport, never()).generate(any());
    }

    @Test
    void createInstance_whenZeroDependencyDepth_andValueSupportSupportsDependencyValue_thenGeneratesSupportedValue() throws InvocationTargetException,
                                                                                                                     InstantiationException,
                                                                                                                     IllegalAccessException {
        Dependency dependency = createDependencyWithDependencyDepth(0);

        when(valueSupport.supports(any())).thenReturn(true);
        doReturn((Value<?>) Object::new).when(valueSupport).generate(any());

        when(constructor.getParameters()).thenReturn(new Parameter[]{parameter});
        doReturn(new Object()).when(constructor).newInstance(any());

        dependency.createInstance();

        verify(valueSupport, times(1)).supports(any());
        verify(valueSupport, times(1)).generate(any());
    }

    @Test
    void createInstance_whenZeroDependencyDepth_andValueSupportDoesNotSupport_thenThrowsUnsupportedTypeError() {
        Dependency dependency = createDependencyWithDependencyDepth(0);

        when(constructor.getParameters()).thenReturn(new Parameter[]{parameter});
        doReturn(Impl.class).when(constructor).getDeclaringClass();
        doReturn(Object.class).when(parameter).getType();
        when(valueSupport.supports(any())).thenReturn(false);

        assertThatThrownBy(dependency::createInstance).isInstanceOf(UnsupportedTypeError.class);
    }

    @Test
    void createInstance_whenSeveralDependencyDepths_butValueSupportSupportsDependencyValue_thenHandlesSupportDirectly() {
        int depth = 5;
        Dependency dependency = createDependencyWithDependencyDepth(depth);

        when(valueSupport.supports(any())).thenReturn(true);
        doReturn((Value<?>) Object::new).when(valueSupport).generate(any());

        when(constructor.getParameters()).thenReturn(new Parameter[]{parameter});

        dependency.createInstance();

        verify(valueSupport, times(1)).supports(any());
        verify(valueSupport, times(1)).generate(any());
        verify(extractDependencyConstructor, never()).apply(any());
    }

    @SuppressWarnings("ConstantValue")
    @Test
    void createInstance_whenSeveralDependencyDepths_butNoExtractingDependencyConstructorFunction_thenDoesNotTryToCreateNewDependency() throws InvocationTargetException,
                                                                                                                                       InstantiationException,
                                                                                                                                       IllegalAccessException {
        int depth = 5;
        Function<Class<?>, Constructor<?>> extractDependencyConstructor = null;
        Dependency dependency = new Dependency(
                Impl.class,
                constructor,
                valueSupport,
                false,
                depth,
                extractDependencyConstructor
        );

        doReturn(Object.class).when(parameter).getType();
        when(constructor.getParameters()).thenReturn(new Parameter[]{parameter});
        doReturn(Impl.class).when(constructor).getDeclaringClass();
        when(valueSupport.supports(any())).thenReturn(false);

        assertThatThrownBy(dependency::createInstance).isInstanceOf(UnsupportedTypeError.class);

        // Would be invoked for every depth
        verify(constructor, never()).newInstance(any());
    }

    @Test
    void shouldBeConstructable_whenConstructorIsAccessible_thenIsConstructable() throws NoSuchMethodException {
        Constructor<?> constructor = Impl.class.getDeclaredConstructor();

        assertThat(Dependency.shouldBeConstructable(constructor, false)).isTrue();
    }

    @Test
    void shouldBeConstructable_whenConstructorIsInaccessible_butForcingAccess_thenIsConstructable() throws NoSuchMethodException {
        Constructor<?> constructor = DefaultPrivateConstructor.class.getDeclaredConstructor();

        assertThat(Dependency.shouldBeConstructable(constructor, true)).isTrue();
    }

    @Test
    void shouldBeConstructable_whenConstructorIsInaccessible_andNotForcingAccess_thenIsNotConstructable() throws NoSuchMethodException {
        Constructor<?> constructor = DefaultPrivateConstructor.class.getDeclaredConstructor();

        assertThat(Dependency.shouldBeConstructable(constructor, false)).isFalse();
    }

    @Test
    void shouldBeConstructable_whenConstructorIsNull_thenIsNotConstructable() {
        assertThat(Dependency.shouldBeConstructable(null, false)).isFalse();
    }

    @Test
    void shouldBeConstructable_whenConstructorIsOwnedByAbstractUnit_thenIsNotConstructable() throws NoSuchMethodException {
        Constructor<?> constructor = Base.class.getDeclaredConstructor(int.class);

        assertThat(Dependency.shouldBeConstructable(constructor, false)).isFalse();
    }

    private Dependency createDependencyWithForcedAccessAndDependencyDepth(boolean forceAccess, int depth) {
        return new Dependency(
                Impl.class,
                constructor,
                valueSupport,
                forceAccess,
                depth,
                extractDependencyConstructor
        );
    }

    private Dependency createDependencyWithDependencyDepth(int depth) {
        return createDependencyWithForcedAccessAndDependencyDepth(true, depth);
    }
}
