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
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.testunits.constructor.ArgConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPackageConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPrivateConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPublicConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.NoConstructor;

import java.lang.reflect.InvocationTargetException;

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
    void supports_whenConstructorIsVisible_andHasNoParameters_thenIsSupported() {
        assertThat(generator.supports(NoConstructor.class)).isTrue();
        assertThat(generator.supports(DefaultPublicConstructor.class)).isTrue();
    }

    @Test
    void supports_whenConstructorIsInvisible_andForcingAccess_thenIsSupported() {
        ObjectValueGenerator generator = ObjectValueGenerator.withForcedAccess();

        assertThat(generator.supports(DefaultPackageConstructor.class)).isTrue();
        assertThat(generator.supports(DefaultPrivateConstructor.class)).isTrue();
    }

    @Test
    void supports_whenConstructorIsInvisible_andNotForcingAccess_thenIsNotSupported() {
        assertThat(generator.supports(DefaultPackageConstructor.class)).isFalse();
        assertThat(generator.supports(DefaultPrivateConstructor.class)).isFalse();
    }

    @Test
    void supports_whenConstructorIsVisible_butHasParameters_thenIsNotSupported() {
        assertThat(generator.supports(ArgConstructor.class)).isFalse();
    }

    @Test
    void generate_whenForcesConstructorAccess_andClassHasDefaultConstructor_thenConstructsValue() {
        ObjectValueGenerator generator = ObjectValueGenerator.withForcedAccess();

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
    void generate_whenDoesNotForceAccess_andClassHasNoDefaultConstructor_thenThrowsUnsupportedTypeError() {
        assertThatThrownBy(() -> generator.generate(DefaultPackageConstructor.class))
                .isInstanceOf(UnsupportedTypeError.class);
        assertThatThrownBy(() -> generator.generate(DefaultPrivateConstructor.class))
                .isInstanceOf(UnsupportedTypeError.class);
    }

    @Test
    void generate_whenClassHasOnlyArgumentConstructor_thenThrowsUnsupportedTypeError() {
        assertThatThrownBy(() -> generator.generate(ArgConstructor.class))
                .isInstanceOf(UnsupportedTypeError.class);
    }

    @Test
    void generate_whenConstructionLeadsToFailure_shouldFailFast_thenThrowsUnsupportedTypeError() {
        assertThatThrownBy(() -> generator.generate(FailingConstructor.class))
                .isInstanceOf(UnsupportedConstructionError.class)
                .hasMessageContaining("Failed to construct")
                .hasCauseInstanceOf(InvocationTargetException.class);
    }

    private static class FailingConstructor {
        public FailingConstructor() {
            throw new RuntimeException();
        }
    }
}
