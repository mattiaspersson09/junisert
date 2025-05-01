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
import io.github.mattiaspersson09.junisert.api.internal.support.SupportBuilder;
import io.github.mattiaspersson09.junisert.common.logging.Logger;

import java.util.ArrayList;
import java.util.function.Supplier;

final class JavaLangSupport {
    private static final Supplier<Runnable> RUNNABLE = () -> () -> Logger.getLogger("Anonymous runnable")
            .info("running");

    private JavaLangSupport() {
    }

    static AggregatedValueGenerator getLangSupport() {
        return SupportBuilder.createSupport()
                .supportSingle(Appendable.class, StringBuilder.class, StringBuilder::new)
                .support(CharSequence.class)
                .withImplementation(String.class, String::new)
                .withImplementation(StringBuilder.class, StringBuilder::new)
                .withImplementation(StringBuffer.class, StringBuffer::new)
                .supportSingle(Iterable.class, ArrayList.class, ArrayList::new)
                .supportSingle(Runnable.class, RUNNABLE)
                .support(Throwable.class)
                .withImplementation(RuntimeException.class, RuntimeException::new)
                .withImplementation(AssertionError.class, AssertionError::new)
                .supportSingle(Thread.class, () -> new Thread(RUNNABLE.get()))
                .supportSingle(ThreadLocal.class, ThreadLocal::new)
                .build();
    }
}
