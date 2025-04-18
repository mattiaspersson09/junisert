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
package io.github.mattiaspersson09.junisert.core.reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * Is a unit member which can be called (also known as invoke) with arguments to update some state within a
 * unit instance.
 */
public interface Invokable {
    /**
     * Uses reflection to update state inside an instance of a parent unit.
     *
     * @param instance of the parent unit
     * @param args     values to inject to update the state
     * @return result of the invocation
     * @throws InvocationTargetException if invocation fails
     * @throws IllegalAccessException    if the invocation isn't possible or allowed
     * @see #accepts()
     */
    Object invoke(Object instance, Object... args) throws InvocationTargetException, IllegalAccessException;

    /**
     * Types this invokable accepts as values for invocation arguments.
     *
     * @return collection of value types
     */
    Collection<Class<?>> accepts();

    /**
     * Name of this invokable, can be a descriptive signature of the unit member or
     * short version of the member's class name.
     *
     * @return name for this invokable
     */
    default String getName() {
        return getClass().getSimpleName();
    }
}
