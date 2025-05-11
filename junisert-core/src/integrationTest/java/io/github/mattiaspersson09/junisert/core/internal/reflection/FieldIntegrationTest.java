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

import io.github.mattiaspersson09.junisert.testunits.field.NotAccessible;
import io.github.mattiaspersson09.junisert.testunits.field.ValueFields;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FieldIntegrationTest {
    @Test
    void getType_returnsReflectedType() throws NoSuchFieldException {
        Field privateField = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(privateField.getType()).isEqualTo(String.class);
    }

    @Test
    void setValue_whenParentInstance_thenSetsValue() throws NoSuchFieldException, NoSuchMethodException,
                                                     InvocationTargetException, InstantiationException,
                                                     IllegalAccessException {
        Object instance = ValueFields.class.getConstructor().newInstance();
        Field privateField = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        privateField.setValue(instance, "value");

        assertThat(privateField.getValue(instance)).isEqualTo("value");
    }

    @Test
    void setValue_whenNotParentInstance_thenThrowsReflectionException() throws NoSuchFieldException {
        Field privateField = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThatThrownBy(() -> privateField.setValue(null, "value"))
                .isInstanceOf(ReflectionException.class);
        assertThatThrownBy(() -> privateField.setValue(new Object(), "value"))
                .isInstanceOf(ReflectionException.class);
    }

    @Test
    void getValue_whenParentInstance_andFieldHasValue_thenReturnsValue() throws NoSuchMethodException,
                                                                         InvocationTargetException,
                                                                         InstantiationException,
                                                                         IllegalAccessException,
                                                                         NoSuchFieldException {
        Object instance = ValueFields.class.getConstructor().newInstance();
        Field field = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(field.getValue(instance)).isEqualTo("value");
    }

    @Test
    void getValue_whenNotParentInstance_thenThrowsReflectionException() throws NoSuchFieldException {
        Field field = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThatThrownBy(() -> field.getValue(null)).isInstanceOf(ReflectionException.class);
        assertThatThrownBy(() -> field.getValue(new Object())).isInstanceOf(ReflectionException.class);
    }

    @Test
    void setValue_whenFieldIsNotAccessible_thenForcesAccess_andSetsValue() throws NoSuchFieldException,
                                                                           NoSuchMethodException,
                                                                           InvocationTargetException,
                                                                           InstantiationException,
                                                                           IllegalAccessException {
        Object instance = NotAccessible.class.getConstructor().newInstance();

        Field privateField = Field.of(NotAccessible.class.getDeclaredField("privateField"));
        Field packageField = Field.of(NotAccessible.class.getDeclaredField("packageField"));
        Field immutableField = Field.of(NotAccessible.class.getDeclaredField("immutableField"));

        privateField.setValue(instance, null);
        packageField.setValue(instance, null);
        immutableField.setValue(instance, null);

        assertThat(privateField.getValue(instance)).isNull();
        assertThat(packageField.getValue(instance)).isNull();
        assertThat(immutableField.getValue(instance)).isNull();
    }

    @Test
    void getValue_whenFieldIsNotAccessible_thenForcesAccess_andGetsValue() throws NoSuchFieldException,
                                                                           NoSuchMethodException,
                                                                           InvocationTargetException,
                                                                           InstantiationException,
                                                                           IllegalAccessException {
        Object instance = NotAccessible.class.getConstructor().newInstance();

        Field privateField = Field.of(NotAccessible.class.getDeclaredField("privateField"));
        Field packageField = Field.of(NotAccessible.class.getDeclaredField("packageField"));
        Field immutableField = Field.of(NotAccessible.class.getDeclaredField("immutableField"));

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
        Field privateField = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        privateField.setValue(instance, "value");

        assertThat(privateField.getValueOrElse(instance, "fallback")).isEqualTo("value");
    }

    @Test
    void getValueOrElse_whenAccessIsNotPossible_thenGetsFallback() throws NoSuchMethodException,
                                                                   InvocationTargetException,
                                                                   InstantiationException,
                                                                   IllegalAccessException, NoSuchFieldException {
        Object instance = ValueFields.class.getConstructor().newInstance();
        Field privateField = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        privateField.setValue(instance, "value");

        assertThat(privateField.getValueOrElse(new Object(), "fallback")).isEqualTo("fallback");
    }

    @Test
    void accepts_returnsReflectedType() throws NoSuchFieldException {
        Field field = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

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
        Field field = Field.of(ValueFields.class.getDeclaredField("arrayField"));

        assertThat(field.invoke(instance, "updated", "value")).isEqualTo(new Object[]{"updated", "value"});
    }

    @Test
    void invoke_whenArrayField_andInvokingWithSingleValue_thenReturnsFieldArray() throws NoSuchFieldException,
                                                                                  NoSuchMethodException,
                                                                                  InvocationTargetException,
                                                                                  InstantiationException,
                                                                                  IllegalAccessException {
        Object instance = ValueFields.class.getDeclaredConstructor().newInstance();
        Field field = Field.of(ValueFields.class.getDeclaredField("arrayField"));

        assertThat(field.invoke(instance, "updated value")).isEqualTo(new Object[]{"updated value"});
    }

    @Test
    void invoke_whenSingleObjectField_andInvokingWithSingleValue_thenReturnsUpdatedValue() throws NoSuchMethodException,
                                                                                           InvocationTargetException,
                                                                                           InstantiationException,
                                                                                           IllegalAccessException,
                                                                                           NoSuchFieldException {
        Object instance = ValueFields.class.getConstructor().newInstance();
        Field field = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(field.invoke(instance, "updated value")).isEqualTo("updated value");
    }

    @Test
    void invoke_whenSingleObjectField_andInvokingWithNull_thenReturnsUpdatedValue() throws NoSuchMethodException,
                                                                                    InvocationTargetException,
                                                                                    InstantiationException,
                                                                                    IllegalAccessException,
                                                                                    NoSuchFieldException {
        Object instance = ValueFields.class.getConstructor().newInstance();
        Field field = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(field.invoke(instance, (Object) null)).isNull();
        assertThat(field.invoke(instance, new Object[]{null})).isNull();
    }

    @Test
    void invoke_whenSingleObjectField_andInvokingWithArrayValues_thenThrowsReflectionException() throws NoSuchMethodException,
                                                                                                 InvocationTargetException,
                                                                                                 InstantiationException,
                                                                                                 IllegalAccessException,
                                                                                                 NoSuchFieldException {
        Object instance = ValueFields.class.getConstructor().newInstance();
        Field field = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThatThrownBy(() -> field.invoke(instance, "updated", "value"))
                .isInstanceOf(ReflectionException.class)
                .cause()
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("only accepts a single value");
    }

    @Test
    void invoke_whenUnknownInstance_thenThrowsUncheckedException() throws NoSuchFieldException {
        Field field = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThatThrownBy(() -> field.invoke(null, (Object) null)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> field.invoke(new Object(), (Object) null)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void isImmutable_whenFieldHasFinalModifier_thenIsImmutable() throws NoSuchFieldException {
        Field publicConstant = Field.of(ValueFields.class.getDeclaredField("PUBLIC_CONSTANT"));
        Field privateConstant = Field.of(ValueFields.class.getDeclaredField("PRIVATE_CONSTANT"));
        Field immutableInstanceField = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(publicConstant.modifier().isFinal()).isTrue();
        assertThat(privateConstant.modifier().isFinal()).isTrue();
        assertThat(immutableInstanceField.modifier().isFinal()).isTrue();

        assertThat(publicConstant.isImmutable()).isTrue();
        assertThat(privateConstant.isImmutable()).isTrue();
        assertThat(immutableInstanceField.isImmutable()).isTrue();
    }

    @Test
    void isImmutable_whenFieldDoesNotHaveFinalModifier_thenIsNotImmutable() throws NoSuchFieldException {
        Field privateField = Field.of(ValueFields.class.getDeclaredField("privateObjectField"));
        Field staticField = Field.of(ValueFields.class.getDeclaredField("privateStaticObjectField"));

        assertThat(privateField.modifier().isFinal()).isFalse();
        assertThat(staticField.modifier().isFinal()).isFalse();

        assertThat(privateField.isImmutable()).isFalse();
        assertThat(staticField.isImmutable()).isFalse();
    }

    @Test
    void isClassConstant_whenIsNotInstanceField_andHasFinalModifier_thenIsClassConstant() throws NoSuchFieldException {
        Field publicConstant = Field.of(ValueFields.class.getDeclaredField("PUBLIC_CONSTANT"));
        Field privateConstant = Field.of(ValueFields.class.getDeclaredField("PRIVATE_CONSTANT"));

        assertThat(publicConstant.isInstanceMember()).isFalse();
        assertThat(privateConstant.isInstanceMember()).isFalse();
        assertThat(publicConstant.modifier().isFinal()).isTrue();
        assertThat(privateConstant.modifier().isFinal()).isTrue();

        assertThat(publicConstant.isClassConstant()).isTrue();
        assertThat(privateConstant.isClassConstant()).isTrue();
    }

    @Test
    void isClassConstant_whenIsNotInstanceField_butDoesNotHaveFinalModifier_thenIsNotClassConstant() throws NoSuchFieldException {
        Field privateStatic = Field.of(ValueFields.class.getDeclaredField("privateStaticObjectField"));

        assertThat(privateStatic.isInstanceMember()).isFalse();
        assertThat(privateStatic.modifier().isFinal()).isFalse();
        assertThat(privateStatic.isClassConstant()).isFalse();
    }

    @Test
    void isClassConstant_whenInstanceField_thenIsNotClassConstant() throws NoSuchFieldException {
        Field instanceField = Field.of(ValueFields.class.getDeclaredField("privateObjectField"));

        assertThat(instanceField.isInstanceMember()).isTrue();
        assertThat(instanceField.isClassConstant()).isFalse();
    }

    @Test
    void isInstanceImmutable_whenFieldHasFinalModifier_andNotStaticModifier_thenIsInstanceImmutable() throws NoSuchFieldException {
        Field immutable = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(immutable.modifier().isFinal()).isTrue();
        assertThat(immutable.modifier().isStatic()).isFalse();

        assertThat(immutable.isInstanceImmutable()).isTrue();
    }

    @Test
    void isInstanceImmutable_whenFieldHasStaticModifier_thenIsNotInstanceImmutable() throws NoSuchFieldException {
        Field constant = Field.of(ValueFields.class.getDeclaredField("PUBLIC_CONSTANT"));
        Field staticField = Field.of(ValueFields.class.getDeclaredField("privateStaticObjectField"));

        assertThat(constant.modifier().isStatic()).isTrue();
        assertThat(staticField.modifier().isStatic()).isTrue();

        assertThat(constant.isInstanceImmutable()).isFalse();
        assertThat(staticField.isInstanceImmutable()).isFalse();
    }

    @Test
    void isInstanceImmutable_whenFieldDoesNotHaveFinalModifier_thenIsNotInstanceImmutable() throws NoSuchFieldException {
        Field nonFinal = Field.of(ValueFields.class.getDeclaredField("privateObjectField"));

        assertThat(nonFinal.modifier().isFinal()).isFalse();
        assertThat(nonFinal.isInstanceImmutable()).isFalse();
    }

    @Test
    void getUnit() throws NoSuchFieldException {
        Field field = Field.of(ValueFields.class.getDeclaredField("privateObjectField"));

        assertThat(field.getParent()).isEqualTo(ValueFields.class);
    }

    @Test
    void equalsTest() throws NoSuchFieldException {
        Field field = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(field).isEqualTo((Object) field);
        assertThat(field).isEqualTo(Field.of(ValueFields.class.getDeclaredField("immutableStringValueField")));

        assertThat(field).isNotEqualTo(null);
        assertThat(field).isNotEqualTo(Field.of(ValueFields.class.getDeclaredField("arrayField")));
        assertThat(field).isNotEqualTo(new Object());
    }

    @Test
    void hashCodeTest() throws NoSuchFieldException {
        Field field = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(field.hashCode()).isEqualTo(((Object) field).hashCode());
        assertThat(field.hashCode())
                .isEqualTo(Field.of(ValueFields.class.getDeclaredField("immutableStringValueField")).hashCode());

        assertThat(field.hashCode()).isNotEqualTo(Objects.hashCode(null));
        assertThat(field.hashCode())
                .isNotEqualTo(Field.of(ValueFields.class.getDeclaredField("arrayField")).hashCode());
        assertThat(field.hashCode()).isNotEqualTo(new Object().hashCode());
    }

    @Test
    void toStringTest() throws NoSuchFieldException {
        Field field = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(field.toString()).isEqualTo("ValueFields.immutableStringValueField(class java.lang.String)");
    }
}
