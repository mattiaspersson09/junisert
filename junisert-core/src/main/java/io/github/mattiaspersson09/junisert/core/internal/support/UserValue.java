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
package io.github.mattiaspersson09.junisert.core.internal.support;

import io.github.mattiaspersson09.junisert.api.value.Value;

/**
 * Value wrapper from a value defined by a user, is generated from registered support.
 * Is used to determine where a value originated from when caching.
 */
public class UserValue implements Value<Object> {
    private final Value<?> originalValue;

    /**
     * Creates a new user defined value wrapper.
     *
     * @param originalValue original generated value
     */
    public UserValue(Value<?> originalValue) {
        this.originalValue = originalValue;
    }

    @Override
    public Object get() {
        return originalValue.get();
    }

    @Override
    public Object asEmpty() {
        return originalValue.asEmpty();
    }
}
