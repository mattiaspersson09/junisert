/*
 * Copyright (c) 2026 Mattias Persson
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

import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.common.sort.Order;
import io.github.mattiaspersson09.junisert.common.sort.Sortable;

import java.util.Comparator;

final class SupportComparator implements Comparator<ValueGenerator<?>> {
    @Override
    public int compare(ValueGenerator<?> o1, ValueGenerator<?> o2) {
        Sortable left = toSortable(o1);
        Sortable right = toSortable(o2);

        // Support added later should be placed before earlier added support
        if (left.order().equals(right.order()) && left.order().isBefore(Order.DEFAULT)) {
            return -1;
        }

        return left.compareTo(right);
    }

    private Sortable toSortable(ValueGenerator<?> generator) {
        return (generator instanceof Sortable) ? (Sortable) generator : () -> Order.DEFAULT;
    }
}
