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

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.testunits.constructor.ArgConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPackageConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPrivateConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPublicConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.RecursiveArgConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.SeveralArgAndRecursiveConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.SeveralArgConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.SeveralParameterConstructors;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Base;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.ExtendingImpl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;
import io.github.mattiaspersson09.junisert.testunits.unit.enumeration.EnumUnit;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CommonGeneratorsIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger("Common generators");

    Collection<ValueGenerator<?>> commonGenerators;

    @BeforeEach
    void setUp() {
        commonGenerators = Arrays.asList(
                new PrimitiveValueGenerator(),
                new WrapperPrimitiveValueGenerator(),
                new InterfaceValueGenerator(),
                ObjectValueGenerator.withForcedAccess(),
                ParameterObjectValueGenerator.withForcedAccess(ObjectValueGenerator.withForcedAccess()),
                new EnumValueGenerator(),
                new ArrayValueGenerator()
        );
    }

    @ParameterizedTest
    @ValueSource(classes = {
            int[].class,
            boolean[].class,
            Integer[].class,
            Super[].class,
            Base[].class,
            Impl[].class,
            EnumUnit[].class
    })
    void arrayValueGeneratorIsOnlySupporter(Class<?> arrayType) {
        assertNoCompetitionFor(arrayType, ArrayValueGenerator.class);
    }

    @Test
    void enumValueGeneratorIsOnlySupporter() {
        assertNoCompetitionFor(EnumUnit.class, EnumValueGenerator.class);
    }

    @Test
    void interfaceValueGeneratorIsOnlySupporter() {
        assertNoCompetitionFor(Super.class, InterfaceValueGenerator.class);
    }

    @Test
    void objectValueGeneratorIsOnlySupporter() {
        assertNoCompetitionFor(Impl.class, ObjectValueGenerator.class);
        assertNoCompetitionFor(ExtendingImpl.class, ObjectValueGenerator.class);
        assertNoCompetitionFor(DefaultPublicConstructor.class, ObjectValueGenerator.class);
        assertNoCompetitionFor(DefaultPackageConstructor.class, ObjectValueGenerator.class);
        assertNoCompetitionFor(DefaultPrivateConstructor.class, ObjectValueGenerator.class);
    }

    @Test
    void parameterObjectValueGeneratorIsOnlySupporter() {
        assertNoCompetitionFor(ArgConstructor.class, ParameterObjectValueGenerator.class);
        assertNoCompetitionFor(RecursiveArgConstructor.class, ParameterObjectValueGenerator.class);
        assertNoCompetitionFor(SeveralArgConstructor.class, ParameterObjectValueGenerator.class);
        assertNoCompetitionFor(SeveralParameterConstructors.class, ParameterObjectValueGenerator.class);
        assertNoCompetitionFor(SeveralArgAndRecursiveConstructor.class, ParameterObjectValueGenerator.class);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            boolean.class,
            char.class,
            byte.class,
            short.class,
            int.class,
            long.class,
            float.class,
            double.class
    })
    void primitiveValueGeneratorIsOnlySupporter(Class<?> primitive) {
        assertNoCompetitionFor(primitive, PrimitiveValueGenerator.class);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Number.class
    })
    void wrapperPrimitiveValueGeneratorIsOnlySupporter(Class<?> wrapper) {
        assertNoCompetitionFor(wrapper, WrapperPrimitiveValueGenerator.class);
    }

    private void assertNoCompetitionFor(Class<?> type, Class<? extends ValueGenerator<?>> supporter) {
        Collection<ValueGenerator<?>> competitors = this.commonGenerators.stream()
                .filter(gen -> !gen.getClass().equals(supporter))
                .collect(Collectors.toList());

        LOGGER.test("Making sure {0}.class is only supported by {1}", type.getSimpleName(), supporter.getSimpleName());

        for (ValueGenerator<?> generator : competitors) {
            LOGGER.test("No competition check -> {0}", generator.getClass().getSimpleName());
            assertThat(generator.supports(type)).isFalse();
            assertThatThrownBy(() -> generator.generate(type)).isInstanceOf(UnsupportedTypeError.class);
        }
    }
}
