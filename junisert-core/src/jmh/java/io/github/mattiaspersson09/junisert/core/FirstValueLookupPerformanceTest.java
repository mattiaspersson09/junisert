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
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPrivateConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPublicConstructor;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;
import io.github.mattiaspersson09.junisert.testunits.unit.enumeration.EnumUnit;

import java.security.SecurityPermission;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class FirstValueLookupPerformanceTest {
    ValueService valueService;

    @Setup(Level.Trial)
    public void setUp() {
        System.out.println("Setting up for value lookup benchmark");
        valueService = SingletonValueService.getInstance();
    }

    @Benchmark
    public void lookup_primitiveValue(Blackhole blackhole) {
        blackhole.consume(valueService.getValue(int.class).get());
    }

    @Benchmark
    public void lookup_wrapperPrimitiveValue(Blackhole blackhole) {
        blackhole.consume(valueService.getValue(Integer.class).get());
    }

    @Benchmark
    public void lookup_arrayValue(Blackhole blackhole) {
        blackhole.consume(valueService.getValue(int[].class).get());
    }

    @Benchmark
    public void lookup_enumValue(Blackhole blackhole) {
        blackhole.consume(valueService.getValue(EnumUnit.class).get());
    }

    @Benchmark
    public void lookup_earlyJavaInternal(Blackhole blackhole) {
        blackhole.consume(valueService.getValue(String.class).get());
    }

    @Benchmark
    public void lookup_proxyValue(Blackhole blackhole) {
        blackhole.consume(valueService.getValue(Super.class).get());
    }

    @Benchmark
    public void lookup_lateJavaInternal(Blackhole blackhole) {
        blackhole.consume(valueService.getValue(SecurityPermission.class).get());
    }

    @Benchmark
    public void lookup_defaultConstructor_notNeedingForcedAccess(Blackhole blackhole) {
        blackhole.consume(valueService.getValue(DefaultPublicConstructor.class).get());
    }

    @Benchmark
    public void lookup_defaultConstructor_withForcedAccessNeeded(Blackhole blackhole) {
        blackhole.consume(valueService.getValue(DefaultPrivateConstructor.class).get());
    }

    @Benchmark
    public void lookup_argumentConstructor_withSimpleDependencies(Blackhole blackhole) {
        blackhole.consume(valueService.getValue(WithSimpleDependencies.class).get());
    }

    public static class WithSimpleDependencies {
        private final boolean booleanField;
        private final Boolean wrapperBooleanField;
        private final int intField;
        private final Integer wrapperIntField;

        public WithSimpleDependencies(boolean booleanField,
                                      Boolean wrapperBooleanField,
                                      int intField,
                                      Integer wrapperIntField) {
            this.booleanField = booleanField;
            this.wrapperBooleanField = wrapperBooleanField;
            this.intField = intField;
            this.wrapperIntField = wrapperIntField;
        }

        public boolean isBooleanField() {
            return booleanField;
        }

        public Boolean getWrapperBooleanField() {
            return wrapperBooleanField;
        }

        public int getIntField() {
            return intField;
        }

        public Integer getWrapperIntField() {
            return wrapperIntField;
        }
    }
}
