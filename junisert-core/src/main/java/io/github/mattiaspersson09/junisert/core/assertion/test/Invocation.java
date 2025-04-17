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
package io.github.mattiaspersson09.junisert.core.assertion.test;

import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.core.reflection.Invokable;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BooleanSupplier;

class Invocation {
    private static final Logger LOGGER = Logger.getLogger("Unit invocation");

    private final Object[] args;
    private final Invokable invokable;
    private Object unitInstance;
    private BooleanSupplier effect;

    private Invocation(Invokable invokable, Object... args) {
        this.invokable = invokable;
        this.args = args;
    }

    public static Invocation target(Invokable invokable, Object... args) {
        return new Invocation(invokable, args);
    }

    public Invocation fromInstance(Object unitInstance) {
        this.unitInstance = unitInstance;
        return this;
    }

    public Invocation shouldResultIn(BooleanSupplier effect) {
        this.effect = effect;
        return this;
    }

    public Object invoke() {
        if (unitInstance == null) {
            throw new UnitAssertionError("Is missing an instance to invoke from");
        }

        try {
            Object result = invokable.invoke(unitInstance, args);

            if (!effect.getAsBoolean()) {
                throw new UnitAssertionError("Invocation didn't have desired effect");
            }

            return result;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
