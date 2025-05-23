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
package io.github.mattiaspersson09.junisert.api.internal.support;

import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Base;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.ExtendingImpl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.OtherImpl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SupportBuilderTest {
    @Test
    void buildingSupport_whenBuildingSeveralSupports_thenCanChainBuildSupport() {
        AggregatedValueGenerator support = SupportBuilder.createSupport()
                .support(OtherImpl.class)
                .withImplementation(OtherImpl.class, OtherImpl::new)
                .support(Impl.class)
                .withImplementation(ExtendingImpl.class, ExtendingImpl::new)
                .supportSingle(Base.class, Impl::new)
                .supportSingle(Super.class, Impl.class, Impl::new)
                .build();

        assertThat(support.aggregated()).hasSize(4);
    }

    @Test
    void buildingSupport_whenNotFinishingSupportWithImplementations_butHasOtherSupport_thenIgnoresEmptySupport() {
        AggregatedValueGenerator support = SupportBuilder.createSupport()
                .support(Impl.class)
                .support(Super.class)
                .supportSingle(OtherImpl.class, OtherImpl.class, OtherImpl::new)
                .support(Base.class)
                .supportSingle(ExtendingImpl.class, ExtendingImpl::new)
                .build();

        assertThat(support.aggregated())
                .hasSize(2)
                .noneMatch(generator -> generator.supports(Super.class))
                .noneMatch(generator -> generator.supports(Impl.class))
                .noneMatch(generator -> generator.supports(Base.class))
                .anyMatch(generator -> generator.supports(OtherImpl.class))
                .anyMatch(generator -> generator.supports(ExtendingImpl.class));
    }

    @Test
    void buildingSupport_whenTryingToBuildEmptySupport_thenThrowsUnsupportedOperationException() {
        SupportBuilder.Support<?> support = SupportBuilder.createSupport()
                .support(Super.class);

        SupportBuilder supportBuilder = SupportBuilder.createSupport();

        assertThatThrownBy(support::build)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Not allowed to build an empty support");
        assertThatThrownBy(supportBuilder::build)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Not allowed to build an empty support");
    }

    @Test
    void builtSupport_whenBuildingSeveralSupportImplementations_thenBuiltSupportForAllImplementations() {
        AggregatedValueGenerator support = SupportBuilder.createSupport()
                .support(Super.class)
                .withImplementation(Impl.class, Impl::new)
                .withImplementation(OtherImpl.class, OtherImpl::new)
                .build();

        assertThat(support.aggregated())
                .hasSize(1)
                .allMatch(generator -> generator.supports(Impl.class))
                .allMatch(generator -> generator.supports(OtherImpl.class));

        ValueGenerator<?> supportGenerator = support.aggregated()
                .stream()
                .findFirst()
                .orElseThrow(RuntimeException::new);

        assertThat(supportGenerator.generate(Impl.class).get()).isEqualTo(new Impl());
        assertThat(supportGenerator.generate(OtherImpl.class).get()).isEqualTo(new OtherImpl());
    }

    @Test
    void builtSupport_whenBuildingSameSupportSeveralTimes_thenOnlyOneSupportIsAdded() {
        AggregatedValueGenerator support = SupportBuilder.createSupport()
                .support(Super.class)
                .withImplementation(Impl.class, Impl::new)
                .supportSingle(Super.class, Impl.class, Impl::new)
                .supportSingle(Super.class, Impl.class, Impl::new)
                .build();

        assertThat(support.aggregated())
                .hasSize(1)
                .allMatch(generator -> generator.supports(Super.class))
                .allMatch(generator -> generator.supports(Base.class))
                .allMatch(generator -> generator.supports(Impl.class));
    }

    @Test
    void builtSupport_whenSupportingSameTypeButDifferentImplementations_thenBuiltWithAllSupports() {
        AggregatedValueGenerator support = SupportBuilder.createSupport()
                .support(Super.class)
                .withImplementation(ExtendingImpl.class, ExtendingImpl::new)
                .supportSingle(Super.class, Impl.class, Impl::new)
                .supportSingle(Super.class, OtherImpl.class, OtherImpl::new)
                .build();

        assertThat(support.aggregated())
                .hasSize(3)
                .allMatch(generator -> generator.supports(Super.class))
                .anyMatch(generator -> generator.supports(Impl.class))
                .anyMatch(generator -> generator.supports(ExtendingImpl.class))
                .anyMatch(generator -> generator.supports(OtherImpl.class));
    }

    @Test
    void builtSupport_whenSupportingPolymorphicChain_thenBuiltSupportForWholeChain() {
        AggregatedValueGenerator support = SupportBuilder.createSupport()
                .supportSingle(Super.class, ExtendingImpl.class, ExtendingImpl::new)
                .build();

        assertThat(support.aggregated())
                .hasSize(1)
                .allMatch(generator -> generator.supports(Super.class))
                .allMatch(generator -> generator.supports(Base.class))
                .anyMatch(generator -> generator.supports(Impl.class))
                .anyMatch(generator -> generator.supports(ExtendingImpl.class));

        ValueGenerator<?> supportGenerator = support.aggregated()
                .stream()
                .findFirst()
                .orElseThrow(RuntimeException::new);

        assertThat(supportGenerator.generate(Super.class).get()).isEqualTo(new ExtendingImpl());
        assertThat(supportGenerator.generate(Base.class).get()).isEqualTo(new ExtendingImpl());
        assertThat(supportGenerator.generate(Impl.class).get()).isEqualTo(new ExtendingImpl());
        assertThat(supportGenerator.generate(ExtendingImpl.class).get()).isEqualTo(new ExtendingImpl());
    }

    @Test
    void builtSupport_whenSupportingSpecificType_thenBuiltSupportForJustThatType() {
        AggregatedValueGenerator support = SupportBuilder.createSupport()
                .supportSingle(ExtendingImpl.class, ExtendingImpl::new)
                .build();

        assertThat(support.aggregated())
                .hasSize(1)
                .noneMatch(generator -> generator.supports(Super.class))
                .noneMatch(generator -> generator.supports(Base.class))
                .noneMatch(generator -> generator.supports(Impl.class))
                .anyMatch(generator -> generator.supports(ExtendingImpl.class));
    }
}
