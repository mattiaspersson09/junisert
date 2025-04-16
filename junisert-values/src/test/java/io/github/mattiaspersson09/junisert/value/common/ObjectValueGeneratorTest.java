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
package io.github.mattiaspersson09.junisert.value.common;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeException;
import io.github.mattiaspersson09.junisert.testunits.constructor.ArgConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPackageConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPrivateConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPublicConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.NoConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ObjectValueGeneratorTest {
    private ObjectValueGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new ObjectValueGenerator();
    }

    @Test
    void supports_whenConstructorWithoutParameters_thenIsSupported() {
        assertThat(generator.supports(NoConstructor.class)).isTrue();
        assertThat(generator.supports(DefaultPublicConstructor.class)).isTrue();
        assertThat(generator.supports(DefaultPackageConstructor.class)).isTrue();
        assertThat(generator.supports(DefaultPrivateConstructor.class)).isTrue();
    }

    @Test
    void supports_whenConstructorsWithParameters_thenIsNotSupported() {
        assertThat(generator.supports(ArgConstructor.class)).isFalse();
    }

    @Test
    void generate_whenForcesConstructorAccess_andClassHasDefaultConstructor_thenConstructsValue() {
        ObjectValueGenerator generator = new ObjectValueGenerator(true);

        assertThat(generator.generate(NoConstructor.class).get()).isNotNull();
        assertThat(generator.generate(DefaultPublicConstructor.class).get()).isNotNull();
        assertThat(generator.generate(DefaultPackageConstructor.class).get()).isNotNull();
        assertThat(generator.generate(DefaultPrivateConstructor.class).get()).isNotNull();
    }

    @Test
    void generate_whenDoesNotForceConstructorAccess_andClassHasDefaultConstructor_thenConstructsValue() {
        assertThat(generator.generate(NoConstructor.class).get()).isNotNull();
        assertThat(generator.generate(DefaultPublicConstructor.class).get()).isNotNull();
    }

    @Test
    void generate_whenDoesNotForceAccess_andClassHasNoDefaultConstructor_thenThrowsUnsupportedTypeException() {
        assertThatThrownBy(() -> generator.generate(DefaultPackageConstructor.class))
                .isInstanceOf(UnsupportedConstructionError.class);
        assertThatThrownBy(() -> generator.generate(DefaultPrivateConstructor.class))
                .isInstanceOf(UnsupportedConstructionError.class);
    }

    @Test
    void generate_whenClassHasOnlyArgumentConstructor_thenThrowsUnsupportedTypeException() {
        assertThatThrownBy(() -> generator.generate(ArgConstructor.class))
                .isInstanceOf(UnsupportedTypeException.class);
    }
}
