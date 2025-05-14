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
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Invokable;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Performs test injection of values for invokable unit members. Users can set up unit instance state
 * before the injection and test the result after injection. The tests are performed on the unit instance that the
 * {@link Invokable} target is declared in.<br>
 * <br>
 * Setup and result is not required and will be skipped if missing.
 *
 * @see Invokable
 * @see ValueService
 */
class Injection {
    private static final Logger LOGGER = Logger.getLogger("Injection");

    private final InstanceCreator instanceCreator;
    private final Invokable injectionTarget;
    private Consumer<Object> setup;
    private Predicate<Object> result;

    /**
     * Creates a new test injection.
     *
     * @param injectionTarget to inject values into from a unit instance
     * @param instanceCreator that can create unit instances
     */
    Injection(Invokable injectionTarget, InstanceCreator instanceCreator) {
        this.injectionTarget = injectionTarget;
        this.instanceCreator = instanceCreator;
        this.result = result -> true;
    }

    /**
     * Setup that should pass before injection, it can be setting up values for the unit instance that should later
     * be affected.
     *
     * @param setup that should pass before injection
     */
    public void setup(Consumer<Object> setup) {
        this.setup = Objects.requireNonNull(setup);
    }

    /**
     * Result that should pass after injection, it can be to make sure state is changed inside the unit instance
     * after the injection.
     *
     * @param result that should pass after injection
     */
    public void shouldResultIn(Predicate<Object> result) {
        this.result = Objects.requireNonNull(result);
    }

    /**
     * Try to inject {@code arguments} into given {@link Invokable}. The injection is only seen as successful
     * if the injection does not throw and the injection gives the desired result.<br>
     * <ul>
     * <li>If no setup is given then the setup is ignored.</li>
     * <li>If no desired result is given then the result test always passes.</li>
     * </ul>
     *
     * @param arguments to inject
     * @return true if setup passes, injection is successful and produces the desired result
     * @see #setup(Consumer)
     * @see #shouldResultIn(Predicate)
     */
    public boolean inject(Object... arguments) {
        Object unitInstance = instanceCreator.createInstance(injectionTarget.getParent());

        if (setup != null) {
            setup.accept(unitInstance);
        }

        LOGGER.test("Injecting: arguments({0}) -> {1}", Arrays.toString(arguments), injectionTarget);
        injectionTarget.invoke(unitInstance, arguments);

        return result.test(unitInstance);
    }
}
