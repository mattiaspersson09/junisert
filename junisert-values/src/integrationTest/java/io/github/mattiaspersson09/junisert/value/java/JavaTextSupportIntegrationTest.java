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
package io.github.mattiaspersson09.junisert.value.java;


import java.text.Bidi;
import java.text.ChoiceFormat;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JavaTextSupportIntegrationTest extends JavaInternalIntegrationTest {
    @ParameterizedTest
    @ValueSource(classes = {
            // Super types
            DateFormat.class,
            Format.class,
            NumberFormat.class,
            // Implementations
            Bidi.class,
            ChoiceFormat.class,
            DecimalFormat.class,
            MessageFormat.class,
            SimpleDateFormat.class,
    })
    void javaText(Class<?> type) {
        assertIsSupported(type);
    }
}
