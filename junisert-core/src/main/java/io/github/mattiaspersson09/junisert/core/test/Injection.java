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
package io.github.mattiaspersson09.junisert.core.test;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.core.reflection.Invokable;
import io.github.mattiaspersson09.junisert.core.reflection.Unit;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Performs injection of values for invokable unit members.
 */
public class Injection {
    private static final Logger LOGGER = Logger.getLogger(Injection.class);

    private final Invokable injectionTarget;
    private final Unit parentUnit;
    private Predicate<Object> injectionSetup;
    private Predicate<Object> injectionEffect;
    private Supplier<? extends Error> onInjectionError;

    public Injection(Unit parentUnit, Invokable injectionTarget) {
        this.injectionTarget = injectionTarget;
        this.parentUnit = parentUnit;
    }

    public void setup(Predicate<Object> setup) {
        this.injectionSetup = setup;
    }

    public void shouldResultIn(Predicate<Object> effect) {
        this.injectionEffect = effect;
    }

    public void onInjectionError(Supplier<? extends Error> onInjectionError) {
        this.onInjectionError = onInjectionError;
    }

    /**
     *
     * @param args
     * @return
     */
    public boolean inject(Object... args) {
        Object unitInstance = parentUnit.createInstance()
                .orElseThrow(() -> new UnsupportedConstructionError(parentUnit.getType()));

        if (!injectionSetup.test(unitInstance)) {
            LOGGER.warn("Injection precondition setup was unsuccessful");
            return false;
        }

        try {
            LOGGER.test("Invoking: arguments({0}) -> {1}.{2}({3})",
                    Arrays.toString(args),
                    parentUnit.getName(),
                    injectionTarget.getName(),
                    injectionTarget.accepts());
            injectionTarget.invoke(unitInstance, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw onInjectionError.get();
        }

        return injectionEffect.test(unitInstance);
    }
}
