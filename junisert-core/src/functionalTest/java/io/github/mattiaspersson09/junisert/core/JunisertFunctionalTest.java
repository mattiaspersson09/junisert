/*
 * Copyright (c) 2026 Mattias Persson
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
package io.github.mattiaspersson09.junisert.core;

import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.core.units.lombok.LombokDataUnit;
import io.github.mattiaspersson09.junisert.core.units.lombok.LombokUnit;
import io.github.mattiaspersson09.junisert.testunits.constructor.ArgConstructor;
import io.github.mattiaspersson09.junisert.testunits.unit.bean.BeanCompliantButNotRecommended;
import io.github.mattiaspersson09.junisert.testunits.unit.bean.BeanCompliantModel;
import io.github.mattiaspersson09.junisert.testunits.unit.bean.BeanVisibleFields;
import io.github.mattiaspersson09.junisert.testunits.unit.pojo.DeepDependencyModel;
import io.github.mattiaspersson09.junisert.testunits.unit.pojo.ImmutableModel;
import io.github.mattiaspersson09.junisert.testunits.unit.pojo.ImmutableModelBrokenGetter;
import io.github.mattiaspersson09.junisert.testunits.unit.pojo.UnknownDependencyImmutable;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JunisertFunctionalTest {
    @ParameterizedTest
    @ValueSource(classes = {
            BeanCompliantModel.class,
            LombokDataUnit.class,
            LombokUnit.class,
            DeepDependencyModel.class
    })
    void givenPlainObject_whenAssertingWellImplementedPojo_thenShouldPassAssertion(Class<?> plainObject) {
        Junisert.assertThatPojo(plainObject).isWellImplemented();
    }

    @ParameterizedTest
    @ValueSource(classes = {
            ImmutableModel.class,
            UnknownDependencyImmutable.class
    })
    void givenImmutable_whenAssertingPojo_thenShouldPassAssertion(Class<?> immutable) {
        Junisert.assertThatUnit(immutable)
                .asPojo()
                .isWellImplemented();
    }

    @ParameterizedTest
    @ValueSource(classes = {
            BeanCompliantModel.class,
            BeanCompliantButNotRecommended.class
    })
    void givenBean_whenAssertingJavaBeanCompliant_thenShouldPassAssertion(Class<?> bean) {
        Junisert.assertThatUnit(bean).isJavaBeanCompliant();
    }

    @Test
    void givenBean_whenMissingDefaultConstructor_thenFailsAssertion() {
        assertThatThrownBy(() -> Junisert.assertThatUnit(ArgConstructor.class).isJavaBeanCompliant())
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("expected to have a default constructor");
    }

    @Test
    void givenBean_whenBeanHasVisibleFields_thenFailsAssertion() {
        assertThatThrownBy(() -> Junisert.assertThatUnit(BeanVisibleFields.class).isJavaBeanCompliant())
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("expected to only have private properties");
    }

    @Test
    void givenImmutable_whenTrulyImmutable_thenShouldPassAssertion() {
        Junisert.assertThatUnit(ImmutableModel.class).isImmutable();
    }

    @ParameterizedTest
    @ValueSource(classes = {
            BeanCompliantModel.class,
            LombokDataUnit.class,
            LombokUnit.class,
            DeepDependencyModel.class,
            ImmutableModelBrokenGetter.class
    })
    void givenImmutable_whenIsNotImmutable_thenFailsAssertion(Class<?> type) {
        assertThatThrownBy(() -> Junisert.assertThatUnit(type).isImmutable()).isInstanceOf(UnitAssertionError.class);
    }
}
