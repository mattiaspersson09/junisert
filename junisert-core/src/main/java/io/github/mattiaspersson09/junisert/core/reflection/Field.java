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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Representing a reflected field as member of a unit, being a wrapper for {@link java.lang.reflect.Field}.
 */
public class Field extends Member implements Invokable {
    private final java.lang.reflect.Field origin;
    // Can have several different style methods
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

    /**
     * Gets an unmodifiable view of the setter methods associated with this field.
     *
     * @return setters associated with this field
     */
    public List<Method> getSetters() {
        return Collections.unmodifiableList(setters);
    }

    /**
     * Gets an unmodifiable view of the getter methods associated with this field.
     *
     * @return getters associated with this field
     */
    public List<Method> getGetters() {
        return Collections.unmodifiableList(getters);
    }

    /**
     * Setting value for this field from a constructed parent instance using reflection. This method
     * will never throw if access to this field is illegal, it's up to the caller to handle
     * further operations based on the result.
     *
     * @param unitInstance as constructed parent object
     * @param value        to set for this field
     * @return true if update was successful, otherwise false
     */
    public boolean setValue(Object unitInstance, Object value) {
        try {
            origin.set(unitInstance, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Getting value from this field from a constructed parent instance using reflection.
     * {@link IllegalAccessException} will be thrown if access is not possible because of restriction or
     * unknown {@code unitInstance}.
     *
     * @param unitInstance as constructed parent object
     * @return current value
     * @throws IllegalAccessException if not possible to access this field
     * @see #getValueOrElse(Object, Object)
     */
    public Object getValue(Object unitInstance) throws IllegalAccessException {
        try {
            return origin.get(unitInstance);
        } catch (Exception e) {
            throw new IllegalAccessException("Tried to get value from " + getName() + " but it was not possible");
        }
    }

    /**
     * Gets value from this field from a constructed parent instance using reflection like {@link #getValue(Object)},
     * but accepts a fallback value if access is not possible.
     *
     * @param unitInstance as constructed parent object
     * @param fallback     value if access is not possible
     * @return current value, otherwise the fallback
     */
    public Object getValueOrElse(Object unitInstance, Object fallback) {
        try {
            return origin.get(unitInstance);
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Checks if this field is an instance field or not. An instance field is non-static and is owned by
     * instances and not the owning class.
     *
     * @return true if this is an instance field
     */
    public boolean isInstanceField() {
        return !modifier().isStatic();
    }

    @Override
    public Class<?> getType() {
        return origin.getType();
    }

    @Override
    public Object invoke(Object instance, Object... args) throws IllegalAccessException, InvocationTargetException {
        Object value;

        if (getType().isArray()) {
            value = args;
        } else {
            if (args.length > 1) {
                throw new InvocationTargetException(
                        new IllegalArgumentException(getName() + " only accepts a single value"));
            }

            value = args[0];
        }

        if (!setValue(instance, value)) {
            throw new IllegalAccessException("Tried to set value for " + getName() + " but it was not possible");
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
