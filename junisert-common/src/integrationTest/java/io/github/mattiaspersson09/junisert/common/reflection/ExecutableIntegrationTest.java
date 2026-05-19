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
package io.github.mattiaspersson09.junisert.common.reflection;

import io.github.mattiaspersson09.junisert.testunits.constructor.SeveralArgConstructor;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExecutableIntegrationTest {
    @Test
    void hasParameters() throws NoSuchMethodException {
        Executable constructor = Constructor.of(SeveralArgConstructor.class
                .getDeclaredConstructor(Object.class, Object.class, Object.class));

        assertThat(constructor.hasParameters(Object.class, Object.class, Object.class)).isTrue();
        assertThat(constructor.hasParameters(Object.class, Object.class)).isFalse();
        assertThat(constructor.hasParameters(Object.class)).isFalse();
        assertThat(constructor.hasParameters(new Class<?>[0])).isFalse();
    }
}
