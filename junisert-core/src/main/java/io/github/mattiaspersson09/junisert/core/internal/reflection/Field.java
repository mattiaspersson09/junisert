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

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Representing a reflected field as member of a unit, being a wrapper for {@link java.lang.reflect.Field}.
 */
public class Field extends Member implements Invokable {
    private final java.lang.reflect.Field origin;

    Field(java.lang.reflect.Field origin) {
        super(origin);
        this.origin = origin;
        this.origin.setAccessible(true);
    }

    /**
     * Creates a reflected field wrapper from {@code origin} field.
     *
     * @param origin to wrap
     * @return reflected field
     */
    public static Field of(java.lang.reflect.Field origin) {
        return new Field(origin);
    }

    /**
     * Setting value for this field from a constructed parent instance using reflection. This method
     * will never throw if access to this field is illegal, it's up to the caller to handle
     * further operations based on the result.
     *
     * @param unitInstance as constructed parent object
     * @param value        to set for this field
     * @throws ReflectionException if not possible to update field state
     */
    public void setValue(Object unitInstance, Object value) throws ReflectionException {
        try {
            origin.set(unitInstance, value);
        } catch (Exception e) {
            throw new ReflectionException("Unable to set value for field: " + this, e);
        }
    }

    /**
     * Getting value from this field from a constructed parent instance using reflection.
     * {@link ReflectionException} will be thrown if access is not possible because of restriction or
     * unknown {@code unitInstance}.
     *
     * @param unitInstance as constructed parent object
     * @return current value
     * @throws ReflectionException if not possible to access this field
     */
    public Object getValue(Object unitInstance) throws ReflectionException {
        try {
            return origin.get(unitInstance);
        } catch (Exception e) {
            throw new ReflectionException("Unable to get value from field: " + this, e);
        }
    }

    /**
     * Checks if this field is immutable, it's considered immutable if the final modifier is present.<br>
     * <br>
     * This is a convenience method and is the same as calling {@code field.modifier().isFinal()}.
     * If the desire is to check for immutable instance fields or immutable class fields then
     * {@link #isInstanceImmutable()} and {@link #isClassConstant()} should be used instead.
     * <p><br>
     * Example of immutable fields:
     * <pre>
     *     public class Immutable {
     *       private static final Object PRIVATE_CLASS_IMMUTABLE = new Object();
     *       protected static final Object PROTECTED_CLASS_IMMUTABLE = new Object();
     *       public static final Object PUBLIC_CLASS_IMMUTABLE = new Object();
     *       static final Object PACKAGE_CLASS_IMMUTABLE = new Object();
     *
     *       private final Object privateImmutable;
     *       protected final Object protectedImmutable;
     *       public final Object publicImmutable;
     *       final Object packageImmutable;
     *     }
     * </pre>
     *
     * @return true if this field is constant
     * @see #isClassConstant()
     * @see #isInstanceImmutable()
     */
    public boolean isImmutable() {
        return modifier().isFinal();
    }

    /**
     * Checks if this field is a constant value owned by classes and not instances.
     * <p><br>
     * Example of class constants:
     * <pre>
     * public class ClassWithConstants {
     *   public static final Object PUBLIC_CLASS_CONSTANT = new Object();
     *   protected static final Object PROTECTED_CLASS_CONSTANT = new Object();
     *   private static final Object PRIVATE_CLASS_CONSTANT = new Object();
     *   static final Object PACKAGE_CLASS_CONSTANT = new Object();
     * }
     * </pre>
     *
     * @return true if this field is an immutable constant owned by a class
     * @see #isImmutable()
     * @see #isInstanceImmutable()
     */
    public boolean isClassConstant() {
        return !isInstanceMember() && isImmutable();
    }

    /**
     * Checks if this field is an immutable field owned by instances. An immutable instance field is non-static
     * and has the final modifier.
     * <p><br>
     * Example of immutable instance fields:
     * <pre>
     * public class ImmutableFields {
     *   private final Object privateImmutable;
     *   public final Object publicImmutable;
     *   protected final Object publicImmutable;
     *   final Object packageImmutable;
     *
     *   ImmutableFields(Object privateImmutable, Object publicImmutable, Object packageImmutable) {
     *     this.publicImmutable = publicImmutable;
     *     this.privateImmutable = privateImmutable;
     *     this.packageImmutable = packageImmutable;
     *   }
     * }
     * </pre>
     *
     * @return true if this field is immutable
     * @see #isClassConstant()
     * @see #isImmutable()
     */
    public boolean isInstanceImmutable() {
        return isInstanceMember() && isImmutable();
    }

    /**
     * Checks if this field is exactly or subtype of given {@code type}.
     *
     * @param type of field
     * @return true if this field is
     */
    public boolean isTypeOf(Class<?> type) {
        return type.isAssignableFrom(getType());
    }

    /**
     * Checks if this field's type is a primitive type.
     *
     * @return true if it's primitive
     */
    public boolean isPrimitive() {
        return getType().isPrimitive();
    }

    /**
     * Checks if this field's type is an array.
     *
     * @return true if it's an array
     */
    public boolean isArray() {
        return getType().isArray();
    }

    /**
     * Checks if this field's type is an enumeration.
     *
     * @return true if it's an enumeration
     */
    public boolean isEnum() {
        return getType().isEnum();
    }

    /**
     * Checks if this field's type is a boolean, primitive or the wrapper variant.
     *
     * @return true if boolean
     * @see #isPrimitive()
     */
    public boolean isBoolean() {
        return isTypeOf(boolean.class) || isTypeOf(Boolean.class);
    }

    @Override
    public Class<?> getType() {
        return origin.getType();
    }

    @Override
    public Object invoke(Object instance, Object... args) throws ReflectionException {
        Object value;

        if (getType().isArray()) {
            value = args;
        } else {
            if (args.length > 1) {
                throw new ReflectionException(new IllegalArgumentException(this + " only accepts a single value"));
            }

            value = args[0];
        }

        setValue(instance, value);

        return getValue(instance);
    }

    @Override
    public Collection<Class<?>> accepts() {
        return Collections.singletonList(getType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Field field = (Field) o;
        return Objects.equals(origin, field.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), origin);
    }

    @Override
    public String toString() {
        return String.format("%s.%s(%s)", getParent().getSimpleName(), getName(), getType());
    }
}
