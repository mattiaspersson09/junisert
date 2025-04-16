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

import io.github.mattiaspersson09.junisert.api.value.Value;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

final class ValueCache {
    private final Map<Class<?>, Value<?>> cache;

    ValueCache() {
        this.cache = new ConcurrentHashMap<>();
    }

    boolean contains(Class<?> type) {
        return cache.containsKey(type);
    }

    Value<?> get(Class<?> type) {
        return cache.get(type);
    }

    Value<?> save(Class<?> type, Value<?> value) {
        /*
            If the value to cache is lazy we need to construct the implementation at this point
            to propagate construction failure to the caller and be able to cache the value.
            If we cache a lazy value it will be constructed several more times later, resulting in
            unnecessary duplicates.
         */
        CachableValue cacheValue = new CachableValue(value.get(), value.asEmpty());
        return cache.computeIfAbsent(type, (key) -> cacheValue);
    }

    int size() {
        return cache.size();
    }

    private static class CachableValue implements Value<Object> {
        private final Object concrete;
        private final Object empty;

        private CachableValue(Object concrete, Object empty) {
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
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CachableValue that = (CachableValue) o;
            return Objects.equals(concrete, that.concrete) && Objects.equals(empty, that.empty);
        }

        @Override
        public int hashCode() {
            return Objects.hash(concrete, empty);
        }
    }
}
