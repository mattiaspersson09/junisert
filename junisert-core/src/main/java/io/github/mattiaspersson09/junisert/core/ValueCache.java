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
package io.github.mattiaspersson09.junisert.core;

import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.core.internal.support.UserValue;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache holding generated values during assertions, used to share values and prioritize user defined support values.
 */
public final class ValueCache {
    private final Map<Class<?>, Value<?>> cache;

    /**
     * Creates a new empty cache.
     */
    public ValueCache() {
        this.cache = new ConcurrentHashMap<>();
    }

    boolean contains(Class<?> type) {
        return cache.containsKey(type);
    }

    Value<?> get(Class<?> type) {
        return cache.get(type);
    }

    Value<?> save(Class<?> type, Value<?> value) {
        Value<?> cached = get(type);

        // User registered support values should override old cached values which aren't from a registered support
        if (cached != null && value instanceof UserValue && !(cached instanceof UserCacheValue)) {
            CacheValue newValue = new UserCacheValue(value.get(), value.asEmpty());
            cache.put(type, newValue);
            return newValue;
        }

        return cache.computeIfAbsent(type, (key) -> new CacheValue(value.get(), value.asEmpty()));
    }

    int size() {
        return cache.size();
    }

    void clear() {
        cache.clear();
    }

    static class CacheValue implements Value<Object> {
        private final Object concrete;
        private final Object empty;

        CacheValue(Object concrete, Object empty) {
            this.concrete = concrete;
            this.empty = empty;
        }

        @Override
        public Object get() {
            return concrete;
        }

        @Override
        public Object asEmpty() {
            return empty;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            CacheValue that = (CacheValue) object;
            return Objects.equals(concrete, that.concrete) && Objects.equals(empty, that.empty);
        }

        @Override
        public int hashCode() {
            return Objects.hash(concrete, empty);
        }

        @Override
        public String toString() {
            return "CacheValue{" +
                    "concrete=" + concrete +
                    ", empty=" + empty +
                    '}';
        }
    }

    private static class UserCacheValue extends CacheValue {
        UserCacheValue(Object concrete, Object empty) {
            super(concrete, empty);
        }
    }
}
