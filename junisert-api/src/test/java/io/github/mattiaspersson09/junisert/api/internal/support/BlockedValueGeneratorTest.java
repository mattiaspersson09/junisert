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
import io.github.mattiaspersson09.junisert.testunits.constructor.ArgConstructor;
import io.github.mattiaspersson09.junisert.testunits.constructor.NoConstructor;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Base;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BlockedValueGeneratorTest {
    @Test
    void generate_typeIsBlocked_throwsBlockedTypeException() {
        BlockedValueGenerator<?> generator = new BlockedValueGenerator<>(ArgConstructor.class);

        assertThatThrownBy(() -> generator.generate(ArgConstructor.class)).isInstanceOf(BlockedTypeException.class);
    }

    @Test
    void generate_notTheBlockedType_throwsUnsupportedTypeException() {
        BlockedValueGenerator<?> generator = new BlockedValueGenerator<>(ArgConstructor.class);

        assertThatThrownBy(() -> generator.generate(NoConstructor.class)).isInstanceOf(UnsupportedTypeError.class);
    }

    @Test
    void supports_onlyLooksForBlockedType() {
        BlockedValueGenerator<?> blocked = new BlockedValueGenerator<>(Super.class);

        assertThat(blocked.supports(Super.class)).isTrue();
        assertThat(blocked.supports(Base.class)).isFalse();
        assertThat(blocked.supports(Impl.class)).isFalse();
    }
}
