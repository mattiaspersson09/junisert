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
package io.github.mattiaspersson09.junisert.api.internal.support;

/**
 * Used when a type is blocked and should not be used in the code base, but was
 * found during a test suite.
 */
public class BlockedTypeException extends RuntimeException {
    private static final String BLOCKED_TYPE = "Found blocked '%s'. Either remove the block or "
            + "consider removing the usage from code base.";

    public BlockedTypeException(Class<?> type) {
        super(String.format(BLOCKED_TYPE, type));
    }
}
