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
 * Used when a failure occurs during value generation and the construction of a concrete value fails due to one of:
 *
 * <ol>
 * <li>reflection failure</li>
 * <li>lazy construction failure</li>
 * </ol>
 */
public class UnsupportedConstructionError extends Error {
    public UnsupportedConstructionError(Class<?> type, Throwable cause) {
        super(String.format("Failed to construct concrete value of: %s", type), cause);
    }

    public UnsupportedConstructionError(Class<?> type) {
        super(String.format("Failed to construct concrete value of: %s", type));
    }

    public UnsupportedConstructionError(Throwable cause) {
        super(cause);
    }
}
