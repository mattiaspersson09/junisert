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
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultAndArgConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPackageConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPrivateConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultProtectedConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPublicConstructor;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConstructorIntegrationTest {
    @Test
    void whenConstructing_thenIsSettingParameters() throws NoSuchMethodException {
        Constructor publicConstructor = Constructor.of(DefaultPublicConstructor.class.getDeclaredConstructor());
        Constructor argConstructor = Constructor.of(ArgConstructor.class.getDeclaredConstructor(Object.class));

        assertThat(publicConstructor.getParameterTypes()).isEmpty();
        assertThat(argConstructor.getParameterTypes())
                .hasSize(1)
                .allMatch(parameter -> parameter.equals(Object.class));
    }

    @Test
    void isDefault_whenIsDefaultConstructor_thenIsTrue() throws NoSuchMethodException {
        Constructor publicConstructor = Constructor.of(DefaultPublicConstructor.class.getDeclaredConstructor());
        Constructor packageConstructor = Constructor.of(DefaultPackageConstructor.class.getDeclaredConstructor());
        Constructor privateConstructor = Constructor.of(DefaultPrivateConstructor.class.getDeclaredConstructor());
        Constructor protectedConstructor = Constructor.of(DefaultProtectedConstructor.class.getDeclaredConstructor());

        assertThat(publicConstructor.isDefault()).isTrue();
        assertThat(packageConstructor.isDefault()).isTrue();
        assertThat(privateConstructor.isDefault()).isTrue();
        assertThat(protectedConstructor.isDefault()).isTrue();
    }

    @Test
    void isDefault_whenIsNotDefaultConstructor_thenIsFalse() throws NoSuchMethodException {
        Constructor argConstructor = Constructor.of(ArgConstructor.class.getDeclaredConstructor(Object.class));

        assertThat(argConstructor.isDefault()).isFalse();
    }

    @Test
    void getType_shouldReturnConstructorType() throws NoSuchMethodException {
        Constructor publicConstructor = Constructor.of(DefaultPublicConstructor.class.getDeclaredConstructor());

        assertThat(publicConstructor.getType()).isEqualTo(java.lang.reflect.Constructor.class);
    }

    @Test
    void invoke_givenNoInstance_andEmptyArray_whenDefaultConstructor_thenInvokes() throws NoSuchMethodException,
                                                                                   InvocationTargetException,
                                                                                   IllegalAccessException {
        Constructor publicConstructor = Constructor.of(DefaultAndArgConstructor.class.getDeclaredConstructor());
        Object[] emptyArgument = new Object[]{};

        assertThat(publicConstructor.invoke(null)).isEqualTo(new DefaultAndArgConstructor());
        assertThat(publicConstructor.invoke(null, emptyArgument)).isEqualTo(new DefaultAndArgConstructor());
    }

    @Test
    void invoke_givenNoInstance_butGivenArgument_whenArgumentConstructor_thenInvokes() throws NoSuchMethodException,
                                                                                       InvocationTargetException,
                                                                                       IllegalAccessException {
        Constructor publicConstructor = Constructor.of(
                DefaultAndArgConstructor.class.getDeclaredConstructor(String.class));
        Object[] argument = new Object[]{""};

        assertThat(publicConstructor.invoke(null, argument)).isEqualTo(new DefaultAndArgConstructor(""));
    }

    @Test
    void invoke_givenInstance_whenInstanceHasSameOriginAsConstructor_thenInvokes() throws NoSuchMethodException,
                                                                                   InvocationTargetException,
                                                                                   IllegalAccessException {
        Constructor publicConstructor = Constructor.of(DefaultAndArgConstructor.class.getDeclaredConstructor());
        Constructor argConstructor = Constructor.of(
                DefaultAndArgConstructor.class.getDeclaredConstructor(String.class));
        Object instance = new DefaultAndArgConstructor();

        assertThat(publicConstructor.invoke(instance)).isEqualTo(new DefaultAndArgConstructor());
        assertThat(argConstructor.invoke(instance, "")).isEqualTo(new DefaultAndArgConstructor(""));
    }

    @Test
    void invoke_givenInstance_whenUnknownInstanceOrigin_thenThrowsIllegalArgumentException() throws NoSuchMethodException {
        Constructor publicConstructor = Constructor.of(DefaultAndArgConstructor.class.getDeclaredConstructor());
        Object instance = new DefaultPublicConstructor();

        assertThatThrownBy(() -> publicConstructor.invoke(instance)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void invoke_whenAbstractClass_thenInvocationTargetException() throws NoSuchMethodException {
        Constructor publicConstructor = Constructor.of(AbstractConstructor.class.getDeclaredConstructor());
        Object[] argument = new Object[]{};

        assertThatThrownBy(() -> publicConstructor.invoke(null, argument))
                .isInstanceOf(InvocationTargetException.class);
    }

    @Test
    void accepts_whenHasParameters_thenReturnsParameterTypes() throws NoSuchMethodException {
        Constructor argConstructor = Constructor.of(AbstractConstructor.class
                .getDeclaredConstructor(Object.class, String.class, Integer.class));

        assertThat(argConstructor.accepts()).hasSize(3)
                .anyMatch(Object.class::equals)
                .anyMatch(String.class::equals)
                .anyMatch(Integer.class::equals);
    }

    @Test
    void accepts_whenHasNoParameters_thenReturnsEmptyCollection() throws NoSuchMethodException {
        Constructor argConstructor = Constructor.of(AbstractConstructor.class.getDeclaredConstructor());

        assertThat(argConstructor.accepts()).isEmpty();
    }

    @Test
    void equalsTest() throws NoSuchMethodException {
        Constructor publicConstructor = Constructor.of(DefaultPublicConstructor.class.getDeclaredConstructor());
        Constructor argConstructor = Constructor.of(ArgConstructor.class.getDeclaredConstructor(Object.class));

        assertThat(publicConstructor).isEqualTo((Object) publicConstructor);
        assertThat(publicConstructor)
                .isEqualTo(Constructor.of(DefaultPublicConstructor.class.getDeclaredConstructor()));

        assertThat(publicConstructor).isNotEqualTo(argConstructor);
        assertThat(publicConstructor).isNotEqualTo(new Object());
        assertThat(publicConstructor).isNotEqualTo(null);
    }

    @Test
    void hashCodeTest() throws NoSuchMethodException {
        Constructor publicConstructor = Constructor.of(DefaultPublicConstructor.class.getDeclaredConstructor());
        Constructor argConstructor = Constructor.of(ArgConstructor.class.getDeclaredConstructor(Object.class));

        assertThat(publicConstructor.hashCode()).isEqualTo(((Object) publicConstructor).hashCode());
        assertThat(publicConstructor.hashCode())
                .isEqualTo(Constructor.of(DefaultPublicConstructor.class.getDeclaredConstructor()).hashCode());

        assertThat(publicConstructor.hashCode()).isNotEqualTo(argConstructor.hashCode());
        assertThat(publicConstructor.hashCode()).isNotEqualTo(new Object().hashCode());
        assertThat(publicConstructor.hashCode()).isNotEqualTo(Objects.hashCode(null));
    }

    @Test
    void toStringTest() throws NoSuchMethodException {
        Constructor argConstructor = Constructor.of(ArgConstructor.class.getDeclaredConstructor(Object.class));

        assertThat(argConstructor.toString()).isEqualTo("ArgConstructor([class java.lang.Object])");
    }

    private static abstract class AbstractConstructor {
        public AbstractConstructor() {
        }

        public AbstractConstructor(Object arg, String arg2, Integer arg3) {
        }
    }
}
