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

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeException;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;

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

        assertThatThrownBy(() -> aggregated.generate(Integer.class)).isInstanceOf(UnsupportedTypeException.class);
    }

    @Test
    void generate_whenThereIsNoAggregatedGenerator_thenThrowsUnsupportedTypeException() {
        AggregatedSupportGenerator aggregated = new AggregatedSupportGenerator(Collections.emptyList());

        assertThatThrownBy(() -> aggregated.generate(Integer.class)).isInstanceOf(UnsupportedTypeException.class);
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

    private static class IntGenerator implements ValueGenerator<Number> {
        private final int value;
        private final int empty;

        public IntGenerator(int value, int empty) {
            this.value = value;
            this.empty = empty;
        }

        @Override
        public Value<? extends Number> generate(Class<?> fromType) throws UnsupportedTypeException {
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
        public Value<? extends Number> generate(Class<?> fromType) throws UnsupportedTypeException {
            return () -> value;
        }

        @Override
        public boolean supports(Class<?> type) {
            return Integer.class.equals(type);
        }
    }
}
