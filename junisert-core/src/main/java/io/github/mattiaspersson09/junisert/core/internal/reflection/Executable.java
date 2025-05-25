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

import java.util.List;

/**
 * Reflected executables can have parameters and accepts {@code 0..N} number of arguments.
 */
public interface Executable {
    /**
     * Checks if this executable has any parameter.
     *
     * @return true if any parameter is present
     */
    boolean hasParameters();

    /**
     * Checks if this executable doesn't have any parameter.
     *
     * @return true if no parameter is present
     */
    boolean hasNoParameters();

    /**
     * Checks if this executable has {@code count} number of parameters.
     *
     * @param count parameters present
     * @return true if parameter count matches given count
     */
    boolean hasParameterCount(int count);

    /**
     * Checks if this executable has any parameter matching exactly {@code type}. To check polymorphic types then
     * {@link #hasParameterFrom(Class)} and {@link #hasParameterOf(Class)} should be used instead.
     *
     * @param type to find
     * @return true if a parameter of type is present
     * @see #hasParameterFrom(Class)
     * @see #hasParameterOf(Class)
     */
    boolean hasParameterType(Class<?> type);

    /**
     * Checks if this executable has a parameter which is a super type of or is given {@code type}. Meaning
     * that a parameter can be assigned/cast <em>from</em> given {@code type}.
     * <p>
     * Pseudo example:
     * <pre>
     * Parameter parameter = (Parameter) type;
     * </pre>
     *
     * @param type of any parameter
     * @return true if a parameter is assignable from given type
     * @see #hasParameterOf(Class)
     */
    boolean hasParameterFrom(Class<?> type);

    /**
     * Checks if this executable has any parameter which is a subtype of or is given {@code type}. Meaning
     * that a parameter can be assigned/cast <em>to</em> given {@code type}.
     * <p>
     * Pseudo example:
     * <pre>
     * Type type = (Type) parameter;
     * </pre>
     *
     * @param type of any parameter
     * @return true if a parameter is assignable to given type
     * @see #hasParameterFrom(Class)
     */
    boolean hasParameterOf(Class<?> type);

    /**
     * Parameter types this executable accepts as arguments in declaration order.
     *
     * @return parameter types
     */
    List<Class<?>> getParameterTypes();
}
