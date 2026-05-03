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
package io.github.mattiaspersson09.junisert.api.internal.support;

import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Base;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.ExtendingImpl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.OtherImpl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImplementationTest {
    @Test
    void get() {
        Implementation<?> value = new Implementation<>(Impl.class, Impl::new);

        assertThat(value.get()).isEqualTo(new Impl());
    }

    @Test
    void asEmpty() {
        Implementation<?> value = new Implementation<>(Base.class, Value.of(Impl::new, OtherImpl::new));

        assertThat(value.get()).isEqualTo(new Impl());
        assertThat(value.asEmpty()).isEqualTo(new OtherImpl());
    }

    @Test
    void isImplementationOf_whenOriginIsSuperTypeOfValue_thenValueIsAnImplementation() {
        Implementation<?> value = new Implementation<>(Impl.class, Impl::new);

        assertThat(value.isImplementationOf(Super.class)).isTrue();
        assertThat(value.isImplementationOf(Base.class)).isTrue();
    }

    @Test
    void isImplementationOf_whenOriginIsTheSameType_thenValueIsAnImplementation() {
        Implementation<?> value = new Implementation<>(Impl.class, Impl::new);

        assertThat(value.isImplementationOf(Impl.class)).isTrue();
    }

    @Test
    void isImplementationOf_whenOriginIsSubTypeOfValue_thenValueIsNotAnImplementation() {
        Implementation<?> value = new Implementation<>(Impl.class, Impl::new);
        Implementation<?> superValue = new Implementation<>(Super.class, Impl::new);

        assertThat(value.isImplementationOf(ExtendingImpl.class)).isFalse();
        assertThat(superValue.isImplementationOf(ExtendingImpl.class)).isFalse();
    }

    @Test
    void isImplementationOf_whenOriginIsNotSuperTypeOfValue_andNotTheSameType_thenValueIsNotAnImplementation() {
        Implementation<?> value = new Implementation<>(Impl.class, Impl::new);

        assertThat(value.isImplementationOf(OtherImpl.class)).isFalse();
    }

    @Test
    void isImplementationOf_whenOriginIsNull_thenValueIsNotAnImplementation() {
        Implementation<?> value = new Implementation<>(Super.class, Impl::new);

        assertThat(value.isImplementationOf(null)).isFalse();
    }

    @Test
    void equals_whenSameImplementationType_thenIsEqual() {
        Implementation<?> value = new Implementation<>(Super.class, Impl::new);

        assertThat(value).isEqualTo(new Implementation<>(Super.class, Impl::new));
    }

    @Test
    void equals_whenIsReference_thenIsEqual() {
        Implementation<?> value = new Implementation<>(Super.class, Impl::new);

        assertThat(value).isEqualTo((Object) value);
    }

    @Test
    void equals_whenNotTheSameImplementationType_thenIsNotEqual() {
        Implementation<?> value = new Implementation<>(Super.class, Impl::new);

        assertThat(value).isNotEqualTo(new Implementation<>(Impl.class, Impl::new));
    }

    @Test
    void equals_whenOtherIsNull_thenIsNotEqual() {
        Implementation<?> value = new Implementation<>(Super.class, Impl::new);

        assertThat(value).isNotEqualTo(null);
    }

    @Test
    void equals_whenOtherIsNotTheSameValue_thenIsNotEqual() {
        Implementation<?> value = new Implementation<>(Super.class, Impl::new);

        assertThat(value.equals((Value<?>) Impl::new)).isFalse();
    }

    @Test
    void hashCode_whenSameImplementationType_thenIsEqual() {
        Implementation<?> value = new Implementation<>(Super.class, Impl::new);

        assertThat(value.hashCode()).isEqualTo(new Implementation<>(Super.class, Impl::new).hashCode());
    }

    @Test
    void hashCode_whenNotTheSameImplementationType_thenIsNotEqual() {
        Implementation<?> value = new Implementation<>(Super.class, Impl::new);

        assertThat(value.hashCode()).isNotEqualTo(new Implementation<>(Impl.class, Impl::new).hashCode());
    }

    @Test
    void hashCode_whenOtherIsNotTheSameValue_thenIsNotEqual() {
        Implementation<?> value = new Implementation<>(Super.class, Impl::new);

        assertThat(value.hashCode()).isNotEqualTo(((Value<?>) Impl::new).hashCode());
    }

    @Test
    void toString_hasUsefulImplementationInformation() {
        Implementation<?> value = new Implementation<>(Super.class, Impl::new);

        assertThat(value.toString())
                .contains(Implementation.class.getSimpleName())
                .contains(Super.class.getSimpleName());
    }
}
