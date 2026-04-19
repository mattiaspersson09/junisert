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

import io.github.mattiaspersson09.junisert.api.assertion.PlainObjectAssertion;
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertion;
import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedSupportGenerator;
import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.api.internal.support.SupportBuilder;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.logging.Logger;
import io.github.mattiaspersson09.junisert.core.assertion.PlainObjectAssertionImpl;
import io.github.mattiaspersson09.junisert.core.assertion.UnitAssertionImpl;
import io.github.mattiaspersson09.junisert.core.internal.AssertionResource;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Unit;
import io.github.mattiaspersson09.junisert.core.internal.support.PrimitiveSupport;
import io.github.mattiaspersson09.junisert.core.internal.support.SortableSupport;

import java.util.function.Supplier;

/**
 * This is the base class of the framework, acting as facade for asserting on different type of units.
 * All framework handling and assertions should go through this class to ensure internal caching and
 * re-usage of already created values. Default setup and resources will be shared and injected through different
 * assertions by this class.
 */
public final class Junisert {
    private static final Logger LOGGER = Logger.getLogger("Junisert");
    static final int INSTANCE_DEPENDENCY_DEPTH = 3;

    private Junisert() {
    }

    /**
     * Creates a new fluent {@link UnitAssertion} for given {@code unitClass}.
     *
     * @param unitClass to assert on
     * @return a new unit assertion
     */
    public static UnitAssertion assertThatUnit(Class<?> unitClass) {
        return new UnitAssertionImpl(getAssertionResource(unitClass));
    }

    /**
     * Creates a new fluent {@link PlainObjectAssertion} for given {@code pojoClass}.
     *
     * @param pojoClass to assert on
     * @return a new plain object assertion
     */
    public static PlainObjectAssertion assertThatPojo(Class<?> pojoClass) {
        return new PlainObjectAssertionImpl(getAssertionResource(pojoClass));
    }

    /**
     * Will register a custom created support that will support and generate values for a type/several types
     * during assertions.
     *
     * @param support to register
     * @see ValueGenerator
     * @see #registerSupport(Class, Class, Value)
     * @see #registerSupport(Class, Value)
     */
    public static void registerSupport(ValueGenerator<?> support) {
        SupportRegistry.get().register(support);
    }

    /**
     * <strong>OBS!</strong> {@code implementation} should not construct a negative (null or primitive zero/false)
     * value, it will affect value comparison during assertions and produce error-prone results.
     * It's empty representation however is allowed to be negative, see
     * {@link Value#of(Supplier, Supplier) lazy value} and {@link Value#ofEager(Object, Object) eager value}
     * for more details.<br>
     * <br>
     * Will register custom support for a polymorphic type, where you can support a hierarchy of values.
     * During assertions if any type between {@code superType} and {@code implementationType} appears,
     * this support can generate values for it.<br>
     * <br>
     * <p>Example support for {@code CharSequence}, {@code AbstractStringBuilder} and {@code StringBuffer}:
     * <pre>
     * Junisert.registerSupport(CharSequence.class, StringBuffer.class, StringBuffer::new);
     * </pre>
     * Example with the same support but non-null empty value:
     * <pre>
     * Junisert.registerSupport(CharSequence.class, StringBuffer.class, Value.of(() -> new StringBuffer("Junisert"), StringBuffer::new));
     * </pre>
     *
     * @param superType          to support
     * @param implementationType of {@code superType} to support
     * @param implementation     of {@code implementationType} to lazily construct
     * @param <T>                type for super type
     * @param <I>                type for implementation
     * @see #registerSupport(Class, Value)
     */
    public static <T, I extends T> void registerSupport(Class<T> superType,
                                                        Class<I> implementationType,
                                                        Value<I> implementation) {
        ValueGenerator<?> support = implementationType.isPrimitive()
                ? new PrimitiveSupport<>(implementationType, implementation)
                : SupportBuilder.createSupport()
                        .supportSingle(superType, implementationType, implementation)
                        .build();
        registerSupport(new SortableSupport(support));
    }

    /**
     * <strong>OBS!</strong> {@code implementation} should not construct a negative (null or primitive zero/false)
     * value, it will affect value comparison during assertions and produce error-prone results.
     * It's empty representation however is allowed to be negative, see
     * {@link Value#of(Supplier, Supplier) lazy value} and {@link Value#ofEager(Object, Object) eager value}
     * for more details.<br>
     * <br>
     * Will register custom support for a specific type, this will only support a single type and not an entire
     * polymorphic hierarchy.
     * To support a hierarchy
     * {@link #registerSupport(Class, Class, Value) Junisert.registerSupport(Class, Class, Value)}
     * should be used instead.<br>
     * <br>
     * <p>Example support for {@code StringBuffer}:
     * <pre>
     * Junisert.registerSupport(StringBuffer.class, StringBuffer::new);
     * </pre>
     * Example with the same support but non-null empty value:
     * <pre>
     * Junisert.registerSupport(StringBuffer.class, Value.of(() -> new StringBuffer("Junisert"), StringBuffer::new));
     * </pre>
     *
     * @param implementationType to support
     * @param implementation     of {@code implementationType} to lazily construct, not a negative value
     * @param <T>                type of implementation
     * @see #registerSupport(Class, Class, Value)
     */
    public static <T> void registerSupport(Class<T> implementationType, Value<T> implementation) {
        registerSupport(implementationType, implementationType, implementation);
    }

    private static AssertionResource getAssertionResource(Class<?> unitClass) {
        AggregatedValueGenerator valueSupport = SupportRegistry.get()
                .defaultValueSupport()
                .mergeFirst(new AggregatedSupportGenerator(SupportRegistry.get().registeredSupport()));

        ValueGenerator<Object> constructorSupport = new CachingDependencyGenerator(valueSupport,
                SupportRegistry.get().cache());

        return new AssertionResource(Unit.of(unitClass),
                new ConstructorInstanceCreator(constructorSupport, INSTANCE_DEPENDENCY_DEPTH),
                SingletonValueService.getInstance());
    }
}
