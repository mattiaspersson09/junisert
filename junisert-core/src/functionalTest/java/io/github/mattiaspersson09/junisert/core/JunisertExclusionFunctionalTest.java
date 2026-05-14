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
package io.github.mattiaspersson09.junisert.core;

import io.github.mattiaspersson09.junisert.api.assertion.Exclusion;
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.testunits.getter.TwoButOnlyOneWorking;
import io.github.mattiaspersson09.junisert.testunits.unit.NeedsExclusion;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JunisertExclusionFunctionalTest {
    @Test
    void givenPojoWithTwoGetters_andOneGetterIsNotWorking_whenExcludingBrokenGetter_thenPassesAssertion() {
        assertThatThrownBy(() -> Junisert.assertThatPojo(TwoButOnlyOneWorking.class).hasGetters())
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("Found getter: field")
                .hasMessageContaining("was not getting value");

        Junisert.assertThatPojo(TwoButOnlyOneWorking.class)
                .excludingMethod("field")
                .hasGetters();
    }

    @Test
    void givenPojoWithTwoSetters_andOneSetterIsNotWorking_whenExcludingBrokenSetter_thenPassesAssertion() {
        assertThatThrownBy(() -> Junisert.assertThatPojo(
                io.github.mattiaspersson09.junisert.testunits.setter.TwoButOnlyOneWorking.class).hasSetters())
                .isInstanceOf(UnitAssertionError.class)
                .hasMessageContaining("Found setter: field")
                .hasMessageContaining("was not setting value");

        Junisert.assertThatPojo(io.github.mattiaspersson09.junisert.testunits.setter.TwoButOnlyOneWorking.class)
                .excludingMethod("field", Object.class)
                .hasSetters();
    }

    @Test
    void givenUnitWithFieldIssues_whenExcludingIssueField_thenPassesAssertion() {
        assertThatThrownBy(() -> Junisert.assertThatUnit(NeedsExclusion.class).isJavaBeanCompliant())
                .isInstanceOf(UnitAssertionError.class);
        assertThatThrownBy(() -> Junisert.assertThatPojo(NeedsExclusion.class).isWellImplemented())
                .isInstanceOf(UnitAssertionError.class);

        Junisert.assertThatUnit(NeedsExclusion.class)
                .excludingField("fieldNotPrivate")
                .excludingField("fieldMissingSetter")
                .excludingField("fieldMissingGetter")
                .isJavaBeanCompliant()
                .asPojo()
                .excludingField("fieldMissingFromToString")
                .isWellImplemented();
    }

    @Test
    void givenUnitWithMemberIssues_whenExcludingProblematicMember_thenPassesAssertion() {
        Exclusion exclusion = Exclusion.exclude()
                .fieldMatching(field -> field.getName().toLowerCase().contains("missing"))
                .fieldMatching(field -> field.getName().toLowerCase().contains("notprivate"))
                .methodMatching(method -> method.getName().equals("field"))
                .build();

        Junisert.assertThatPojo(NeedsExclusion.class)
                .excluding(exclusion)
                .isWellImplemented();

        Junisert.assertThatPojo(TwoButOnlyOneWorking.class)
                .excluding(exclusion)
                .hasGetters();
    }
}
