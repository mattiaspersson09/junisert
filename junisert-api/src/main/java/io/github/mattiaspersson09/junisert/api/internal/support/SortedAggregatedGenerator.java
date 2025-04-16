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
import io.github.mattiaspersson09.junisert.common.sort.Order;
import io.github.mattiaspersson09.junisert.common.sort.Sortable;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * <h4>INTERNAL DISCLAIMER:</h4>
 * <p>
 * Internal API and not considered stable for direct usage by external users of this API,
 * can be modified, become invisible, moved, renamed or removed without proper notice.
 * This class is visible because of support for Java version 8 and lack of modularity
 * and when support is dropping for version 8 this will lose visibility.
 * </p>
 */
public final class SortedAggregatedGenerator extends AggregatedSupportGenerator implements Sortable {
    private Order order;

    private SortedAggregatedGenerator(Collection<ValueGenerator<?>> generators) {
        super(generators);
        this.order = Order.DEFAULT;
    }

    static SortedAggregatedGenerator from(AggregatedValueGenerator generator) {
        return new SortedAggregatedGenerator(generator.aggregated()
                .stream()
                .sorted()
                .collect(Collectors.toList()));
    }

    public SortedAggregatedGenerator order(Order order) {
        this.order = order;
        return this;
    }

    @Override
    public AggregatedValueGenerator merge(ValueGenerator<?> generator) {
        return from(super.merge(generator));
    }

    @Override
    public Order order() {
        return order;
    }
}
