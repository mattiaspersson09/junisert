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

import io.github.mattiaspersson09.junisert.core.internal.test.AbstractUnitTest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;

public class AbstractAssertionTest {
    @Test
    @SuppressWarnings("unchecked")
    void createTest_whenFailingToCreateTest_thenThrowsRuntimeException() {
        AbstractUnitTest<?> unitTest = Mockito.mock(AbstractUnitTest.class);
        AssertionResource assertionResource = Mockito.mock(AssertionResource.class);
        Assertion assertion = new Assertion(assertionResource);

        doThrow().when(unitTest).getClass();

        assertThatThrownBy(() -> assertion.createTest(unitTest.getClass()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    void runtTest_whenFailingToCreateTest_thenThrowsRuntimeException() {
        AbstractUnitTest<?> unitTest = Mockito.mock(AbstractUnitTest.class);
        AssertionResource assertionResource = Mockito.mock(AssertionResource.class);
        Assertion assertion = new Assertion(assertionResource);

        doThrow().when(unitTest).getClass();

        assertThatThrownBy(() -> assertion.runTest(unitTest.getClass()))
                .isInstanceOf(RuntimeException.class);
    }

    private static class Assertion extends AbstractAssertion<Assertion> {
        protected Assertion(AssertionResource assertionResource) {
            super(assertionResource);
        }
    }
}
