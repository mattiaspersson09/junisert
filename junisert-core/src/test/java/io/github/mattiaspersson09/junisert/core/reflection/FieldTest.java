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

import io.github.mattiaspersson09.junisert.testunits.field.NotAccessible;
import io.github.mattiaspersson09.junisert.testunits.field.ValueFields;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FieldTest {
    @Test
    void getType_returnsReflectedType() throws NoSuchFieldException {
        Field privateField = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(privateField.getType()).isEqualTo(String.class);
    }

    @Test
    void setValue_whenParentInstance_thenIsTrue() throws NoSuchFieldException, NoSuchMethodException,
                                                  InvocationTargetException, InstantiationException,
                                                  IllegalAccessException {
        Object instance = ValueFields.class.getConstructor().newInstance();
        Field privateField = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(privateField.setValue(instance, "value")).isTrue();
    }

    @Test
    void setValue_whenNotParentInstance_thenIsFalse() throws NoSuchFieldException {
        Field privateField = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(privateField.setValue(null, "value")).isFalse();
        assertThat(privateField.setValue(new Object(), "value")).isFalse();
    }

    @Test
    void getValue_whenParentInstance_andFieldHasValue_thenReturnsValue() throws NoSuchMethodException,
                                                                         InvocationTargetException,
                                                                         InstantiationException,
                                                                         IllegalAccessException,
                                                                         NoSuchFieldException {
        Object instance = ValueFields.class.getConstructor().newInstance();
        Field field = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(field.getValue(instance)).isEqualTo("value");
    }

    @Test
    void getValue_whenNotParentInstance_thenThrowsIllegalAccessException() throws NoSuchFieldException {
        Field field = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThatThrownBy(() -> field.getValue(null)).isInstanceOf(IllegalAccessException.class);
        assertThatThrownBy(() -> field.getValue(new Object())).isInstanceOf(IllegalAccessException.class);
    }

    @Test
    void setValue_whenFieldIsNotAccessible_thenForcesAccess_andSetsValue() throws NoSuchFieldException,
                                                                           NoSuchMethodException,
                                                                           InvocationTargetException,
                                                                           InstantiationException,
                                                                           IllegalAccessException {
        Object instance = NotAccessible.class.getConstructor().newInstance();

        Field privateField = new Field(NotAccessible.class.getDeclaredField("privateField"));
        Field packageField = new Field(NotAccessible.class.getDeclaredField("packageField"));
        Field immutableField = new Field(NotAccessible.class.getDeclaredField("immutableField"));

        assertThat(privateField.setValue(instance, null)).isTrue();
        assertThat(packageField.setValue(instance, null)).isTrue();
        assertThat(immutableField.setValue(instance, null)).isTrue();
    }

    @Test
    void getValue_whenFieldIsNotAccessible_thenForcesAccess_andGetsValue() throws NoSuchFieldException,
                                                                           NoSuchMethodException,
                                                                           InvocationTargetException,
                                                                           InstantiationException,
                                                                           IllegalAccessException {
        Object instance = NotAccessible.class.getConstructor().newInstance();

        Field privateField = new Field(NotAccessible.class.getDeclaredField("privateField"));
        Field packageField = new Field(NotAccessible.class.getDeclaredField("packageField"));
        Field immutableField = new Field(NotAccessible.class.getDeclaredField("immutableField"));

        assertThat(privateField.getValue(instance)).isNull();
        assertThat(packageField.getValue(instance)).isNull();
        assertThat(immutableField.getValue(instance)).isNull();
    }

    @Test
    void getValueOrElse_whenAccessIsPossible_thenGetsFieldValue() throws NoSuchMethodException,
                                                                  InvocationTargetException,
                                                                  InstantiationException,
                                                                  IllegalAccessException,
                                                                  NoSuchFieldException {
        Object instance = ValueFields.class.getConstructor().newInstance();
        Field privateField = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        privateField.setValue(instance, "value");

        assertThat(privateField.getValueOrElse(instance, "fallback")).isEqualTo("value");
    }

    @Test
    void getValueOrElse_whenAccessIsNotPossible_thenGetsFallback() throws NoSuchMethodException,
                                                                   InvocationTargetException,
                                                                   InstantiationException,
                                                                   IllegalAccessException, NoSuchFieldException {
        Object instance = ValueFields.class.getConstructor().newInstance();
        Field privateField = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        privateField.setValue(instance, "value");

        assertThat(privateField.getValueOrElse(new Object(), "fallback")).isEqualTo("fallback");
    }

    @Test
    void accepts_returnsReflectedType() throws NoSuchFieldException {
        Field field = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(field.accepts())
                .hasSize(1)
                .allMatch(String.class::equals);
    }

    @Test
    void invoke_whenArrayField_andInvokingWithArrayOfValues_thenReturnsFieldArray() throws NoSuchFieldException,
                                                                                    NoSuchMethodException,
                                                                                    InvocationTargetException,
                                                                                    InstantiationException,
                                                                                    IllegalAccessException {
        Object instance = ValueFields.class.getDeclaredConstructor().newInstance();
        Field field = new Field(ValueFields.class.getDeclaredField("arrayField"));

        assertThat(field.invoke(instance, "updated", "value")).isEqualTo(new Object[]{"updated", "value"});
    }

    @Test
    void invoke_whenArrayField_andInvokingWithSingleValue_thenReturnsFieldArray() throws NoSuchFieldException,
                                                                                  NoSuchMethodException,
                                                                                  InvocationTargetException,
                                                                                  InstantiationException,
                                                                                  IllegalAccessException {
        Object instance = ValueFields.class.getDeclaredConstructor().newInstance();
        Field field = new Field(ValueFields.class.getDeclaredField("arrayField"));

        assertThat(field.invoke(instance, "updated value")).isEqualTo(new Object[]{"updated value"});
    }

    @Test
    void invoke_whenSingleObjectField_andInvokingWithSingleValue_thenReturnsUpdatedValue() throws NoSuchMethodException,
                                                                                           InvocationTargetException,
                                                                                           InstantiationException,
                                                                                           IllegalAccessException,
                                                                                           NoSuchFieldException {
        Object instance = ValueFields.class.getConstructor().newInstance();
        Field field = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(field.invoke(instance, "updated value")).isEqualTo("updated value");
    }

    @Test
    void invoke_whenSingleObjectField_andInvokingWithNull_thenReturnsUpdatedValue() throws NoSuchMethodException,
                                                                                    InvocationTargetException,
                                                                                    InstantiationException,
                                                                                    IllegalAccessException,
                                                                                    NoSuchFieldException {
        Object instance = ValueFields.class.getConstructor().newInstance();
        Field field = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(field.invoke(instance, (Object) null)).isNull();
        assertThat(field.invoke(instance, new Object[]{null})).isNull();
    }

    @Test
    void invoke_whenSingleObjectField_andInvokingWithArrayValues_thenThrowsInvocationTargetException() throws NoSuchMethodException,
                                                                                                       InvocationTargetException,
                                                                                                       InstantiationException,
                                                                                                       IllegalAccessException,
                                                                                                       NoSuchFieldException {
        Object instance = ValueFields.class.getConstructor().newInstance();
        Field field = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThatThrownBy(() -> field.invoke(instance, "updated", "value"))
                .isInstanceOf(InvocationTargetException.class)
                .cause()
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("only accepts a single value");
    }

    @Test
    void invoke_whenUnknownInstance_thenThrowsIllegalAccessException() throws NoSuchFieldException {
        Field field = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThatThrownBy(() -> field.invoke(null, (Object) null)).isInstanceOf(IllegalAccessException.class);
        assertThatThrownBy(() -> field.invoke(new Object(), (Object) null)).isInstanceOf(IllegalAccessException.class);
    }

    @Test
    void isInstanceField_whenHasStaticModifier_thenIsNotInstanceField() throws NoSuchFieldException {
        Field publicConstant = new Field(ValueFields.class.getDeclaredField("PUBLIC_CONSTANT"));
        Field privateConstant = new Field(ValueFields.class.getDeclaredField("PRIVATE_CONSTANT"));

        assertThat(publicConstant.isInstanceField()).isFalse();
        assertThat(privateConstant.isInstanceField()).isFalse();
    }

    @Test
    void isInstanceField_whenIsNonStatic_thenIsInstanceField() throws NoSuchFieldException {
        Field immutableField = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(immutableField.isInstanceField()).isTrue();
    }

    @Test
    void equalsTest() throws NoSuchFieldException {
        Field field = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(field).isEqualTo((Object) field);
        assertThat(field).isEqualTo(new Field(ValueFields.class.getDeclaredField("immutableStringValueField")));

        assertThat(field).isNotEqualTo(null);
        assertThat(field).isNotEqualTo(new Field(ValueFields.class.getDeclaredField("arrayField")));
        assertThat(field).isNotEqualTo(new Object());
    }

    @Test
    void hashCodeTest() throws NoSuchFieldException {
        Field field = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(field.hashCode()).isEqualTo(((Object) field).hashCode());
        assertThat(field.hashCode())
                .isEqualTo(new Field(ValueFields.class.getDeclaredField("immutableStringValueField")).hashCode());

        assertThat(field.hashCode()).isNotEqualTo(Objects.hashCode(null));
        assertThat(field.hashCode())
                .isNotEqualTo(new Field(ValueFields.class.getDeclaredField("arrayField")).hashCode());
        assertThat(field.hashCode()).isNotEqualTo(new Object().hashCode());
    }

    @Test
    void toStringTest() throws NoSuchFieldException {
        Field field = new Field(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(field.toString()).contains("immutableStringValueField");
    }
}
