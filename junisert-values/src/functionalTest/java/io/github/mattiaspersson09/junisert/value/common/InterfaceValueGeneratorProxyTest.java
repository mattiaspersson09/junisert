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

import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InterfaceValueGeneratorProxyTest {
    private InterfaceValueGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new InterfaceValueGenerator();
    }

    @Test
    void equalsProxy() {
        Value<?> proxyValue = generator.generate(Super.class);
        Super proxy = (Super) proxyValue.get();

        assertThat(proxy).isEqualTo((Object) proxy);
        assertThat(proxy).isEqualTo(generator.generate(Super.class).get());
        assertThat(proxy).isNotEqualTo(null);
        assertThat(proxy).isNotEqualTo(new Object());
    }

    @Test
    void hashCodeProxy() {
        Value<?> proxyValue = generator.generate(Super.class);
        Super proxy = (Super) proxyValue.get();

        assertThat(proxy.hashCode()).isEqualTo(((Object) proxy).hashCode());
        assertThat(proxy.hashCode()).isEqualTo(generator.generate(Super.class).get().hashCode());
        assertThat(proxy.hashCode()).isNotEqualTo(Objects.hashCode(null));
        assertThat(proxy.hashCode()).isNotEqualTo(new Object().hashCode());
    }

    @Test
    void toStringProxy() {
        Value<?> proxyValue = generator.generate(Super.class);
        Super proxy = (Super) proxyValue.get();

        assertThat(proxy.toString()).isEqualTo("Junisert$InterfaceProxy");
    }

    @Test
    void classWithProxy_whenEquals_thenShouldBeAbleToCompare() {
        Super proxy = (Super) generator.generate(Super.class).get();
        Super emptyProxy = (Super) generator.generate(Super.class).asEmpty();

        ComparableClass instance = new ComparableClass(proxy, 1);
        assertThat(instance).isEqualTo((Object) instance);
        assertThat(instance).isEqualTo(new ComparableClass((Super) generator.generate(Super.class).get(), 1));
        assertThat(instance).isNotEqualTo(new ComparableClass(emptyProxy, 1));
    }

    @Test
    void classWithProxy_whenHashCode_thenShouldBeAbleToCompare() {
        Super proxy = (Super) generator.generate(Super.class).get();
        Super emptyProxy = (Super) generator.generate(Super.class).asEmpty();

        ComparableClass instance = new ComparableClass(proxy, 1);
        assertThat(instance.hashCode()).isEqualTo(((Object) instance).hashCode());
        assertThat(instance.hashCode()).isEqualTo(
                new ComparableClass((Super) generator.generate(Super.class).get(), 1).hashCode());
        assertThat(instance.hashCode()).isNotEqualTo(new ComparableClass(emptyProxy, 1).hashCode());
    }

    private static class ComparableClass {
        private final Super proxyField;
        private final int intField;

        public ComparableClass(Super proxyField, int intField) {
            this.proxyField = proxyField;
            this.intField = intField;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            ComparableClass that = (ComparableClass) object;
            return intField == that.intField && Objects.equals(proxyField, that.proxyField);
        }

        @Override
        public int hashCode() {
            return Objects.hash(proxyField, intField);
        }

        @Override
        public String toString() {
            return "ComparableClass{" +
                    "proxyField=" + proxyField +
                    ", intField=" + intField +
                    '}';
        }
    }
}
