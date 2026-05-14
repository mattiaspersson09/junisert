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
package io.github.mattiaspersson09.junisert.common.reflection.util;

import io.github.mattiaspersson09.junisert.common.reflection.Unit;
import io.github.mattiaspersson09.junisert.testunits.method.FakeEqualsMethods;
import io.github.mattiaspersson09.junisert.testunits.method.FakeHashCodeMethods;
import io.github.mattiaspersson09.junisert.testunits.method.FakeToStringMethods;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MethodsIntegrationTest {
    @Test
    void givenObjectMethods_whenTheyAreFake_thenIsNotMethod() {
        assertThat(Unit.of(FakeEqualsMethods.class).getMethods())
                .noneMatch(Methods::isEqualsMethod);

        assertThat(Unit.of(FakeHashCodeMethods.class).getMethods())
                .noneMatch(Methods::isHashCodeMethod);

        assertThat(Unit.of(FakeToStringMethods.class).getMethods())
                .noneMatch(Methods::isToStringMethod);
    }
}
