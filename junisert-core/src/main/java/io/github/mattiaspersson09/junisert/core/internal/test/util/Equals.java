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
 * Tests that equals-method of instances adheres to bullet list in {@link Object#equals(Object)}.
 *
 * @see Object#equals(Object)
 */
public final class Equals {
    private static final Logger LOGGER = Logger.getLogger("Equals");

    private final Object instance;

    Equals(Object instance) {
        this.instance = Objects.requireNonNull(instance);
    }

    /**
     * Creates a new equals of given {@code instance}.
     *
     * @param instance non null to test
     * @return a new equals of instance
     */
    public static Equals ofInstance(Object instance) {
        return new Equals(instance);
    }

    /**
     * <strong>Reflexivity</strong>: for any non-null reference value x, x.equals(x) should return true.
     *
     * @return this equals to continue checking
     * @throws UnitAssertionError if instance equals check is not reflexive
     */
    @SuppressWarnings("all")
    public Equals isReflexive() throws UnitAssertionError {
        LOGGER.test("Reflexive check -> instance.equals(instance)");

        if (!instance.equals(instance)) {
            LOGGER.fail(methodName() + " fails reflexive check", "to equal reference of itself", "it did not");
            throw new UnitAssertionError(methodName() + " was expected to be reflexive, but it was not");
        }

        return this;
    }

    /**
     * <strong>Symmetry</strong>: for any non-null reference values x and y, x.equals(y) should return true if and only
     * if
     * y.equals(x) returns true.
     *
     * @param otherInstance non null to compare
     * @return this equals to continue checking
     * @throws UnitAssertionError if instance equals check is not symmetric
     * @see #isNotSymmetricWith(Object)
     */
    public Equals isSymmetricWith(Object otherInstance) throws UnitAssertionError {
        Objects.requireNonNull(otherInstance);

        LOGGER.test("Symmetry check -> instance.equals(otherInstance)");

        if (!otherInstance.equals(instance) || !instance.equals(otherInstance)) {
            LOGGER.fail(methodName() + " fails symmetry check",
                    instance + " to be symmetric with " + otherInstance,
                    "it was not");
            throw new UnitAssertionError(methodName() + " was expected to be <true> when comparing "
                    + instance + " with " + otherInstance);
        }

        return this;
    }

    /**
     * Same as calling {@link #isSymmetricWith(Object)} but lazily constructs {@code otherInstance} before checking.
     *
     * @param otherInstance non null to lazily compare
     * @return this equals to continue checking
     * @throws UnitAssertionError if instance equals check is not symmetric
     * @see #isSymmetricWith(Object)
     */
    @SuppressWarnings("UnusedReturnValue")
    public Equals isSymmetricWith(Supplier<Object> otherInstance) throws UnitAssertionError {
        Objects.requireNonNull(otherInstance);
        return isSymmetricWith(otherInstance.get());
    }

    /**
     * This is a direct opposite check variant of {@link #isSymmetricWith(Object)}.
     *
     * @param otherInstance to compare, may be null
     * @return this equals to continue checking
     * @throws UnitAssertionError if instance equals check happens to be symmetric
     */
    public Equals isNotSymmetricWith(Object otherInstance) throws UnitAssertionError {
        LOGGER.test("Non symmetry check -> instance.equals(otherInstance)");

        if (instance.equals(otherInstance)) {
            LOGGER.fail(methodName() + " fails non symmetry check",
                    instance + " to NOT be symmetric with " + otherInstance,
                    "it was");
            throw new UnitAssertionError(methodName() + " was expected to be <false> when comparing "
                    + instance + " with " + otherInstance);
        }

        return this;
    }

    /**
     * Same as calling {@link #isNotSymmetricWith(Object)} but lazily constructs {@code otherInstance} before checking.
     *
     * @param otherInstance non null to lazily compare
     * @return this equals to continue checking
     * @throws UnitAssertionError if instance equals check happens to be symmetric
     */
    @SuppressWarnings("UnusedReturnValue")
    public Equals isNotSymmetricWith(Supplier<Object> otherInstance) throws UnitAssertionError {
        Objects.requireNonNull(otherInstance);
        return isNotSymmetricWith(otherInstance.get());
    }

    /**
     * <strong>Transitivity</strong>: for any non-null reference values x, y, and z, if x.equals(y) returns true
     * and y.equals(z) returns true, then x.equals(z) should return true.
     *
     * @param second non null instance y to compare
     * @param third  non null instance z to compare
     * @return this equals to continue checking
     * @throws UnitAssertionError if instance equals check is not transitive
     */
    public Equals isTransitiveWith(Object second, Object third) throws UnitAssertionError {
        Objects.requireNonNull(second);
        Objects.requireNonNull(third);

        LOGGER.test("Transitive check -> instance.equals(otherInstance)");

        if (!instance.equals(second) || !second.equals(third) || !instance.equals(third)) {
            LOGGER.fail(methodName() + " fails transitive check",
                    instance + " to be transitive with " + second + " and " + third,
                    "it was not");
            throw new UnitAssertionError(methodName() + " was expected to be <true> when comparing with "
                    + "other instances with the same values");
        }

        return this;
    }

    /**
     * Same as calling {@link #isTransitiveWith(Object, Object)} but lazily constructs {@code second}
     * and {@code third} before checking.
     *
     * @param second non null instance to lazily compare
     * @param third  non null instance to lazily compare
     * @return this equals to continue checking
     * @throws UnitAssertionError if instance equals check is not transitive
     */
    @SuppressWarnings("UnusedReturnValue")
    public Equals isTransitiveWith(Supplier<Object> second, Supplier<Object> third) throws UnitAssertionError {
        Objects.requireNonNull(second);
        Objects.requireNonNull(third);
        return isTransitiveWith(second.get(), third.get());
    }

    /**
     * <strong>Consistency</strong>: for any non-null reference values x and y, multiple invocations of x.equals(y)
     * consistently return {@code true}. Provided no information used in equals comparisons on the objects is modified.
     * <br>
     * <br>
     * If given {@code consistentTimes} is {@code 0} then this check is skipped and ignored.
     *
     * @param otherInstance   non null to compare
     * @param consistentTimes to check
     * @return this equals to continue checking
     * @throws IllegalArgumentException if {@code consistentTimes} is a negative value
     * @throws UnitAssertionError       if instance equals check is not consistently true
     */
    public Equals isConsistentWith(Object otherInstance, int consistentTimes) throws IllegalArgumentException,
                                                                              UnitAssertionError {
        Objects.requireNonNull(otherInstance);

        if (consistentTimes < 0) {
            throw new IllegalArgumentException("Consistent times is not allowed to be a negative value");
        } else if (consistentTimes == 0) {
            LOGGER.test("Consistency check -> SKIPPED (given zero times to check)");
            return this;
        }

        LOGGER.test("Consistency check (x{0} times) -> instance.equals(otherInstance)", consistentTimes);

        for (int timesChecked = 1; timesChecked <= consistentTimes; timesChecked++) {
            if (!instance.equals(otherInstance)) {
                LOGGER.fail(methodName() + " fails consistency check",
                        instance + " to be consistent with " + otherInstance,
                        "it was not on check " + timesChecked + "/" + consistentTimes);
                throw new UnitAssertionError(methodName() + " was expected to still be <true> when comparing "
                        + instance + " with " + otherInstance + " after " + consistentTimes + " checks");
            }
        }

        return this;
    }

    /**
     * Same as calling {@link #isConsistentWith(Object, int)} but lazily constructs
     * {@code otherInstance} before checking.
     *
     * @param otherInstance   non null to lazily compare
     * @param consistentTimes to check
     * @return this equals to continue checking
     * @throws UnitAssertionError if instance equals check is not consistently true
     */
    @SuppressWarnings("UnusedReturnValue")
    public Equals isConsistentWith(Supplier<Object> otherInstance, int consistentTimes) throws UnitAssertionError {
        Objects.requireNonNull(otherInstance);
        return isConsistentWith(otherInstance.get(), consistentTimes);
    }

    private String methodName() {
        return instance.getClass().getSimpleName() + ".equals(Object)";
    }
}
