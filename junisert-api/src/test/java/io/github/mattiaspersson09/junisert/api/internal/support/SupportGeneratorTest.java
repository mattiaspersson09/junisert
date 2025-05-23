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

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.sort.Order;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Base;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.ExtendingImpl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.OtherImpl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SupportGeneratorTest {
    @Test
    void mustHaveSupportingType() {
        assertThatThrownBy(() -> new SupportGenerator<>(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void mutableSupport() {
        Implementation<Impl> implementation = new Implementation<>(Impl.class, Impl::new);

        assertThat(new SupportGenerator<>(Super.class).size()).isEqualTo(0);
        assertThat(new SupportGenerator<>(Super.class).addSupport(implementation).size()).isEqualTo(1);
    }

    @Test
    void support_whenValueIsImplementationOfType_thenIsSupported() {
        Implementation<Impl> implementation = new Implementation<>(Impl.class, Impl::new);
        SupportGenerator<Super> support = new SupportGenerator<>(Super.class, implementation);
        SupportGenerator<Impl> concreteSupport = new SupportGenerator<>(Impl.class, implementation);

        assertThat(support.supports(Super.class)).isTrue();
        assertThat(support.supports(Base.class)).isTrue();
        assertThat(support.supports(Impl.class)).isTrue();
        assertThat(concreteSupport.supports(Impl.class)).isTrue();
    }

    @Test
    void support_isSupportingDifferentPolymorphicTypes() {
        Implementation<? extends Super> impl1 = new Implementation<>(ExtendingImpl.class, ExtendingImpl::new);
        Implementation<? extends Super> impl2 = new Implementation<>(OtherImpl.class, OtherImpl::new);
        SupportGenerator<Super> support = new SupportGenerator<>(Super.class, Arrays.asList(impl1, impl2));

        assertThat(support.supports(Super.class)).isTrue();
        assertThat(support.supports(Base.class)).isTrue();
        assertThat(support.supports(Impl.class)).isTrue();
        assertThat(support.supports(ExtendingImpl.class)).isTrue();
        assertThat(support.supports(OtherImpl.class)).isTrue();
    }

    @Test
    void support_whenTypeIsNarrowerThanImplementation_thenIsNotSupported() {
        SupportGenerator<Super> support = new SupportGenerator<>(Super.class,
                new Implementation<>(Impl.class, Impl::new));

        assertThat(support.supports(ExtendingImpl.class)).isFalse();
    }

    @Test
    void support_whenValueIsNotImplementationOfType_thenIsNotSupported() {
        Implementation<Impl> implementation = new Implementation<>(Impl.class, Impl::new);
        SupportGenerator<Super> support = new SupportGenerator<>(Super.class, implementation);
        SupportGenerator<Impl> concreteSupport = new SupportGenerator<>(Impl.class, implementation);

        assertThat(support.supports(OtherImpl.class)).isFalse();
        assertThat(support.supports(ExtendingImpl.class)).isFalse();
        assertThat(concreteSupport.supports(Super.class)).isFalse();
        assertThat(concreteSupport.supports(Base.class)).isFalse();
        assertThat(concreteSupport.supports(ExtendingImpl.class)).isFalse();
        assertThat(concreteSupport.supports(OtherImpl.class)).isFalse();
    }

    @Test
    void support_whenSupportIsOnlyForConcreteType_thenWiderTypeIsNotSupported_andNarrowerTypeIsNotSupported() {
        Implementation<Impl> implementation = new Implementation<>(Impl.class, Impl::new);
        SupportGenerator<Impl> concreteSupport = new SupportGenerator<>(Impl.class, implementation);

        assertThat(concreteSupport.supports(Super.class)).isFalse();
        assertThat(concreteSupport.supports(Base.class)).isFalse();
        assertThat(concreteSupport.supports(ExtendingImpl.class)).isFalse();
    }

    @Test
    void generate_whenTypeIsSupported_thenReturnsDesiredValue() {
        Implementation<Impl> impl = new Implementation<>(Impl.class, Impl::new);
        Implementation<ExtendingImpl> extendingImpl = new Implementation<>(ExtendingImpl.class, ExtendingImpl::new);
        Implementation<OtherImpl> otherImpl = new Implementation<>(OtherImpl.class, OtherImpl::new);
        SupportGenerator<Super> support = new SupportGenerator<>(Super.class,
                Arrays.asList(impl, extendingImpl, otherImpl));

        assertThat(support.generate(Super.class).get()).isEqualTo(impl.get());
        assertThat(support.generate(Base.class).get()).isEqualTo(impl.get());
        assertThat(support.generate(Impl.class).get()).isEqualTo(impl.get());
        assertThat(support.generate(ExtendingImpl.class).get()).isEqualTo(extendingImpl.get());
        assertThat(support.generate(OtherImpl.class).get()).isEqualTo(otherImpl.get());
    }

    @Test
    void generate_order() {
        Implementation<Impl> impl = new Implementation<>(Impl.class, Impl::new).order(Order.FIRST);
        Implementation<ExtendingImpl> impl2 = new Implementation<>(ExtendingImpl.class, ExtendingImpl::new);
        Implementation<OtherImpl> impl3 = new Implementation<>(OtherImpl.class, OtherImpl::new);

        SupportGenerator<Super> support = new SupportGenerator<>(Super.class, Arrays.asList(impl3, impl2, impl));

        assertThat(support.generate(Super.class).get()).isEqualTo(impl.get());
    }

    @Test
    void canBeOrdered() {
        SupportGenerator<?> support = new SupportGenerator<>(Super.class);

        assertThat(support.order()).isEqualTo(Order.DEFAULT);
        assertThat(support.order(Order.FIRST).order()).isEqualTo(Order.FIRST);
    }

    @Test
    void order_whenGivenInvalidNewOrder_thenKeepsCurrentOrder() {
        SupportGenerator<?> support = new SupportGenerator<>(Super.class).order(Order.DEFAULT);

        assertThat(support.order(null).order()).isEqualTo(Order.DEFAULT);
    }

    @Test
    void generate_whenTypeIsNotSupported_thenThrowsUnsupportedTypeException() {
        Implementation<Impl> implementation = new Implementation<>(Impl.class, Impl::new);
        SupportGenerator<Super> support = new SupportGenerator<>(Super.class, implementation);

        assertThatThrownBy(() -> support.generate(ExtendingImpl.class)).isInstanceOf(UnsupportedTypeError.class);
        assertThatThrownBy(() -> support.generate(OtherImpl.class)).isInstanceOf(UnsupportedTypeError.class);
    }

    @Test
    void equals_whenSupportingSameType_andSameImplementationType_thenIsEqual() {
        Implementation<Super> implementation = new Implementation<>(Super.class, Impl::new);
        Implementation<Super> otherImplementation = new Implementation<>(Super.class, OtherImpl::new);

        SupportGenerator<?> support = new SupportGenerator<>(Super.class, implementation);
        SupportGenerator<?> otherSupport = new SupportGenerator<>(Super.class, otherImplementation);

        assertThat(support).isEqualTo(otherSupport);
    }

    @Test
    void equals_whenIsReference_thenIsEqual() {
        Implementation<Super> implementation = new Implementation<>(Super.class, Impl::new);
        SupportGenerator<?> support = new SupportGenerator<>(Super.class, implementation);

        assertThat(support).isEqualTo((Object) support);
    }

    @Test
    void equals_whenSupportingSameType_butDifferentImplementationType_thenIsNotEqual() {
        Implementation<Impl> implementation = new Implementation<>(Impl.class, Impl::new);
        Implementation<OtherImpl> otherImplementation = new Implementation<>(OtherImpl.class, OtherImpl::new);

        SupportGenerator<?> support = new SupportGenerator<>(Super.class, implementation);
        SupportGenerator<?> otherSupport = new SupportGenerator<>(Super.class, otherImplementation);
        SupportGenerator<?> concreteSupport = new SupportGenerator<>(Impl.class, implementation);

        assertThat(support).isNotEqualTo(otherSupport);
        assertThat(support).isNotEqualTo(concreteSupport);
    }

    @Test
    void equals_whenIsNotTheSameClass_thenIsNotEqual() {
        Implementation<Impl> implementation = new Implementation<>(Impl.class, Impl::new);

        SupportGenerator<Super> support = new SupportGenerator<>(Super.class, implementation);
        ValueGenerator<Super> other = new ValueGenerator<Super>() {
            @Override
            public Value<Super> generate(Class<?> fromType) throws UnsupportedTypeError {
                return null;
            }

            @Override
            public boolean supports(Class<?> type) {
                return false;
            }
        };

        assertThat(support).isNotEqualTo(other);
    }

    @Test
    void equals_whenIsNull_thenIsNotEqual() {
        Implementation<Impl> implementation = new Implementation<>(Impl.class, Impl::new);

        SupportGenerator<?> support = new SupportGenerator<>(Super.class, implementation);

        assertThat(support).isNotEqualTo(null);
    }

    @Test
    void hashCode_whenSupportingSameType_andSameImplementationType_thenIsEqual() {
        Implementation<Super> implementation = new Implementation<>(Super.class, Impl::new);
        Implementation<Super> otherImplementation = new Implementation<>(Super.class, OtherImpl::new);

        SupportGenerator<?> support = new SupportGenerator<>(Super.class, implementation);
        SupportGenerator<?> otherSupport = new SupportGenerator<>(Super.class, otherImplementation);

        assertThat(support.hashCode()).isEqualTo(otherSupport.hashCode());
    }

    @Test
    void hashCode_whenSupportingSameType_butDifferentImplementationType_thenIsNotEqual() {
        Implementation<Impl> implementation = new Implementation<>(Impl.class, Impl::new);
        Implementation<OtherImpl> otherImplementation = new Implementation<>(OtherImpl.class, OtherImpl::new);

        SupportGenerator<?> support = new SupportGenerator<>(Super.class, implementation);
        SupportGenerator<?> otherSupport = new SupportGenerator<>(Super.class, otherImplementation);
        SupportGenerator<?> concreteSupport = new SupportGenerator<>(Impl.class, implementation);

        assertThat(support.hashCode()).isNotEqualTo(otherSupport.hashCode());
        assertThat(support.hashCode()).isNotEqualTo(concreteSupport.hashCode());
    }
}
