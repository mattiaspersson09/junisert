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
package io.github.mattiaspersson09.junisert.core.internal.reflection;

/**
 * Used when a reflective operation fails and should be unchecked, wrapping reflective cause.
 */
public class ReflectionException extends RuntimeException {
    /**
     * Creates a new reflection exception with root cause for why the reflection failed.
     *
     * @param message explaining why reflection failed
     * @param cause   for why the reflection failed
     */
    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new reflection exception with root cause for why the reflection failed.
     *
     * @param cause for why the reflection failed
     */
    public ReflectionException(Throwable cause) {
        super(cause);
    }
}
