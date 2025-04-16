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
package io.github.mattiaspersson09.junisert.value.java;

import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.api.internal.support.SupportBuilder;

import java.text.Bidi;
import java.text.ChoiceFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

final class JavaTextSupport {
    static AggregatedValueGenerator getTextSupport() {
        return SupportBuilder.createSupport()
                .supportSingle(Bidi.class, () -> new Bidi("text", Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT))
                .support(Format.class)
                .withImplementation(ChoiceFormat.class, () -> new ChoiceFormat("pattern"))
                .withImplementation(DecimalFormat.class, () -> new DecimalFormat("#"))
                .withImplementation(MessageFormat.class, () -> new MessageFormat("{0}"))
                .withImplementation(SimpleDateFormat.class, () -> new SimpleDateFormat("yyyy-MM-dd"))
                .build();
    }
}
