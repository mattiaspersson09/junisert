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
package io.github.mattiaspersson09.junisert.api.internal.support;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AggregatedSupportGeneratorTest {
    @Test
    void generate_whenThereAreAggregatedGenerators_thenGetsValueFromSupportingGenerator() {
        ValueGenerator<?> primitive = new IntGenerator(1, 0);
        ValueGenerator<?> wrapper = new IntegerGenerator(1_000);
        AggregatedSupportGenerator aggregated = new AggregatedSupportGenerator(Arrays.asList(primitive, wrapper));

        assertThat(aggregated.generate(int.class).get()).isEqualTo(1);
        assertThat(aggregated.generate(int.class).asEmpty()).isEqualTo(0);
        assertThat(aggregated.generate(Integer.class).get()).isEqualTo(1_000);
        assertThat(aggregated.generate(Integer.class).asEmpty()).isNull();
    }

    @Test
    void generate_whenThereIsNoSupportedGenerator_thenThrowsUnsupportedTypeException() {
        ValueGenerator<?> primitive = new IntGenerator(1, 0);
        AggregatedSupportGenerator aggregated = new AggregatedSupportGenerator(Collections.singletonList(primitive));

        assertThatThrownBy(() -> aggregated.generate(Integer.class)).isInstanceOf(UnsupportedTypeError.class);
    }

    @Test
    void generate_whenThereIsNoAggregatedGenerator_thenThrowsUnsupportedTypeException() {
        AggregatedSupportGenerator aggregated = new AggregatedSupportGenerator(Collections.emptyList());

        assertThatThrownBy(() -> aggregated.generate(Integer.class)).isInstanceOf(UnsupportedTypeError.class);
    }

    @Test
    void supports_whenHaveAggregatedGenerator_andSupportingType_thenIsTrue() {
        ValueGenerator<?> wrapper = new IntegerGenerator(1_000);
        AggregatedSupportGenerator aggregated = new AggregatedSupportGenerator(Collections.singletonList(wrapper));

        assertThat(aggregated.supports(Integer.class)).isTrue();
    }

    @Test
    void supports_whenNoAggregatedGenerator_andNoSupport_thenIsFalse() {
        AggregatedSupportGenerator aggregated = new AggregatedSupportGenerator(Collections.emptyList());

        assertThat(aggregated.supports(Integer.class)).isFalse();
    }

    @Test
    void aggregated_returnsUnmodifiableView() {
        ValueGenerator<?> wrapper = new IntegerGenerator(1_000);
        AggregatedSupportGenerator aggregated = new AggregatedSupportGenerator(Collections.singletonList(wrapper));

        assertThat(aggregated.aggregated()).hasSize(1);
        assertThatThrownBy(() -> aggregated.aggregated().add(new IntegerGenerator(1_000)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void merge_whenNonAggregatedValueGenerator_thenMergedWithValueGenerator() {
        ValueGenerator<?> valueGenerator = new IntegerGenerator(1);

        AggregatedSupportGenerator mergingLast = new AggregatedSupportGenerator(Collections.singletonList(
                new IntGenerator(1, 0)));
        AggregatedSupportGenerator mergingFirst = new AggregatedSupportGenerator(Collections.singletonList(
                new IntGenerator(1, 0)));

        assertThat(mergingLast.merge(valueGenerator).aggregated())
                .hasSize(2)
                .anyMatch(generator -> generator.getClass().equals(IntegerGenerator.class))
                .anyMatch(generator -> generator.getClass().equals(IntGenerator.class));
        assertThat(mergingFirst.mergeFirst(valueGenerator).aggregated())
                .hasSize(2)
                .anyMatch(generator -> generator.getClass().equals(IntegerGenerator.class))
                .anyMatch(generator -> generator.getClass().equals(IntGenerator.class));
    }

    @Test
    void merge_givenAggregatedValueGenerator_whenSeveralAggregated_thenMergedWithAllValueGenerators() {
        AggregatedValueGenerator aggregatedValueGenerator = new AggregatedSupportGenerator(Arrays.asList(
                new IntegerGenerator(1_000),
                new IntGenerator(1, 0)
        ));

        AggregatedSupportGenerator mergingLast = new AggregatedSupportGenerator(Collections.emptyList());
        AggregatedSupportGenerator mergingFirst = new AggregatedSupportGenerator(Collections.emptyList());

        assertThat(mergingLast.merge(aggregatedValueGenerator).aggregated())
                .hasSize(2)
                .anyMatch(generator -> generator.getClass().equals(IntegerGenerator.class))
                .anyMatch(generator -> generator.getClass().equals(IntGenerator.class));
        assertThat(mergingFirst.mergeFirst(aggregatedValueGenerator).aggregated())
                .hasSize(2)
                .anyMatch(generator -> generator.getClass().equals(IntegerGenerator.class))
                .anyMatch(generator -> generator.getClass().equals(IntGenerator.class));
    }

    @Test
    void merge_whenHasAggregatedGeneratorsFromBefore_thenMerges_andAddsMergedLast() {
        AggregatedValueGenerator aggregatedValueGenerator = new AggregatedSupportGenerator(Arrays.asList(
                new IntegerGenerator(1_000),
                new IntGenerator(1, 0)
        ));

        AggregatedSupportGenerator aggregated = new AggregatedSupportGenerator(
                Collections.singletonList(new SupportGenerator<>(Super.class)));

        AggregatedValueGenerator merged = aggregated.merge(aggregatedValueGenerator);

        assertThat(merged.aggregated()).first().isInstanceOf(SupportGenerator.class);
        assertThat(merged.aggregated())
                .hasSize(3)
                .anyMatch(generator -> generator.getClass().equals(SupportGenerator.class))
                .anyMatch(generator -> generator.getClass().equals(IntegerGenerator.class))
                .anyMatch(generator -> generator.getClass().equals(IntGenerator.class));
    }

    @Test
    void mergeFirst_whenHasAggregatedGeneratorsFromBefore_thenMerges_andAddsMergedFirst() {
        AggregatedValueGenerator aggregatedValueGenerator = new AggregatedSupportGenerator(Arrays.asList(
                new IntegerGenerator(1_000),
                new IntGenerator(1, 0)
        ));

        AggregatedSupportGenerator aggregated = new AggregatedSupportGenerator(
                Collections.singletonList(new SupportGenerator<>(Super.class)));

        AggregatedValueGenerator merged = aggregated.mergeFirst(aggregatedValueGenerator);

        assertThat(merged.aggregated()).last().isInstanceOf(SupportGenerator.class);
        assertThat(merged.aggregated())
                .hasSize(3)
                .anyMatch(generator -> generator.getClass().equals(SupportGenerator.class))
                .anyMatch(generator -> generator.getClass().equals(IntegerGenerator.class))
                .anyMatch(generator -> generator.getClass().equals(IntGenerator.class));
    }

    private static class IntGenerator implements ValueGenerator<Number> {
        private final int value;
        private final int empty;

        public IntGenerator(int value, int empty) {
            this.value = value;
            this.empty = empty;
        }

        @Override
        public Value<? extends Number> generate(Class<?> fromType) throws UnsupportedTypeError {
            return Value.ofEager(value, empty);
        }

        @Override
        public boolean supports(Class<?> type) {
            return int.class.equals(type);
        }
    }

    private static class IntegerGenerator implements ValueGenerator<Number> {
        private final Integer value;

        public IntegerGenerator(Integer value) {
            this.value = value;
        }

        @Override
        public Value<? extends Number> generate(Class<?> fromType) throws UnsupportedTypeError {
            return () -> value;
        }

        @Override
        public boolean supports(Class<?> type) {
            return Integer.class.equals(type);
        }
    }
}
