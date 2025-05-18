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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class EqualsTest {
    private static final String VALUE = "value";
    private static final String DIFFERENT_VALUE = "different value";

    private Equals equals;

    @BeforeEach
    void setUp() {
        equals = new Equals(new Instance(VALUE));
    }

    @Test
    void isReflexive_whenEqualsReference_thenIsReflexive() {
        equals.isReflexive();
    }

    @Test
    void reflexive_whenNotEqualsReference_thenFailsReflexivity() {
        assertThatThrownBy(new Equals(new NonReflexive())::isReflexive).isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void symmetric_whenOtherInstanceHasTheSameValues_thenIsSymmetric() {
        equals.isSymmetricWith(() -> new Instance(VALUE));

        assertThatThrownBy(() -> Equals.ofInstance(new Instance(VALUE)).isNotSymmetricWith(new Instance(VALUE)))
                .isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void symmetric_whenOtherInstanceHasDifferentValues_thenFailsSymmetry() {
        Instance otherInstance = new Instance(DIFFERENT_VALUE);

        equals.isNotSymmetricWith(() -> new Instance(DIFFERENT_VALUE));
        assertThatThrownBy(() -> equals.isSymmetricWith(otherInstance)).isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void symmetric_whenAnyEqualInstanceIsNotEqualToTheOther_thenFailsSymmetry() {
        NonSymmetric nonSymmetric = new NonSymmetric();
        Equals equals = new Equals(nonSymmetric);

        equals.isNotSymmetricWith(nonSymmetric);
        assertThatThrownBy(() -> equals.isSymmetricWith(nonSymmetric)).isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void transitive_whenAllInstancesAreEqual_thenIsTransitive() {
        equals.isTransitiveWith(() -> new Instance(VALUE), () -> new Instance(VALUE));
    }

    @Test
    void transitive_whenAnyInstanceDoesNotEqual_thenFailsTransitivity() {
        Object sameValue = new Instance(VALUE);
        Object differentValue = new Instance(DIFFERENT_VALUE);

        assertThatThrownBy(() -> equals.isTransitiveWith(sameValue, differentValue))
                .isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> equals.isTransitiveWith(differentValue, sameValue))
                .isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> equals.isTransitiveWith(differentValue, differentValue))
                .isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void consistent_givenTimesToBeConsistent_whenConsistentEveryTime_thenIsConsistent() {
        equals.isConsistentWith(() -> new Instance(VALUE), 1);
    }

    @Test
    void consistent_givenTimesToBeConsistent_whenNotEqualEveryInvocation_thenFailsConsistency() {
        assertThatThrownBy(() -> Equals.ofInstance(new Inconsistent()).isConsistentWith(new Inconsistent(), 4))
                .isInstanceOf(UnitAssertionError.class);
    }

    @Test
    void consistent_givenZeroTimesToBeConsistent_thenSkipsCheck() {
        equals.isConsistentWith(new Object(), 0);
    }

    @Test
    void consistent_givenNegativeConsistencyTimes_thenThrowsIllegalArgumentException() {
        assertThatThrownBy(() -> equals.isConsistentWith(new Object(), -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static class Instance {
        private final String field;

        public Instance(String field) {
            this.field = field;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Instance instance = (Instance) object;
            return Objects.equals(field, instance.field);
        }
    }

    private static class NonSymmetric {
        private static int equalsCount = 1;

        @Override
        @SuppressWarnings("all")
        public boolean equals(Object object) {
            return (equalsCount++) % 2 == 0;
        }
    }

    private static class NonReflexive {
        @Override
        public boolean equals(Object object) {
            return false;
        }
    }

    @SuppressWarnings("unused")
    private static class Inconsistent {
        private static int equalsCount = 1;
        private Object field;

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            if ((equalsCount++) % 4 == 0) return false;
            Inconsistent other = (Inconsistent) object;
            return Objects.equals(field, other.field);
        }
    }
}
