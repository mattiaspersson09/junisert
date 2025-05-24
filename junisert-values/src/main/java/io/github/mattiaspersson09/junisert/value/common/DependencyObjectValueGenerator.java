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
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.logging.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * {@link ValueGenerator} supporting and creating instance values of objects using <em>argument constructors</em>.
 * If an argument constructor (having parameters) isn't present in the class then it's not supported.
 *
 * <p><br>
 * This generator supports dependency recursion, meaning that if a parameter is found that is not directly supported
 * by a given dependency generator, this generator will try to construct instance of that object as well using
 * reflection. This operation can be configured by building a {@code DependencyObjectValueGenerator} with a
 * {@code maxDependencyDepth} using {@link DependencyObjectValueGenerator.Builder}. Dependency instances is
 * recursively created until {@code maxDependencyDepth} is reached or a construction on the way there is unsupported.
 *
 * @see #buildDependencySupport(ValueGenerator)
 */
public class DependencyObjectValueGenerator implements ValueGenerator<Object> {
    private static final Logger LOGGER = Logger.getLogger(DependencyObjectValueGenerator.class);
    static final int MAX_DEPENDENCY_DEPTH = 5;

    private final ValueGenerator<?> dependencyGenerator;
    private final boolean forceConstructorAccess;
    private final int maxDependencyDepth;

    /**
     * Creates a new dependency object value generator, without trying to force access to argument constructor
     * with reflection and {@code maxDependencyDepth = 0}.
     *
     * @param dependencyGenerator non-null {@link ValueGenerator} creating values for supported constructor parameters
     */
    public DependencyObjectValueGenerator(ValueGenerator<?> dependencyGenerator) {
        this.dependencyGenerator = Objects.requireNonNull(dependencyGenerator);
        this.forceConstructorAccess = false;
        this.maxDependencyDepth = 0;
    }

    DependencyObjectValueGenerator(ValueGenerator<?> dependencyGenerator,
                                   boolean forceConstructorAccess,
                                   int maxDependencyDepth) {
        this.dependencyGenerator = dependencyGenerator;
        this.forceConstructorAccess = forceConstructorAccess;
        this.maxDependencyDepth = maxDependencyDepth;
    }

    /**
     * Creates a new {@code DependencyObjectValueGenerator} with given {@link ValueGenerator} that
     * will help with generating dependency values if supported. Created generator won't be able to handle unsupported
     * dependencies, to be able to handle deep dependency generation use {@link #buildDependencySupport(ValueGenerator)}
     * and build the generator with {@link Builder#withMaxDependencyDepth(int) withMaxDependencyDepth(int)} instead.
     *
     * @param dependencyGenerator non-null creating values for supported constructor parameters
     * @return a new {@code DependencyObjectValueGenerator}
     */
    public static DependencyObjectValueGenerator withForcedAccess(ValueGenerator<?> dependencyGenerator) {
        return new DependencyObjectValueGenerator(dependencyGenerator, true, 0);
    }

    /**
     * Starts building a new {@code DependencyObjectValueGenerator} with given {@link ValueGenerator} that
     * will help with generating dependency values if supported.
     *
     * @param dependencyGenerator non-null that can generate dependency values
     * @return a new {@link Builder}
     * @see ValueGenerator
     */
    public static Builder buildDependencySupport(ValueGenerator<?> dependencyGenerator) {
        return new Builder(dependencyGenerator);
    }

    @Override
    public Value<?> generate(Class<?> fromType) throws UnsupportedTypeError, UnsupportedConstructionError {
        if (WrapperPrimitiveValueGenerator.isWrapperPrimitive(fromType)) {
            throw new UnsupportedTypeError(fromType);
        }

        Optional<Constructor<?>> argumentConstructor = findConstructor(fromType, this::isArgumentConstructor);

        if (argumentConstructor.isPresent()) {
            Constructor<?> constructor = argumentConstructor.get();

            try {
                if (forceConstructorAccess) {
                    constructor.setAccessible(true);
                }

                Object[] arguments = Stream.of(constructor.getParameters())
                        .map(Parameter::getType)
                        .map(parameter -> createInjectableDependency(fromType, parameter))
                        .toArray();

                /*
                    We choose to fail fast instead of letting user figure out why reflection fails later
                    if we were to use lazy construction.
                */
                return Value.ofEager(constructor.newInstance(arguments));
            } catch (Exception e) {
                throw new UnsupportedConstructionError(fromType, e);
            }
        }

        throw new UnsupportedTypeError(fromType);
    }

    @Override
    public boolean supports(Class<?> type) {
        if (WrapperPrimitiveValueGenerator.isWrapperPrimitive(type)) {
            return false;
        }

        Optional<Constructor<?>> argumentConstructor = findConstructor(type, this::isArgumentConstructor);

        if (!argumentConstructor.isPresent()) {
            return false;
        }

        Constructor<?> constructor = argumentConstructor.get();

        if (!isConstructable(constructor)) {
            return false;
        }

        Parameter[] parameters = constructor.getParameters();

        for (Parameter parameter : parameters) {
            boolean isDependencyPotentiallyConstructable = Dependency.shouldBeConstructable(
                    findConstructorWithFewestParameters(parameter.getType()).orElse(null),
                    forceConstructorAccess
            );

            if (!dependencyGenerator.supports(parameter.getType()) && !isDependencyPotentiallyConstructable) {
                return false;
            }
        }

        return true;
    }

    private boolean isArgumentConstructor(Constructor<?> constructor) {
        return constructor.getParameterCount() > 0;
    }

    private boolean isConstructable(Constructor<?> constructor) {
        return Dependency.shouldBeConstructable(constructor, forceConstructorAccess);
    }

    private Object createInjectableDependency(Class<?> dependentUnit, Class<?> parameter) {
        // If there is support for the parameter type, let the support construct it and ignore dependency construction
        if (dependencyGenerator.supports(parameter)) {
            return dependencyGenerator.generate(parameter).get();
        }

        Dependency dependency = new Dependency(
                dependentUnit,
                findConstructorWithFewestParameters(parameter).orElse(null),
                dependencyGenerator,
                forceConstructorAccess,
                maxDependencyDepth,
                nestedDependency -> findConstructorWithFewestParameters(nestedDependency).orElse(null)
        );

        // Dependency is not abstract or an interface and can access constructor to create instance
        if (dependency.isSupported()) {
            return dependency.createInstance();
        }

        // There are deeper dependencies that we can't directly construct, at this point we can't continue
        LOGGER.warn("Unable to find support for dependency <{0}> in dependent unit: {1}",
                parameter, dependentUnit);
        throw new UnsupportedTypeError(parameter);
    }

    private Optional<Constructor<?>> findConstructor(Class<?> type, Predicate<Constructor<?>> predicate) {
        return Stream.of(type.getDeclaredConstructors())
                .filter(predicate)
                .min(Comparator.comparingInt(Constructor::getParameterCount));
    }

    private Optional<Constructor<?>> findConstructorWithFewestParameters(Class<?> type) {
        return Stream.of(type.getDeclaredConstructors())
                .min(Comparator.comparingInt(Constructor::getParameterCount));
    }

    int getMaxDependencyDepth() {
        return maxDependencyDepth;
    }

    /**
     * Building new {@link DependencyObjectValueGenerator}s with desired properties.
     */
    public static class Builder {
        private final ValueGenerator<?> dependencyGenerator;
        private boolean forcedConstructorAccess;
        private int dependencyDepth;

        /**
         * Creates a new builder.
         *
         * @param dependencyGenerator non-null creating values for supported constructor parameters
         */
        public Builder(ValueGenerator<?> dependencyGenerator) {
            this.dependencyGenerator = Objects.requireNonNull(dependencyGenerator);
        }

        /**
         * Try to force constructor access (if inaccessible) with reflection.
         *
         * @return this builder to continue building
         */
        public Builder withForcedAccess() {
            this.forcedConstructorAccess = true;
            return this;
        }

        /**
         * Sets max acceptable depth dependencies can recursively handle their own dependencies with reflection
         * if needed. A dependency will first and foremost try to get dependency values from given
         * {@code dependencyGenerator}, otherwise with reflection if {@code maxDependencyDepth > 0}.
         * Depth and recursion is <strong>only</strong> triggered if a dependency is found which is not already
         * supported and has no accessible <em>default constructor</em>.
         * Depth can be set to {@code 0} to boost creation time if support for all dependencies
         * are registered.<br>
         * <br>
         * Too large depth will affect performance, so it's currently capped.
         *
         * @param maxDependencyDepth larger or equal to 0, but capped at {@value #MAX_DEPENDENCY_DEPTH}
         * @return this builder to continue building
         */
        public Builder withMaxDependencyDepth(int maxDependencyDepth) {
            this.dependencyDepth = maxDependencyDepth;
            return this;
        }

        /**
         * Creates a new {@link DependencyObjectValueGenerator} with built properties.
         *
         * @return a new {@link DependencyObjectValueGenerator} with built properties
         * @throws IllegalArgumentException if trying to build generator with a dependency depth larger than
         *                                  {@value #MAX_DEPENDENCY_DEPTH}
         */
        public DependencyObjectValueGenerator build() throws IllegalArgumentException {
            if (dependencyDepth > MAX_DEPENDENCY_DEPTH) {
                throw new IllegalArgumentException("Dependency depth isn't allowed be larger than "
                        + MAX_DEPENDENCY_DEPTH);
            }

            int maxDepth = Math.max(dependencyDepth, 0);
            LOGGER.config("Built with max accepted dependency depth: {0}", maxDepth);

            return new DependencyObjectValueGenerator(dependencyGenerator, forcedConstructorAccess, maxDepth);
        }
    }
}
