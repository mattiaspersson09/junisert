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
import java.util.Collection;
import java.util.Objects;

/**
 * Representing a reflected constructor as member of a unit, being a wrapper for {@link java.lang.reflect.Constructor}.
 */
public class Constructor extends ExecutableMember implements Invokable {
    private final java.lang.reflect.Constructor<?> origin;

    Constructor(java.lang.reflect.Constructor<?> origin) {
        super(origin);
        this.origin = origin;
        this.origin.setAccessible(true);
    }

    /**
     * Creates a reflected constructor wrapper from {@code origin} constructor.
     *
     * @param origin to wrap
     * @return reflected constructor
     */
    public static Constructor of(java.lang.reflect.Constructor<?> origin) {
        return new Constructor(origin);
    }

    /**
     * Checks if this constructor is a default constructor, having no parameters and can be either
     * public, protected, private or package-private.
     *
     * @return true if this is a default constructor
     */
    public boolean isDefault() {
        return hasNoParameters();
    }

    @Override
    public Class<?> getType() {
        return origin.getClass();
    }

    @Override
    public Object invoke(Object instance, Object... args) throws InvocationTargetException, IllegalAccessException {
        if (instance != null && !origin.getDeclaringClass().equals(instance.getClass())) {
            throw new IllegalArgumentException("Instance class does not have the same origin as this constructor");
        }

        try {
            return origin.newInstance(args);
        } catch (InstantiationException e) {
            throw new InvocationTargetException(e);
        }
    }

    @Override
    public Collection<Class<?>> accepts() {
        return getParameterTypes();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Constructor that = (Constructor) o;
        return Objects.equals(origin, that.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), origin);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", origin.getDeclaringClass().getSimpleName(), getParameterTypes());
    }
}
