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
package io.github.mattiaspersson09.junisert.core.test;

import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.core.reflection.Invokable;
import io.github.mattiaspersson09.junisert.testunits.setter.BeanStyle;

import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InjectionTest {
    @Mock
    InstanceCreator instanceCreator;
    @Mock
    Invokable target;

    private Injection injection;

    @BeforeEach
    void setUp() {
        injection = new Injection(target, instanceCreator);
    }

    @Test
    void inject_whenInjectionIsSuccessful_thenReturnsTrue() {
        doReturn(BeanStyle.class).when(target).getParent();
        doReturn((Value<?>) BeanStyle::new).when(instanceCreator).createInstance(BeanStyle.class);

        assertThat(injection.inject()).isTrue();
    }

    @Test
    void inject_whenInjectionIsUnsuccessful_thenReturnsFalse() {
        doReturn(BeanStyle.class).when(target).getParent();
        doReturn((Value<?>) BeanStyle::new).when(instanceCreator).createInstance(BeanStyle.class);

        injection.shouldResultIn(instance -> false);

        assertThat(injection.inject()).isFalse();
    }

    @Test
    void inject_whenCanNotConstructInstance_thenPropagatesServiceFailure() {
        when(instanceCreator.createInstance(target.getParent())).thenThrow(UnsupportedConstructionError.class);

        assertThatThrownBy(() -> injection.inject()).isInstanceOf(UnsupportedConstructionError.class);
    }

    @Test
    void inject_whenSetupIsUnsuccessful_thenReturnsFalse() {
        doReturn(BeanStyle.class).when(target).getParent();
        doReturn((Value<?>) BeanStyle::new).when(instanceCreator).createInstance(BeanStyle.class);

        injection.setup(instance -> false);

        assertThat(injection.inject()).isFalse();
    }

    @Test
    void inject_whenInjectionFails_thenThrowsGivenError() throws InvocationTargetException, IllegalAccessException {
        doReturn(BeanStyle.class).when(target).getParent();
        when(target.invoke(any(), any())).thenThrow(InvocationTargetException.class);

        injection.onInjectionFail(() -> new UnitAssertionError("injection failure"));

        assertThatThrownBy(() -> injection.inject(new Object()))
                .isInstanceOf(UnitAssertionError.class)
                .hasMessage("injection failure");
    }

    @Test
    void inject_whenInjectionFails_andNoGivenErrorSupplier_thenThrowsGenericUnitAssertionError() throws InvocationTargetException,
                                                                                                 IllegalAccessException {
        doReturn(BeanStyle.class).when(target).getParent();
        doReturn((Value<?>) BeanStyle::new).when(instanceCreator).createInstance(BeanStyle.class);
        when(target.invoke(any(), any())).thenThrow(InvocationTargetException.class);

        assertThatThrownBy(() -> injection.inject(new Object()))
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("Failed to invoke");
    }
}
