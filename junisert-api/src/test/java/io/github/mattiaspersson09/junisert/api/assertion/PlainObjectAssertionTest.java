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
package io.github.mattiaspersson09.junisert.api.assertion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlainObjectAssertionTest {
    @Spy
    PlainObjectAssertion plainObjectAssertion;

    @Test
    void isWellImplemented_performsAllAssertions() {
        when(plainObjectAssertion.hasGetters()).thenReturn(plainObjectAssertion);
        when(plainObjectAssertion.hasSetters()).thenReturn(plainObjectAssertion);
        when(plainObjectAssertion.implementsEqualsAndHashCode()).thenReturn(plainObjectAssertion);
        when(plainObjectAssertion.implementsToString()).thenReturn(plainObjectAssertion);

        plainObjectAssertion.isWellImplemented();

        verify(plainObjectAssertion, times(1)).hasGetters();
        verify(plainObjectAssertion, times(1)).hasSetters();
        verify(plainObjectAssertion, times(1)).implementsEqualsAndHashCode();
        verify(plainObjectAssertion, times(1)).implementsToString();
    }
}
