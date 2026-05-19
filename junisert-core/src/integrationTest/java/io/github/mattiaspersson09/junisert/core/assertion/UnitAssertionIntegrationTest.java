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

import io.github.mattiaspersson09.junisert.api.assertion.Exclusion;
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertion;
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.common.reflection.Constructor;
import io.github.mattiaspersson09.junisert.common.reflection.Field;
import io.github.mattiaspersson09.junisert.common.reflection.Unit;
import io.github.mattiaspersson09.junisert.core.NoCacheTestValueService;
import io.github.mattiaspersson09.junisert.core.TestInstanceCreator;
import io.github.mattiaspersson09.junisert.testunits.constructor.ArgConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.SeveralParameterConstructors;
import io.github.mattiaspersson09.junisert.testunits.constructor.assign.ConstructorAssignsNoProperties;
import io.github.mattiaspersson09.junisert.testunits.constructor.assign.ConstructorAssignsProperty;
import io.github.mattiaspersson09.junisert.testunits.constructor.assign.ConstructorAssignsSomeProperties;
import io.github.mattiaspersson09.junisert.testunits.unit.pojo.ImmutableModel;
import io.github.mattiaspersson09.junisert.testunits.unit.pojo.ImmutableModelMultipleConstructors;
import io.github.mattiaspersson09.junisert.testunits.unit.pojo.SelfConstructorImmutableModel;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UnitAssertionIntegrationTest {

    @Test
    void givenImmutableUnit_thatAssignsAllProperties_whenValidatingExpectedState_thenPassesAssertion() {
        UnitAssertion<ImmutableModel> assertion = createAssertion(ImmutableModel.class);

        assertion.whenCreatedFromConstructor(long.class, String.class, boolean.class)
                .withSupport(String.class, Value.ofEager("validated"))
                .hasAssignedAllProperties()
                .hasAssignedProperty("longField")
                .hasAssignedProperties("stringField", "booleanField")
                .hasState(model -> model.getLongField() > 0, "long property is above zero")
                .hasState(ImmutableModel::isBooleanField)
                .hasState(model -> model.getStringField().equals("validated"),
                        "string property has value from support");

        assertion.whenCreatedFromConstructor(Constructor::hasParameters)
                .withSupport(String.class, Value.ofEager("validated"))
                .hasAssignedAllProperties()
                .hasAssignedProperty("longField")
                .hasAssignedProperties("stringField", "booleanField")
                .hasState(model -> model.getLongField() > 0, "long property is above zero")
                .hasState(ImmutableModel::isBooleanField)
                .hasState(model -> model.getStringField().equals("validated"),
                        "string property has value from support");
    }

    @Test
    void givenImmutableUnitWithMultipleConstructors_thatAssignsProperties_whenValidatingExpectedState_thenPassesAssertion() {
        UnitAssertion<SelfConstructorImmutableModel> assertion = createAssertion(SelfConstructorImmutableModel.class);

        assertion.whenCreatedFromConstructor(Constructor::hasParameters)
                .withSupport(String.class, Value.ofEager("validated"))
                .hasAssignedAllProperties()
                .hasAssignedProperty("longField")
                .hasAssignedProperties("stringField", "booleanField")
                .hasState(unit -> unit.getLongField() > 0, "long above zero")
                .hasState(unit -> unit.getStringField().equals("validated"), "string property has value from support")
                .hasState(SelfConstructorImmutableModel::isBooleanField);
    }

    @Test
    void whenCreatedFromConstructor_givenFilter_whenNoConstructorMatchesFilter_thenFailsAssertion() {
        UnitAssertion<ArgConstructor> assertion = createAssertion(ArgConstructor.class);

        assertThatThrownBy(() -> assertion.whenCreatedFromConstructor(Constructor::isDefault))
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("Expected unit: ArgConstructor")
                .hasMessageContaining("To have any constructor matching given filter");
    }

    @Test
    void whenCreatedFromConstructor_givenFilter_whenOneMatchesFilter_thenCreatesConstructorAssertion() {
        assertThat(createAssertion(ArgConstructor.class)
                .whenCreatedFromConstructor(Constructor::hasParameters))
                .isInstanceOf(ConstructorAssertionImpl.class);
    }

    @Test
    void whenCreatedFromConstructor_givenFilter_whenMultipleMatchesFilter_thenCreatesMultipleConstructorAssertion() {
        assertThat(createAssertion(SeveralParameterConstructors.class)
                .whenCreatedFromConstructor(Constructor::hasParameters))
                .isInstanceOf(MultipleConstructorAssertion.class);
    }

    @Test
    void whenCreatedFromConstructor_givenParameters_whenNoConstructorMatchesParameters_thenFailsAssertion() {
        UnitAssertion<ArgConstructor> assertion = createAssertion(ArgConstructor.class);

        assertThatThrownBy(() -> assertion.whenCreatedFromConstructor(String.class))
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("Expected unit: ArgConstructor")
                .hasMessageContaining("To have a constructor with parameters")
                .hasMessageContaining(String.class.getSimpleName());
    }

    @Test
    void hasAssignedProperty_whenPropertyDoesNotExist_thenFailsAssertion() {
        UnitAssertion<ConstructorAssignsNoProperties> assertion = createAssertion(ConstructorAssignsNoProperties.class);

        assertThatThrownBy(() -> assertion.whenCreatedFromConstructor().hasAssignedProperty("unknownProperty"))
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("property with name 'unknownProperty' could not be found");
    }

    @Test
    void multipleConstructors_hasAssignedProperty_whenPropertyDoesNotExist_thenFailsAssertion() {
        UnitAssertion<SeveralParameterConstructors> assertion = createAssertion(SeveralParameterConstructors.class);

        assertThatThrownBy(() -> assertion.whenCreatedFromConstructor(Constructor::hasParameters)
                .hasAssignedProperty("unknownProperty"))
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("property with name 'unknownProperty' could not be found");
    }

    @Test
    void hasAssignedProperties_whenPropertyDoesNotExist_thenFailsAssertion() {
        UnitAssertion<ConstructorAssignsNoProperties> assertion = createAssertion(ConstructorAssignsNoProperties.class);

        assertThatThrownBy(() -> assertion.whenCreatedFromConstructor()
                .hasAssignedProperties("stringProperty", "unknownProperty", "objectProperty"))
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("property with name 'unknownProperty' could not be found");
    }

    @Test
    void hasAssignedProperty_whenConstructorDidNotAssignProperty_thenFailsAssertion() {
        UnitAssertion<ConstructorAssignsNoProperties> assertion = createAssertion(ConstructorAssignsNoProperties.class);

        assertThatThrownBy(() -> assertion.whenCreatedFromConstructor().hasAssignedProperty("stringProperty"))
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("Expected constructor: ConstructorAssignsNoProperties([])")
                .hasMessageContaining("To assign value to property: stringProperty");
    }

    @Test
    void hasAssignedProperties_whenConstructorDidNotAssignProperty_thenFailsAssertion() {
        UnitAssertion<ConstructorAssignsNoProperties> assertion = createAssertion(ConstructorAssignsNoProperties.class);

        assertThatThrownBy(() -> assertion.whenCreatedFromConstructor()
                .hasAssignedProperties("stringProperty", "objectProperty"))
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("Expected constructor: ConstructorAssignsNoProperties([])")
                .hasMessageContaining("To assign value to properties")
                .hasMessageContaining("stringProperty")
                .hasMessageContaining("objectProperty");
    }

    @Test
    void hasAssignedAllProperties_whenConstructorDidNotAssignCertainProperties_thenFailsAssertion() {
        UnitAssertion<ConstructorAssignsSomeProperties> assertion = createAssertion(
                ConstructorAssignsSomeProperties.class);

        assertThatThrownBy(() -> assertion.whenCreatedFromConstructor().hasAssignedAllProperties())
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("There were unassigned properties")
                .hasMessageContaining("To assign value to properties")
                .hasMessageContaining("objectProperty");
    }

    @Test
    void hasState_givenState_whenConstructedUnitDoesNotHaveState_thenFailsAssertion() {
        UnitAssertion<ConstructorAssignsProperty> assertion = createAssertion(ConstructorAssignsProperty.class);

        assertThatThrownBy(() -> assertion.whenCreatedFromConstructor(String.class)
                .hasState(unit -> unit.getProperty().equals("wrong value")))
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("ConstructorAssignsProperty has wrong state")
                .hasMessageContaining("To have a certain state");

        assertThatThrownBy(() -> assertion.whenCreatedFromConstructor(String.class)
                .hasState(unit -> unit.getProperty().equals("wrong value"), "description"))
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("ConstructorAssignsProperty has wrong state")
                .hasMessageContaining("To have a state described as: description");
    }

    @Test
    void afterConstructorAssertion_givenSingleConstructor_whenAssertThatUnit_thenSwitchesBackToUnitAssertion() {
        UnitAssertion<ImmutableModel> assertion = createAssertion(ImmutableModel.class);

        assertion.isImmutable()
                .whenCreatedFromConstructor(c -> true)
                .hasAssignedAllProperties()
                .assertThatUnit()
                .asPojo()
                .isWellImplemented();
    }

    @Test
    void afterConstructorAssertion_givenMultipleConstructor_whenAssertThatUnit_thenSwitchesBackToUnitAssertion() {
        UnitAssertion<ImmutableModelMultipleConstructors> assertion = createAssertion(
                ImmutableModelMultipleConstructors.class);

        assertion.isImmutable()
                .whenCreatedFromConstructor(c -> true)
                .hasAssignedAllProperties()
                .assertThatUnit()
                .asPojo()
                .isWellImplemented();
    }

    private <T> UnitAssertionImpl<T> createAssertion(Class<T> unit) {
        return new UnitAssertionImpl<>(unit, createAssertionResource(unit));
    }

    private AssertionResource createAssertionResource(Class<?> unit) {
        return new AssertionResource(
                Unit.of(unit),
                new TestInstanceCreator(),
                NoCacheTestValueService.withAllValueGenerators(),
                Exclusion.exclude()
                        .fieldMatching(Field::isSynthetic)
                        .build()
        );
    }
}
