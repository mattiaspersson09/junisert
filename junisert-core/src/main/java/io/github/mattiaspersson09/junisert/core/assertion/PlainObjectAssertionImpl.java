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
package io.github.mattiaspersson09.junisert.core.assertion;

import io.github.mattiaspersson09.junisert.api.assertion.PlainObjectAssertion;
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.api.internal.service.ValueService;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.SharedResource;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Unit;
import io.github.mattiaspersson09.junisert.core.internal.test.HasGetters;
import io.github.mattiaspersson09.junisert.core.internal.test.HasSetters;
import io.github.mattiaspersson09.junisert.core.internal.test.ImplementsEquals;
import io.github.mattiaspersson09.junisert.core.internal.test.ImplementsHashCode;
import io.github.mattiaspersson09.junisert.core.internal.test.ImplementsToString;


public class PlainObjectAssertionImpl implements PlainObjectAssertion {
    private final ValueService valueService;
    private final InstanceCreator instanceCreator;
    private final Unit unitUnderAssertion;

    public PlainObjectAssertionImpl(SharedResource sharedResource) {
        this.valueService = sharedResource.getValueService();
        this.instanceCreator = sharedResource.getInstanceCreator();
        this.unitUnderAssertion = sharedResource.getUnitUnderAssertion();
    }

    @Override
    public PlainObjectAssertion hasGetters() throws UnitAssertionError {
        new HasGetters(valueService, instanceCreator).test(unitUnderAssertion);

        return this;
    }

    @Override
    public PlainObjectAssertion hasSetters() throws UnitAssertionError {
        new HasSetters(valueService, instanceCreator).test(unitUnderAssertion);

        return this;
    }

    @Override
    public PlainObjectAssertion implementsEqualsAndHashCode() throws UnitAssertionError {
        new ImplementsEquals(valueService, instanceCreator).test(unitUnderAssertion);
        new ImplementsHashCode(valueService, instanceCreator).test(unitUnderAssertion);

        return this;
    }

    @Override
    public PlainObjectAssertion implementsToString() throws UnitAssertionError {
        new ImplementsToString(valueService, instanceCreator).test(unitUnderAssertion);

        return this;
    }
}
