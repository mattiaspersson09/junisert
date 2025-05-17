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
package io.github.mattiaspersson09.junisert.value.common;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.logging.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Presented as a field type, method parameter or constructor parameter. A dependency is either already supported
 * and can easily be constructed using a given support {@link ValueGenerator} or needs to be reflectively
 * constructed. {@link ValueGenerator} given for a dependency should have registered support
 * and preferably caching abilities, to be performance friendly.<br>
 * <br>
 * A dependency will try to handle its own dependencies with supporting generator or with reflection if given
 * {@code dependencyDepthsLeft is above 0}. Too large depth will affect performance, so it should
 * be capped at a decent level by the caller.
 */
final class Dependency {
    private static final Logger LOGGER = Logger.getLogger(Dependency.class);

    private final Class<?> dependentUnit;
    private final Constructor<?> constructor;
    private final ValueGenerator<?> valueSupport;
    private final boolean forceConstructorAccess;
    private final int dependencyDepthsLeft;
    private final Function<Class<?>, Constructor<?>> extractDependencyConstructor;

    /**
     * Creates a new dependency to be constructed for a dependent unit.
     *
     * @param dependentUnit                wanting instance of this dependency
     * @param dependencyConstructor        this dependency should be instantiated from
     * @param valueSupport                 to help generating this dependency's own dependencies if needed
     * @param forceConstructorAccess       with reflection if necessary when {@code dependencyConstructor} is
     *                                     inaccessible
     * @param dependencyDepthsLeft         to go even deeper in dependency stack, reflectively if needed
     * @param extractDependencyConstructor function to find a deeper dependency's suitable constructor if needed and
     *                                     {@code dependencyDepthsLeft} is above zero
     */
    Dependency(Class<?> dependentUnit,
               Constructor<?> dependencyConstructor,
               ValueGenerator<?> valueSupport,
               boolean forceConstructorAccess,
               int dependencyDepthsLeft,
               Function<Class<?>, Constructor<?>> extractDependencyConstructor) {
        this.dependentUnit = dependentUnit;
        this.constructor = dependencyConstructor;
        this.forceConstructorAccess = forceConstructorAccess;
        this.valueSupport = valueSupport;
        this.dependencyDepthsLeft = dependencyDepthsLeft;
        this.extractDependencyConstructor = extractDependencyConstructor;
    }

    /**
     * Creates an instance of this dependency using supplied constructor. This dependency will also,
     * if instructed with given {@code dependencyDepthsLeft above zero}, try to reflectively create a new
     * {@link Dependency} of its own.
     * This will start a recursive dependency construction chain for each parameter found.
     * <strong>But</strong>, only if {@link ValueGenerator} value support does not support the parameter.
     *
     * @return instance of this dependency
     * @throws UnsupportedTypeError         if unable to find support for or reflectively create a parameter
     * @throws UnsupportedConstructionError if not supporting construction of this dependency, using given constructor
     *                                      and found parameters (if any)
     */
    Object createInstance() throws UnsupportedTypeError, UnsupportedConstructionError {
        // TODO: ability to switch to non-recursive constructor, if this is recursive
        // Scenario: dependency has dependency to an instance of itself in a constructor, to supply values
        // Potential units like this: immutable models with one all-argument constructor and one as above scenario
        Constructor<?> invokingConstructor = constructor;

        try {
            if (forceConstructorAccess) {
                invokingConstructor.setAccessible(true);
            }

            LOGGER.info("Finding dependency support for constructor: {0}", invokingConstructor);

            Object[] arguments = Stream.of(invokingConstructor.getParameters())
                    .map(this::toValue)
                    .toArray();

            return invokingConstructor.newInstance(arguments);
        } catch (Exception e) {
            LOGGER.warn("Unable to construct dependency <{0}> for dependent unit: {1}",
                    invokingConstructor.getDeclaringClass(), dependentUnit);
            LOGGER.warn("Register support for <{0}> to avoid this issue", invokingConstructor.getDeclaringClass());
            throw new UnsupportedConstructionError(constructor.getDeclaringClass(), e);
        }
    }

    /**
     * Checks if this dependency should be able to be constructed with currently given constructor.
     * <strong>Supporting generator should be prioritized</strong> to prevent unnecessary work, this check should only
     * be used after support said a parameter (dependency type) is not supported.<br>
     * <br>
     * Conditions: instance from given constructor is only constructable if this dependency is <em>not abstract</em>
     * and the constructor is either public or is forced access with reflection.
     *
     * @return true if given constructor should be able to construct this dependency
     * @see ValueGenerator#supports(Class)
     */
    boolean isSupported() {
        return shouldBeConstructable(constructor, forceConstructorAccess);
    }

//    private Optional<Constructor<?>> findNonRecursiveConstructor() {
//        return Stream.of(constructor.getDeclaringClass().getDeclaredConstructors())
//                .filter(c -> !isRecursiveConstructor(c))
//                .findAny();
//    }

    private Object toValue(Parameter parameter) {
        // Support generator should be prioritized to be able to use caching abilities and re-usage
        // and to prevent unnecessary work that might affect performance
        if (valueSupport.supports(parameter.getType())) {
            return valueSupport.generate(parameter.getType()).get();
        } else if (isRecursiveParameter(parameter) || isCyclicParameter(parameter)) {
            // TODO: try to switch to non-recursive constructor if possible, constructor might validate input
            LOGGER.info("Found recursive or cyclic (leading to recursion) parameter: {0}", parameter.getType());

            if (dependencyDepthsLeft == 0) {
                LOGGER.info(
                        "Is at deepest accepted dependency, will set <null> for <{0}> to avoid vicious recursive cycle",
                        parameter.getType());
                return null;
            }
        }

        if (dependencyDepthsLeft > 0 && extractDependencyConstructor != null) {
            Dependency dependency = createDependency(parameter);

            if (dependency.isSupported()) {
                return dependency.createInstance();
            }
        }

        LOGGER.warn("Unable to find support for dependency <{0}> in dependent unit: {1}",
                parameter.getType(), constructor.getDeclaringClass());
        throw new UnsupportedTypeError(parameter.getType());
    }

    private Dependency createDependency(Parameter parameter) {
        return new Dependency(
                constructor.getDeclaringClass(),
                extractDependencyConstructor.apply(parameter.getType()),
                valueSupport,
                forceConstructorAccess,
                dependencyDepthsLeft - 1,
                extractDependencyConstructor);
    }

    private boolean isRecursiveParameter(Parameter parameter) {
        return parameter.getType().equals(constructor.getDeclaringClass());
    }

    // A cyclic parameter is described as a parameter that; if trying to construct leads to indirect recursion
    private boolean isCyclicParameter(Parameter parameter) {
        if (extractDependencyConstructor == null) {
            return false;
        }

        Constructor<?> constructorConstructionLeadsTo = extractDependencyConstructor.apply(parameter.getType());
        return isRecursiveConstructor(constructorConstructionLeadsTo);
    }

    private boolean isRecursiveConstructor(Constructor<?> constructor) {
        return constructor != null && Stream.of(constructor.getParameters())
                .anyMatch(this::isRecursiveParameter);
    }

    /**
     * Checking constructable should not care about support generator for dependencies, just manual construction.
     * <strong>Supporting generator should be prioritized</strong> to prevent unnecessary work, this check should only
     * be used after support said a parameter (dependency type) is not supported.<br>
     * <br>
     * Conditions: instance from given constructor is only constructable if this dependency is <em>not abstract</em>
     * and the constructor is either public or is forced access with reflection.
     *
     * @param constructor     this dependency will be constructed from
     * @param isForcingAccess to (potentially) inaccessible constructor with reflection
     * @return true if given constructor should be able to construct this dependency
     * @see ValueGenerator#supports(Class)
     */
    static boolean shouldBeConstructable(Constructor<?> constructor, boolean isForcingAccess) {
        return constructor != null
                && !Modifier.isAbstract(constructor.getDeclaringClass().getModifiers())
                && (Modifier.isPublic(constructor.getModifiers()) || isForcingAccess);
    }
}
