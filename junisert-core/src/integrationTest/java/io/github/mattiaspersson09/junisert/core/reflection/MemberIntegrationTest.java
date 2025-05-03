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
package io.github.mattiaspersson09.junisert.core.reflection;

import io.github.mattiaspersson09.junisert.testunits.constructor.DefaultPublicConstructor;
import io.github.mattiaspersson09.junisert.testunits.field.ValueFields;
import io.github.mattiaspersson09.junisert.testunits.method.PolymorphicMethods;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberIntegrationTest {
    @Test
    void getParent() throws NoSuchMethodException, NoSuchFieldException {
        Member method = Method.of(PolymorphicMethods.class.getDeclaredMethod("publicBaseNoParameters"));
        Member constructor = Constructor.of(DefaultPublicConstructor.class.getDeclaredConstructor());
        Member field = Field.of(ValueFields.class.getDeclaredField("immutableStringValueField"));

        assertThat(method.getParent()).isEqualTo(PolymorphicMethods.class);
        assertThat(constructor.getParent()).isEqualTo(DefaultPublicConstructor.class);
        assertThat(field.getParent()).isEqualTo(ValueFields.class);
    }
}
