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

import io.github.mattiaspersson09.junisert.api.value.Value;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ValueCacheTest {
    private static ValueCache cache;

    private TypeValue[] cacheTypes;

    @BeforeAll
    static void beforeAll() {
        cache = new ValueCache();
    }

    @BeforeEach
    void setUp() {
        cacheTypes = new TypeValue[]{
                new TypeValue(Object.class, new Object(), null),
                new TypeValue(String.class, "str", null),
                new TypeValue(Number.class, 1, null),
                new TypeValue(Long.class, 2, null),
                new TypeValue(Integer.class, 3, null),
                new TypeValue(long.class, 4, 0),
                new TypeValue(int.class, 5, 0)
        };
    }

    @Test
    void save_whenConstructionFails_thenPropagatesToCaller() {
        assertThatThrownBy(() -> cache.save(ThrowingValue.class, new ThrowingValue(new RuntimeException())))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void isConcurrencySafe() throws InterruptedException {
        int threads = 10;
        CountDownLatch latch = new CountDownLatch(threads);
        ExecutorService service = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            service.execute(() -> {
                this.saveToCache();
                latch.countDown();
            });
        }

        latch.await();

        assertThat(cache.size()).isEqualTo(cacheTypes.length);

        for (TypeValue cached : cacheTypes) {
            assertThat(cache.contains(cached.type())).isTrue();
            assertThat(cache.get(cached.type()).get()).isEqualTo(cached.get());
            sanityCastTest(cached.type(), cache.get(cached.type()).get());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void sanityCastTest(Class<T> clazz, Object object) {
        T value = (T) object;
        noop(value);
    }

    private void noop(Object value) {
    }

    private void saveToCache() {
        Arrays.stream(cacheTypes)
                .parallel()
                .forEach(value -> cache.save(value.type(), value));
    }

    private static class ThrowingValue implements Value<Object> {
        private final RuntimeException failure;

        public ThrowingValue(RuntimeException failure) {
            this.failure = failure;
        }

        @Override
        public Object get() {
            throw failure;
        }
    }

    private static class TypeValue implements Value<Object> {
        private final Class<?> type;
        private final Object value;
        private final Object empty;

        public TypeValue(Class<?> type, Object value, Object empty) {
            this.type = type;
            this.value = value;
            this.empty = empty;
        }

        public Class<?> type() {
            return type;
        }

        @Override
        public Object get() {
            return value;
        }

        @Override
        public Object asEmpty() {
            return empty;
        }
    }
}
