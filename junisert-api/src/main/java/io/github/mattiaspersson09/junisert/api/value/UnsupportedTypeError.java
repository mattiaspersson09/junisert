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
 * When a value type isn't supported in a single {@link ValueGenerator} or among
 * several available generators.
 */
public class UnsupportedTypeError extends Error {
    private static final String NOT_SUPPORTED_TYPE = "Unable to find support for type '%s'. "
            + "Consider registering a custom support for your test suite.";

    /**
     * Creates a new unsupported type error where given {@code type} weren't supported in the test suite among
     * one or several generators.
     *
     * @param type which weren't supported
     */
    public UnsupportedTypeError(Class<?> type) {
        super(String.format(NOT_SUPPORTED_TYPE, type));
    }
}
