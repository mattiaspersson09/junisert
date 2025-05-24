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
package io.github.mattiaspersson09.junisert.core.internal.test;

import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Unit;

/**
 * A unit test performs a specific test around a given {@link Unit} and seen internally as a lower abstraction and
 * extension of assertions, separated to not clutter the assertion classes if a test is extensive.<br>
 * <br>
 * Tests should provide fine-tuned logging over what is being tested and it's outcome.
 */
public interface UnitTest {
    /**
     * Performs a new test around given {@code unit}, may throw {@link UnitAssertionError} to propagate that
     * an assertion fails from this test.
     *
     * @param unit to test
     */
    void test(Unit unit);
}
