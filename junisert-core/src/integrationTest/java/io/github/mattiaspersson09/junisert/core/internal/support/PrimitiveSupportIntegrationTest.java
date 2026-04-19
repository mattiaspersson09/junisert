/*
 * Copyright (c) 2026 Mattias Persson
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
package io.github.mattiaspersson09.junisert.core.internal.support;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.core.Junisert;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PrimitiveSupportIntegrationTest {
    @Test
    void givenImplementation_whenSupportIsNotPrimitive_thenThrowsIllegalArgumentException() {
        assertThatThrownBy(() -> new PrimitiveSupport<>(Object.class, Object::new))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("support isn't primitive");
    }

    @Test
    void generate_whenNotSupported_thenThrowsUnsupportedTypeError() {
        PrimitiveSupport<Integer> intSupport = new PrimitiveSupport<>(int.class, () -> 1);

        assertThatThrownBy(() -> intSupport.generate(long.class))
                .isInstanceOf(UnsupportedTypeError.class);
    }

    @Test
    void generate_whenSupported_thenGeneratesPrimitiveValue() {
        PrimitiveSupport<Integer> intSupport = new PrimitiveSupport<>(int.class, () -> 1);

        assertThat(intSupport.generate(int.class)).satisfies(value -> {
            assertThat(value.get()).isEqualTo(1);
            assertThat(value.asEmpty()).isEqualTo(0);
        });
    }

    @Test
    void implementation() {
        Junisert.registerSupport(new IntClassValueGenerator());
        Junisert.registerSupport(Supplier.class, Value.class, Value.ofEager(Value.ofEager(1, 0), Value.ofEager(2, -1)));

        Junisert.assertThatPojo(PrimitiveSupport.class)
                .implementsEqualsAndHashCode()
                .implementsToString();
    }

    private static class IntClassValueGenerator implements ValueGenerator<Object> {
        @Override
        public Value<?> generate(Class<?> fromType) throws UnsupportedTypeError {
            return Value.ofEager(int.class, byte.class);
        }

        @Override
        @SuppressWarnings("all")
        public boolean supports(Class<?> type) {
            return int.class.getClass().equals(type);
        }
    }
}
