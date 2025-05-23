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
package io.github.mattiaspersson09.junisert.value.common;

import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedSupportGenerator;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DependencyObjectRecursionTest {
    @Test
    void generate_whenRecursiveSelfParameter_andRecursiveConstructorHasConstructableOrSupportedTypes_thenCanConstruct() {
        AggregatedSupportGenerator argumentGenerator = new AggregatedSupportGenerator(Arrays.asList(
                ObjectValueGenerator.withForcedAccess(),
                new PrimitiveValueGenerator()
        ));
        DependencyObjectValueGenerator generator = DependencyObjectValueGenerator
                .buildDependencySupport(argumentGenerator)
                .withForcedAccess()
                .withMaxDependencyDepth(0)
                .build();

        RecursiveWithNonNullableParameters value = (RecursiveWithNonNullableParameters) generator
                .generate(RecursiveWithNonNullableParameters.class)
                .get();

        assertThat(value).isNotNull();
        assertThat(value.nullableSelf).isNotNull();
        assertThat(value.nullableObject).isNotNull();
        assertThat(value.nonNullableInt).isEqualTo(1);
        assertThat(value.nonNullableBoolean).isTrue();

        assertThat(value.nullableSelf.nullableSelf).isNull();
        assertThat(value.nullableSelf.nullableObject).isNotNull();
        assertThat(value.nullableSelf.nonNullableInt).isEqualTo(1);
        assertThat(value.nullableSelf.nonNullableBoolean).isTrue();
    }

    @Test
    void generate_whenRecursiveParameter_andConstructableOrSupportedDependency_thenCanConstruct() {
        AggregatedSupportGenerator argumentGenerator = new AggregatedSupportGenerator(Arrays.asList(
                ObjectValueGenerator.withForcedAccess(),
                new PrimitiveValueGenerator()
        ));
        DependencyObjectValueGenerator generator = DependencyObjectValueGenerator
                .buildDependencySupport(argumentGenerator)
                .withForcedAccess()
                .withMaxDependencyDepth(0)
                .build();

        HasRecursiveParameter value = (HasRecursiveParameter) generator
                .generate(HasRecursiveParameter.class)
                .get();

        assertThat(value).isNotNull();
        assertThat(value.recursive).isNotNull();
        assertThat(value.nonNullableInt).isEqualTo(1);
        assertThat(value.nonNullableBoolean).isTrue();

        assertThat(value.recursive.nullableSelf).isNull();
        assertThat(value.recursive.nullableObject).isNotNull();
        assertThat(value.recursive.nonNullableInt).isEqualTo(1);
        assertThat(value.recursive.nonNullableBoolean).isTrue();
    }

    @Test
    void generate_whenRecursiveParameterLeadingToCycle_andNotSupportedDependency_thenSetsToNullAtDepth() {
        AggregatedSupportGenerator argumentGenerator = new AggregatedSupportGenerator(Collections.singletonList(
                ObjectValueGenerator.withForcedAccess()
        ));
        DependencyObjectValueGenerator generatorWithDepthZero = DependencyObjectValueGenerator
                .buildDependencySupport(argumentGenerator)
                .withForcedAccess()
                .withMaxDependencyDepth(0)
                .build();

        DependencyObjectValueGenerator generatorWithDepthOne = DependencyObjectValueGenerator
                .buildDependencySupport(argumentGenerator)
                .withForcedAccess()
                .withMaxDependencyDepth(1)
                .build();

        CyclicRecursiveParameter cyclicRecursiveZeroDepth = (CyclicRecursiveParameter) generatorWithDepthZero
                .generate(CyclicRecursiveParameter.class)
                .get();
        CyclicRecursiveParameter cyclicRecursiveOneDepth = (CyclicRecursiveParameter) generatorWithDepthOne
                .generate(CyclicRecursiveParameter.class)
                .get();

        assertThat(cyclicRecursiveZeroDepth).isNotNull();
        assertThat(cyclicRecursiveZeroDepth.self).isNotNull();
        assertThat(cyclicRecursiveZeroDepth.cycle).isNotNull();
        assertThat(cyclicRecursiveZeroDepth.cycle.recursive).isNull();

        assertThat(cyclicRecursiveOneDepth).isNotNull();
        assertThat(cyclicRecursiveOneDepth.self).isNotNull();
        assertThat(cyclicRecursiveOneDepth.cycle).isNotNull();
        // First nested dependency (depth 1)
        assertThat(cyclicRecursiveOneDepth.cycle.recursive).isNotNull();
        assertThat(cyclicRecursiveOneDepth.cycle.recursive.self).isNull();
        assertThat(cyclicRecursiveOneDepth.cycle.recursive.cycle).isNull();
    }

    @Test
    void generate_whenCyclicParameterLeadingToRecursive_andNotSupportedDependency_thenSetsToNullAtDepth() {
        AggregatedSupportGenerator argumentGenerator = new AggregatedSupportGenerator(Collections.singletonList(
                ObjectValueGenerator.withForcedAccess()
        ));
        DependencyObjectValueGenerator generatorWithDepthZero = DependencyObjectValueGenerator
                .buildDependencySupport(argumentGenerator)
                .withForcedAccess()
                .withMaxDependencyDepth(0)
                .build();

        DependencyObjectValueGenerator generatorWithDepthOne = DependencyObjectValueGenerator
                .buildDependencySupport(argumentGenerator)
                .withForcedAccess()
                .withMaxDependencyDepth(1)
                .build();

        Cyclic cycle = (Cyclic) generatorWithDepthZero
                .generate(Cyclic.class)
                .get();

        Cyclic cycleOneDepth = (Cyclic) generatorWithDepthOne
                .generate(Cyclic.class)
                .get();

        assertThat(cycle).isNotNull();
        assertThat(cycle.recursive).isNotNull();
        assertThat(cycle.recursive.cycle).isNull();
        assertThat(cycle.recursive.self).isNull();

        assertThat(cycleOneDepth).isNotNull();
        assertThat(cycleOneDepth.recursive).isNotNull();
        // First nested dependency (depth 1)
        assertThat(cycleOneDepth.recursive.cycle).isNotNull();
        assertThat(cycleOneDepth.recursive.self).isNotNull();
        assertThat(cycleOneDepth.recursive.cycle.recursive).isNull();
        assertThat(cycleOneDepth.recursive.self.cycle).isNull();
        assertThat(cycleOneDepth.recursive.self.self).isNull();
    }

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private static class CyclicRecursiveParameter {
        private final Cyclic cycle;
        private final CyclicRecursiveParameter self;

        public CyclicRecursiveParameter(Cyclic cycle, CyclicRecursiveParameter self) {
            this.cycle = cycle;
            this.self = self;
        }
    }

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private static class Cyclic {
        private final CyclicRecursiveParameter recursive;

        public Cyclic(CyclicRecursiveParameter recursive) {
            this.recursive = recursive;
        }
    }

    private static class HasRecursiveParameter {
        private final RecursiveWithNonNullableParameters recursive;
        private final int nonNullableInt;
        private final boolean nonNullableBoolean;

        public HasRecursiveParameter(RecursiveWithNonNullableParameters recursive,
                                     int nonNullableInt,
                                     boolean nonNullableBoolean) {
            this.recursive = recursive;
            this.nonNullableInt = nonNullableInt;
            this.nonNullableBoolean = nonNullableBoolean;
        }
    }

    private static class RecursiveWithNonNullableParameters {
        private final RecursiveWithNonNullableParameters nullableSelf;
        private final Object nullableObject;
        private final int nonNullableInt;
        private final boolean nonNullableBoolean;

        public RecursiveWithNonNullableParameters(RecursiveWithNonNullableParameters nullableSelf,
                                                  Object nullableObject,
                                                  int nonNullableInt,
                                                  boolean nonNullableBoolean) {
            this.nullableSelf = nullableSelf;
            this.nullableObject = nullableObject;
            this.nonNullableInt = nonNullableInt;
            this.nonNullableBoolean = nonNullableBoolean;
        }
    }
}
