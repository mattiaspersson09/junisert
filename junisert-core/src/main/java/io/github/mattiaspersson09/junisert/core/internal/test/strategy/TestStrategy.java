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
package io.github.mattiaspersson09.junisert.core.internal.test.strategy;

import io.github.mattiaspersson09.junisert.core.internal.reflection.Field;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Method;

import java.util.function.Predicate;

/**
 * Strategy to adapt when testing units.
 */
public interface TestStrategy {
    /**
     * Creates a predicate to test if a method is a getter for given {@code field}, according to this test strategy.
     *
     * @param field to create a getter test for
     * @return a method test
     */
    Predicate<Method> isGetterForField(Field field);

    /**
     * Creates a predicate to test if a method is a setter for given {@code field}, according to this test strategy.
     *
     * @param field to create a setter test for
     * @return a method test
     */
    Predicate<Method> isSetterForField(Field field);

    /**
     * Name of this test strategy.
     *
     * @return name of this test strategy
     */
    String name();

    /**
     * Creates a new test strategy to adhere to the <em>Java Bean Specification</em>.
     *
     * @return a new test strategy
     */
    static TestStrategy javaBeanCompliant() {
        return new JavaBeanTestStrategy();
    }

    /**
     * Creates a new flexible test strategy not bound to any convention.
     *
     * @return a new test strategy
     */
    static TestStrategy none() {
        return new NoTestStrategy();
    }
}
