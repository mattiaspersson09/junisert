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
package io.github.mattiaspersson09.junisert.core;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;

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
public class ConstructorInstanceCreatorTest {
    @Mock
    ValueGenerator<Object> instanceGenerator;
    private InstanceCreator instanceCreator;

    @BeforeEach
    void setUp() {
        instanceCreator = new ConstructorInstanceCreator(instanceGenerator);
    }

    @Test
    void givenInstanceGenerator_whenGeneratorSupportsCreation_thenCreatesInstance() {
        when(instanceGenerator.supports(any())).thenReturn(true);
        doReturn((Value<?>) UnitClass::new).when(instanceGenerator).generate(UnitClass.class);

        assertThat(instanceCreator.createInstance(UnitClass.class)).isNotNull();
        assertThat(instanceCreator.createInstance(UnitClass.class)).isInstanceOf(UnitClass.class);
    }

    @Test
    void givenInstanceGenerator_whenGeneratorDoesNotSupportCreation_thenThrowsUnsupportedTypeError() {
        when(instanceGenerator.supports(any())).thenReturn(false);

        assertThatThrownBy(() -> instanceCreator.createInstance(UnitClass.class))
                .isInstanceOf(UnsupportedTypeError.class);
    }

    private static class UnitClass {
    }
}
