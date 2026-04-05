/*
 * Copyright (c) 2025-2026 Mattias Persson
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
package io.github.mattiaspersson09.junisert.value.java;

import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

class JavaInternalIntegrationTest {
    private static final Logger LOGGER = Logger.getLogger(JavaInternalIntegrationTest.class);

    private final ValueGenerator<?> generator;
    private final SupportInvoker supportInvoker;

    public JavaInternalIntegrationTest() {
        this(null);
    }

    protected JavaInternalIntegrationTest(SupportInvoker invoker) {
        this.generator = JavaInternals.getSupported();
        this.supportInvoker = invoker;
    }

    protected void assertIsSupported(Class<?> javaType) {
        Value<?> value = generator.generate(javaType);

        assertThat(generator.supports(javaType)).isTrue();
        assertThat(value.asEmpty()).isNull();
        assertThat(value.get()).isNotNull();
        assertThat(value.get()).isInstanceOf(javaType);
        assertThat(javaType.cast(value.get())).isInstanceOf(javaType);
    }

    protected void assertThatFunctionalSupportCanBeUsed(Class<?> javaType) {
        if (supportInvoker == null) {
            LOGGER.info("No support invoker setup, can't run test");
            return;
        }

        if (!javaType.isAnnotationPresent(FunctionalInterface.class)) {
            LOGGER.info("Not a functional interface, ignoring: {0}", javaType);
            return;
        }

        supportInvoker.invoke(javaType, generator.generate(javaType));
    }

    protected void assertThatSupportCanBeUsed(Class<?> javaType) {
        if (supportInvoker == null) {
            LOGGER.info("No support invoker setup, can't run test");
            return;
        }

        supportInvoker.invokeVisible(javaType, generator.generate(javaType));
    }
}
