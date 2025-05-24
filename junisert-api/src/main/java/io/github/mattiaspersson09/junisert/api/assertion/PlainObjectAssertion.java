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
package io.github.mattiaspersson09.junisert.api.assertion;

/**
 * Assertion for POJO's (Plain Old Java Object's) which doesn't necessarily follow a specific naming convention,
 * but assumed and treated as a simple object carrying properties.
 *
 * @see #isWellImplemented()
 */
public interface PlainObjectAssertion {
    /**
     * Asserts that unit has a working getter for all <em>instance</em> fields. This assertion is flexible
     * and accepts both <em>Java Bean</em> compliant and <em>builder/record</em> styles and does not enforce
     * public visibility.
     * <p>
     * Typical Java Bean compliant styles:
     * <ul>
     * <li>{@code public Type get<capitalized property name>()}</li>
     * <li>{@code public boolean is<capitalized property name>}</li>
     * </ul>
     * Builder/record style:
     * <ul>
     * <li> {@code public Type <property name>()} </li>
     * </ul>
     *
     * @return this assertion chained to assert more
     * @throws UnitAssertionError if unit fails assertion
     */
    PlainObjectAssertion hasGetters() throws UnitAssertionError;

    /**
     * Asserts that unit has a working setter for all <em>instance</em> fields. This assertion is flexible
     * and accepts both <em>Java Bean</em> compliant and <em>builder/record</em> styles and does not enforce
     * public visibility.
     * <p>
     * Java Bean compliant style:
     * <ul>
     * <li>{@code public void set<capitalized property name>(Type arg)}</li>
     * <li>{@code public Unit set<capitalized property name>(Type arg)}</li>
     * </ul>
     * Builder/record style:
     * <ul>
     * <li>{@code public void <property name>(Type arg)}</li>
     * <li>{@code public Unit <property name>(Type arg)}</li>
     * </ul>
     *
     * @return this assertion chained to assert more
     * @throws UnitAssertionError if unit fails assertion
     */
    PlainObjectAssertion hasSetters() throws UnitAssertionError;

    /**
     * Asserts that unit implements both {@code equals} and {@code hashCode}. These are asserted together since
     * it's <em>generally necessary</em> to maintain object contract, that units overriding one
     * should also override the other.
     * If {@code equals} pass assertion, so should {@code hashCode} also since they harmonize.
     *
     * @return this assertion chained to assert more
     * @throws UnitAssertionError if unit fails assertion
     * @see Object#equals(Object)
     * @see Object#hashCode()
     */
    PlainObjectAssertion implementsEqualsAndHashCode() throws UnitAssertionError;

    /**
     * Asserts that unit implements {@code toString} and it returns a suitable textual representation of the object.
     * This assertion will enforce that {@code toString} contains the name of the unit and all <em>instance fields</em>
     * with their current value is shown.<br>
     * For field check this asserts that {@code "<property name>=<property value>"} is present for every instance field.
     *
     * @return this assertion chained to assert more
     * @throws UnitAssertionError if unit fails assertion
     * @see Object#toString()
     */
    PlainObjectAssertion implementsToString() throws UnitAssertionError;

    /**
     * This is a convenience asserting operation that performs all assertions in recommended sequential order.
     * This operation is <strong>not suitable for immutable</strong> POJO's.<br>
     * <p>
     * Assertion order:
     * <ol>
     * <li>{@link #hasGetters()}</li>
     * <li>{@link #hasSetters()}</li>
     * <li>{@link #implementsEqualsAndHashCode()}</li>
     * <li>{@link #implementsToString()}</li>
     * </ol>
     */
    default void isWellImplemented() {
        this.hasGetters()
                .hasSetters()
                .implementsEqualsAndHashCode()
                .implementsToString();
    }
}
