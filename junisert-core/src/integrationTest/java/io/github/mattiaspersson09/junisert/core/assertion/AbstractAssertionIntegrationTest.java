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
package io.github.mattiaspersson09.junisert.core.assertion;

import io.github.mattiaspersson09.junisert.api.assertion.Exclusion;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.common.reflection.Unit;
import io.github.mattiaspersson09.junisert.core.NoCacheTestValueService;
import io.github.mattiaspersson09.junisert.core.TestInstanceCreator;
import io.github.mattiaspersson09.junisert.core.assertion.AbstractAssertion.TemporaryValueService;
import io.github.mattiaspersson09.junisert.testunits.unit.pojo.ImmutableModel;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractAssertionIntegrationTest {

    @Test
    void valueService_whenAssertionHasTemporarySupport_thenCreatesTemporaryValueService() {
        Assertion assertion = new Assertion(new AssertionResource(
                Unit.of(ImmutableModel.class),
                new TestInstanceCreator(),
                NoCacheTestValueService.withAllValueGenerators(),
                Exclusion.exclude().build()
        ));

        assertThat(assertion.getValueService()).isNotInstanceOf(TemporaryValueService.class);
        assertThat(assertion.withSupport(String.class, () -> "support value").getValueService())
                .isInstanceOf(TemporaryValueService.class);
    }

    private static class Assertion extends AbstractAssertion<Assertion> {
        protected Assertion(AssertionResource assertionResource) {
            super(assertionResource);
        }

        public Value<?> generateValue(Class<?> type) {
            return getValueService().getValue(type);
        }
    }
}
