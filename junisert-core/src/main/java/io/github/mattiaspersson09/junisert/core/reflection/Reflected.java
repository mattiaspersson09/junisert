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

/**
 * Required information for identifying a reflected element.
 */
public interface Reflected {
    /**
     * Returns name for this reflected element.
     *
     * @return name for this reflected element
     */
    String getName();

    /**
     * Returns the represented type of this reflected element. Can be a field type, method return type or origin type.
     *
     * @return type of this reflected element
     */
    Class<?> getType();

    /**
     * Returns a wrapper representing this reflected element's modifiers.
     *
     * @return modifier wrapper
     * @see Modifier
     */
    Modifier modifier();

    /**
     * Checks if this reflected element is synthetically created.
     *
     * @return true if this element is synthetically created
     */
    boolean isSynthetic();
}
