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

import static org.assertj.core.api.Assertions.assertThat;

public class UnitAssertionErrorTest {
    @Test
    void unitAssertionErrorWithoutCause() {
        assertThat(new UnitAssertionError("failed assertion"))
                .isInstanceOf(AssertionError.class)
                .hasMessage("failed assertion");
    }

    @Test
    void unitAssertionErrorWithCause() {
        assertThat(new UnitAssertionError("failed assertion", new AssertionError("inner assertion")))
                .isInstanceOf(AssertionError.class)
                .hasMessage("failed assertion")
                .cause()
                .isInstanceOf(AssertionError.class)
                .hasMessage("inner assertion");
    }
}
