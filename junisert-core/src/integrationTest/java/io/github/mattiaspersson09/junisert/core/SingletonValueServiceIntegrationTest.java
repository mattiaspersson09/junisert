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
package io.github.mattiaspersson09.junisert.core;

import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ArrayValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.DependencyObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.EnumValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.InterfaceValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.PrimitiveValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.WrapperPrimitiveValueGenerator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonValueServiceIntegrationTest {
    @Test
    void supportGeneratorPriority_primitiveSupportFirst_argumentConstructorWithDependenciesLast() {
        SingletonValueService valueService = SingletonValueService.getInstance();

        assertThat(valueService.getRegisteredSupport())
                .hasSize(9)
                .satisfies(support -> {
                    assertThat(support.get(0)).isInstanceOf(PrimitiveValueGenerator.class);
                    assertThat(support.get(1)).isInstanceOf(WrapperPrimitiveValueGenerator.class);
                    assertThat(support.get(2)).isInstanceOf(ArrayValueGenerator.class);
                    assertThat(support.get(3)).isInstanceOf(EnumValueGenerator.class);
                    // Java internals predefined support
                    assertThat(support.get(4)).isInstanceOf(AggregatedValueGenerator.class);
                    // Interface proxies, must be after java internals
                    assertThat(support.get(5)).isInstanceOf(InterfaceValueGenerator.class);
                    // Objects from default constructor, not forcing access with reflection
                    assertThat(support.get(6)).isInstanceOf(ObjectValueGenerator.class);
                    // Objects from default constructor, forcing access with reflection
                    assertThat(support.get(7)).isInstanceOf(ObjectValueGenerator.class);
                    // Objects from argument constructor, forcing access with reflection
                    assertThat(support.get(8)).isInstanceOf(DependencyObjectValueGenerator.class);
                });
    }
}
