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

import io.github.mattiaspersson09.junisert.api.internal.service.ValueService;

import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class CachedValueLookupPerformanceTest {
    ValueService valueService;

    @Setup(Level.Trial)
    public void setUp() {
        System.out.println("Setting up for value lookup benchmark");
        valueService = SingletonValueService.getInstance();
        System.out.println("Pre caching value");
        valueService.getValue(List.class);
    }

    @Benchmark
    public void lookup_javaInternalAlreadyCached(Blackhole blackhole) {
        blackhole.consume(valueService.getValue(List.class).get());
    }
}
