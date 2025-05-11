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

public interface UnitAssertion {
    /**
     * Assumes this unit is a plain object for assertion, which doesn't necessarily follow a convention.
     *
     * @return a plain object assertion
     */
    PlainObjectAssertion asPojo();

    /**
     * Is a convenience assertion to assert that a unit complies with the <em>Java Bean Specification</em>. This
     * should make sure that a unit follows convention, which <strong>requires</strong> that:
     * <ul>
     * <li>Unit <em>must</em> have a default constructor (accepting no arguments).</li>
     * <li>All instance properties <em>must</em> be private.</li>
     * <li>All instance properties <em>must</em> have a working getter.</li>
     * <li>All instance properties <em>must</em> have a working setter.</li>
     * </ul>
     *
     * Other than enforcing convention, this assertion might still check for but not enforce:
     * <ul>
     * <li>Unit <em>should</em> implement {@link java.io.Serializable}, directly or indirectly</li>
     * <li>Unit <em>should</em> override {@code equals and hashCode}</li>
     * <li>Unit <em>should</em> override {@code toString}</li>
     * </ul>
     * Above checks might log a warning but not be enforced. If the user wants to enforce non required checks they
     * should
     * treat the unit as a <em>POJO</em> (Plain Old Java Object) and use {@link #asPojo()}.
     *
     * @return this assertion, chained to be able to assert more
     * @throws UnitAssertionError if some requirement is not met
     * @see #asPojo()
     */
    UnitAssertion isJavaBeanCompliant() throws UnitAssertionError;
}
