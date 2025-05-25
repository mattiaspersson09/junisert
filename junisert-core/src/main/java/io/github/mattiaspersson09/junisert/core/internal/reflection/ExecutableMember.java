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

import io.github.mattiaspersson09.junisert.core.internal.reflection.util.Parameters;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Unit member that has parameters and accepts {@code 0..N} number of arguments.
 */
public abstract class ExecutableMember extends Member implements Executable {
    private final List<Parameter> parameters;

    /**
     * Creates a new executable member of given {@code origin}.
     *
     * @param origin of this member
     */
    protected ExecutableMember(java.lang.reflect.Executable origin) {
        super(origin);
        this.parameters = Collections.unmodifiableList(Parameters.map(origin.getParameters()));
    }

    @Override
    public boolean hasParameters() {
        return !parameters.isEmpty();
    }

    @Override
    public boolean hasNoParameters() {
        return parameters.isEmpty();
    }

    @Override
    public boolean hasParameterCount(int count) {
        return parameters.size() == count;
    }

    @Override
    public boolean hasParameterType(Class<?> type) {
        return parameters.stream()
                .anyMatch(parameter -> parameter.getType().equals(type));
    }

    @Override
    public boolean hasParameterFrom(Class<?> type) {
        return parameters.stream()
                .anyMatch(parameter -> parameter.getType().isAssignableFrom(type));
    }

    @Override
    public boolean hasParameterOf(Class<?> type) {
        return parameters.stream()
                .anyMatch(parameter -> type.isAssignableFrom(parameter.getType()));
    }

    @Override
    public List<Class<?>> getParameterTypes() {
        return parameters.stream()
                .map(Parameter::getType)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        ExecutableMember that = (ExecutableMember) object;
        return Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parameters);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getName(), getParameterTypes());
    }
}
