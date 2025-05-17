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
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.testunits.constructor.ArgConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPackageConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPrivateConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPublicConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.InceptionArgConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.PackageArgConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.RecursiveArgConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.SeveralArgConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.SeveralParameterConstructors;
import io.github.mattiaspersson09.junisert.testunits.unit.pojo.ImmutableModel;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DependencyObjectValueGeneratorTest {
    @Mock
    ValueGenerator<Object> argumentGenerator;

    private DependencyObjectValueGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new DependencyObjectValueGenerator(argumentGenerator);
    }

    @Test
    void supports_whenThereIsAnArgumentConstructor_andArgumentGeneratorSupports_thenIsSupported() {
        when(argumentGenerator.supports(any())).thenReturn(true);

        assertThat(generator.supports(ArgConstructor.class)).isTrue();
        assertThat(generator.supports(SeveralArgConstructor.class)).isTrue();
        assertThat(generator.supports(SeveralParameterConstructors.class)).isTrue();
    }

    @Test
    void supports_whenThereIsAnArgumentConstructorWithNonConstructableDependency_andArgumentGeneratorDoesNotSupport_thenIsNotSupported() {
        when(argumentGenerator.supports(any())).thenReturn(false);

        assertThat(generator.supports(ImmutableModel.class)).isFalse();
    }

    @Test
    void supports_whenThereIsAnArgumentConstructor_andArgumentGeneratorDoesNotSupport_butCanHandleRecursively_thenIsSupported() {
        when(argumentGenerator.supports(any())).thenReturn(false);

        assertThat(generator.supports(InceptionArgConstructor.class)).isTrue();
    }

    @Test
    void supports_whenForcingAccess_andArgumentGeneratorSupports_andThereIsAnInaccessibleArgConstructor_thenIsSupported() {
        when(argumentGenerator.supports(any())).thenReturn(true);

        assertThat(DependencyObjectValueGenerator.withForcedAccess(argumentGenerator)
                .supports(PackageArgConstructor.class))
                .isTrue();
    }

    @Test
    void supports_whenNotForcingAccess_andThereIsAnInaccessibleArgConstructor_thenIsNotSupported() {
        assertThat(generator.supports(PackageArgConstructor.class)).isFalse();
    }

    @Test
    void supports_whenThereIsNoArgumentConstructor_thenIsNotSupported() {
        assertThat(generator.supports(DefaultPublicConstructor.class)).isFalse();
        assertThat(generator.supports(DefaultPrivateConstructor.class)).isFalse();
        assertThat(generator.supports(DefaultPackageConstructor.class)).isFalse();
    }

    @Test
    void supports_whenArgumentGeneratorDoesNotSupportParameter_butParameterIsRecursive_thenSupports() {
        when(argumentGenerator.supports(RecursiveArgConstructor.class)).thenReturn(false);

        assertThat(generator.supports(RecursiveArgConstructor.class)).isTrue();
    }

    @Test
    void generate_whenThereIsNoArgumentConstructor_thenThrowsUnsupportedTypeError() {
        assertThatThrownBy(() -> generator.generate(DefaultPublicConstructor.class))
                .isInstanceOf(UnsupportedTypeError.class);
        assertThatThrownBy(() -> generator.generate(DefaultPrivateConstructor.class))
                .isInstanceOf(UnsupportedTypeError.class);
        assertThatThrownBy(() -> generator.generate(DefaultPackageConstructor.class))
                .isInstanceOf(UnsupportedTypeError.class);
    }

    @Test
    void generate_whenNotForcingAccess_andConstructorIsInaccessible_thenThrowsUnsupportedConstructionError() {
        when(argumentGenerator.supports(Object.class)).thenReturn(true);
        doReturn(((Value<?>) Object::new)).when(argumentGenerator).generate(Object.class);

        assertThatThrownBy(() -> generator.generate(PackageArgConstructor.class))
                .isInstanceOf(UnsupportedConstructionError.class);
    }

    @Test
    void generate_whenForcingAccess_andConstructorIsInaccessible_andArgumentGeneratorSupports_thenGeneratesValue() {
        when(argumentGenerator.supports(Object.class)).thenReturn(true);
        doReturn(((Value<?>) Object::new)).when(argumentGenerator).generate(Object.class);

        assertThat(DependencyObjectValueGenerator.withForcedAccess(argumentGenerator)
                .generate(PackageArgConstructor.class))
                .isNotNull();
    }

    @Test
    void generate_whenSeveralConstructorsWithParameters_thenGeneratesFromLessParameters() {
        when(argumentGenerator.supports(Object.class)).thenReturn(true);
        doReturn(((Value<?>) Object::new)).when(argumentGenerator).generate(Object.class);

        SeveralParameterConstructors object = (SeveralParameterConstructors) generator.generate(
                SeveralParameterConstructors.class).get();

        assertThat(object.getFirstConstructorField()).isNotNull();
        assertThat(object.getSecondConstructorField()).isNull();
    }

    @Test
    void givenNegativeMaxDependencyDepth_whenBuilding_thenIsBuiltWithZeroDepth() {
        DependencyObjectValueGenerator generator = DependencyObjectValueGenerator
                .buildDependencySupport(argumentGenerator)
                .withMaxDependencyDepth(-1)
                .build();

        assertThat(generator.getMaxDependencyDepth()).isZero();
    }

    @Test
    void givenTooLargeDepth_whenBuilding_thenThrowsIllegalArgumentException() {
        DependencyObjectValueGenerator.Builder builder = DependencyObjectValueGenerator
                .buildDependencySupport(argumentGenerator)
                .withMaxDependencyDepth(DependencyObjectValueGenerator.MAX_DEPENDENCY_DEPTH + 1);

        assertThatThrownBy(builder::build).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Dependency depth isn't allowed be larger than")
                .hasMessageContaining(Objects.toString(DependencyObjectValueGenerator.MAX_DEPENDENCY_DEPTH));
    }
}
