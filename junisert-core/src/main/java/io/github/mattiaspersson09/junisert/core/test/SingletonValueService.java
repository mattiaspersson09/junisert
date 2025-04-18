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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

final class SingletonValueService implements ValueService {
    private static final SingletonValueService INSTANCE = new SingletonValueService(new ValueCache());

    private final List<ValueGenerator<?>> generators;
    private final ValueCache valueCache;

    SingletonValueService(ValueCache valueCache) {
        this.generators = Collections.synchronizedList(new ArrayList<>());
        this.valueCache = valueCache;
    }

    public static SingletonValueService getInstance() {
        return INSTANCE;
    }

    @Override
    public void registerSupport(ValueGenerator<?> generator) {
        generators.add(Objects.requireNonNull(generator));
    }

    @Override
    public Value<?> getValue(Class<?> type) {
        if (valueCache.contains(type)) {
            return valueCache.get(type);
        }

        for (ValueGenerator<?> generator : generators) {
            if (generator.supports(type)) {
                Value<?> value = generator.generate(type);

                return Optional.ofNullable(saveAndGetFromCache(type, value))
                        .orElseThrow(() -> new UnsupportedTypeError(type));
            }
        }

        throw new UnsupportedTypeError(type);
    }

    private Value<?> saveAndGetFromCache(Class<?> type, Value<?> value) {
        try {
            return valueCache.save(type, value);
        } catch (Exception e) {
            return null;
        }
    }
}
