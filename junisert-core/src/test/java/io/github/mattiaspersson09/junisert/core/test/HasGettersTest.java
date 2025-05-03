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
import io.github.mattiaspersson09.junisert.api.internal.service.ValueService;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.core.reflection.Unit;
import io.github.mattiaspersson09.junisert.testunits.getter.BeanAndBuilderStyle;
import io.github.mattiaspersson09.junisert.testunits.getter.BeanStyle;
import io.github.mattiaspersson09.junisert.testunits.getter.BooleanBeanStyle;
import io.github.mattiaspersson09.junisert.testunits.getter.BooleanBuilderStyle;
import io.github.mattiaspersson09.junisert.testunits.getter.BuilderStyle;
import io.github.mattiaspersson09.junisert.testunits.getter.MissingGetter;
import io.github.mattiaspersson09.junisert.testunits.getter.NotGettingField;
import io.github.mattiaspersson09.junisert.testunits.getter.TwoButOnlyOneWorking;
import io.github.mattiaspersson09.junisert.testunits.getter.TwoLettersOrLessBeanStyle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class HasGettersTest {
    @Mock
    ValueService valueService;

    private HasGetters hasGetters;

    @BeforeEach
    void setUp() {
        hasGetters = new HasGetters(valueService);
    }

    @Test
    void acceptsBeanStyleGetters() {
        doReturn((Value<?>) Object::new).when(valueService).getValue(Object.class);
        doReturn((Value<?>) BeanStyle::new).when(valueService).getValue(BeanStyle.class);
        doReturn((Value<?>) BooleanBeanStyle::new).when(valueService).getValue(BooleanBeanStyle.class);
        doReturn((Value<?>) TwoLettersOrLessBeanStyle::new).when(valueService)
                .getValue(TwoLettersOrLessBeanStyle.class);
        doReturn(new BooleanValue()).when(valueService).getValue(boolean.class);

        hasGetters.test(Unit.of(BeanStyle.class));
        hasGetters.test(Unit.of(BooleanBeanStyle.class));
        hasGetters.test(Unit.of(TwoLettersOrLessBeanStyle.class));
    }

    @Test
    void acceptsBuilderStyleGetters() {
        doReturn((Value<?>) Object::new).when(valueService).getValue(Object.class);
        doReturn(new BooleanValue()).when(valueService).getValue(boolean.class);
        doReturn((Value<?>) BuilderStyle::new).when(valueService).getValue(BuilderStyle.class);
        doReturn((Value<?>) BooleanBuilderStyle::new).when(valueService).getValue(BooleanBuilderStyle.class);

        hasGetters.test(Unit.of(BuilderStyle.class));
        hasGetters.test(Unit.of(BooleanBuilderStyle.class));
    }

    @Test
    void whenMoreThanOneGetter_thenIsSatisfiedWithAnyWorking() {
        doReturn((Value<?>) Object::new).when(valueService).getValue(any());
        doReturn((Value<?>) BeanAndBuilderStyle::new).when(valueService).getValue(BeanAndBuilderStyle.class);

        hasGetters.test(Unit.of(BeanAndBuilderStyle.class));
    }

    @Test
    void whenMoreThanOneGetter_andOnlyOneIsWorking_thenIsSatisfiedWithOneWorking() {
        doReturn((Value<?>) Object::new).when(valueService).getValue(any());
        doReturn((Value<?>) TwoButOnlyOneWorking::new).when(valueService).getValue(TwoButOnlyOneWorking.class);

        hasGetters.test(Unit.of(TwoButOnlyOneWorking.class));
    }

    @Test
    void whenHasGetter_butGetterIsNotWorking_thenThrowsUnitAssertionError() {
        doReturn((Value<?>) Object::new).when(valueService).getValue(any());

        assertThatThrownBy(() -> hasGetters.test(Unit.of(NotGettingField.class)))
                .isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void whenMissingGetters_thenThrowsUnitAssertionError() {
        assertThatThrownBy(() -> hasGetters.test(Unit.of(MissingGetter.class)))
                .isInstanceOf(UnitAssertionError.class);
    }

    private static class BooleanValue implements Value<Boolean> {
        @Override
        public Boolean get() {
            return true;
        }

        @Override
        public Boolean asEmpty() {
            return false;
        }
    }
}
