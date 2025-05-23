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

import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;

/**
 * Utility class to construct a value generator for java value types
 * that is supported right out of the box.
 */
public final class JavaInternals {
    private JavaInternals() {
    }

    public static ValueGenerator<?> getSupported() {
        return JavaLangSupport.getLangSupport()
                .merge(JavaUtilSupport.getUtilSupport())
                .merge(JavaUtilSupport.getFunctionalSupport())
                .merge(JavaUtilSupport.getStreamSupport())
                .merge(JavaTimeSupport.getTimeSupport())
                .merge(JavaIOSupport.getIOSupport())
                .merge(JavaTextSupport.getTextSupport())
                .merge(JavaSecuritySupport.getSecuritySupport());
    }
}
