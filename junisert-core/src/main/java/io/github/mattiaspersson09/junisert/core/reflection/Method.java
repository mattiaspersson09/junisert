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

import io.github.mattiaspersson09.junisert.core.reflection.util.Parameters;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Method extends Member implements Invokable {
    private final java.lang.reflect.Method origin;
    private final List<Parameter> parameters;

    Method(java.lang.reflect.Method origin) {
        super(origin);
        this.origin = origin;
        this.origin.setAccessible(true);
        this.parameters = Collections.unmodifiableList(Parameters.map(origin.getParameters()));
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public Class<?> getType() {
        return origin.getReturnType();
    }

    @Override
    public Object invoke(Object instance, Object... args) throws InvocationTargetException, IllegalAccessException {
        return origin.invoke(instance, args);
    }

    @Override
    public Collection<Class<?>> accepts() {
        return getParameters().stream()
                .map(Parameter::getType)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Method method = (Method) o;
        return Objects.equals(origin, method.origin) && Objects.equals(parameters, method.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), origin, parameters);
    }

    @Override
    public String toString() {
        return "Method{" +
                "origin=" + origin +
                ", parameters=" + parameters +
                '}';
    }
}
