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

import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CachingDependencyGeneratorTest {
    @Mock
    ValueCache valueCache;
    @Mock
    ValueGenerator<?> dependencyGenerator;
    private CachingDependencyGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new CachingDependencyGenerator(dependencyGenerator, valueCache);
    }

    @Test
    void supports_whenDependencyGeneratorSupports_thenIsTrue() {
        when(dependencyGenerator.supports(any())).thenReturn(true);

        assertThat(generator.supports(Impl.class)).isTrue();
    }

    @Test
    void supports_whenDependencyGeneratorDoesNotSupports_thenIsFalse() {
        when(dependencyGenerator.supports(any())).thenReturn(false);

        assertThat(generator.supports(Impl.class)).isFalse();
    }

    @Test
    void generate_whenCacheContainsValueToGenerate_thenReturnsCachedValue() {
        when(valueCache.contains(any())).thenReturn(true);
        doReturn((Value<?>) Impl::new).when(valueCache).get(any());

        generator.generate(Impl.class);

        verify(valueCache, times(1)).contains(any());
        verify(valueCache, times(1)).get(Impl.class);
        verifyNoInteractions(dependencyGenerator);
    }

    @Test
    void generate_whenCacheDoesNotContainsValueToGenerate_thenGeneratesValueAndCaches() {
        when(valueCache.contains(any())).thenReturn(false);
        when(valueCache.save(any(), any())).thenAnswer(answer -> answer.getArgument(1));
        doReturn((Value<?>) Impl::new).when(dependencyGenerator).generate(Impl.class);

        Value<?> value = generator.generate(Impl.class);

        assertThat(value.get()).isEqualTo(new Impl());

        verify(valueCache, times(1)).save(any(), any());
        verify(dependencyGenerator, times(1)).generate(Impl.class);
    }
}
