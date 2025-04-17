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

import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldTest {
    @Test
    void setValue_whenFieldIsNotAccessible_thenSetsValue() throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object instance = NotAccessible.class.getConstructor().newInstance();

        Field privateField = new Field(NotAccessible.class.getDeclaredField("privateField"));
        Field packageField = new Field(NotAccessible.class.getDeclaredField("packageField"));
        Field immutableField = new Field(NotAccessible.class.getDeclaredField("immutableField"));

        assertThat(privateField.setValue(instance, null)).isTrue();
        assertThat(packageField.setValue(instance, null)).isTrue();
        assertThat(immutableField.setValue(instance, null)).isTrue();
    }

    @Test
    void getValue_whenFieldIsNotAccessible_thenGetsValue() throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object instance = NotAccessible.class.getConstructor().newInstance();

        Field privateField = new Field(NotAccessible.class.getDeclaredField("privateField"));
        Field packageField = new Field(NotAccessible.class.getDeclaredField("packageField"));
        Field immutableField = new Field(NotAccessible.class.getDeclaredField("immutableField"));

        assertThat(privateField.getValue(instance)).isNull();
        assertThat(packageField.getValue(instance)).isNull();
        assertThat(immutableField.getValue(instance)).isNull();
    }
}
