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
package io.github.mattiaspersson09.junisert.api.value;

/**
 * Generates a value representation of a requested type if it's supported.
 *
 * @param <T> type supported by this generator
 * @see Value
 */
public interface ValueGenerator<T> {
    /**
     * Generates a value representation of <b>{@code fromType}</b> if it's supported.
     * Will throw {@link UnsupportedTypeException} if type is not supported by this generator.
     *
     * @param fromType to generate value for
     * @return object value of fromType
     * @throws UnsupportedTypeException if type is not supported by this generator
     */
    Value<? extends T> generate(Class<?> fromType) throws UnsupportedTypeException;

    /**
     * Check if this generator supports requested type.
     *
     * @param type to check
     * @return true if type is supported by this generator
     */
    boolean supports(Class<?> type);
}
