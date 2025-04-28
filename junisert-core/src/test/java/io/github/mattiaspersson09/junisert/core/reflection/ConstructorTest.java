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

import io.github.mattiaspersson09.junisert.testunits.constructor.ArgConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPackageConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPrivateConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPublicConstructor;

import java.util.Objects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConstructorTest {
    @Test
    void whenConstructing_thenIsSettingParameters() throws NoSuchMethodException {
        Constructor publicConstructor = new Constructor(DefaultPublicConstructor.class.getDeclaredConstructor());
        Constructor packageConstructor = new Constructor(DefaultPackageConstructor.class.getDeclaredConstructor());
        Constructor privateConstructor = new Constructor(DefaultPrivateConstructor.class.getDeclaredConstructor());
        Constructor argConstructor = new Constructor(ArgConstructor.class.getDeclaredConstructor(Object.class));

        assertThat(publicConstructor.getParameters()).isEmpty();
        assertThat(packageConstructor.getParameters()).isEmpty();
        assertThat(privateConstructor.getParameters()).isEmpty();
        assertThat(argConstructor.getParameters())
                .hasSize(1)
                .allMatch(parameter -> parameter.getType().equals(Object.class));
    }

    @Test
    void isDefault_whenIsDefaultConstructor_thenIsTrue() throws NoSuchMethodException {
        Constructor publicConstructor = new Constructor(DefaultPublicConstructor.class.getDeclaredConstructor());
        Constructor packageConstructor = new Constructor(DefaultPackageConstructor.class.getDeclaredConstructor());
        Constructor privateConstructor = new Constructor(DefaultPrivateConstructor.class.getDeclaredConstructor());

        assertThat(publicConstructor.isDefault()).isTrue();
        assertThat(packageConstructor.isDefault()).isTrue();
        assertThat(privateConstructor.isDefault()).isTrue();
    }

    @Test
    void isDefault_whenIsNotDefaultConstructor_thenIsFalse() throws NoSuchMethodException {
        Constructor argConstructor = new Constructor(ArgConstructor.class.getDeclaredConstructor(Object.class));

        assertThat(argConstructor.isDefault()).isFalse();
    }

    @Test
    void getType_shouldReturnConstructorType() throws NoSuchMethodException {
        Constructor publicConstructor = new Constructor(DefaultPublicConstructor.class.getDeclaredConstructor());

        assertThat(publicConstructor.getType()).isEqualTo(java.lang.reflect.Constructor.class);
    }

    @Test
    void equalsTest() throws NoSuchMethodException {
        Constructor publicConstructor = new Constructor(DefaultPublicConstructor.class.getDeclaredConstructor());
        Constructor argConstructor = new Constructor(ArgConstructor.class.getDeclaredConstructor(Object.class));

        assertThat(publicConstructor).isEqualTo((Object) publicConstructor);
        assertThat(publicConstructor)
                .isEqualTo(new Constructor(DefaultPublicConstructor.class.getDeclaredConstructor()));

        assertThat(publicConstructor).isNotEqualTo(argConstructor);
        assertThat(publicConstructor).isNotEqualTo(new Object());
        assertThat(publicConstructor).isNotEqualTo(null);
    }

    @Test
    void hashCodeTest() throws NoSuchMethodException {
        Constructor publicConstructor = new Constructor(DefaultPublicConstructor.class.getDeclaredConstructor());
        Constructor argConstructor = new Constructor(ArgConstructor.class.getDeclaredConstructor(Object.class));

        assertThat(publicConstructor.hashCode()).isEqualTo(((Object) publicConstructor).hashCode());
        assertThat(publicConstructor.hashCode())
                .isEqualTo(new Constructor(DefaultPublicConstructor.class.getDeclaredConstructor()).hashCode());

        assertThat(publicConstructor.hashCode()).isNotEqualTo(argConstructor.hashCode());
        assertThat(publicConstructor.hashCode()).isNotEqualTo(new Object().hashCode());
        assertThat(publicConstructor.hashCode()).isNotEqualTo(Objects.hashCode(null));
    }

    @Test
    void toStringTest() throws NoSuchMethodException {
        Constructor argConstructor = new Constructor(ArgConstructor.class.getDeclaredConstructor(Object.class));

        assertThat(argConstructor.toString())
                .contains("parameters")
                .contains("Object");
    }
}
