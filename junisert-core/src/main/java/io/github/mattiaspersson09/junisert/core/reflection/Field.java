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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Field extends Member implements Invokable {
    private final java.lang.reflect.Field origin;
    // Can have both bean style and builder style methods
    private final List<Method> setters;
    private final List<Method> getters;

    Field(java.lang.reflect.Field origin) {
        super(origin);
        this.origin = origin;
        this.origin.setAccessible(true);
        this.setters = new ArrayList<>();
        this.getters = new ArrayList<>();
    }

    void addSetter(Method setter) {
        setters.add(setter);
    }

    void addGetter(Method getter) {
        getters.add(getter);
    }

    public List<Method> getSetters() {
        return Collections.unmodifiableList(setters);
    }

    public List<Method> getGetters() {
        return Collections.unmodifiableList(getters);
    }

    public boolean setValue(Object unitInstance, Object value) {
        try {
            origin.set(unitInstance, value);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    public Object getValue(Object unitInstance) throws IllegalAccessException {
        return origin.get(unitInstance);
    }

    public Object getValueOrElse(Object unitInstance, Object fallback) {
        try {
            return origin.get(unitInstance);
        } catch (IllegalAccessException e) {
            return fallback;
        }
    }

    @Override
    public Class<?> getType() {
        return origin.getType();
    }

    @Override
    public Object invoke(Object instance, Object... args) throws IllegalAccessException {
        if (!setValue(instance, args)) {
            throw new IllegalAccessException();
        }

        return getValue(instance);
    }

    @Override
    public Collection<Class<?>> accepts() {
        return Collections.singletonList(getType());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Field field = (Field) object;
        return Objects.equals(origin, field.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin);
    }

    @Override
    public String toString() {
        return "Field{" +
                "origin=" + origin +
                ", setters=" + setters +
                ", getters=" + getters +
                '}';
    }
}
