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
package io.github.mattiaspersson09.junisert.core.internal.test;

import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.ValueService;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Field;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Unit;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HasGettersTest {
    @Mock
    ValueService valueService;
    @Mock
    InstanceCreator instanceCreator;
    @Mock
    Unit unit;
    @Mock
    Field field;
    private HasGetters hasGetters;

    @BeforeEach
    void setUp() {
        hasGetters = new HasGetters(valueService, instanceCreator);
    }

    @Test
    void givenField_whenNotFindingAnyInstanceGetter_thenThrowsUnitAssertionError() {
        when(unit.getName()).thenReturn("unit");
        when(unit.getFields()).thenReturn(Collections.singletonList(field));
        when(unit.findMethodsMatching(any())).thenReturn(Collections.emptyList());
        when(field.isInstanceMember()).thenReturn(true);

        assertThatThrownBy(() -> hasGetters.test(unit)).isInstanceOf(UnitAssertionError.class);

        verifyNoInteractions(instanceCreator);
        verifyNoInteractions(valueService);
    }
}
