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
package io.github.mattiaspersson09.junisert.api.value;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @see Value#of(Supplier)
 * @see Value#of(Supplier, Supplier)
 */
class LazyValue<T> implements Value<T> {
    private final Supplier<T> value;
    private final Supplier<T> empty;

    LazyValue(Supplier<T> value) {
        this(value, () -> null);
    }

    LazyValue(Supplier<T> value, Supplier<T> empty) {
        this.value = Objects.requireNonNull(value, "Can't construct a lazy value object without a value supplier");
        this.empty = Objects.requireNonNull(empty, "Can't construct a lazy value object without a value supplier");
        ;
    }

    @Override
    public T get() {
        return value.get();
    }

    @Override
    public T asEmpty() {
        return empty.get();
    }
}
