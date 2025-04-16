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

import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.api.assertion.ValueObjectAssertion;
import io.github.mattiaspersson09.junisert.api.internal.service.ValueService;
import io.github.mattiaspersson09.junisert.core.reflection.Unit;

public class ValueObjectAssertionImpl implements ValueObjectAssertion {
    private final Unit unit;
    private final ValueService valueService;

    public ValueObjectAssertionImpl(Unit unit, ValueService valueService) {
        this.unit = unit;
        this.valueService = valueService;
    }

    @Override
    public ValueObjectAssertion hasSetters() throws UnitAssertionError {
        return null;
    }

    @Override
    public ValueObjectAssertion hasGetters() throws UnitAssertionError {
        return null;
    }

    @Override
    public ValueObjectAssertion implementsEquals() throws UnitAssertionError {
        return null;
    }

    @Override
    public ValueObjectAssertion implementsHashCode() throws UnitAssertionError {
        return null;
    }

    @Override
    public ValueObjectAssertion implementsToString() throws UnitAssertionError {
        return null;
    }
}
