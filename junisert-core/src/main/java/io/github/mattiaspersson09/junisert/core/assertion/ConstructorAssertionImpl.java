/*
 * Copyright (c) 2026 Mattias Persson
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
package io.github.mattiaspersson09.junisert.core.assertion;

import io.github.mattiaspersson09.junisert.api.assertion.ConstructorAssertion;
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertion;
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.common.reflection.Constructor;
import io.github.mattiaspersson09.junisert.common.reflection.Field;
import io.github.mattiaspersson09.junisert.core.internal.ValueService;
import io.github.mattiaspersson09.junisert.value.common.PrimitiveValueGenerator;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Direct implementation of {@link ConstructorAssertion} API.
 *
 * @param <T> unit type owning the constructor under assertion
 */
class ConstructorAssertionImpl<T> extends AbstractAssertion<ConstructorAssertion<T>> implements ConstructorAssertion<T> {
    private static final Logger LOGGER = Logger.getLogger("Constructor Assertion");

    private final Constructor constructor;

    /**
     * Creates a new implementation of {@link ConstructorAssertion}.
     *
     * @param constructor       under assertion
     * @param assertionResource needed for assertions
     */
    ConstructorAssertionImpl(Constructor constructor, AssertionResource assertionResource) {
        super(assertionResource);
        this.constructor = constructor;
    }

    /**
     * Gets {@link Constructor} being asserted on.
     *
     * @return constructor under assertion
     */
    Constructor getConstructor() {
        return constructor;
    }

    @Override
    public ConstructorAssertionImpl<T> hasAssignedProperty(String propertyName) {
        T instance = getInstance();
        Field field = getUnitField(propertyName);

        LOGGER.test("Checking that property: {0} - has a value other than initial default value", field);

        if (Objects.equals(getInitialDefaultValueFor(field), field.getValue(instance))) {
            throw new UnitAssertionError(String.format("\nExpected constructor: %s\nTo assign value to property: %s",
                    constructor, propertyName));
        }

        return this;
    }

    @Override
    public ConstructorAssertionImpl<T> hasAssignedProperties(String... propertyNames) {
        T instance = getInstance();
        List<Field> fields = Stream.of(propertyNames)
                .map(this::getUnitField)
                .collect(Collectors.toList());

        LOGGER.test("Checking that properties: {0} - has a value other than initial default value", fields);

        List<Field> unassignedProperties = fields.stream()
                .filter(field -> Objects.equals(getInitialDefaultValueFor(field), field.getValue(instance)))
                .collect(Collectors.toList());

        if (!unassignedProperties.isEmpty()) {
            throw new UnitAssertionError(String.format("There were unassigned properties\nExpected constructor: %s"
                    + "\nTo assign value to properties: %s", constructor, unassignedProperties));
        }

        return this;
    }

    @Override
    public ConstructorAssertionImpl<T> hasAssignedAllProperties() {
        T instance = getInstance();
        List<Field> unassignedProperties = getUnit().findFieldsMatching(Field::isInstanceMember)
                .stream()
                .filter(field -> Objects.equals(getInitialDefaultValueFor(field), field.getValue(instance)))
                .collect(Collectors.toList());

        LOGGER.test("Checking that every property has a value other than initial default value");

        if (!unassignedProperties.isEmpty()) {
            throw new UnitAssertionError(String.format("There were unassigned properties\nExpected constructor: %s"
                    + "\nTo assign value to properties: %s", constructor, unassignedProperties));
        }

        return this;
    }

    @Override
    public ConstructorAssertionImpl<T> hasState(Predicate<T> stateValidation) {
        Objects.requireNonNull(stateValidation);
        T instance = getInstance();

        LOGGER.test("Validating unit state");

        if (!stateValidation.test(instance)) {
            throw new UnitAssertionError(String.format("%s has wrong state\nExpected unit after constructor: %s"
                    + "\nTo have a certain state, which was not true", getUnit().getName(), constructor));
        }

        return this;
    }

    @Override
    public ConstructorAssertionImpl<T> hasState(Predicate<T> stateValidation, String description) {
        Objects.requireNonNull(stateValidation);
        T instance = getInstance();

        LOGGER.test("Validating unit state: {0}", description);

        if (!stateValidation.test(instance)) {
            throw new UnitAssertionError(String.format("%s has wrong state\nExpected unit after constructor: %s"
                    + "\nTo have a state described as: %s", getUnit().getName(), constructor, description));
        }

        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public UnitAssertion<T> assertThatUnit() {
        return new UnitAssertionImpl<>((Class<T>) getUnit().getType(), getAssertionResource());
    }

    private Field getUnitField(String propertyName) {
        return getUnit().findFieldsMatching(f -> f.getName().equals(propertyName))
                .stream()
                .findFirst()
                .orElseThrow(() -> new UnitAssertionError("A property with name '" + propertyName + "'"
                        + " could not be found, is the name correct?"));
    }

    @SuppressWarnings("unchecked")
    private T getInstance() {
        ValueService valueService = getValueService();

        Object[] arguments = constructor.getParameterTypes()
                .stream()
                .map(valueService::getValue)
                .map(Value::get)
                .toArray();

        return (T) constructor.invoke(null, arguments);
    }

    private Object getInitialDefaultValueFor(Field field) {
        return getInitialDefaultValueFor(field.getType());
    }

    /**
     * Default initial value are as defined in Java Language Specification > 4.12.5. Initial Values of Variables:
     * <a href="https://docs.oracle.com/javase/specs/jls/se25/html/jls-4.html#jls-4.12.5">JLS section 4.12.5</a>
     */
    private Object getInitialDefaultValueFor(Class<?> type) {
        if (type.isPrimitive()) {
            return new PrimitiveValueGenerator()
                    .generate(type)
                    .asEmpty();
        }

        // Reference type
        return null;
    }
}
