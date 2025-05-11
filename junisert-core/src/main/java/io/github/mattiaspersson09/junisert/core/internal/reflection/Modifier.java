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
 * Convenience wrapper for {@link java.lang.reflect.Modifier} that takes no arguments and simplify checking or filtering
 * reflected elements with certain modifiers.
 */
public final class Modifier {
    private final int value;

    Modifier(int value) {
        this.value = value;
    }

    public boolean isPublic() {
        return java.lang.reflect.Modifier.isPublic(value);
    }

    public boolean isProtected() {
        return java.lang.reflect.Modifier.isProtected(value);
    }

    public boolean isPrivate() {
        return java.lang.reflect.Modifier.isPrivate(value);
    }

    public boolean isPackagePrivate() {
        return !isPublic() && !isProtected() && !isPrivate();
    }

    public boolean isFinal() {
        return java.lang.reflect.Modifier.isFinal(value);
    }

    public boolean isAbstract() {
        return java.lang.reflect.Modifier.isAbstract(value);
    }

    public boolean isInterface() {
        return java.lang.reflect.Modifier.isInterface(value);
    }

    public boolean isNative() {
        return java.lang.reflect.Modifier.isNative(value);
    }

    public boolean isStatic() {
        return java.lang.reflect.Modifier.isStatic(value);
    }

    public boolean isStrict() {
        return java.lang.reflect.Modifier.isStrict(value);
    }

    public boolean isSynchronized() {
        return java.lang.reflect.Modifier.isSynchronized(value);
    }

    public boolean isTransient() {
        return java.lang.reflect.Modifier.isTransient(value);
    }

    public boolean isVolatile() {
        return java.lang.reflect.Modifier.isVolatile(value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Modifier modifier = (Modifier) object;
        return value == modifier.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
