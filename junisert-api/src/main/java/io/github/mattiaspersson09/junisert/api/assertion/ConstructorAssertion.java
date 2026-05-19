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
package io.github.mattiaspersson09.junisert.api.assertion;

import java.util.function.Predicate;

/**
 * Assertion for unit constructors which is branched from- and owned by {@link UnitAssertion}.
 * Asserts state in a unit after being instantiated from a constructor.
 *
 * @param <T> unit type owning constructor under assertion
 */
public interface ConstructorAssertion<T> extends Assertion<ConstructorAssertion<T>> {
    /**
     * Asserts that the constructor assigns a value to instance property with given name.
     * Property is not considered assigned if the value is a default initial value, as defined in
     * Java Language Specification > 4.12.5. Initial Values of Variables:
     * <a href="https://docs.oracle.com/javase/specs/jls/se25/html/jls-4.html#jls-4.12.5">JLS section 4.12.5</a>
     *
     * @param propertyName of property to check
     * @return this assertion, chained to be able to assert more
     * @throws UnitAssertionError if property were not found or assigned a value
     */
    ConstructorAssertion<T> hasAssignedProperty(String propertyName) throws UnitAssertionError;

    /**
     * Asserts that the constructor assigns a value to instance properties with given names.
     * A property is not considered assigned if the value is a default initial value, as defined in
     * Java Language Specification > 4.12.5. Initial Values of Variables:
     * <a href="https://docs.oracle.com/javase/specs/jls/se25/html/jls-4.html#jls-4.12.5">JLS section 4.12.5</a>
     *
     * @param propertyNames of properties to check
     * @return this assertion, chained to be able to assert more
     * @throws UnitAssertionError if a property were not found or assigned a value
     * @see #hasAssignedAllProperties()
     */
    ConstructorAssertion<T> hasAssignedProperties(String... propertyNames);

    /**
     * Asserts that the constructor assigns a value to all instance properties.
     * Property is not considered assigned if the value is a default initial value, as defined in
     * Java Language Specification > 4.12.5. Initial Values of Variables:
     * <a href="https://docs.oracle.com/javase/specs/jls/se25/html/jls-4.html#jls-4.12.5">JLS section 4.12.5</a>
     *
     * @return this assertion, chained to be able to assert more
     * @throws UnitAssertionError if a property were not assigned a value
     */
    ConstructorAssertion<T> hasAssignedAllProperties() throws UnitAssertionError;

    /**
     * Asserts that unit owning the constructor has a certain state after being instantiated.
     *
     * @param stateValidation to test constructed unit instance with
     * @return this assertion, chained to be able to assert more
     * @throws UnitAssertionError if constructed unit has other state than expected
     * @see #hasState(Predicate, String)
     */
    ConstructorAssertion<T> hasState(Predicate<T> stateValidation) throws UnitAssertionError;

    /**
     * Asserts that unit owning the constructor has a certain state, described by {@code description},
     * after being instantiated.
     *
     * @param stateValidation to test constructed unit instance with
     * @param description     describing the validation
     * @return this assertion, chained to be able to assert more
     * @throws UnitAssertionError if constructed unit has other state than expected
     */
    ConstructorAssertion<T> hasState(Predicate<T> stateValidation, String description) throws UnitAssertionError;

    /**
     * Jumps back to {@link UnitAssertion} to continue asserting on the unit owning the constructor.
     * This method doesn't perform any tests and just switches assertion.
     *
     * @return a new {@link UnitAssertion}
     */
    UnitAssertion<T> assertThatUnit();
}
