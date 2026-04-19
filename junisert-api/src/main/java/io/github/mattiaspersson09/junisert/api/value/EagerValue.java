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

/**
 * @see Value#ofEager(Object)
 */
class EagerValue<T> implements Value<T> {
    private final T value;
    private final T empty;

    EagerValue(T value) throws IllegalArgumentException {
        this(value, null);
    }

    EagerValue(T value, T empty) throws IllegalArgumentException {
        Objects.requireNonNull(value, "Value isn't allowed to be missing");

        if (Objects.equals(empty, value)) {
            throw new IllegalArgumentException("Value and it's empty representation isn't allowed to be the same");
        }

        this.value = value;
        this.empty = empty;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public T asEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        EagerValue<?> that = (EagerValue<?>) object;
        return Objects.equals(value, that.value) && Objects.equals(empty, that.empty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, empty);
    }

    @Override
    public String toString() {
        return "EagerValue{" +
                "value=" + value +
                ", empty=" + empty +
                '}';
    }
}
