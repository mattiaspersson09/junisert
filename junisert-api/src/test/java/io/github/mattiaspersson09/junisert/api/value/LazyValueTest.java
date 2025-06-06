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
package io.github.mattiaspersson09.junisert.api.value;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LazyValueTest {
    @Test
    void nullOnNotEmptyValue_shouldFailFast_thenThrowsUncheckedException() {
        assertThatThrownBy(() -> new LazyValue<>(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Can't construct a lazy value object without a value supplier");
    }

    @Test
    void asEmpty_isDefaultNull() {
        assertThat(new LazyValue<>(String::new).asEmpty()).isNull();
    }

    @Test
    void isPolymorphic() {
        Value<CharSequence> lazyValue = new LazyValue<>(() -> new StringBuilder("string"));

        assertThat(lazyValue.get().toString()).isEqualTo("string");
    }
}
