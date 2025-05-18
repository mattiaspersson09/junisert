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
package io.github.mattiaspersson09.junisert.value.java;

import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;

import java.security.SecurityPermission;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaInternalsSupportPriorityTest {
    @Test
    void supportGeneratorPriority_javaLangFirst_javaSecurityLast() {
        AggregatedValueGenerator support = (AggregatedValueGenerator) JavaInternals.getSupported();
        List<ValueGenerator<?>> aggregated = new ArrayList<>(support.aggregated());

        assertThat(aggregated).first().satisfies(generator -> generator.supports(String.class));
        assertThat(aggregated).last().satisfies(generator -> generator.supports(SecurityPermission.class));
    }
}
