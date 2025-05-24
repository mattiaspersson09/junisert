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


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Representing a reflected unit, being a wrapper for unit classes. Is not supported for intentions other than
 * constructing an assertable and testable unit, which were created by the user.
 */
public final class Unit implements Reflected {
    private final Class<?> origin;
    private final Modifier modifier;
    private final List<Field> fields;
    private final List<Constructor> constructors;
    private final List<Method> methods;

    Unit(Class<?> origin) {
        this.origin = Objects.requireNonNull(origin, "unit origin can't be null");
        this.modifier = new Modifier(origin.getModifiers());
        this.fields = new ArrayList<>();
        this.constructors = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    /**
     * Creates a new unit from an origin class created by the user, to be used for assertion and tests.
     *
     * @param origin of unit
     * @return new testable unit
     */
    public static Unit of(Class<?> origin) {
        Unit unit = new Unit(origin);

        Stream.of(origin.getDeclaredConstructors())
                .map(Constructor::new)
                .forEach(unit.constructors::add);

        Stream.of(origin.getDeclaredFields())
                .map(Field::new)
                .forEach(unit.fields::add);

        Stream.of(origin.getDeclaredMethods())
                .map(Method::new)
                .forEach(unit.methods::add);

        return unit;
    }

    /**
     * Returns an unmodifiable view of fields declared by this unit.
     *
     * @return unmodifiable view of declared fields
     */
    public List<Field> getFields() {
        return Collections.unmodifiableList(fields);
    }

    /**
     * Returns an unmodifiable view of constructors declared by this unit.
     *
     * @return unmodifiable view of declared constructors
     */
    public List<Constructor> getConstructors() {
        return Collections.unmodifiableList(constructors);
    }

    /**
     * Returns an unmodifiable view of methods declared by this unit.
     *
     * @return unmodifiable view of declared methods
     */
    public List<Method> getMethods() {
        return Collections.unmodifiableList(methods);
    }

    /**
     * Checks if this unit has not declared any fields.
     *
     * @return true if no fields are declared
     */
    public boolean hasNoFields() {
        return fields.isEmpty();
    }

    /**
     * Checks if this unit has a declared a field with {@code name}.
     *
     * @param name of declared field
     * @return true if declared field with given name is present
     */
    public boolean hasField(String name) {
        return fields.stream()
                .anyMatch(field -> field.getName().equals(name));
    }

    /**
     * Checks if this unit has any declared field matching given {@code predicate}.
     *
     * @param predicate to match field with
     * @return true if a declared field matches given predicate
     */
    public boolean hasFieldMatching(Predicate<Field> predicate) {
        Objects.requireNonNull(predicate);

        return fields.stream()
                .anyMatch(predicate);
    }

    /**
     * Finds fields declared by this unit which matches given {@code predicate}.
     *
     * @param predicate to match fields with
     * @return view of declared fields matching given predicate
     */
    public List<Field> findFieldsMatching(Predicate<Field> predicate) {
        Objects.requireNonNull(predicate);

        return fields.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Checks if this unit has declared a default constructor, which does not accept any arguments.
     *
     * @return true if a default constructor is present
     */
    public boolean hasDefaultConstructor() {
        return constructors.stream()
                .anyMatch(Constructor::isDefault);
    }

    /**
     * Checks if this unit is missing a default constructor, which does not accept any arguments.
     *
     * @return true if no default constructor is present
     */
    public boolean hasNoDefaultConstructor() {
        return constructors.stream()
                .noneMatch(Constructor::isDefault);
    }

    /**
     * Checks if this unit has declared a constructor which accepts {@code 1..N} number of arguments.
     *
     * @return true if a constructor accepting arguments is present
     */
    public boolean hasArgumentConstructor() {
        return constructors.stream()
                .anyMatch(constructor -> !constructor.isDefault());
    }

    /**
     * Checks if this unit has any declared constructor matching given {@code predicate}.
     *
     * @param predicate to match constructor with
     * @return true if a declared constructor matches given predicate
     */
    public boolean hasConstructorMatching(Predicate<Constructor> predicate) {
        Objects.requireNonNull(predicate);

        return constructors.stream()
                .anyMatch(predicate);
    }

    /**
     * Finds constructors declared by this unit which matches given {@code predicate}.
     *
     * @param predicate to match constructors with
     * @return view of declared constructors matching given predicate
     */
    public List<Constructor> findConstructorsMatching(Predicate<Constructor> predicate) {
        Objects.requireNonNull(predicate);

        return constructors.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Checks if this unit has not declared any methods.
     *
     * @return true if no methods are declared
     */
    public boolean hasNoMethods() {
        return methods.isEmpty();
    }

    /**
     * Checks if this unit has a declared a method with {@code name}.
     *
     * @param name of declared method
     * @return true if declared method with given name is present
     */
    public boolean hasMethod(String name) {
        return methods.stream()
                .anyMatch(method -> method.getName().equals(name));
    }

    /**
     * Checks if this unit has any declared method matching given {@code predicate}.
     *
     * @param predicate to match method with
     * @return true if a declared method matches given predicate
     */
    public boolean hasMethodMatching(Predicate<Method> predicate) {
        Objects.requireNonNull(predicate);

        return methods.stream()
                .anyMatch(predicate);
    }

    /**
     * Finds methods declared by this unit which matches given {@code predicate}.
     *
     * @param predicate to match methods with
     * @return view of declared methods matching given predicate
     */
    public List<Method> findMethodsMatching(Predicate<Method> predicate) {
        Objects.requireNonNull(predicate);

        return methods.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return origin.getSimpleName();
    }

    @Override
    public Class<?> getType() {
        return origin;
    }

    @Override
    public Modifier modifier() {
        return modifier;
    }

    @Override
    public boolean isSynthetic() {
        return origin.isSynthetic();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Unit unit = (Unit) object;
        return Objects.equals(origin, unit.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin);
    }

    @Override
    public String toString() {
        return "Unit{" +
                "origin=" + origin +
                ", modifier=" + modifier +
                ", fields=" + fields +
                ", constructors=" + constructors +
                ", methods=" + methods +
                '}';
    }
}
