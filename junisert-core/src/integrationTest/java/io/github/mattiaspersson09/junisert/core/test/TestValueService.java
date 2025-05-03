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
package io.github.mattiaspersson09.junisert.core.test;

import io.github.mattiaspersson09.junisert.api.internal.service.ValueService;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class TestValueService implements ValueService {
    private final List<ValueGenerator<?>> generators;

    TestValueService(ValueGenerator<?> generator) {
        this(Collections.singletonList(generator));
    }

    TestValueService(ValueGenerator<?>... generators) {
        this(Arrays.asList(generators));
    }

    TestValueService(List<ValueGenerator<?>> generators) {
        this.generators = generators;
    }

    @Override
    public void registerSupport(ValueGenerator<?> generator) {
        generators.add(generator);
    }

    @Override
    public Value<?> getValue(Class<?> type) throws UnsupportedTypeError {
        for (ValueGenerator<?> generator : generators) {
            if (generator.supports(type)) {
                return generator.generate(type);
            }
        }

        throw new UnsupportedTypeError(type);
    }
}
