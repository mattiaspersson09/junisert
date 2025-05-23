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
 * Primarily used to create instances of {@link Unit} classes.
 */
public interface InstanceCreator {
    /**
     * Creates a new unique instance of given {@code unitClass}.
     *
     * @param unitClass to create instance of
     * @return new unique instance of given unit class
     */
    Object createInstance(Class<?> unitClass);

    /**
     * Creates a new unique instance of given {@link Unit}.
     *
     * @param unit to create instance of
     * @return new unique instance of given unit
     */
    default Object createInstance(Unit unit) {
        return createInstance(unit.getType());
    }
}
