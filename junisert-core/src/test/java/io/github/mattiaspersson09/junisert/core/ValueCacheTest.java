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
import io.github.mattiaspersson09.junisert.core.ValueCache.CacheValue;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.ExtendingImpl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ValueCacheTest {
    private ValueCache valueCache;

    @BeforeEach
    void setUp() {
        valueCache = new ValueCache();
    }

    @Test
    void save_whenValueIsNotCached_thenCachesAndReturnsCached() {
        assertThat(valueCache.size()).isZero();

        Value<?> cached = valueCache.save(Impl.class, new CacheValue(new Impl(), null));

        assertThat(valueCache.size()).isEqualTo(1);
        assertThat(cached.get()).isEqualTo(new Impl());
    }

    @Test
    void save_whenValueIsAlreadyCached_thenReturnsAlreadyCached() {
        Value<?> cached = valueCache.save(Impl.class, new CacheValue(new ExtendingImpl(), null));
        Value<?> alreadyCached = valueCache.save(Impl.class, new CacheValue(new Impl(), null));

        assertThat(valueCache.size()).isEqualTo(1);
        assertThat(alreadyCached).isSameAs(cached);
        assertThat(alreadyCached.get()).isInstanceOf(ExtendingImpl.class);
    }
}
