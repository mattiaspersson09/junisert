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
package io.github.mattiaspersson09.junisert.core.internal;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;

/**
 * Providing broad support for different type of values. {@code ValueService}s should cache used values so that they
 * can be re-used to boost performance.
 */
public interface ValueService {
    /**
     * Registers a value supporting generator.
     *
     * @param generator that supports values for some type
     */
    void registerSupport(ValueGenerator<?> generator);

    /**
     * Registers a value supporting generator with a given {@code supportName}.
     *
     * @param generator   that supports values for some type
     * @param supportName for the value supporting generator
     */
    void registerNamedSupport(ValueGenerator<?> generator, String supportName);

    /**
     * Gets value for given {@code type} from a registered support or cache source.
     *
     * @param type to get value for
     * @return value for {@code type}
     * @throws UnsupportedTypeError if given type isn't supported among the registered generators or
     *                              found in a cache source
     */
    Value<?> getValue(Class<?> type) throws UnsupportedTypeError;
}
