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
package io.github.mattiaspersson09.junisert.core.internal.test.util;

import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HashCodeTest {
    private static final String VALUE = "value";
    private static final String DIFFERENT_VALUE = "different value";

    private HashCode hashCode;

    @BeforeEach
    void setUp() {
        hashCode = new HashCode(VALUE);
    }

    @Test
    void isConsistent_whenIsReference_thenIsConsistent() {
        hashCode.isConsistent();
    }

    @Test
    void isConsistent_whenReturningDifferentValues_thenFailsConsistency() {
        assertThatThrownBy(new HashCode(new InconsistentHashCode())::isConsistent)
                .isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void isNotEmpty_whenReturningValue_thenIsNotEmpty() {
        hashCode.isNotEmpty();
    }

    @Test
    void isNotEmpty_whenZero_thenFailsEmptyCheck() {
        assertThatThrownBy(new HashCode(new ZeroHashCode())::isNotEmpty).isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void isEqualTo_whenHavingSameValues_thenIsEqual() {
        hashCode.isEqualTo(VALUE);
        hashCode.isEqualTo(() -> VALUE);
    }

    @Test
    void isEqualTo_whenNotHavingSameValues_thenFailsConsistency() {
        assertThatThrownBy(() -> hashCode.isEqualTo(DIFFERENT_VALUE)).isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> hashCode.isEqualTo(() -> DIFFERENT_VALUE)).isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void isNotEqualTo_whenHavingDifferentValues_thenIsNotEqual() {
        hashCode.isNotEqualTo(DIFFERENT_VALUE);
        hashCode.isNotEqualTo(() -> DIFFERENT_VALUE);
    }

    @Test
    void isNotEqualTo_whenHavingSameValues_thenFailsUniqueness() {
        assertThatThrownBy(() -> hashCode.isNotEqualTo(VALUE)).isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> hashCode.isNotEqualTo(() -> VALUE)).isInstanceOf(UnitAssertionError.class);
    }

    private static class Instance {
        private final String field;

        public Instance(String field) {
            this.field = field;
        }

        @Override
        public int hashCode() {
            return Objects.hash(field);
        }
    }

    private static class InconsistentHashCode {
        private static int hashCodeCount = 1;

        @Override
        @SuppressWarnings("all")
        public int hashCode() {
            return hashCodeCount++;
        }
    }

    private static class ZeroHashCode {
        @Override
        public int hashCode() {
            return 0;
        }
    }
}
