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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Constructor extends Member {
    private final java.lang.reflect.Constructor<?> origin;
    private final List<Parameter> parameters;

    Constructor(java.lang.reflect.Constructor<?> origin, List<Parameter> parameters) {
        super(origin);
        this.origin = origin;
        this.parameters = Collections.unmodifiableList(Objects.requireNonNull(parameters));
    }

    public boolean isDefault() {
        return origin.getParameterCount() == 0;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public Class<?> getType() {
        return origin.getClass();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Constructor that = (Constructor) object;
        return Objects.equals(origin, that.origin) && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, parameters);
    }

    @Override
    public String toString() {
        return "Constructor{" +
                "origin=" + origin +
                ", parameters=" + parameters +
                '}';
    }
}
