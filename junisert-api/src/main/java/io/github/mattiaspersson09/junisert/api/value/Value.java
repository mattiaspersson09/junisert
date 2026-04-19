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

import java.util.function.Supplier;

/**
 * Representation of an object's value and an empty representation.
 *
 * @param <T> concrete value to be supplied
 * @see #of(Supplier, Supplier) lazy value construction
 */
@FunctionalInterface
public interface Value<T> extends Supplier<T> {
    /**
     * Gets a resulting concrete value. This will construct an underlying lazy value or get an already constructed
     * eager value. If the underlying value were lazy, this operation might throw {@link UnsupportedConstructionError}.
     *
     * @return constructed value
     * @see #asEmpty()
     */
    @Override
    T get();

    /**
     * Gets an empty value representation of {@link #get()}. This is usually represented as {@code null} for objects
     * which aren't <em>primitive</em> if not implemented.
     *
     * @return empty representation
     */
    default T asEmpty() {
        return null;
    }

    /**
     * Creates value from an already constructed value with {@code null} as it's empty representation.<br>
     * <br>
     * Is the same as calling {@link #ofEager(Object, Object) Value.ofEager(Object, Object)} with
     * {@code null} as empty representation.
     *
     * @param notEmpty non null value
     * @param <T>      type of {@code notEmpty}
     * @return already constructed value and null as empty value
     * @see #of(Supplier)
     */
    static <T> Value<T> ofEager(T notEmpty) {
        return new EagerValue<>(notEmpty);
    }

    /**
     * Creates value from an eagerly already constructed value with another value as it's empty counterpart.
     * Value isn't allowed to be empty/missing and {@code emptyRepresentation}
     * isn't allowed to be the same as {@code notEmpty}, to secure safe comparisons.<br>
     * <br>
     * <p>Example creating a {@code String} value:
     * <pre>
     * Value&lt;String&gt; value = Value.ofEager("Junisert", "empty");
     * </pre>
     *
     * @param notEmpty            object value
     * @param emptyRepresentation of but not the same as notEmpty
     * @param <T>                 type of {@code notEmpty}
     * @return already constructed value
     * @throws IllegalArgumentException if notEmpty and emptyRepresentation is the same value
     * @see #of(Supplier, Supplier)
     */
    static <T> Value<T> ofEager(T notEmpty, T emptyRepresentation) throws IllegalArgumentException {
        return new EagerValue<>(notEmpty, emptyRepresentation);
    }

    /**
     * Creates a lazy value representation, where {@code notEmpty} has not yet been constructed.
     * Used for storing expensive or timely object creations to be constructed only when needed.
     * A lazy value is not safe to compare to other values with <b>{@code equals}</b> or
     * <b>{@code hashCode}</b> because of anonymity of the value type.<br>
     * <br>
     * Is the same as calling {@link #of(Supplier, Supplier) Value.of(Supplier, Supplier)} with supplier
     * having null result as empty representation.
     *
     * @param notEmpty non-null value supplier
     * @param <T>      type of value inside supplier
     * @return a lazy, not yet constructed value
     * @see #of(Supplier, Supplier)
     */
    static <T> Value<T> of(Supplier<T> notEmpty) {
        return new LazyValue<>(notEmpty);
    }

    /**
     * Creates a lazy value representation, where {@code notEmpty} and {@code emptyRepresentation}
     * has not yet been constructed.
     * {@code emptyRepresentation} should not be the same as {@code notEmpty}, to secure safe comparisons.<br>
     * <br>
     * <p>Example creating a {@code String} value:
     * <pre>
     * Value&lt;String&gt; value = Value.of(() -> "Junisert", String::new);
     * </pre>
     * Used for storing expensive or timely object creations to be constructed only when needed.
     * A lazy value is not safe to compare to other values with <b>{@code equals}</b> or
     * <b>{@code hashCode}</b> because of anonymity of the value type.
     *
     * @param notEmpty            non-null value supplier
     * @param emptyRepresentation non-null value supplier for empty representation of notEmpty
     * @param <T>                 type of value inside supplier
     * @return a lazy, not yet constructed value
     */
    static <T> Value<T> of(Supplier<T> notEmpty, Supplier<T> emptyRepresentation) {
        return new LazyValue<>(notEmpty, emptyRepresentation);
    }
}
