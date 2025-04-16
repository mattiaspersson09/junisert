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
package io.github.mattiaspersson09.junisert.value.common;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeException;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;

import java.lang.reflect.Constructor;

public class ObjectValueGenerator implements ValueGenerator<Object> {
    private final boolean forceConstructorAccess;

    public ObjectValueGenerator() {
        this(false);
    }

    public ObjectValueGenerator(boolean forceConstructorAccess) {
        this.forceConstructorAccess = forceConstructorAccess;
    }

    public static ObjectValueGenerator withForcedConstructorAccess(boolean forceConstructorAccess) {
        return new ObjectValueGenerator(forceConstructorAccess);
    }

    @Override
    public Value<?> generate(Class<?> fromType) throws UnsupportedTypeException {
        if (!supports(fromType)) {
            throw new UnsupportedTypeException(fromType);
        }

        try {
            Constructor<?> noArgConstructor = fromType.getDeclaredConstructor();

            if (forceConstructorAccess) {
                noArgConstructor.setAccessible(true);
            }

            return Value.ofEager(noArgConstructor.newInstance());
        } catch (Exception e) {
            throw new UnsupportedConstructionError(fromType, e);
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        try {
            // Would throw NoSuchMethodException if no default (no parameter) constructor is found
            type.getDeclaredConstructor();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
