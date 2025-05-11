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
package io.github.mattiaspersson09.junisert.core;

import io.github.mattiaspersson09.junisert.api.assertion.PlainObjectAssertion;
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertion;
import io.github.mattiaspersson09.junisert.core.assertion.PlainObjectAssertionImpl;
import io.github.mattiaspersson09.junisert.core.assertion.UnitAssertionImpl;
import io.github.mattiaspersson09.junisert.core.internal.DefaultInstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.SharedResource;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Unit;

public final class Junisert {
    private static final InstanceCreator DEFAULT_INSTANCE_CREATOR = new DefaultInstanceCreator();

    private Junisert() {
    }

    public static UnitAssertion assertThatUnit(Class<?> unitClass) {
        return new UnitAssertionImpl(getDefaultTestResource(unitClass));
    }

    public static PlainObjectAssertion assertThatPojo(Class<?> pojoClass) {
        return new PlainObjectAssertionImpl(getDefaultTestResource(pojoClass));
    }

    static SharedResource getDefaultTestResource(Class<?> unitUnderAssertion) {
        return new SharedResource(
                Unit.of(unitUnderAssertion),
                DEFAULT_INSTANCE_CREATOR,
                SingletonValueService.getInstance()
        );
    }
}
