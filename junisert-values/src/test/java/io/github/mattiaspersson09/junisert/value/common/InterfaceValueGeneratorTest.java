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
import io.github.mattiaspersson09.junisert.api.value.Value;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class InterfaceValueGeneratorTest {
    private InterfaceValueGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new InterfaceValueGenerator();
    }

    @Test
    void supports_whenTypeIsAnInterface_thenIsSupporting() {
        assertThat(generator.supports(Interface.class)).isTrue();
        assertThat(generator.supports(NestedInterface.class)).isTrue();
        assertThat(generator.supports(NestedInterface.Interface.class)).isTrue();
    }

    @Test
    void supports_whenTypeIsNotAnInterface_thenIsNotSupporting() {
        assertThat(generator.supports(Abstract.class)).isFalse();
        assertThat(generator.supports(FinalClass.class)).isFalse();
    }

    @Test
    void generate() {
        assertThat(generator.generate(Interface.class).get()).isNotNull();
        assertThat(generator.generate(Interface.class).asEmpty()).isNull();
        assertThat(generator.generate(NestedInterface.class).get()).isNotNull();
        assertThat(generator.generate(NestedInterface.class).asEmpty()).isNull();
        assertThat(generator.generate(NestedInterface.Interface.class).get()).isNotNull();
        assertThat(generator.generate(NestedInterface.Interface.class).asEmpty()).isNull();
    }

    @Test
    void generate_whenTypeIsNotSupported_thenThrowsUnsupportedTypeError() {
        assertThatThrownBy(() -> generator.generate(Abstract.class)).isInstanceOf(UnsupportedTypeError.class);
        assertThatThrownBy(() -> generator.generate(FinalClass.class)).isInstanceOf(UnsupportedTypeError.class);
    }

    @Test
    void notSupportingProxyInvocation_whenConstructedProxy_thenCanNotInvokeMethods() {
        Value<?> proxyValue = generator.generate(Interface.class);
        Interface proxy = (Interface) proxyValue.get();

        assertThatThrownBy(proxy::invoke)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("Unable to invoke");
    }

    private interface Interface {
        void invoke();
    }

    private interface NestedInterface {
        interface Interface {
        }
    }

    private abstract static class Abstract {
    }

    private static final class FinalClass {
    }
}
