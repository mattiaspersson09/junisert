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
import io.github.mattiaspersson09.junisert.core.internal.reflection.util.Fields;

import java.util.function.Predicate;

final class JavaBeanTestStrategy implements TestStrategy {
    @Override
    public Predicate<Method> isGetterForField(Field field) {
        return method -> {
            String propertyName = Fields.toCapitalizedPropertyName(field);

            return (method.getName().equals("get" + propertyName)
                    || method.getName().equals("is" + propertyName)
                    || method.getName().equals("get" + Fields.toBooleanCapitalizedPropertyName(field)))
                    && method.isProducing(field.getType());
        };
    }

    @Override
    public Predicate<Method> isSetterForField(Field field) {
        return method -> {
            String propertyName = Fields.toCapitalizedPropertyName(field);

            return (method.getName().equals("set" + propertyName)
                    || method.getName().equals("set" + Fields.toBooleanCapitalizedPropertyName(field)))
                    && method.hasParameterCount(1)
                    && method.hasParameterOf(field.getType());
        };
    }

    @Override
    public String name() {
        return "Java Bean compliant";
    }
}
