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
package io.github.mattiaspersson09.junisert.core.internal.reflection;


import java.util.Collection;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InvokableTest {
    @Test
    void getName_givesDefaultName() {
        assertThat(new InvokableName().getName()).isEqualTo("InvokableName");
    }

    @Test
    void getParent_givesDefaultParent() {
        assertThat(new InvokableName().getParent()).isEqualTo(InvokableTest.class);
    }

    private static class InvokableName implements Invokable {
        @Override
        public Object invoke(Object instance, Object... args) {
            return null;
        }

        @Override
        public Collection<Class<?>> accepts() {
            return null;
        }
    }
}
