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
package io.github.mattiaspersson09.junisert.api.assertion;

import io.github.mattiaspersson09.junisert.common.reflection.Field;
import io.github.mattiaspersson09.junisert.common.reflection.Member;
import io.github.mattiaspersson09.junisert.common.reflection.Method;
import io.github.mattiaspersson09.junisert.common.reflection.Unit;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExclusionTest {
    @Test
    void fieldExclusion_givenSingleFilter_whenItMatches_thenFieldIsExcluded() {
        Exclusion exclusion = Exclusion.exclude()
                .fieldMatching(field -> field.getName().contains("exclude"))
                .build();

        assertThat(exclusion.excludedFields(Unit.of(Fields.class)))
                .hasSize(2)
                .extracting(Field::getName)
                .containsOnly("excludeString", "excludeBoolean");

        assertThat(Unit.of(Fields.class).getFields())
                .filteredOn(field -> !field.isSynthetic())
                .filteredOn(exclusion::isNotExcluded)
                .hasSize(1)
                .extracting(Field::getName)
                .containsOnly("include");
    }

    @Test
    void fieldExclusion_givenAggregatedFilters_whenAnyFilterMatches_thenFieldIsExcluded() {
        Exclusion exclusion = Exclusion.exclude()
                .fieldMatching(Field::isBoolean)
                .fieldMatching(field -> field.isTypeOf(String.class))
                .build();

        assertThat(exclusion.excludedFields(Unit.of(Fields.class)))
                .hasSize(2)
                .extracting(Field::getName)
                .containsOnly("excludeString", "excludeBoolean");
    }

    @Test
    void fieldExclusion_whenNoFiltersExists_thenNoFieldIsExcluded() {
        Exclusion exclusion = Exclusion.exclude()
                .build();

        assertThat(exclusion.excludedFields(Unit.of(Fields.class))).isEmpty();
    }

    @Test
    void methodExclusion_givenSingleFilter_whenItMatches_thenMethodIsExcluded() {
        Exclusion exclusion = Exclusion.exclude()
                .methodMatching(method -> method.getName().equals("consumer"))
                .build();

        assertThat(exclusion.excludedMethods(Unit.of(Methods.class)))
                .hasSize(1)
                .extracting(Method::getName)
                .containsOnly("consumer");

        assertThat(Unit.of(Methods.class).getMethods())
                .filteredOn(method -> !method.isSynthetic())
                .filteredOn(exclusion::isNotExcluded)
                .hasSize(2)
                .extracting(Method::getName)
                .containsOnly("producer", "function");
    }

    @Test
    void methodExclusion_givenAggregatedFilters_whenAnyFilterMatches_thenMethodIsExcluded() {
        Exclusion exclusion = Exclusion.exclude()
                .methodMatching(Method::isConsumer)
                .methodMatching(Method::isProducer)
                .build();

        assertThat(exclusion.excludedMethods(Unit.of(Methods.class)))
                .extracting(Method::getName)
                .doesNotContain("function")
                .contains("consumer", "producer");
    }

    @Test
    void methodExclusion_whenNoFiltersExists_thenNoMethodIsExcluded() {
        Exclusion exclusion = Exclusion.exclude()
                .build();

        assertThat(exclusion.excludedMethods(Unit.of(Methods.class))).isEmpty();
    }

    @Test
    void exclusion_givenFilters_whenFilterMatches_thenMemberIsExcluded() {
        Exclusion exclusion = Exclusion.exclude()
                .build();
        exclusion.add(Exclusion.exclude()
                .fieldMatching(field -> field.isTypeOf(String.class))
                .methodMatching(method -> method.getName().equals("consumer"))
                .build());
        exclusion.addFieldExclusion(Field::isBoolean);
        exclusion.addMethodExclusion(Method::isFunction);

        List<Member> excludedMembers = new ArrayList<>();
        excludedMembers.addAll(Unit.of(Fields.class).findFieldsMatching(exclusion::isExcluded));
        excludedMembers.addAll(Unit.of(Methods.class).findMethodsMatching(exclusion::isExcluded));

        assertThat(excludedMembers)
                .hasSize(4)
                .extracting(Member::getName)
                .contains("excludeBoolean", "excludeString", "consumer", "function")
                .doesNotContain("include", "producer");
    }

    @SuppressWarnings("unused")
    private static class Fields {
        private String excludeString;
        private boolean excludeBoolean;
        private Object include;
    }

    @SuppressWarnings("unused")
    private static class Methods {
        public void consumer(String consumed) {
        }

        public String producer() {
            return null;
        }

        public String function(String applied) {
            return null;
        }
    }
}
