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
import io.github.mattiaspersson09.junisert.common.sort.Order;
import io.github.mattiaspersson09.junisert.common.sort.Sortable;

import java.util.Objects;

/**
 * A value generator that will always throw an exception on generating value.
 * A blocked type will raise {@link BlockedTypeException} if found.
 * Otherwise {@link UnsupportedTypeException} will be thrown if this generator is used for
 * other types than the blocked type. This generator should not be used for other reasons
 * then to block classes and <i>should be ordered first</i> among other generators to ensure blocking behavior.
 *
 * <p><br>
 * <b>Usage:</b> if a code base should avoid using a certain type for reasons,
 * this generator can be used to signal that the type is not supported and should not be used.
 * <br>
 * Example:
 * <pre>
 *     BlockedValueGenerator&lt;Unsafe> unsafeBlocker = new BlockedValueGenerator&lt;>(Unsafe.class);
 *     // register generator for test suite
 *     // ...
 *     // trying to generate Unsafe.class
 *     public Value&lt;?> getValueForType(Class&lt;?> type) {
 *       for (ValueGenerator&lt;?> generator : registeredGenerators) {
 *         if (generator.supports(type) {
 *           // will effectively throw {@link BlockedTypeException} and blocking usage
 *           return generator.generate(type);
 *         }
 *       }
 *     }
 * </pre>
 *
 * @param <T> type to block
 * @see BlockedTypeException
 */
public final class BlockedValueGenerator<T> implements ValueGenerator<T>, Sortable {
    private final Class<T> blocked;

    public BlockedValueGenerator(Class<T> blocked) {
        this.blocked = Objects.requireNonNull(blocked, "Can't be null if it should be blocked");
    }

    /**
     * Will always throw, {@link BlockedTypeException} for a blocked type and
     * {@link UnsupportedTypeException} if {@code fromType} is not the blocked type
     * this generator is looking for.
     *
     * @param fromType to throw for
     * @throws BlockedTypeException     if blocked type is encountered
     * @throws UnsupportedTypeException if the type isn't blocked
     */
    @Override
    public Value<? extends T> generate(Class<?> fromType) throws BlockedTypeException, UnsupportedTypeException {
        if (supports(fromType)) {
            throw new BlockedTypeException(fromType);
        }

        throw new UnsupportedTypeException(fromType);
    }

    @Override
    public boolean supports(Class<?> type) {
        return blocked.equals(type);
    }

    @Override
    public Order order() {
        return Order.FIRST;
    }
}
