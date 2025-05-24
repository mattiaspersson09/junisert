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
package io.github.mattiaspersson09.junisert.core.internal.test.strategy;

import io.github.mattiaspersson09.junisert.core.internal.reflection.Field;
import io.github.mattiaspersson09.junisert.core.internal.reflection.Method;

import java.util.function.Predicate;

final class NoTestStrategy implements TestStrategy {
    @Override
    public Predicate<Method> isGetterForField(Field field) {
        return new JavaBeanTestStrategy()
                .isGetterForField(field)
                .or(this.isRecordStyleGetter(field));
    }

    @Override
    public Predicate<Method> isSetterForField(Field field) {
        return new JavaBeanTestStrategy()
                .isSetterForField(field)
                .or(this.isBuilderStyleSetter(field));
    }

    @Override
    public String name() {
        return "None (flexible)";
    }

    private Predicate<Method> isRecordStyleGetter(Field field) {
        return method -> method.getName().equals(field.getName()) && method.isProducing(field.getType());
    }

    private Predicate<Method> isBuilderStyleSetter(Field field) {
        return method -> method.getName().equals(field.getName())
                && method.hasParameterCount(1)
                && method.hasParameterTo(field.getType());
    }
}
