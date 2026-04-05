/*
 * Copyright (c) 2025-2026 Mattias Persson
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
package io.github.mattiaspersson09.junisert.value.java;

import io.github.mattiaspersson09.junisert.value.common.EnumValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.PrimitiveValueGenerator;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.AbstractSequentialList;
import java.util.AbstractSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JavaUtilSupportIntegrationTest extends JavaInternalIntegrationTest {
    JavaUtilSupportIntegrationTest() {
        super(new SupportInvoker(Arrays.asList(
                new PrimitiveValueGenerator(),
                new EnumValueGenerator(),
                new ObjectValueGenerator(),
                JavaUtilSupport.getFunctionalSupport(),
                JavaUtilSupport.getConcurrentSupport()
        )));
    }

    @ParameterizedTest
    @ValueSource(classes = {
            // Super types
            AbstractCollection.class,
            AbstractList.class,
            AbstractSet.class,
            AbstractQueue.class,
            AbstractSequentialList.class,
            AbstractMap.class,
            ArrayDeque.class,
            Collection.class,
            Deque.class,
            Enumeration.class,
            Iterator.class,
            List.class,
            ListIterator.class,
            Map.class,
            NavigableMap.class,
            NavigableSet.class,
            Queue.class,
            Set.class,
            SortedMap.class,
            SortedSet.class,
            // Implementations
            ArrayList.class,
            Base64.Encoder.class,
            Base64.Decoder.class,
            Calendar.class,
            Dictionary.class,
            Date.class,
            HashMap.class,
            HashSet.class,
            Hashtable.class,
            IdentityHashMap.class,
            LinkedHashMap.class,
            LinkedHashSet.class,
            Locale.class,
            LinkedList.class,
            GregorianCalendar.class,
            Optional.class,
            OptionalInt.class,
            OptionalDouble.class,
            OptionalLong.class,
            PriorityQueue.class,
            Properties.class,
            Random.class,
            Scanner.class,
            SimpleTimeZone.class,
            Stack.class,
            StringJoiner.class,
            StringTokenizer.class,
            Timer.class,
            TimeZone.class,
            TreeMap.class,
            TreeSet.class,
            UUID.class,
            Vector.class,
            WeakHashMap.class,
    })
    void javaUtil(Class<?> type) {
        assertIsSupported(type);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            BiConsumer.class,
            BiFunction.class,
            BinaryOperator.class,
            BiPredicate.class,
            BooleanSupplier.class,
            Consumer.class,
            DoubleBinaryOperator.class,
            DoubleConsumer.class,
            DoubleFunction.class,
            DoublePredicate.class,
            DoubleSupplier.class,
            DoubleToIntFunction.class,
            DoubleToLongFunction.class,
            DoubleUnaryOperator.class,
            Function.class,
            IntBinaryOperator.class,
            IntConsumer.class,
            IntFunction.class,
            IntPredicate.class,
            IntSupplier.class,
            IntToDoubleFunction.class,
            IntToLongFunction.class,
            IntUnaryOperator.class,
            LongBinaryOperator.class,
            LongConsumer.class,
            LongFunction.class,
            LongPredicate.class,
            LongSupplier.class,
            LongToDoubleFunction.class,
            LongToIntFunction.class,
            LongUnaryOperator.class,
            ObjDoubleConsumer.class,
            ObjIntConsumer.class,
            ObjLongConsumer.class,
            Predicate.class,
            Supplier.class,
            ToDoubleBiFunction.class,
            ToDoubleFunction.class,
            ToIntBiFunction.class,
            ToIntFunction.class,
            ToLongBiFunction.class,
            ToLongFunction.class,
            UnaryOperator.class,
    })
    void javaFunctional(Class<?> type) {
        assertIsSupported(type);
        assertThatFunctionalSupportCanBeUsed(type);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            BaseStream.class,
            DoubleStream.class,
            IntStream.class,
            LongStream.class,
            Stream.class,
    })
    void javaStream(Class<?> type) {
        assertIsSupported(type);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            // Super types
            BlockingQueue.class,
            BlockingDeque.class,
            Callable.class,
            CompletionStage.class,
            CompletionService.class,
            ConcurrentMap.class,
            ConcurrentNavigableMap.class,
            Delayed.class,
            Executor.class,
            ExecutorService.class,
            Future.class,
            RunnableFuture.class,
            RunnableScheduledFuture.class,
            ScheduledExecutorService.class,
            ScheduledFuture.class,
            TransferQueue.class,
            // Implementations
            AbstractExecutorService.class,
            ArrayBlockingQueue.class,
            CompletableFuture.class,
            ConcurrentHashMap.class,
            ConcurrentLinkedDeque.class,
            ConcurrentLinkedQueue.class,
            ConcurrentSkipListMap.class,
            ConcurrentSkipListSet.class,
            CopyOnWriteArrayList.class,
            CopyOnWriteArraySet.class,
            CountDownLatch.class,
            CountedCompleter.class,
            CyclicBarrier.class,
            DelayQueue.class,
            Exchanger.class,
            ExecutorCompletionService.class,
            ForkJoinPool.class,
            ForkJoinTask.class,
            FutureTask.class,
            LinkedBlockingDeque.class,
            LinkedBlockingQueue.class,
            LinkedTransferQueue.class,
            Phaser.class,
            PriorityBlockingQueue.class,
            ScheduledThreadPoolExecutor.class,
            Semaphore.class,
            SynchronousQueue.class,
            ThreadLocalRandom.class,
            ThreadPoolExecutor.class,
            TimeUnit.class
    })
    void javaConcurrent(Class<?> type) {
        assertIsSupported(type);
        assertThatFunctionalSupportCanBeUsed(type);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            AtomicBoolean.class,
            AtomicInteger.class,
            AtomicIntegerArray.class,
            AtomicLong.class,
            AtomicLongArray.class,
            AtomicReference.class,
            AtomicReferenceArray.class,
            DoubleAccumulator.class,
            DoubleAdder.class,
            LongAccumulator.class,
            LongAdder.class
    })
    void javaConcurrentAtomic(Class<?> type) {
        assertIsSupported(type);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            // Super types
            Lock.class,
            ReadWriteLock.class,
            // Implementations
            ReentrantLock.class,
            ReentrantReadWriteLock.class,
            StampedLock.class
    })
    void javaConcurrentLocks(Class<?> type) {
        assertIsSupported(type);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            Enumeration.class,
            RunnableScheduledFuture.class,
            CountedCompleter.class
    })
    void noOpSupport(Class<?> type) {
        assertThatSupportCanBeUsed(type);
    }
}
