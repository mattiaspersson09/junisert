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
package io.github.mattiaspersson09.junisert.core.assertion.test;

import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.api.internal.service.ValueService;
import io.github.mattiaspersson09.junisert.core.reflection.UnitCreator;
import io.github.mattiaspersson09.junisert.testunits.setter.NotSettingField;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HasSetterTest extends BaseUnitTest {
    @Mock
    ValueService valueService;

    private HasSetter hasSetter;

    @BeforeEach
    void setUp() {
        hasSetter = new HasSetter(valueService);
    }

    @Test
    void field_whenHasNoSetters_thenThrowsUnitAssertionError() {
        when(unit.getFields()).thenReturn(Collections.singletonList(field));
        when(field.getSetters()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> hasSetter.test(unit)).isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void field_whenHasSetter_butSetterIsNotChangingValue_thenThrowsUnitAssertionError() {
        when(valueService.findValue(any())).thenReturn(Optional.of(String::new));

        assertThatThrownBy(() -> hasSetter.test(UnitCreator.createFrom(NotSettingField.class)))
                .isInstanceOf(UnitAssertionError.class);
    }

    private <T> T instanceFrom(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
