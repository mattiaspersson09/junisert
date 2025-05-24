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

    /**
     * Checks if this modifier consists of a value determining <em>public</em> visibility.
     *
     * @return true if this modifier has value for <em>public</em> visibility
     */
    public boolean isPublic() {
        return java.lang.reflect.Modifier.isPublic(value);
    }

    /**
     * Checks if this modifier consists of a value determining <em>protected</em> visibility.
     *
     * @return true if this modifier has value for <em>protected</em> visibility
     */
    public boolean isProtected() {
        return java.lang.reflect.Modifier.isProtected(value);
    }

    /**
     * Checks if this modifier consists of a value determining <em>private</em> visibility.
     *
     * @return true if this modifier has value for <em>private</em> visibility
     */
    public boolean isPrivate() {
        return java.lang.reflect.Modifier.isPrivate(value);
    }

    /**
     * Checks if this modifier consists of a value determining <em>package-private</em> (default) visibility.
     *
     * @return true if this modifier has value for <em>package-private</em> visibility
     */
    public boolean isPackagePrivate() {
        return !isPublic() && !isProtected() && !isPrivate();
    }

    /**
     * Checks if this modifier consists of a value determining <em>final</em> modifier.
     *
     * @return true if this modifier has value for <em>final</em> modifier
     */
    public boolean isFinal() {
        return java.lang.reflect.Modifier.isFinal(value);
    }

    /**
     * Checks if this modifier consists of a value determining <em>abstract</em> modifier.
     *
     * @return true if this modifier has value for <em>abstract</em> modifier
     */
    public boolean isAbstract() {
        return java.lang.reflect.Modifier.isAbstract(value);
    }

    /**
     * Checks if this modifier consists of a value determining <em>interface</em> modifier.
     *
     * @return true if this modifier has value for <em>interface</em> modifier
     */
    public boolean isInterface() {
        return java.lang.reflect.Modifier.isInterface(value);
    }

    /**
     * Checks if this modifier consists of a value determining <em>native</em> modifier.
     *
     * @return true if this modifier has value for <em>native</em> modifier
     */
    public boolean isNative() {
        return java.lang.reflect.Modifier.isNative(value);
    }

    /**
     * Checks if this modifier consists of a value determining <em>static</em> modifier.
     *
     * @return true if this modifier has value for <em>static</em> modifier
     */
    public boolean isStatic() {
        return java.lang.reflect.Modifier.isStatic(value);
    }

    /**
     * Checks if this modifier consists of a value determining <em>strict floating-point</em> modifier.
     *
     * @return true if this modifier has value for <em>strict floating-point</em> modifier
     */
    public boolean isStrict() {
        return java.lang.reflect.Modifier.isStrict(value);
    }

    /**
     * Checks if this modifier consists of a value determining <em>synchronized</em> modifier.
     *
     * @return true if this modifier has value for <em>synchronized</em> modifier
     */
    public boolean isSynchronized() {
        return java.lang.reflect.Modifier.isSynchronized(value);
    }

    /**
     * Checks if this modifier consists of a value determining <em>transient</em> modifier.
     *
     * @return true if this modifier has value for <em>transient</em> modifier
     */
    public boolean isTransient() {
        return java.lang.reflect.Modifier.isTransient(value);
    }

    /**
     * Checks if this modifier consists of a value determining <em>volatile</em> modifier.
     *
     * @return true if this modifier has value for <em>volatile</em> modifier
     */
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
