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
package io.github.mattiaspersson09.junisert.api.assertion;

import io.github.mattiaspersson09.junisert.common.annotation.ExcludeFromJacocoGeneratedReport;
import io.github.mattiaspersson09.junisert.common.reflection.Field;
import io.github.mattiaspersson09.junisert.common.reflection.Method;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Excludes unit members during operations.
 * If a member matching an exclusion filter appears during an operation, it should be skipped and the operation
 * should not be able to fail because of an excluded member.
 *
 * @param <E> type of excluder
 */
@ExcludeFromJacocoGeneratedReport
public interface Excluder<E> {
    /**
     * Adds {@code filter} to exclusions to later be applied in an operation to skip matching field.
     *
     * @param filter to apply when excluding
     * @return this excluder
     */
    E excludingField(Predicate<Field> filter);

    /**
     * Adds a field name filter to exclusions to later be applied in an operation to skip matching field.
     * Is a convenience way and the same as calling {@link #excludingField(Predicate)} with
     * name matching:
     * <pre>
     * excludeField(field -> {@link Field#getName() field.getName()}.equals(fieldName)
     * </pre>
     *
     * @param fieldName to create an exclusion filter for
     * @return this excluder
     */
    default E excludingField(String fieldName) {
        return excludingField(field -> field.getName().equals(fieldName));
    }

    /**
     * Adds {@code filter} to exclusions to later be applied in an operation to skip matching method.
     *
     * @param filter to apply when excluding
     * @return this excluder
     */
    E excludingMethod(Predicate<Method> filter);

    /**
     * Adds a method filter to exclusions to later be applied in an operation to skip matching method.
     * Is a convenience way and the same as calling {@link #excludingMethod(Predicate)} with
     * name- and parameter matching:
     * <pre>
     * excludingMethod(method -> {@link Method#getName() method.getName()}.equals(methodName) &amp;&amp; {@link Method#getParameterTypes() method.getParameterTypes()}.equals(Arrays.asList(methodParameters)))
     * </pre>
     *
     * @param methodName       to create an exclusion filter for
     * @param methodParameters if any to match with {@code methodName}
     * @return this excluder
     */
    default E excludingMethod(String methodName, Class<?>... methodParameters) {
        return excludingMethod(method -> method.getName().equals(methodName)
                && method.getParameterTypes().equals(Arrays.asList(methodParameters)));
    }

    /**
     * Creates or adds {@link Exclusion} to current exclusion filters. If no exclusion exists this creates a new
     * base exclusion, otherwise merges with current exclusion filters.
     *
     * @param exclusion to add
     * @return this excluder
     */
    E excluding(Exclusion exclusion);
}
