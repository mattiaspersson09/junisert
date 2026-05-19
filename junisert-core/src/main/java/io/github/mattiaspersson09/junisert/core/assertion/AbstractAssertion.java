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
package io.github.mattiaspersson09.junisert.core.assertion;

import io.github.mattiaspersson09.junisert.api.assertion.Assertion;
import io.github.mattiaspersson09.junisert.api.assertion.Excluder;
import io.github.mattiaspersson09.junisert.api.assertion.Exclusion;
import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedSupportGenerator;
import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.common.reflection.Field;
import io.github.mattiaspersson09.junisert.common.reflection.Method;
import io.github.mattiaspersson09.junisert.common.reflection.Unit;
import io.github.mattiaspersson09.junisert.core.CachingDependencyGenerator;
import io.github.mattiaspersson09.junisert.core.Junisert;
import io.github.mattiaspersson09.junisert.core.SupportRegistry;
import io.github.mattiaspersson09.junisert.core.ValueCache;
import io.github.mattiaspersson09.junisert.core.internal.InstanceCreator;
import io.github.mattiaspersson09.junisert.core.internal.ValueService;
import io.github.mattiaspersson09.junisert.core.internal.support.SortableSupport;
import io.github.mattiaspersson09.junisert.core.internal.support.SupportComparator;
import io.github.mattiaspersson09.junisert.core.internal.test.AbstractUnitTest;
import io.github.mattiaspersson09.junisert.core.internal.test.UnitTest;
import io.github.mattiaspersson09.junisert.value.common.DependencyObjectValueGenerator;

import java.util.Collections;
import java.util.function.Predicate;

/**
 * Base class for assertions, responsible for dependency injection and resource setup for implementations.
 *
 * @param <A> type of assertion
 */
abstract class AbstractAssertion<A> implements Assertion<A>, Excluder<A> {
    private static final Logger LOGGER = Logger.getLogger(AbstractAssertion.class);

    private final AssertionResource assertionResource;
    private final ValueCache assertionCache;

    protected AbstractAssertion(AssertionResource assertionResource) {
        this.assertionResource = assertionResource;
        this.assertionCache = new ValueCache();
    }

    /**
     * Gets current resources needed for assertion.
     *
     * @return current assertion resource
     */
    protected final AssertionResource getAssertionResource() {
        return assertionResource.getSupport().isEmpty()
                ? assertionResource
                : new AssertionResource(getUnit(), getInstanceCreator(), getValueService(),
                        assertionResource.getExclusion(), assertionResource.getSupport());
    }

    /**
     * Gets current {@link Unit} under assertion.
     *
     * @return current unit
     */
    protected final Unit getUnit() {
        return assertionResource.getUnitUnderAssertion();
    }

    /**
     * Gets current assertion {@link Exclusion} filters.
     *
     * @return current exclusion filters
     */
    protected final Exclusion getExclusion() {
        return assertionResource.getExclusion();
    }

    /**
     * Gets current {@link ValueService}.
     *
     * @return value service
     */
    protected final ValueService getValueService() {
        return assertionResource.getSupport().isEmpty()
                ? assertionResource.getValueService()
                : createValueServiceWithTemporarySupport();
    }

    /**
     * Creates a qualified {@link UnitTest} and injects dependencies needed during construction.
     *
     * @param test to construct
     * @param <T>  test type
     * @return qualified unit test with injected dependencies
     */
    protected final <T extends AbstractUnitTest<T>> T createTest(Class<T> test) {
        try {
            return test.getDeclaredConstructor(ValueService.class, InstanceCreator.class)
                    .newInstance(getValueService(), getInstanceCreator())
                    .withExclusion(assertionResource.getExclusion());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Runs a qualified {@link UnitTest} and injects dependencies needed during construction.
     *
     * @param test to construct and run
     * @param <T>  test type
     */
    protected final <T extends AbstractUnitTest<T>> void runTest(Class<T> test) {
        createTest(test).test(getUnit());
    }

    @Override
    @SuppressWarnings("unchecked")
    public A withSupport(ValueGenerator<?> support) {
        assertionResource.getSupport().add(SortableSupport.toSortable(support));
        assertionResource.getSupport().sort(new SupportComparator());
        LOGGER.config("Registered assertion support: {0}", support);
        return (A) this;
    }

    @Override
    public <T, I extends T> A withSupport(Class<T> superType, Class<I> implementationType, Value<I> implementation) {
        ValueGenerator<?> support = SupportRegistry.createSupport(superType, implementationType, implementation);
        return withSupport(support);
    }

    @Override
    public <T> A withSupport(Class<T> implementationType, Value<T> implementation) {
        return withSupport(implementationType, implementationType, implementation);
    }

    @Override
    @SuppressWarnings("unchecked")
    public A excludingField(Predicate<Field> filter) {
        assertionResource.getExclusion().addFieldExclusion(filter);
        return (A) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public A excludingMethod(Predicate<Method> filter) {
        assertionResource.getExclusion().addMethodExclusion(filter);
        return (A) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public A excluding(Exclusion exclusion) {
        assertionResource.getExclusion().add(exclusion);
        return (A) this;
    }

    private InstanceCreator getInstanceCreator() {
        return assertionResource.getSupport().isEmpty()
                ? assertionResource.getInstanceCreator()
                : createInstanceCreatorWithTemporarySupport();
    }

    private InstanceCreator createInstanceCreatorWithTemporarySupport() {
        AggregatedValueGenerator support = new AggregatedSupportGenerator(assertionResource.getSupport())
                .merge(new AggregatedSupportGenerator(SupportRegistry.get().registeredSupport()))
                .merge(SupportRegistry.get().defaultValueSupport());

        ValueGenerator<?> cachingValueSupport = new CachingDependencyGenerator(support, assertionCache);

        return InstanceCreator.usingConstructor(cachingValueSupport, Junisert.INSTANCE_DEPENDENCY_DEPTH);
    }

    private ValueService createValueServiceWithTemporarySupport() {
        AggregatedValueGenerator support = SupportRegistry.get()
                .defaultValueSupport()
                .mergeFirst(new AggregatedSupportGenerator(assertionResource.getSupport()));
        AggregatedValueGenerator temporary = new AggregatedSupportGenerator(Collections.singletonList(
                DependencyObjectValueGenerator.buildDependencySupport(support)
                        .withForcedAccess()
                        .withMaxDependencyDepth(Junisert.INSTANCE_DEPENDENCY_DEPTH)
                        .build()))
                .mergeFirst(support);

        return new TemporaryValueService(assertionResource.getValueService(), temporary);
    }

    static class TemporaryValueService implements ValueService {
        private final ValueService valueService;
        private final ValueGenerator<?> temporarySupport;

        public TemporaryValueService(ValueService valueService, ValueGenerator<?> temporarySupport) {
            this.valueService = valueService;
            this.temporarySupport = temporarySupport;
        }

        @Override
        public Value<?> getValue(Class<?> type) throws UnsupportedTypeError {
            if (temporarySupport.supports(type)) {
                return temporarySupport.generate(type);
            }

            return valueService.getValue(type);
        }
    }
}
