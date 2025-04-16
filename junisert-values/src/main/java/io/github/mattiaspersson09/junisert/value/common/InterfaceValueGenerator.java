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
package io.github.mattiaspersson09.junisert.value.common;

import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeException;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InterfaceValueGenerator implements ValueGenerator<Object> {
    @Override
    public Value<?> generate(Class<?> fromType) throws UnsupportedTypeException {
        if (!supports(fromType)) {
            throw new UnsupportedTypeException(fromType);
        }

        return Value.of(() -> Proxy.newProxyInstance(fromType.getClassLoader(), new Class[]{fromType},
                new AnonymousInvocation()));
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.isInterface();
    }

    private static class AnonymousInvocation implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            throw new UnsupportedOperationException("Unable to invoke on an anonymously constructed proxy object");
        }
    }
}
