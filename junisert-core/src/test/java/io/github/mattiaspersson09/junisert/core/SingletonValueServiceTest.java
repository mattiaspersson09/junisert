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
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SingletonValueServiceTest {
    @Mock
    ValueCache valueCache;
    @Mock
    ValueGenerator<?> support;
    private SingletonValueService valueService;

    @BeforeEach
    void setUp() {
        valueService = new SingletonValueService(valueCache);
    }

    @Test
    void getInstance_thenHasDefaultSupportRegistered() {
        assertThat(SingletonValueService.getInstance().supportSize()).isGreaterThan(0);
    }

    @Test
    void getInstance_whenCalledSeveralTimes_thenAlwaysSameReference() {
        assertThat(SingletonValueService.getInstance()).isSameAs(SingletonValueService.getInstance());
    }

    @Test
    void registerSupport_whenAddingSupport_thenSupportIsAddedToGenerators() {
        int supportBefore = valueService.supportSize();

        valueService.registerSupport(support);

        assertThat(valueService.supportSize()).isGreaterThan(supportBefore);
    }

    @Test
    void registerNamedSupport_whenAddingSupport_thenSupportIsAddedToGenerators() {
        int supportBefore = valueService.supportSize();

        valueService.registerNamedSupport(support, "support name");

        assertThat(valueService.supportSize()).isGreaterThan(supportBefore);
    }

    @Test
    void getValue_whenValueIsCached_thenGetsFromCache() {
        when(valueCache.contains(any())).thenReturn(true);
        doReturn(new CacheValue()).when(valueCache).get(any());

        Value<?> value = valueService.getValue(Super.class);

        assertThat(value).isInstanceOf(CacheValue.class);

        verify(valueCache, times(1)).get(any());
    }

    @Test
    void getValue_whenValueIsNotCached_thenTriesToGenerate() {
        valueService.clear();
        when(valueCache.contains(any())).thenReturn(false);

        assertThatThrownBy(() -> valueService.getValue(Impl.class)).isInstanceOf(UnsupportedTypeError.class);
    }

    @Test
    void getValue_whenValueIsNotCached_thenGeneratesValue_andCaches() {
        when(valueCache.contains(any())).thenReturn(false);
        when(valueCache.save(any(), any())).thenAnswer(answer -> answer.getArgument(1));

        Value<?> value = valueService.getValue(Impl.class);

        assertThat(value.get()).isEqualTo(new Impl());

        verify(valueCache, times(1)).save(eq(Impl.class), any());
    }

    private static class CacheValue implements Value<Object> {
        @Override
        public Object get() {
            return null;
        }
    }
}
