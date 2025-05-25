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
package io.github.mattiaspersson09.junisert.core.internal;

import io.github.mattiaspersson09.junisert.core.internal.reflection.Unit;

/**
 * Used to wrap and supply expensive resources that should only be constructed once and be injected where needed
 * during assertions.
 */
public final class AssertionResource {
    private final Unit unitUnderAssertion;
    private final InstanceCreator instanceCreator;
    private final ValueService valueService;

    /**
     * Creates a new resource to share and inject where needed.
     *
     * @param unitUnderAssertion currently being asserted
     * @param instanceCreator    creating instances of {@code unitUnderAssertion}
     * @param valueService       providing value support with potentially caching abilities
     */
    public AssertionResource(Unit unitUnderAssertion, InstanceCreator instanceCreator, ValueService valueService) {
        this.unitUnderAssertion = unitUnderAssertion;
        this.instanceCreator = instanceCreator;
        this.valueService = valueService;
    }

    /**
     * Gets the {@link Unit} currently being asserted on.
     *
     * @return unit under assertion
     */
    public Unit getUnitUnderAssertion() {
        return unitUnderAssertion;
    }

    /**
     * Gets the {@link InstanceCreator} that should be used for creating instances of the unit under assertion.
     *
     * @return instance creator to use
     */
    public InstanceCreator getInstanceCreator() {
        return instanceCreator;
    }

    /**
     * Gets the {@link ValueService} that should provide value support.
     *
     * @return supporting value service
     */
    public ValueService getValueService() {
        return valueService;
    }
}
