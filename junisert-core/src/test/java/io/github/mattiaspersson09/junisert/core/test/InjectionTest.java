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

import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.core.reflection.Invokable;
import io.github.mattiaspersson09.junisert.core.reflection.Unit;
import io.github.mattiaspersson09.junisert.testunits.setter.BeanStyle;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InjectionTest {
    @Mock
    Unit unit;
    @Mock
    Invokable target;

    private Injection injection;

    @BeforeEach
    void setUp() {
        injection = new Injection(unit, target);
    }

    @Test
    void inject_whenInjectionIsSuccessful_thenReturnsTrue() {
        Object unitInstance = new BeanStyle();

        injection.setup(instance -> true);
        injection.shouldResultIn(instance -> true);

        when(unit.createInstance()).thenReturn(Optional.of(unitInstance));

        assertThat(injection.inject()).isTrue();
    }

    @Test
    void inject_whenInjectionIsUnsuccessful_thenReturnsFalse() {
        Object unitInstance = new BeanStyle();

        injection.setup(instance -> true);
        injection.shouldResultIn(instance -> false);

        when(unit.createInstance()).thenReturn(Optional.of(unitInstance));

        assertThat(injection.inject()).isFalse();
    }

    @Test
    void inject_whenMissingUnitInstance_thenThrowsUnsupportedConstructionError() {
        when(unit.createInstance()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> injection.inject()).isInstanceOf(UnsupportedConstructionError.class);
    }

    @Test
    void inject_whenSetupIsUnsuccessful_andUnableToGuaranteeInjection_thenReturnsFalse() {
        injection.setup(instance -> false);
        injection.shouldResultIn(instance -> true);

        when(unit.createInstance()).thenReturn(Optional.of(new BeanStyle()));

        assertThat(injection.inject()).isFalse();
    }

    @Test
    void inject_whenInjectionFails_thenThrowsGivenOnError() throws InvocationTargetException, IllegalAccessException {
        Object unitInstance = new BeanStyle();

        injection.setup(instance -> true);
        injection.shouldResultIn(instance -> true);
        injection.onInjectionError(() -> new Error("injection failure"));

        when(unit.createInstance()).thenReturn(Optional.of(unitInstance));
        when(target.invoke(any(), any())).thenThrow(InvocationTargetException.class);

        assertThatThrownBy(() -> injection.inject(new Object()))
                .isInstanceOf(Error.class)
                .hasMessage("injection failure");
    }
}
