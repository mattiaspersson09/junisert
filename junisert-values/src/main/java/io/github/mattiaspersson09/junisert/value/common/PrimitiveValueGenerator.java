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
import java.util.stream.Stream;

public class PrimitiveValueGenerator implements ValueGenerator<Object> {
    @Override
    public Value<?> generate(Class<?> fromType) throws UnsupportedTypeException {
        return Primitive.from(fromType)
                .map(primitive -> Value.ofEager(primitive.value, primitive.emptyValue))
                .orElseThrow(() -> new UnsupportedTypeException(fromType));
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.isPrimitive();
    }

    private enum Primitive {
        BOOLEAN(boolean.class, true, false),
        CHAR(char.class, '1', '0'),
        BYTE(byte.class, (byte) 1, (byte) 0),
        SHORT(short.class, (short) 1, (short) 0),
        INTEGER(int.class, 1, 0),
        LONG(long.class, 1L, 0L),
        FLOAT(float.class, 1.0F, 0.0F),
        DOUBLE(double.class, 1.0D, 0.0D);

        private final Class<?> type;
        private final Object value;
        private final Object emptyValue;

        Primitive(Class<?> type, Object value, Object emptyValue) {
            this.type = type;
            this.value = value;
            this.emptyValue = emptyValue;
        }

        private static Optional<Primitive> from(Class<?> primitiveType) {
            return Stream.of(values())
                    .filter(primitive -> primitive.type.equals(primitiveType))
                    .findAny();
        }
    }
}
