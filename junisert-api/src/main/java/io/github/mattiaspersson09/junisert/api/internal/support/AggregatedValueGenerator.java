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

import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;

import java.util.Collection;

/**
 * <strong>INTERNAL DISCLAIMER:</strong>
 * <p>
 * Internal API and not considered stable for direct usage by external users of this API,
 * can be modified, become invisible, moved, renamed or removed without proper notice.
 * This class is visible because of support for Java version 8 and lack of modularity
 * and when support is dropping for version 8 this will lose visibility.
 * </p><br>
 *
 * Generator using several other aggregated generators internally to support different kind of value types.
 */
public interface AggregatedValueGenerator extends ValueGenerator<Object> {
    /**
     * Internally aggregated generators inside this generator in an unmodifiable view.
     *
     * @return unmodifiable view of aggregated generators
     */
    Collection<ValueGenerator<?>> aggregated();

    /**
     * Merge this generator with another {@link ValueGenerator}. If this {@code AggregatedValueGenerator} already
     * has aggregated generators, then the merged generator will be <em>last</em> in the resulting aggregation.
     *
     * @param generator to merge with
     * @return a new aggregated generator with the merged generator aggregated last
     */
    AggregatedValueGenerator merge(ValueGenerator<?> generator);

    /**
     * Merge this generator with another {@link ValueGenerator}. If this {@code AggregatedValueGenerator} already
     * has aggregated generators, then the merged generator will be <em>first</em> in the resulting aggregation.
     *
     * @param generator to merge with
     * @return a new aggregated generator with the merged generator aggregated first
     */
    AggregatedValueGenerator mergeFirst(ValueGenerator<?> generator);
}
