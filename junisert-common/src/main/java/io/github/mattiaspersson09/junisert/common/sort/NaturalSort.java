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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

/**
 * Convenience class to inherit from to gain ability to be naturally sorted in an
 * ordered manner among other sortable objects.
 * Inherited classes announce that it can be naturally sorted and have ability to be re-ordered,
 * classes that inherits {@code NaturalSort} <b>MUST</b> declare itself as the generic type. If inherited
 * class does not point out itself for natural sort, then {@link UnsupportedOperationException} will be thrown
 * to prevent {@link ClassCastException}.
 *
 * <p><br>
 * Example:
 * <pre>
 * public class Ordered extends NaturalSort&lt;Ordered> {
 *   public Ordered(Order initialOrder) {
 *     super(initialOrder);
 *   }
 * }
 *
 * Ordered prioritized = new Ordered({@link Order#DEFAULT});
 * Ordered normal = new Ordered({@link Order#FIRST});
 * // potentially a lot of other objects affecting performance when searching
 * List&lt;Ordered> sorted = Stream.of(normal.ordered({@link Order#DEFAULT}), prioritized.ordered({@link Order#FIRST}))
 *     .sorted()
 *     .collect(Collectors.toList());
 *
 * for (Ordered order : sorted) {
 *   // should begin with prioritized object
 *   if (isWhatWeWant(order)) {
 *       return order;
 *   }
 * }
 * </pre>
 *
 * @param <SELF> itself as naturally sortable
 * @see Sortable
 * @see Order
 */
public abstract class NaturalSort<SELF extends NaturalSort<SELF> & Sortable> implements Sortable {
    private static final String UNEXPECTED_GENERIC = "Unexpected generic self\nexpected: %s\nbut found: %s";

    private Order order;

    /**
     * Creating a naturally sortable object with an initial sort order. Will throw
     * {@link UnsupportedOperationException} if the inherited type isn't declaring itself generically as {@code SELF}
     *
     * @param initialOrder during sorts, {@link Order#DEFAULT} if missing
     * @throws UnsupportedOperationException if generic type mismatch
     */
    protected NaturalSort(Order initialOrder) throws UnsupportedOperationException {
        if (!isGenericSelf()) {
            throw new UnsupportedOperationException(String.format(UNEXPECTED_GENERIC, getClass(), getGenericType()));
        }

        this.order = Optional.ofNullable(initialOrder).orElse(Order.DEFAULT);
    }

    /**
     * Reorder this naturally sortable object, if {@code order} is null the previous order will be kept.
     *
     * @param order new sorting order
     * @return this object, potentially reordered
     */
    @SuppressWarnings("unchecked")
    public SELF ordered(Order order) {
        if (order != null) {
            this.order = order;
        }

        return (SELF) this;
    }

    @Override
    public Order order() {
        return order;
    }

    private boolean isGenericSelf() {
        return getClass().equals(getGenericType());
    }

    private Class<?> getGenericType() {
        ParameterizedType self = findThisClassRecursively(getClass());
        Type genericType = self.getActualTypeArguments()[0];

        // Inheriting class also has generic type, we need raw class type
        if (ParameterizedType.class.isAssignableFrom(genericType.getClass())) {
            return (Class<?>) ((ParameterizedType) genericType).getRawType();
        }

        return (Class<?>) self.getActualTypeArguments()[0];
    }

    private ParameterizedType findThisClassRecursively(Class<?> current) {
        if (hasGenericParameters(current.getSuperclass()) && NaturalSort.class.equals(current.getSuperclass())) {
            return (ParameterizedType) current.getGenericSuperclass();
        }

        return findThisClassRecursively(current.getSuperclass());
    }

    private boolean hasGenericParameters(Class<?> clazz) {
        return clazz.getTypeParameters().length > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NaturalSort<?> that = (NaturalSort<?>) o;
        return Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order);
    }
}
