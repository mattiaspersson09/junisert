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
package io.github.mattiaspersson09.junisert.core;

import java.util.Objects;

class InceptionModel {
    private final Arg arg;

    public InceptionModel(Arg arg) {
        this.arg = arg;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        InceptionModel that = (InceptionModel) object;
        return Objects.equals(arg, that.arg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arg);
    }

    @Override
    public String toString() {
        return "InceptionArgConstructor{" +
                "arg=" + arg +
                '}';
    }

    public static class Arg {
        DeeperArg deeperArg;

        public Arg(DeeperArg deeperArg, int intArg) {
            this.deeperArg = deeperArg;
            if (intArg == 0) {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public String toString() {
            return "Arg{" +
                    "arg=" + deeperArg +
                    '}';
        }
    }

    public static class DeeperArg {
        Object deepestObject;

        public DeeperArg(Object deepestObject) {
            this.deepestObject = Objects.requireNonNull(deepestObject);
        }
    }
}
