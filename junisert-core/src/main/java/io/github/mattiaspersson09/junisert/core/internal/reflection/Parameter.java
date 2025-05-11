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

import java.util.Objects;

/**
 * Reflected parameter of a constructor or method, being a wrapper for {@link java.lang.reflect.Parameter}.
 * Used for invoking constructors and methods where parameters holds information about the accepted
 * type of arguments.
 */
public final class Parameter implements Reflected {
    private final java.lang.reflect.Parameter origin;
    private final Modifier modifier;

    Parameter(java.lang.reflect.Parameter origin) {
        this.origin = Objects.requireNonNull(origin);
        this.modifier = new Modifier(origin.getModifiers());
    }

    /**
     * Creates a reflected parameter wrapper from {@code origin} parameter.
     *
     * @param origin to wrap
     * @return reflected parameter
     */
    public static Parameter of(java.lang.reflect.Parameter origin) {
        return new Parameter(origin);
    }

    public boolean isVarArgs() {
        return origin.isVarArgs();
    }

    public boolean isAnnotated() {
        return origin.getDeclaredAnnotations().length > 0;
    }

    @Override
    public String getName() {
        return origin.getName();
    }

    @Override
    public Class<?> getType() {
        return origin.getType();
    }

    @Override
    public Modifier modifier() {
        return modifier;
    }

    @Override
    public boolean isSynthetic() {
        return origin.isSynthetic();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Parameter parameter = (Parameter) object;
        return Objects.equals(origin, parameter.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin);
    }

    @Override
    public String toString() {
        return getType() + " " + getName();
    }
}
