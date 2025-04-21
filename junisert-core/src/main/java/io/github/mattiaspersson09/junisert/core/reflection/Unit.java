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

import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public final class Unit implements Reflected {
    private final Class<?> origin;
    private final Modifier modifier;
    private final List<Field> fields;
    private final List<Constructor> constructors;
    private final List<Method> methods;
    private Supplier<Object> instanceSupplier;

    Unit(Class<?> origin) {
        this.origin = Objects.requireNonNull(origin, "unit origin can't be null");
        this.modifier = new Modifier(origin.getModifiers());
        this.fields = new ArrayList<>();
        this.constructors = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    public static Unit of(Class<?> origin) {
        return UnitCreator.createFrom(origin);
    }

    void addField(Field field) {
        fields.add(Objects.requireNonNull(field));
    }

    void addMethod(Method method) {
        methods.add(Objects.requireNonNull(method));
    }

    void addConstructor(Constructor constructor) {
        constructors.add(Objects.requireNonNull(constructor));
    }

    void setInstanceSupplier(Supplier<Object> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    public Optional<Object> createInstance() throws UnsupportedConstructionError {
        return Optional.ofNullable(instanceSupplier)
                .map(Supplier::get);
    }

    public List<Field> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public List<Constructor> getConstructors() {
        return Collections.unmodifiableList(constructors);
    }

    public List<Method> getMethods() {
        return Collections.unmodifiableList(methods);
    }

    public boolean hasField(String name) {
        return fields.stream()
                .anyMatch(field -> field.getName().equals(name));
    }

    public boolean hasDefaultConstructor() {
        return constructors.stream()
                .anyMatch(Constructor::isDefault);
    }

    public boolean hasArgumentConstructor() {
        return constructors.stream()
                .anyMatch(constructor -> !constructor.getParameters().isEmpty());
    }

    public boolean hasMethod(String name) {
        return methods.stream()
                .anyMatch(method -> method.getName().equals(name));
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
        return Objects.equals(origin, unit.origin)
                && Objects.equals(fields, unit.fields)
                && Objects.equals(constructors, unit.constructors)
                && Objects.equals(methods, unit.methods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, fields, constructors, methods);
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
