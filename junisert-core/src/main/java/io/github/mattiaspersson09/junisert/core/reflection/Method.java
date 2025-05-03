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
package io.github.mattiaspersson09.junisert.core.reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Objects;

/**
 * Representing a reflected method as member of a unit, being a wrapper for {@link java.lang.reflect.Method}.
 */
public class Method extends ExecutableMember implements Invokable {
    private final java.lang.reflect.Method origin;

    Method(java.lang.reflect.Method origin) {
        super(origin);
        this.origin = origin;
        this.origin.setAccessible(true);
    }

    /**
     * Creates a reflected method wrapper from {@code origin} method.
     *
     * @param origin to wrap
     * @return reflected method
     */
    public static Method of(java.lang.reflect.Method origin) {
        return new Method(origin);
    }

    /**
     * Checks if this method is returning exactly given {@code type}. To check polymorphic types then
     * {@link #hasReturnTypeAssignableFrom(Class)} and {@link #hasReturnTypeAssignableTo(Class)} should be used instead.
     *
     * @param type this method might return
     * @return true if given type is returned by this method
     * @see #hasReturnTypeAssignableFrom(Class)
     * @see #hasReturnTypeAssignableTo(Class)
     */
    public boolean hasReturnType(Class<?> type) {
        return getType().equals(type);
    }

    /**
     * Checks if this method is returning a super type of or exactly given {@code subType}.
     *
     * @param type of the returning type
     * @return true if returning type
     * @see #isProducing(Class)
     */
    public boolean hasReturnTypeAssignableFrom(Class<?> type) {
        return getType().isAssignableFrom(type);
    }

    /**
     * Checks if this method is returning a subtype of or exactly given {@code superType}.
     *
     * @param type of the returning type
     * @return true if returning type
     */
    public boolean hasReturnTypeAssignableTo(Class<?> type) {
        return type.isAssignableFrom(getType());
    }

    /**
     * Checks if this method is considered an action method, having no return value other than {@code void}
     * and accepts no arguments.
     *
     * @return true if this method is considered an action
     * @see #isConsumingOnly(Class)
     */
    public boolean isAction() {
        return hasReturnType(void.class) && hasNoParameters();
    }

    /**
     * Checks if this method is considered a consuming method, having no return value other than {@code void} and
     * accepts at least one argument for consumption.
     *
     * @return true if this method is considered a consumer
     * @see #isAction()
     * @see #isConsuming(Class)
     * @see #isConsumingOnly(Class)
     */
    public boolean isConsumer() {
        return hasReturnType(void.class) && hasParameters();
    }

    /**
     * Checks if this method is a consumer and accepts at least given {@code type} as argument.
     * To check if this method only accepts a <em>single</em> argument then {@link #isConsumingOnly(Class)} should be
     * used.
     *
     * @param type or subtype this method can consume
     * @return true if this method consumes given type
     * @see #isConsumingOnly(Class)
     */
    public boolean isConsuming(Class<?> type) {
        return isConsumer() && hasParameterAssignableFrom(type);
    }

    /**
     * Checks if this method consumes a single argument of given {@code type} and does not produce any result.
     * Can be seen as equivalent of a setter or an action accepting an argument.
     *
     * @param type or subtype this method can consume
     * @return true if this method only consumes given type
     */
    public boolean isConsumingOnly(Class<?> type) {
        return isConsuming(type) && hasParameterCount(1);
    }

    /**
     * Checks if this method is considered a producing method, producing a result and accepting no arguments.
     *
     * @return true if this method is considered a producer
     * @see #isFunction()
     * @see #isProducing(Class)
     */
    public boolean isProducer() {
        return !hasReturnType(void.class) && hasNoParameters();
    }

    /**
     * Checks if this method is a producer for given {@code type} and can be seen as
     * equivalent to a getter or factory method.
     *
     * @param type this method is producing
     * @return true if this method is producing given {@code type}
     * @see #isProducer()
     * @see #isFunctionOf(Class, Class)
     */
    public boolean isProducing(Class<?> type) {
        return isProducer() && hasReturnTypeAssignableFrom(type);
    }

    /**
     * Checks if this method is considered a function, accepting {@code 1..N} values and producing a result.
     *
     * @return true if this method is considered a function
     * @see #isProducer()
     * @see #isFunctionOf(Class, Class)
     */
    public boolean isFunction() {
        return !hasReturnType(void.class) && hasParameters();
    }

    /**
     * Checks if this method is considered a function that only accepts {@code input} as argument and producing
     * {@code result}. Can be seen as equivalent of a mapping- or builder-method.
     *
     * @param input  only accepted argument
     * @param result produced after handling input
     * @return true if this method is only accepting given input and producing given result
     * @see #isConsumingOnly(Class)
     * @see #isProducing(Class)
     */
    public boolean isFunctionOf(Class<?> input, Class<?> result) {
        return hasReturnTypeAssignableFrom(result) && hasParameterCount(1) && hasParameterAssignableFrom(input);
    }

    @Override
    public Class<?> getType() {
        return origin.getReturnType();
    }

    @Override
    public Object invoke(Object instance, Object... args) throws InvocationTargetException, IllegalAccessException {
        return origin.invoke(instance, args);
    }

    @Override
    public Collection<Class<?>> accepts() {
        return getParameterTypes();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Method method = (Method) o;
        return Objects.equals(origin, method.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), origin);
    }
}
