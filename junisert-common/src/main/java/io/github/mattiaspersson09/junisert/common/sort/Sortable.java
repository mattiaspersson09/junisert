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
package io.github.mattiaspersson09.junisert.common.sort;

/**
 * Used for natural sort in a collection or stream, to let a higher order object be sorted
 * to a position to be used before lower ordered objects. If two sortable objects has the same order, there is no
 * guarantee which will be positioned before the other.
 *
 * @see Order
 * @see Integer#compare(int, int)
 * @see Comparable
 */
public interface Sortable extends Comparable<Sortable> {
    /**
     * Get the order of this sortable object.
     *
     * @return natural order
     */
    Order order();

    default int compareTo(Sortable o) {
        return Integer.compare(order().value(), o.order().value());
    }
}
