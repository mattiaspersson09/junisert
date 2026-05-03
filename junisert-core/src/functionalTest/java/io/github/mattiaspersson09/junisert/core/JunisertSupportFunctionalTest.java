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

import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Base;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;
import io.github.mattiaspersson09.junisert.testunits.unit.pojo.ModelAbstractDependency;

import java.util.Objects;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JunisertSupportFunctionalTest {
    @BeforeAll
    static void beforeAll() {
        SupportRegistry.get().clearRegisteredSupport();
    }

    @AfterEach
    void tearDown() {
        SupportRegistry.get().clearRegisteredSupport();
        SupportRegistry.get().clearCache();
    }

    @Test
    void givenSupportToRegister_whenNotPreviouslySupported_thenNowSupports() {
        assertThatThrownBy(() -> Junisert.assertThatPojo(ModelAbstractDependency.class).isWellImplemented())
                .isInstanceOf(UnsupportedTypeError.class)
                .hasMessageContaining("support for type '%s'", Base.class);

        Junisert.registerSupport(Base.class, Impl::new);

        Junisert.assertThatPojo(ModelAbstractDependency.class).isWellImplemented();
    }

    @Test
    void givenSupportToRegister_whenSameSupportAsAlreadyRegistered_thenIsPrioritized() {
        Value<?> valueOldSupport = SingletonValueService.getInstance().getValue(int.class);
        Junisert.registerSupport(int.class, () -> 100);
        Value<?> valueNewSupport = SingletonValueService.getInstance().getValue(int.class);

        assertThat(valueNewSupport.get()).isNotEqualTo(valueOldSupport.get());
        assertThat(valueNewSupport.get()).isEqualTo(100);
        assertThat(valueNewSupport.asEmpty()).isEqualTo(0);
    }

    @Test
    void registerSupport_givenPrimitiveSupport_whenSpecifyingEmptyValue_thenNowSupportsValueAndEmptyValue() {
        Value<?> valueOldSupport = SingletonValueService.getInstance().getValue(int.class);
        Junisert.registerSupport(int.class, Value.of(() -> 100, () -> -1));
        Value<?> valueNewSupport = SingletonValueService.getInstance().getValue(int.class);

        assertThat(valueNewSupport.get()).isNotEqualTo(valueOldSupport.get());
        assertThat(valueNewSupport.get()).isEqualTo(100);
        assertThat(valueNewSupport.asEmpty()).isEqualTo(-1);
    }

    @Test
    void givenTemporarySupportToRegister_whenNotPreviouslySupported_thenNowSupports() {
        assertThatThrownBy(() -> Junisert.assertThatPojo(ModelAbstractDependency.class).isWellImplemented())
                .isInstanceOf(UnsupportedTypeError.class)
                .hasMessageContaining("support for type '%s'", Base.class);

        Junisert.assertThatPojo(ModelAbstractDependency.class)
                .withSupport(Base.class, Impl::new)
                .isWellImplemented();
    }

    @Test
    void givenTemporarySupport_whenPerformingSeveralAssertions_thenOnlySupportFirstOperation() {
        assertThatThrownBy(() -> Junisert.assertThatPojo(ModelAbstractDependency.class).isWellImplemented())
                .isInstanceOf(UnsupportedTypeError.class)
                .hasMessageContaining("support for type '%s'", Base.class);

        assertThatThrownBy(() -> Junisert.assertThatUnit(ModelAbstractDependency.class).asPojo().isWellImplemented())
                .isInstanceOf(UnsupportedTypeError.class)
                .hasMessageContaining("support for type '%s'", Base.class);

        Junisert.assertThatPojo(ModelAbstractDependency.class)
                .withSupport(Base.class, Impl::new)
                .isWellImplemented();

        assertThatThrownBy(() -> Junisert.assertThatPojo(ModelAbstractDependency.class).isWellImplemented())
                .isInstanceOf(UnsupportedTypeError.class)
                .hasMessageContaining("support for type '%s'", Base.class);

        Junisert.assertThatUnit(ModelAbstractDependency.class)
                .withSupport(Base.class, Impl::new)
                .asPojo()
                .isWellImplemented();

        assertThatThrownBy(() -> Junisert.assertThatUnit(ModelAbstractDependency.class).asPojo().isWellImplemented())
                .isInstanceOf(UnsupportedTypeError.class)
                .hasMessageContaining("support for type '%s'", Base.class);
    }

    @Test
    void givenTemporarySupport_whenCompetingWithGlobalSupport_thenPrioritizeTemporarySupport() {
        assertThatThrownBy(() -> Junisert.assertThatPojo(UserGlobalSupport.class).isWellImplemented())
                .isInstanceOf(UnsupportedConstructionError.class)
                .hasMessageContaining("to construct concrete value");

        Junisert.registerSupport(Supplier.class, Value.of(() -> () -> "global", () -> () -> "emptyGlobal"));

        Junisert.assertThatPojo(UserGlobalSupport.class)
                .isWellImplemented();

        Junisert.assertThatPojo(UserTemporarySupport.class)
                .withSupport(Supplier.class, Value.of(() -> () -> "temporary", () -> () -> "emptyTemporary"))
                .isWellImplemented();
    }

    @SuppressWarnings("unused")
    private static class UserGlobalSupport {
        private final String result;

        public UserGlobalSupport(Supplier<String> result) {
            Objects.requireNonNull(result);
            this.result = Objects.requireNonNull(result.get());

            if (!"global".equals(this.result) && !"emptyGlobal".equals(this.result)) {
                throw new RuntimeException("Wrong implementation value");
            }
        }

        public String getResult() {
            return result;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            UserGlobalSupport that = (UserGlobalSupport) object;
            return Objects.equals(result, that.result);
        }

        @Override
        public int hashCode() {
            return Objects.hash(result);
        }

        @Override
        public String toString() {
            return "UserGlobalSupport{" +
                    "result=" + result +
                    '}';
        }
    }

    @SuppressWarnings("unused")
    private static class UserTemporarySupport {
        private final String result;

        public UserTemporarySupport(Supplier<String> result) {
            Objects.requireNonNull(result);
            this.result = Objects.requireNonNull(result.get());

            if (!"temporary".equals(this.result) && !"emptyTemporary".equals(this.result)) {
                throw new RuntimeException("Wrong implementation value");
            }
        }

        public String getResult() {
            return result;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            UserTemporarySupport that = (UserTemporarySupport) object;
            return Objects.equals(result, that.result);
        }

        @Override
        public int hashCode() {
            return Objects.hash(result);
        }

        @Override
        public String toString() {
            return "UserTemporarySupport{" +
                    "result=" + result +
                    '}';
        }
    }
}
