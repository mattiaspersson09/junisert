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

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeException;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class WrapperPrimitiveValueGenerator implements ValueGenerator<Object> {
    @Override
    public Value<?> generate(Class<?> fromType) throws UnsupportedTypeException {
        return WrapperPrimitive.from(fromType)
                .map(wrapper -> Value.of(wrapper.value))
                .orElseThrow(() -> new UnsupportedTypeException(fromType));
    }

    @Override
    public boolean supports(Class<?> type) {
        return WrapperPrimitive.from(type).isPresent();
    }

    private enum WrapperPrimitive {
        BOOLEAN(Boolean.class, () -> Boolean.TRUE),
        CHAR(Character.class, () -> '1'),
        BYTE(Byte.class, () -> (byte) 1),
        SHORT(Short.class, () -> (short) 1),
        INTEGER(Integer.class, () -> 1),
        LONG(Long.class, () -> 1L),
        FLOAT(Float.class, () -> 1.0F),
        DOUBLE(Double.class, () -> 1.0D),
        NUMBER(Number.class, () -> 1);

        private final Class<?> type;
        private final Supplier<Object> value;

        WrapperPrimitive(Class<?> type, Supplier<Object> value) {
            this.type = type;
            this.value = value;
        }

        private static Optional<WrapperPrimitive> from(Class<?> wrapperType) {
            return Stream.of(values())
                    .filter(wrapper -> wrapper.type.equals(wrapperType))
                    .findAny();
        }
    }
}
