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

import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedSupportGenerator;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.testunits.constructor.RecursiveArgThrowingConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DependencyObjectDeepConstructionTest {
    @Test
    void givenDependencies_whenMaxDependencyDepthReachesConstructionOfDeepestDependency_thenCanConstruct() {
        AggregatedSupportGenerator argumentGenerator = new AggregatedSupportGenerator(Arrays.asList(
                ObjectValueGenerator.withForcedAccess(),
                new PrimitiveValueGenerator()
        ));
        DependencyObjectValueGenerator generator = DependencyObjectValueGenerator
                .buildDependencySupport(argumentGenerator)
                .withForcedAccess()
                .withMaxDependencyDepth(2)
                .build();

        InceptionArgConstructor inception = (InceptionArgConstructor) generator.generate(InceptionArgConstructor.class)
                .get();

        assertThat(inception).isNotNull();
        assertThat(inception.arg).isNotNull();
        // First dependency depth
        assertThat(inception.arg.deeperArg).isNotNull();
        // Second dependency depth
        assertThat(inception.arg.deeperArg.evenDeeperArg).isNotNull();
        // Has registered support for Object (default constructor)
        assertThat(inception.arg.deeperArg.evenDeeperArg.deepestObject).isNotNull();
    }

    @Test
    void givenDependencies_whenMaxDependencyDepthTooShallow_andNotSupportingDependencyOnThatDepth_thenThrowsUnsupportedTypeError() {
        AggregatedSupportGenerator argumentGenerator = new AggregatedSupportGenerator(Arrays.asList(
                ObjectValueGenerator.withForcedAccess(),
                new PrimitiveValueGenerator()
        ));
        DependencyObjectValueGenerator generator = DependencyObjectValueGenerator
                .buildDependencySupport(argumentGenerator)
                .withForcedAccess()
                .withMaxDependencyDepth(1)
                .build();

        assertThatThrownBy(() -> generator.generate(InceptionArgConstructor.class).get())
                .isInstanceOf(UnsupportedTypeError.class)
                .hasMessageContaining("support for type")
                .hasMessageContaining("EvenDeeperArg");
    }

    @Test
    void givenDependencies_whenMaxDepthReachesEnd_butDependencyOnTheWayIsNotSupported_thenThrowsUnsupportedTypeError() {
        AggregatedSupportGenerator argumentGenerator = new AggregatedSupportGenerator(Collections.singletonList(
                ObjectValueGenerator.withForcedAccess()
        ));
        DependencyObjectValueGenerator generator = DependencyObjectValueGenerator
                .buildDependencySupport(argumentGenerator)
                .withForcedAccess()
                .withMaxDependencyDepth(5)
                .build();
        ;
        assertThatThrownBy(() -> generator.generate(InceptionArgConstructor.class).get())
                .isInstanceOf(UnsupportedTypeError.class)
                .hasMessageContaining("support for type 'int'");
    }

    @Test
    void givenDependencies_whenDependencyCantBeConstructed_thenThrowsUnsupportedConstructionError() {
        AggregatedSupportGenerator argumentGenerator = new AggregatedSupportGenerator(Collections.singletonList(
                ObjectValueGenerator.withForcedAccess()
        ));
        DependencyObjectValueGenerator generator = DependencyObjectValueGenerator
                .buildDependencySupport(argumentGenerator)
                .withForcedAccess()
                .withMaxDependencyDepth(0)
                .build();

        assertThatThrownBy(() -> generator.generate(RecursiveArgThrowingConstructor.class).get())
                .isInstanceOf(UnsupportedConstructionError.class);
    }

    public static class InceptionArgConstructor {
        Arg arg;

        public InceptionArgConstructor(Arg arg) {
            this.arg = arg;
        }

        @Override
        public String toString() {
            return "InceptionArgConstructor{" +
                    "arg=" + arg +
                    '}';
        }

        public static class Arg {
            DeeperArg deeperArg;

            public Arg(DeeperArg deeperArg, int intArg) {
                this.deeperArg = deeperArg;
                if (intArg == 0) {
                    throw new IllegalArgumentException();
                }
            }

            @Override
            public String toString() {
                return "Arg{" +
                        "arg=" + deeperArg +
                        '}';
            }
        }

        public static class DeeperArg {
            EvenDeeperArg evenDeeperArg;

            public DeeperArg(EvenDeeperArg evenDeeperArg) {
                this.evenDeeperArg = evenDeeperArg;
            }

            @Override
            public String toString() {
                return "DeeperArg{" +
                        "arg=" + evenDeeperArg +
                        '}';
            }
        }

        public static class EvenDeeperArg {
            Object deepestObject;

            public EvenDeeperArg(Object deepestObject) {
                this.deepestObject = Objects.requireNonNull(deepestObject);
            }

            @Override
            public String toString() {
                return "EvenDeeperArg{" +
                        "arg=" + deepestObject +
                        '}';
            }
        }
    }
}
