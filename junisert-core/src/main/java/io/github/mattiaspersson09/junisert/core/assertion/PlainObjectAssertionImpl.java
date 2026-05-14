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
package io.github.mattiaspersson09.junisert.core.assertion;

import io.github.mattiaspersson09.junisert.api.assertion.PlainObjectAssertion;
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.core.internal.test.HasGetters;
import io.github.mattiaspersson09.junisert.core.internal.test.HasSetters;
import io.github.mattiaspersson09.junisert.core.internal.test.ImplementsEquals;
import io.github.mattiaspersson09.junisert.core.internal.test.ImplementsHashCode;
import io.github.mattiaspersson09.junisert.core.internal.test.ImplementsToString;

/**
 * Direct implementation of {@link PlainObjectAssertion} API.
 */
public class PlainObjectAssertionImpl extends AbstractAssertion<PlainObjectAssertion> implements PlainObjectAssertion {
    private static final Logger LOGGER = Logger.getLogger(PlainObjectAssertion.class);

    /**
     * Creates a new implementation of {@link PlainObjectAssertion}.
     *
     * @param assertionResource needed for assertions
     */
    public PlainObjectAssertionImpl(AssertionResource assertionResource) {
        super(assertionResource);
    }

    @Override
    public PlainObjectAssertion hasGetters() throws UnitAssertionError {
        runTest(HasGetters.class);

        return this;
    }

    @Override
    public PlainObjectAssertion hasSetters() throws UnitAssertionError {
        if (getUnit().isImmutable()) {
            LOGGER.info("Assertion hasSetters skipped: unit is immutable and can't have setters");
            return this;
        }

        runTest(HasSetters.class);

        return this;
    }

    @Override
    public PlainObjectAssertion implementsEqualsAndHashCode() throws UnitAssertionError {
        runTest(ImplementsEquals.class);
        runTest(ImplementsHashCode.class);

        return this;
    }

    @Override
    public PlainObjectAssertion implementsToString() throws UnitAssertionError {
        runTest(ImplementsToString.class);

        return this;
    }
}
