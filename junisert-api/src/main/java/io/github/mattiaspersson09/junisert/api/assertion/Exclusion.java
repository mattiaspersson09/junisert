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

import io.github.mattiaspersson09.junisert.common.reflection.Field;
import io.github.mattiaspersson09.junisert.common.reflection.Method;
import io.github.mattiaspersson09.junisert.common.reflection.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Assertion exclusion filter for {@link Unit} members. Excluded members will be skipped in evaluations where the test
 * is not <strong>requiring</strong> the excluded member. <br>
 * Members such as {@code Object} methods: {@code equals},
 * {@code hashCode} and {@code toString} will not be skipped when an assertion is performing tests regarding these
 * methods.
 */
public final class Exclusion {
    private final List<Predicate<Field>> fields;
    private final List<Predicate<Method>> methods;

    Exclusion(List<Predicate<Field>> fields, List<Predicate<Method>> methods) {
        this.fields = fields;
        this.methods = methods;
    }

    /**
     * Creates a new exclusion builder.
     *
     * @return builder for exclusion filters
     */
    public static Exclude exclude() {
        return new Exclude();
    }

    /**
     * Checks if given {@link Field} is excluded according to this exclusion.
     *
     * @param field to check against filters
     * @return true if it's excluded
     */
    public boolean isExcluded(Field field) {
        return fieldExcludeFilter().test(field);
    }

    /**
     * Checks if given {@link Field} is not excluded according to this exclusion. Is equal to
     * negating the result of {@link #isExcluded(Field)}.
     *
     * @param field to check against filters
     * @return true if it's not excluded
     */
    public boolean isNotExcluded(Field field) {
        return fieldExcludeFilter().negate().test(field);
    }

    /**
     * Checks if given {@link Method} is excluded according to this exclusion.
     *
     * @param method to check against filters
     * @return true if it's excluded
     */
    public boolean isExcluded(Method method) {
        return methodExcludeFilter().test(method);
    }

    /**
     * Checks if given {@link Method} is not excluded according to this exclusion. Is equal to
     * negating the result of {@link #isExcluded(Method)}.
     *
     * @param method to check against filters
     * @return true if it's not excluded
     */
    public boolean isNotExcluded(Method method) {
        return methodExcludeFilter().negate().test(method);
    }

    /**
     * Adds given {@code exclusion} filters to this exclusion.
     *
     * @param exclusion to add
     */
    public void add(Exclusion exclusion) {
        Objects.requireNonNull(exclusion);
        fields.addAll(exclusion.fields);
        methods.addAll(exclusion.methods);
    }

    /**
     * Adds given {@code filter} to this exclusion field filters.
     *
     * @param filter to add
     */
    public void addFieldExclusion(Predicate<Field> filter) {
        fields.add(Objects.requireNonNull(filter));
    }

    /**
     * Adds given {@code filter} to this exclusion method filters.
     *
     * @param filter to add
     */
    public void addMethodExclusion(Predicate<Method> filter) {
        methods.add(Objects.requireNonNull(filter));
    }

    /**
     * Gets excluded fields according to this exclusion, from given {@link Unit}.
     *
     * @param unit to find excluded fields from
     * @return view of excluded fields
     */
    public List<Field> excludedFields(Unit unit) {
        return unit.getFields()
                .stream()
                .filter(this::isExcluded)
                .collect(Collectors.toList());
    }

    /**
     * Gets excluded methods according to this exclusion, from given {@link Unit}.
     *
     * @param unit to find excluded methods from
     * @return view of excluded methods
     */
    public List<Method> excludedMethods(Unit unit) {
        return unit.getMethods()
                .stream()
                .filter(this::isExcluded)
                .collect(Collectors.toList());
    }

    private Predicate<Field> fieldExcludeFilter() {
        // If no filter is applied then field should not be excluded
        return fields.stream()
                .reduce(Predicate::or)
                .orElseGet(() -> field -> false);
    }

    private Predicate<Method> methodExcludeFilter() {
        // If no filter is applied then method should not be excluded
        return methods.stream()
                .reduce(Predicate::or)
                .orElseGet(() -> method -> false);
    }

    /**
     * Builder for exclusion filters.
     */
    public static class Exclude {
        private final List<Predicate<Field>> fields;
        private final List<Predicate<Method>> methods;

        /**
         * Creates new builder.
         */
        public Exclude() {
            fields = new ArrayList<>();
            methods = new ArrayList<>();
        }

        /**
         * Adds {@code filter} to match with fields to exclude.
         *
         * @param filter to add
         * @return this builder
         */
        public Exclude fieldMatching(Predicate<Field> filter) {
            fields.add(filter);
            return this;
        }

        /**
         * Adds {@code filter} to match with methods to exclude.
         *
         * @param filter to add
         * @return this builder
         */
        public Exclude methodMatching(Predicate<Method> filter) {
            methods.add(filter);
            return this;
        }

        /**
         * Creates a new exclusion from built filters.
         *
         * @return new exclusion
         */
        public Exclusion build() {
            return new Exclusion(fields, methods);
        }
    }
}
