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

public class EagerValueTest {
    @Test
    void nullOnNotEmptyValue_shouldFailFast_thenThrowsNullPointerException() {
        assertThatThrownBy(() -> new EagerValue<>(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void notEmptyAndEmpty_whenIsTheSameValue_thenThrowsIllegalArgumentException() {
        assertThatThrownBy(() -> new EagerValue<>("value", "value"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void equals_whenSameOriginClass_thenIsEqual() {
        EagerValue<?> value = new EagerValue<>("string");
        Object object = "";

        assertThat(value).isEqualTo(new EagerValue<>("string"));
        assertThat(value).isEqualTo(new EagerValue<>("another string"));
        assertThat(value).isEqualTo(new EagerValue<>(object));
    }

    @Test
    void equals_whenIsReference_thenIsEqual() {
        EagerValue<?> value = new EagerValue<>("string");

        assertThat(value).isEqualTo((Object) value);
    }

    @Test
    void equals_whenIsNull_thenIsNotEqual() {
        EagerValue<?> value = new EagerValue<>("string");

        assertThat(value).isNotEqualTo(null);
    }

    @Test
    void equals_whenIsNotTheSameClass_thenIsNotEqual() {
        EagerValue<?> value = new EagerValue<>("string");

        assertThat(value).isNotEqualTo(new EagerValue<String>("string") {
        });
    }

    @Test
    void hashCode_whenSameOriginClass_thenIsEqual() {
        EagerValue<?> value = new EagerValue<>("string");
        Object object = "";

        assertThat(value.hashCode()).isEqualTo(new EagerValue<>("string").hashCode());
        assertThat(value.hashCode()).isEqualTo(new EagerValue<>("another string").hashCode());
        assertThat(value.hashCode()).isEqualTo(new EagerValue<>(object).hashCode());
    }

    @Test
    void hashCode_whenIsNotTheSameOriginClass_thenIsNotEqual() {
        EagerValue<?> value = new EagerValue<>("string");
        Object object = 1;

        assertThat(value.hashCode()).isNotEqualTo(object.hashCode());
    }

    @Test
    void isPolymorphic() {
        EagerValue<CharSequence> eagerValue = new EagerValue<>(new StringBuilder("string"));

        assertThat(eagerValue.get().toString()).isEqualTo("string");
    }
}
