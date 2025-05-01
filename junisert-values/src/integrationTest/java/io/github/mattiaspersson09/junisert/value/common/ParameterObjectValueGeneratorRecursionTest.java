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

public class ParameterObjectValueGeneratorRecursionTest {
    @Test
    void generate_whenRecursiveSelfParameter_andRecursiveConstructorHasNonNullableTypes_thenCanConstruct() {
        AggregatedSupportGenerator argumentGenerator = new AggregatedSupportGenerator(Arrays.asList(
                ObjectValueGenerator.withForcedAccess(),
                new PrimitiveValueGenerator()
        ));
        ParameterObjectValueGenerator generator = new ParameterObjectValueGenerator(argumentGenerator);

        RecursiveWithNonNullableParameters value = (RecursiveWithNonNullableParameters) generator
                .generate(RecursiveWithNonNullableParameters.class)
                .get();

        // shallow object
        assertThat(value).isNotNull();
        assertThat(value.nullableSelf).isNotNull();
        assertThat(value.nullableObject).isNotNull();
        assertThat(value.nonNullableInt).isEqualTo(1);
        assertThat(value.nonNullableBoolean).isTrue();

        // deep object
        assertThat(value.nullableSelf.nullableSelf).isNull();
        assertThat(value.nullableSelf.nullableObject).isNull();
        assertThat(value.nullableSelf.nonNullableInt).isEqualTo(0);
        assertThat(value.nullableSelf.nonNullableBoolean).isFalse();
    }

    @Test
    void generate_whenRecursiveParameter_andRecursiveConstructorHasNonNullableTypes_thenCanConstruct() {
        AggregatedSupportGenerator argumentGenerator = new AggregatedSupportGenerator(Collections.singletonList(
                new PrimitiveValueGenerator()
        ));
        ParameterObjectValueGenerator generator = new ParameterObjectValueGenerator(argumentGenerator);

        HasRecursiveParameter value = (HasRecursiveParameter) generator
                .generate(HasRecursiveParameter.class)
                .get();

        // shallow object
        assertThat(value).isNotNull();
        assertThat(value.recursive).isNotNull();
        assertThat(value.nonNullableInt).isEqualTo(1);
        assertThat(value.nonNullableBoolean).isTrue();

        // deep object
        assertThat(value.recursive.nullableSelf).isNull();
        assertThat(value.recursive.nullableObject).isNull();
        assertThat(value.recursive.nonNullableInt).isEqualTo(0);
        assertThat(value.recursive.nonNullableBoolean).isFalse();
    }

    @Test
    void generate_whenCyclicParameter_andCycleHasRecursiveConstructor_thenCanConstruct() {
        AggregatedSupportGenerator argumentGenerator = new AggregatedSupportGenerator(Collections.singletonList(
                ParameterObjectValueGenerator.withForcedAccess(ObjectValueGenerator.withForcedAccess())
        ));
        ParameterObjectValueGenerator generator = new ParameterObjectValueGenerator(argumentGenerator);

        CyclicRecursiveParameter value = (CyclicRecursiveParameter) generator
                .generate(CyclicRecursiveParameter.class)
                .get();

        System.out.println(value);

        // shallow object
        assertThat(value).isNotNull();
        assertThat(value.cycle).isNotNull();
        assertThat(value.self).isNotNull();

        // cyclic recursion
        assertThat(value.cycle.recursive).isNotNull();

        // deepest
        assertThat(value.cycle.recursive.cycle).isNull();
        assertThat(value.cycle.recursive.self).isNull();
    }

    private static class CyclicRecursiveParameter {
        private final Cyclic cycle;
        private final CyclicRecursiveParameter self;

        public CyclicRecursiveParameter(Cyclic cycle, CyclicRecursiveParameter self) {
            this.cycle = cycle;
            this.self = self;
        }
    }

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
