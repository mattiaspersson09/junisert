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

import io.github.mattiaspersson09.junisert.testunits.constructor.ArgConstructor;

import java.util.Objects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UnitIntegrationTest {
    @Test
    void givenFields_whenFieldsExist_thenHasFieldInformation() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit.hasNoFields()).isFalse();
        assertThat(unit.getFields()).filteredOn(field -> !field.isSynthetic()).hasSize(3);
        assertThat(unit.hasField("CONSTANT")).isTrue();
        assertThat(unit.hasField("field")).isTrue();
        assertThat(unit.hasField("finalField")).isTrue();
        assertThat(unit.hasFieldMatching(Field::isClassConstant)).isTrue();
        assertThat(unit.hasFieldMatching(Field::isInstanceMember)).isTrue();
        assertThat(unit.hasFieldMatching(Field::isImmutable)).isTrue();
        assertThat(unit.hasFieldMatching(Field::isInstanceImmutable)).isTrue();
        assertThat(unit.findFieldsMatching(Field::isClassConstant)).hasSize(1);
        assertThat(unit.findFieldsMatching(Field::isInstanceImmutable)).hasSize(1);
        assertThat(unit.findFieldsMatching(Field::isInstanceMember)).hasSize(2);
    }

    @Test
    void givenFields_whenNoFieldExist_thenHasNoFieldInformation() {
        Unit unit = Unit.of(EmptyUnit.class);

        assertThat(unit.getFields()).filteredOn(field -> !field.isSynthetic()).isEmpty();
        assertThat(unit.findFieldsMatching(Field::isInstanceMember)).isEmpty();
        assertThat(unit.findFieldsMatching(Field::isClassConstant)).isEmpty();
    }

    @Test
    void givenConstructors_whenConstructorsExist_thenHasConstructorInformation() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit.getConstructors()).hasSize(2);
        assertThat(unit.hasNoDefaultConstructor()).isFalse();
        assertThat(Unit.of(ArgConstructor.class).hasNoDefaultConstructor()).isTrue();
        assertThat(unit.hasDefaultConstructor()).isTrue();
        assertThat(unit.hasArgumentConstructor()).isTrue();
        assertThat(unit.hasConstructorMatching(Constructor::isDefault)).isTrue();
        assertThat(unit.hasConstructorMatching(Constructor::hasNoParameters)).isTrue();
        assertThat(unit.hasConstructorMatching(Constructor::hasParameters)).isTrue();
        assertThat(unit.findConstructorsMatching(Constructor::isDefault)).hasSize(1);
        assertThat(unit.findConstructorsMatching(Constructor::hasParameters)).hasSize(1);
    }

    @Test
    void givenConstructors_whenNoConstructorExist_thenHasOnlyAutomaticDefaultConstructor() {
        Unit unit = Unit.of(EmptyUnit.class);

        assertThat(unit.getConstructors()).hasSize(1);
        assertThat(unit.hasNoDefaultConstructor()).isFalse();
        assertThat(unit.hasDefaultConstructor()).isTrue();
        assertThat(unit.hasArgumentConstructor()).isFalse();
        assertThat(unit.hasConstructorMatching(Constructor::isDefault)).isTrue();
        assertThat(unit.hasConstructorMatching(Constructor::hasNoParameters)).isTrue();
        assertThat(unit.hasConstructorMatching(Constructor::hasParameters)).isFalse();
        assertThat(unit.findConstructorsMatching(Constructor::isDefault)).hasSize(1);
        assertThat(unit.findConstructorsMatching(Constructor::hasParameters)).isEmpty();
    }

    @Test
    void givenMethods_whenMethodsExist_thenHasMethodInformation() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit.getMethods()).filteredOn(method -> !method.isSynthetic()).hasSize(6);
        assertThat(unit.hasNoMethods()).isFalse();
        assertThat(unit.hasMethod("getField")).isTrue();
        assertThat(unit.hasMethod("setField")).isTrue();
        assertThat(unit.hasMethod("getFinalField")).isTrue();
        assertThat(unit.hasMethod("equals")).isTrue();
        assertThat(unit.hasMethod("hashCode")).isTrue();
        assertThat(unit.hasMethod("toString")).isTrue();
        assertThat(unit.hasMethodMatching(Method::hasParameters)).isTrue();
        assertThat(unit.hasMethodMatching(Method::hasNoParameters)).isTrue();
        assertThat(unit.findMethodsMatching(Method::isInstanceMember)).hasSize(6);
    }

    @Test
    void givenMethods_whenNoMethodExist_thenHasNoMethodInformation() {
        Unit unit = Unit.of(EmptyUnit.class);

        assertThat(unit.getMethods()).filteredOn(method -> !method.isSynthetic()).hasSize(0);
        assertThat(unit.findMethodsMatching(Method::isInstanceMember)).hasSize(0);
    }

    @Test
    void givenMethods_whenUnitDoesNotOverrideObjectMethods_thenNotDeclaringOverridable() {
        Unit unit = Unit.of(ExampleUnitNotOverridingObjectMethods.class);

        assertThat(unit.hasMethod("equals")).isFalse();
        assertThat(unit.hasMethod("hashCode")).isFalse();
        assertThat(unit.hasMethod("toString")).isFalse();
        assertThat(unit.hasMethodMatching(method -> method.getName().equals("equals"))).isFalse();
        assertThat(unit.hasMethodMatching(method -> method.getName().equals("hashCode"))).isFalse();
        assertThat(unit.hasMethodMatching(method -> method.getName().equals("toString"))).isFalse();
        assertThat(unit.findMethodsMatching(method -> method.getName().equals("equals"))).isEmpty();
        assertThat(unit.findMethodsMatching(method -> method.getName().equals("hashCode"))).isEmpty();
        assertThat(unit.findMethodsMatching(method -> method.getName().equals("toString"))).isEmpty();
    }

    @Test
    void whenHaveModifiers_thenHasInformationAboutModifiers() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit.modifier().isPrivate()).isTrue();
        assertThat(unit.modifier().isStatic()).isTrue();
        assertThat(unit.modifier().isFinal()).isTrue();
    }

    @Test
    void whenNonSynthetic_thenIsNotSynthetic() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit.isSynthetic()).isFalse();
    }

    @Test
    void givenTypeInformation_whenConstructed_thenHasInformation() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit.getType()).isEqualTo(ExampleUnit.class);
        assertThat(unit.getName()).isEqualTo(ExampleUnit.class.getSimpleName());
    }

    @Test
    void givenNullOrigin_whenCreating_thenShouldFailFast() {
        assertThatThrownBy(() -> Unit.of(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("unit origin");
    }

    @Test
    void equals_whenIsReference_thenIsEqual() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit).isEqualTo((Object) unit);
    }

    @Test
    void equals_whenIsEqualInstance_thenIsEqual() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit).isEqualTo(new Unit(ExampleUnit.class));
    }

    @Test
    void equals_whenOtherIsNull_thenIsNotEqual() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit).isNotEqualTo(null);
    }

    @Test
    void equals_whenOtherIsDifferentType_thenIsNotEqual() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit).isNotEqualTo(new Unit(ExampleUnitNotOverridingObjectMethods.class));
        assertThat(unit).isNotEqualTo(new Object());
    }

    @Test
    void equals_whenOtherInstanceHasOtherValues_thenIsNotEqual() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit).isNotEqualTo(Unit.of(EmptyUnit.class));
    }

    @Test
    void hashCode_whenIsReference_thenIsEqual() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit.hashCode()).isEqualTo(((Object) unit).hashCode());
    }

    @Test
    void hashCode_whenIsEqualInstance_thenIsEqual() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit.hashCode()).isEqualTo(new Unit(ExampleUnit.class).hashCode());
    }

    @Test
    void hashCode_whenOtherIsNull_thenIsNotEqual() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit.hashCode()).isNotEqualTo(Objects.hashCode(null));
    }

    @Test
    void hashCode_whenOtherIsDifferentType_thenIsNotEqual() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit.hashCode()).isNotEqualTo(new Unit(ExampleUnitNotOverridingObjectMethods.class).hashCode());
    }

    @Test
    void hashCode_whenOtherInstanceHasOtherValues_thenIsNotEqual() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit.hashCode()).isNotEqualTo(Unit.of(EmptyUnit.class).hashCode());
    }

    @Test
    void toStringIsImplemented() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit.toString())
                .contains("ExampleUnit")
                .contains("field")
                .contains("getField")
                .contains("setField")
                .contains("equals")
                .contains("hashCode")
                .contains("toString");
    }

    private static final class ExampleUnit {
        private static final Object CONSTANT = new Object();
        private Object field;
        private final Object finalField;

        public ExampleUnit() {
            finalField = null;
        }

        public ExampleUnit(Object field) {
            this.field = field;
            this.finalField = field;
        }

        public Object getField() {
            return field;
        }

        public void setField(Object field) {
            this.field = field;
        }

        public Object getFinalField() {
            return finalField;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            ExampleUnit that = (ExampleUnit) object;
            return Objects.equals(field, that.field);
        }

        @Override
        public int hashCode() {
            return Objects.hash(field);
        }

        @Override
        public String toString() {
            return "ExampleUnit{" +
                    "field=" + field +
                    '}';
        }
    }

    private static class ExampleUnitNotOverridingObjectMethods {
        private static final Object CONSTANT = new Object();
        private Object field;

        public Object getField() {
            return field;
        }

        public void setField(Object field) {
            this.field = field;
        }
    }

    private static class EmptyUnit {
    }
}
