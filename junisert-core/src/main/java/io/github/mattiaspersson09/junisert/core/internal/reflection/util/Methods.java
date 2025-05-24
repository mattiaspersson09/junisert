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
package io.github.mattiaspersson09.junisert.core.internal.reflection.util;

import io.github.mattiaspersson09.junisert.core.internal.reflection.Method;

/**
 * Utility class with helper methods for acting on reflected {@link Method}'s.
 */
public final class Methods {
    private Methods() {
    }

    /**
     * Checks if given {@code method} is considered an override of {@link Object#equals(Object)}.
     *
     * @param method to check
     * @return true if given method is override of {@link Object#equals(Object)}
     */
    public static boolean isEqualsMethod(Method method) {
        return "equals".equals(method.getName()) && method.isFunctionOf(Object.class, boolean.class);
    }

    /**
     * Checks if given {@code method} is considered an override of {@link Object#hashCode()}.
     *
     * @param method to check
     * @return true if given method is override of {@link Object#hashCode()}
     */
    public static boolean isHashCodeMethod(Method method) {
        return "hashCode".equals(method.getName()) && method.isProducing(int.class);
    }

    /**
     * Checks if given {@code method} is considered an override of {@link Object#toString()}.
     *
     * @param method to check
     * @return true if given method is override of {@link Object#toString()}
     */
    public static boolean isToStringMethod(Method method) {
        return "toString".equals(method.getName()) && method.isProducing(String.class);
    }
}
