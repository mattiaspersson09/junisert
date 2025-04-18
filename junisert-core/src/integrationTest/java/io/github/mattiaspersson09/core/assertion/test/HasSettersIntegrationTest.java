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
package io.github.mattiaspersson09.core.assertion.test;

import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.api.internal.service.ValueService;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.core.reflection.UnitCreator;
import io.github.mattiaspersson09.junisert.core.test.HasSetters;
import io.github.mattiaspersson09.junisert.testunits.setter.BeanAndBuilderStyle;
import io.github.mattiaspersson09.junisert.testunits.setter.BeanStyle;
import io.github.mattiaspersson09.junisert.testunits.setter.BuilderStyle;
import io.github.mattiaspersson09.junisert.testunits.setter.HybridStyle;
import io.github.mattiaspersson09.junisert.testunits.setter.NotSettingField;
import io.github.mattiaspersson09.junisert.testunits.setter.TwoButOnlyOneWorking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class HasSettersIntegrationTest {
    @Mock
    ValueService valueService;

    private HasSetters hasSetters;

    @BeforeEach
    void setUp() {
        hasSetters = new HasSetters(valueService);
    }

    @Test
    void acceptsBeanStyleSetters() {
        doReturn((Value<?>) Object::new).when(valueService).getValue(any());

        hasSetters.test(UnitCreator.createFrom(BeanStyle.class));
    }

    @Test
    void acceptsBuilderStyleSetters() {
        doReturn((Value<?>) Object::new).when(valueService).getValue(any());

        hasSetters.test(UnitCreator.createFrom(BuilderStyle.class));
    }

    @Test
    void acceptsHybridStyleSetters() {
        doReturn((Value<?>) Object::new).when(valueService).getValue(any());

        hasSetters.test(UnitCreator.createFrom(HybridStyle.class));
    }

    @Test
    void whenMoreThanOneSetter_thenIsSatisfiedWithAnyWorking() {
        doReturn((Value<?>) Object::new).when(valueService).getValue(any());

        hasSetters.test(UnitCreator.createFrom(BeanAndBuilderStyle.class));
    }

    @Test
    void whenMoreThanOneSetter_andOnlyOneIsWorking_thenIsSatisfiedWithOneWorking() {
        doReturn((Value<?>) Object::new).when(valueService).getValue(any());

        hasSetters.test(UnitCreator.createFrom(TwoButOnlyOneWorking.class));
    }

    @Test
    void whenHasSetter_butSetterIsNotWorking_thenThrowsUnitAssertionError() {
        doReturn((Value<?>) Object::new).when(valueService).getValue(any());

        assertThatThrownBy(() -> hasSetters.test(UnitCreator.createFrom(NotSettingField.class)))
                .isInstanceOf(UnitAssertionError.class);
    }
}
