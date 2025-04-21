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

import java.util.Objects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UnitTest {
    @Test
    void whenCreatingUsingConstructor_thenCreatesEmptyUnit_andHasBasicInformation() {
        Unit unit = new Unit(ExampleUnit.class);

        assertThat(unit.getType()).isEqualTo(ExampleUnit.class);
        assertThat(unit.getName()).isEqualTo(ExampleUnit.class.getSimpleName());
        assertThat(unit.isSynthetic()).isFalse();
        assertThat(unit.modifier().isPrivate()).isTrue();
        assertThat(unit.modifier().isStatic()).isTrue();
        assertThat(unit.modifier().isFinal()).isTrue();
        assertThat(unit.getConstructors()).isEmpty();
        assertThat(unit.getFields()).isEmpty();
        assertThat(unit.getMethods()).isEmpty();
        assertThat(unit.hasField("field")).isFalse();
        assertThat(unit.hasField("CONSTANT")).isFalse();
        assertThat(unit.hasDefaultConstructor()).isFalse();
        assertThat(unit.hasArgumentConstructor()).isFalse();
        assertThat(unit.hasMethod("getField")).isFalse();
        assertThat(unit.hasMethod("setField")).isFalse();
        assertThat(unit.hasMethod("equals")).isFalse();
        assertThat(unit.hasMethod("hashCode")).isFalse();
        assertThat(unit.hasMethod("toString")).isFalse();
        assertThat(unit.createInstance()).isEmpty();
    }

    @Test
    void whenCreatingUsingCreatorMethod_thenCreatesFullyFunctionalUnit() {
        Unit unit = Unit.of(ExampleUnit.class);

        assertThat(unit.getType()).isEqualTo(ExampleUnit.class);
        assertThat(unit.getName()).isEqualTo(ExampleUnit.class.getSimpleName());
        assertThat(unit.isSynthetic()).isFalse();
        assertThat(unit.modifier().isPrivate()).isTrue();
        assertThat(unit.modifier().isStatic()).isTrue();
        assertThat(unit.modifier().isFinal()).isTrue();
        assertThat(unit.getConstructors())
                .filteredOn(member -> !member.isSynthetic())
                .hasSize(2)
                .anyMatch(constructor -> constructor.getParameters().isEmpty())
                .anyMatch(constructor -> !constructor.getParameters().isEmpty() && constructor.getParameters()
                        .get(0)
                        .getType()
                        .equals(Object.class));
        assertThat(unit.getFields())
                .filteredOn(member -> !member.isSynthetic())
                .hasSize(2)
                .anyMatch(field -> field.getName().equals("field") && field.getType().equals(Object.class))
                .anyMatch(field -> field.getName().equals("CONSTANT") && field.getType().equals(Object.class));
        assertThat(unit.getMethods())
                .filteredOn(member -> !member.isSynthetic())
                .hasSize(5)
                .anyMatch(method -> method.getName().equals("getField"))
                .anyMatch(method -> method.getName().equals("setField"))
                .anyMatch(method -> method.getName().equals("equals"))
                .anyMatch(method -> method.getName().equals("hashCode"))
                .anyMatch(method -> method.getName().equals("toString"));
        assertThat(unit.hasField("field")).isTrue();
        assertThat(unit.hasField("CONSTANT")).isTrue();
        assertThat(unit.hasDefaultConstructor()).isTrue();
        assertThat(unit.hasArgumentConstructor()).isTrue();
        assertThat(unit.hasMethod("getField")).isTrue();
        assertThat(unit.hasMethod("setField")).isTrue();
        assertThat(unit.hasMethod("equals")).isTrue();
        assertThat(unit.hasMethod("hashCode")).isTrue();
        assertThat(unit.hasMethod("toString")).isTrue();
        assertThat(unit.createInstance()).isPresent()
                .get()
                .isInstanceOf(ExampleUnit.class);
    }

    @Test
    void whenCreatingUsingCreatorMethod_andUnitDoesNotOverrideObjectMethods_thenNotDeclaringOverridable() {
        Unit unit = Unit.of(ExampleUnitNotOverridingObjectMethods.class);

        assertThat(unit.hasMethod("equals")).isFalse();
        assertThat(unit.hasMethod("hashCode")).isFalse();
        assertThat(unit.hasMethod("toString")).isFalse();
    }

    @Test
    void givenNullOrigin_whenCreating_thenShouldFailFast() {
        assertThatThrownBy(() -> new Unit(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("unit origin");
        assertThatThrownBy(() -> Unit.of(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("unit origin");
    }

    @Test
    void equals_whenIsReference_thenIsEqual() {
        Unit unit = new Unit(ExampleUnit.class);

        assertThat(unit).isEqualTo((Object) unit);
    }

    @Test
    void equals_whenIsEqualInstance_thenIsEqual() {
        Unit unit = new Unit(ExampleUnit.class);

        assertThat(unit).isEqualTo(new Unit(ExampleUnit.class));
    }

    @Test
    void equals_whenOtherIsNull_thenIsNotEqual() {
        Unit unit = new Unit(ExampleUnit.class);

        assertThat(unit).isNotEqualTo(null);
    }

    @Test
    void equals_whenOtherIsDifferentType_thenIsNotEqual() {
        Unit unit = new Unit(ExampleUnit.class);

        assertThat(unit).isNotEqualTo(new Unit(ExampleUnitNotOverridingObjectMethods.class));
        assertThat(unit).isNotEqualTo(new Object());
    }

    @Test
    void equals_whenOtherInstanceHasOtherValues_thenIsNotEqual() {
        Unit unit = new Unit(ExampleUnit.class);

        assertThat(unit).isNotEqualTo(Unit.of(ExampleUnit.class));
    }

    @Test
    void hashCode_whenIsReference_thenIsEqual() {
        Unit unit = new Unit(ExampleUnit.class);

        assertThat(unit.hashCode()).isEqualTo(((Object) unit).hashCode());
    }

    @Test
    void hashCode_whenIsEqualInstance_thenIsEqual() {
        Unit unit = new Unit(ExampleUnit.class);

        assertThat(unit.hashCode()).isEqualTo(new Unit(ExampleUnit.class).hashCode());
    }

    @Test
    void hashCode_whenOtherIsNull_thenIsNotEqual() {
        Unit unit = new Unit(ExampleUnit.class);

        assertThat(unit.hashCode()).isNotEqualTo(Objects.hashCode(null));
    }

    @Test
    void hashCode_whenOtherIsDifferentType_thenIsNotEqual() {
        Unit unit = new Unit(ExampleUnit.class);

        assertThat(unit.hashCode()).isNotEqualTo(new Unit(ExampleUnitNotOverridingObjectMethods.class).hashCode());
    }

    @Test
    void hashCode_whenOtherInstanceHasOtherValues_thenIsNotEqual() {
        Unit unit = new Unit(ExampleUnit.class);

        assertThat(unit.hashCode()).isNotEqualTo(Unit.of(ExampleUnit.class).hashCode());
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

        public ExampleUnit() {
        }

        public ExampleUnit(Object field) {
            this.field = field;
        }

        public Object getField() {
            return field;
        }

        public void setField(Object field) {
            this.field = field;
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
}
