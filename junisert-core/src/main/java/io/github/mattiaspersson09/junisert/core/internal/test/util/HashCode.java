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
package io.github.mattiaspersson09.junisert.core.internal.test.util;

import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.common.logging.Logger;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Tests that hashCode-method of instances are returning unique values but consistently the same when having equal
 * property values.
 *
 * @see Object#hashCode()
 */
public final class HashCode {
    private static final Logger LOGGER = Logger.getLogger("HashCode");

    private final Object instance;

    HashCode(Object instance) {
        this.instance = Objects.requireNonNull(instance);
    }

    /**
     * Creates a new hash code of given {@code instance}.
     *
     * @param instance non null to test
     * @return a new hash code of instance
     */
    public static HashCode ofInstance(Object instance) {
        return new HashCode(instance);
    }

    /**
     * Invoking {@code hashCode} consistently returns the same value.
     *
     * @return this hash code to continue checking
     * @throws UnitAssertionError if instance hashCode does not consistently return the same value
     */
    public HashCode isConsistent() throws UnitAssertionError {
        LOGGER.test("Consistency check -> instance.hashCode() == instance.hashCode()");

        if (Objects.hashCode(instance.hashCode()) != Objects.hashCode(instance)) {
            throw new UnitAssertionError(methodName() + " was expected to consistently return the same value");
        }

        return this;
    }

    /**
     * {@code hashCode} does not return an empty value.
     *
     * @return this hash code to continue checking
     * @throws UnitAssertionError if instance hashCode returns an empty value
     */
    public HashCode isNotEmpty() throws UnitAssertionError {
        LOGGER.test("Not empty check -> instance.hashCode() != 0");

        if (Objects.hashCode(instance) == Objects.hashCode(null)) {
            throw new UnitAssertionError(methodName() + " was expected to not return empty value");
        }

        return this;
    }

    /**
     * Check if current instance {@code hashCode} is equal to another instance's {@code hashCode}.
     *
     * @param otherInstance non null to compare
     * @return this hash code to continue checking
     * @throws UnitAssertionError if current hash code is not equal to the other instance's hash code
     */
    public HashCode isEqualTo(Object otherInstance) throws UnitAssertionError {
        Objects.requireNonNull(otherInstance);

        LOGGER.test("Unique check -> instance.hashCode() == otherInstance.hashCode()");

        if (Objects.hashCode(instance) != Objects.hashCode(otherInstance)) {
            throw new UnitAssertionError(instance + " was expected to have the same hash code as " + otherInstance);
        }

        return this;
    }

    /**
     * Same as calling {@link #isEqualTo(Object)} but lazily constructs {@code otherInstance} before checking.
     *
     * @param otherInstance non null to lazily compare
     * @return this hash code to continue checking
     * @throws UnitAssertionError if current hash code is not equal to the other instance's hash code
     */
    public HashCode isEqualTo(Supplier<Object> otherInstance) throws UnitAssertionError {
        Objects.requireNonNull(otherInstance);
        return isEqualTo(otherInstance.get());
    }

    /**
     * Check if current instance {@code hashCode} is not equal to another instance's {@code hashCode}.
     *
     * @param otherInstance non null to compare
     * @return this hash code to continue checking
     * @throws UnitAssertionError if current hash code is equal to the other instance's hash code
     */
    public HashCode isNotEqualTo(Object otherInstance) throws UnitAssertionError {
        Objects.requireNonNull(otherInstance);

        LOGGER.test("Unique check -> instance.hashCode() != otherInstance.hashCode()");

        if (Objects.hashCode(instance) == Objects.hashCode(otherInstance)) {
            throw new UnitAssertionError(instance + " was expected to not have the same hash code as " + otherInstance);
        }

        return this;
    }

    /**
     * Same as calling {@link #isNotEqualTo(Object)} but lazily constructs {@code otherInstance} before checking.
     *
     * @param otherInstance non null to lazily compare
     * @return this hash code to continue checking
     * @throws UnitAssertionError if current hash code is equal to the other instance's hash code
     */
    public HashCode isNotEqualTo(Supplier<Object> otherInstance) throws UnitAssertionError {
        Objects.requireNonNull(otherInstance);
        return isNotEqualTo(otherInstance.get());
    }

    private String methodName() {
        return instance.getClass().getSimpleName() + ".hashCode()";
    }
}
