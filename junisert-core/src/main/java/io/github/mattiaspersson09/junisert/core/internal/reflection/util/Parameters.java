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
package io.github.mattiaspersson09.junisert.core.internal.reflection.util;

import io.github.mattiaspersson09.junisert.core.internal.reflection.Parameter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Parameters {
    private Parameters() {
    }

    public static Parameter map(java.lang.reflect.Parameter parameter) {
        return Parameter.of(parameter);
    }

    public static List<Parameter> map(java.lang.reflect.Parameter[] parameters) {
        return Stream.of(parameters)
                .map(Parameters::map)
                .collect(Collectors.toList());
    }
}
