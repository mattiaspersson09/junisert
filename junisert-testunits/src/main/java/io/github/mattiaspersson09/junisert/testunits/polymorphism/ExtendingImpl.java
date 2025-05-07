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
package io.github.mattiaspersson09.junisert.testunits.polymorphism;

import java.util.Objects;

public class ExtendingImpl extends Impl {
    private Object extendingImplField;

    public ExtendingImpl() {
        super();
    }

    public ExtendingImpl(int value) {
        super(value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        ExtendingImpl extending = (ExtendingImpl) object;
        return Objects.equals(extendingImplField, extending.extendingImplField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), extendingImplField);
    }
}
