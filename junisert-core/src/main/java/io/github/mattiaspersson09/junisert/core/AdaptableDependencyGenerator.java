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
package io.github.mattiaspersson09.junisert.core;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;

final class AdaptableDependencyGenerator implements ValueGenerator<Object> {
    private final ValueGenerator<?> dependencySupport;
    private volatile boolean produceEmptyValues;

    AdaptableDependencyGenerator(ValueGenerator<?> dependencySupport) {
        this.dependencySupport = dependencySupport;
    }

    public synchronized void produceEmptyValues(boolean produceEmptyValues) {
        this.produceEmptyValues = produceEmptyValues;
    }

    @Override
    public Value<?> generate(Class<?> fromType) throws UnsupportedTypeError {
        if (produceEmptyValues) {
            return new ValueAdapter(dependencySupport.generate(fromType));
        }

        return dependencySupport.generate(fromType);
    }

    @Override
    public boolean supports(Class<?> type) {
        return dependencySupport.supports(type);
    }

    private static class ValueAdapter implements Value<Object> {
        private final Value<?> value;

        public ValueAdapter(Value<?> value) {
            this.value = value;
        }

        @Override
        public Object get() {
            return asEmpty();
        }

        @Override
        public Object asEmpty() {
            return value.asEmpty();
        }
    }
}
