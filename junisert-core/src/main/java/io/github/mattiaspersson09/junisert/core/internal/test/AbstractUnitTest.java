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

import io.github.mattiaspersson09.junisert.api.internal.service.ValueService;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.convention.Convention;

public abstract class AbstractUnitTest implements UnitTest {
    protected final ValueService valueService;
    protected final InstanceCreator instanceCreator;
    protected Convention activeConvention;

    public AbstractUnitTest(ValueService valueService, InstanceCreator instanceCreator) {
        this.valueService = valueService;
        this.instanceCreator = instanceCreator;
        this.activeConvention = Convention.none();
    }

    public final void setActiveConvention(Convention convention) {
        this.activeConvention = convention;
    }
}
