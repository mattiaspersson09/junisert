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
package io.github.mattiaspersson09.junisert.core.reflection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Field extends Member {
    private final java.lang.reflect.Field origin;
    // Can have both bean style and builder style methods
    private final List<Setter> setters;
    private final List<Getter> getters;

    Field(java.lang.reflect.Field origin) {
        super(origin);
        this.origin = origin;
        this.setters = new ArrayList<>();
        this.getters = new ArrayList<>();
    }

    void addSetter(Setter setter) {
        setters.add(setter);
    }

    void addGetter(Getter getter) {
        getters.add(getter);
    }

    public List<? extends Method> getSetters() {
        return Collections.unmodifiableList(setters);
    }

    public List<? extends Method> getGetters() {
        return Collections.unmodifiableList(getters);
    }

    @Override
    public Class<?> getType() {
        return origin.getType();
    }

    @Override
    public String getName() {
        return origin.getName();
    }

    @Override
    public boolean isSynthetic() {
        return origin.isSynthetic();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Field field = (Field) o;
        return Objects.equals(origin, field.origin)
                && Objects.equals(setters, field.setters)
                && Objects.equals(getters, field.getters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), origin, setters, getters);
    }
}
