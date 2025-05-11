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
 * Is an identifiable direct member of a unit, such as field, constructor and method. Reflecting members
 * of a unit should inherit from this base class.
 */
public abstract class Member implements Reflected {
    private final java.lang.reflect.Member origin;
    private final Modifier modifier;

    protected Member(java.lang.reflect.Member origin) {
        this.origin = Objects.requireNonNull(origin);
        this.modifier = new Modifier(origin.getModifiers());
    }

    /**
     * Checks if this member is owned by an instance or not. An instance member is non-static and is owned by
     * instances and not a class.
     *
     * @return true if this is an instance member
     */
    public final boolean isInstanceMember() {
        return !modifier.isStatic();
    }

    /**
     * Get the unit type that is parent to this member.
     *
     * @return parent unit
     */
    public final Class<?> getParent() {
        return origin.getDeclaringClass();
    }

    @Override
    public String getName() {
        return origin.getName();
    }

    @Override
    public abstract Class<?> getType();

    @Override
    public final Modifier modifier() {
        return modifier;
    }

    @Override
    public final boolean isSynthetic() {
        return origin.isSynthetic();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(origin, member.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin);
    }

    @Override
    public String toString() {
        return getParent().getSimpleName() + "." + getName();
    }
}
