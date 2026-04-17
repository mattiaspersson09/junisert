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
package io.github.mattiaspersson09.junisert.value.java;

import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.api.internal.support.SupportBuilder;
import io.github.mattiaspersson09.junisert.common.logging.Logger;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.SimpleTimeZone;
import java.util.Stack;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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

final class JavaUtilSupport {
    private static final Logger LOGGER = Logger.getLogger("Junisert -> Anonymous support");
    private static final String CONSUMING = "consuming...";
    private static final short NUMERIC = 1;
    private static final boolean BOOLEAN = true;

    private JavaUtilSupport() {
    }

    /**
     * @return java.util
     */
    static AggregatedValueGenerator getUtilSupport() {
        return SupportBuilder.createSupport()
                .supportSingle(Base64.Decoder.class, Base64::getDecoder)
                .supportSingle(Base64.Encoder.class, Base64::getEncoder)
                .supportSingle(Calendar.class, GregorianCalendar.class, GregorianCalendar::new)
                .support(Collection.class)
                .withImplementation(ArrayList.class, ArrayList::new)
                .withImplementation(LinkedList.class, LinkedList::new)
                .withImplementation(HashSet.class, HashSet::new)
                .withImplementation(TreeSet.class, TreeSet::new)
                .withImplementation(LinkedHashSet.class, LinkedHashSet::new)
                .withImplementation(Stack.class, Stack::new)
                .supportSingle(Date.class, Date::new)
                .supportSingle(Dictionary.class, Properties.class, Properties::new)
                .supportSingle(Enumeration.class, NoOpEnumeration::new)
                .supportSingle(StringTokenizer.class, () -> new StringTokenizer("token"))
                .support(Iterator.class)
                .withImplementation(ListIterator.class, Collections::emptyListIterator)
                .withImplementation(Scanner.class, () -> new Scanner("source"))
                .supportSingle(Locale.class, () -> Locale.ENGLISH)
                .support(Map.class)
                .withImplementation(HashMap.class, HashMap::new)
                .withImplementation(LinkedHashMap.class, LinkedHashMap::new)
                .withImplementation(TreeMap.class, TreeMap::new)
                .withImplementation(IdentityHashMap.class, IdentityHashMap::new)
                .withImplementation(WeakHashMap.class, WeakHashMap::new)
                .supportSingle(Optional.class, Optional::empty)
                .supportSingle(OptionalInt.class, OptionalInt::empty)
                .supportSingle(OptionalLong.class, OptionalLong::empty)
                .supportSingle(OptionalDouble.class, OptionalDouble::empty)
                .supportSingle(Random.class, Random::new)
                .supportSingle(StringJoiner.class, () -> new StringJoiner(""))
                .supportSingle(Timer.class, Timer::new)
                .supportSingle(TimeZone.class, SimpleTimeZone.class, () -> new SimpleTimeZone(0, "Europe/Stockholm"))
                .supportSingle(UUID.class, UUID::randomUUID)
                .support(Queue.class)
                .withImplementation(ArrayDeque.class, ArrayDeque::new)
                .withImplementation(PriorityQueue.class, PriorityQueue::new)
                .build();
    }

    /**
     * @return java.util.function
     */
    static AggregatedValueGenerator getFunctionalSupport() {
        return SupportBuilder.createSupport()
                .supportSingle(BiConsumer.class, () -> (first, second) -> LOGGER.test(CONSUMING))
                .supportSingle(BiFunction.class, () -> (first, second) -> Function.identity())
                .supportSingle(BinaryOperator.class, () -> (first, second) -> Function.identity())
                .supportSingle(BiPredicate.class, () -> (first, second) -> BOOLEAN)
                .supportSingle(BooleanSupplier.class, () -> () -> BOOLEAN)
                .supportSingle(Consumer.class, () -> (first) -> LOGGER.test(CONSUMING))
                .supportSingle(DoubleBinaryOperator.class, () -> (first, second) -> NUMERIC)
                .supportSingle(DoubleConsumer.class, () -> (first) -> LOGGER.test(CONSUMING))
                .supportSingle(DoubleFunction.class, () -> (first) -> NUMERIC)
                .supportSingle(DoublePredicate.class, () -> (first) -> BOOLEAN)
                .supportSingle(DoubleSupplier.class, () -> () -> NUMERIC)
                .supportSingle(DoubleToIntFunction.class, () -> (first) -> NUMERIC)
                .supportSingle(DoubleToLongFunction.class, () -> (first) -> NUMERIC)
                .supportSingle(DoubleUnaryOperator.class, () -> (first) -> NUMERIC)
                .supportSingle(Function.class, () -> (first) -> Function.identity())
                .supportSingle(IntBinaryOperator.class, () -> (first, second) -> NUMERIC)
                .supportSingle(IntConsumer.class, () -> (first) -> LOGGER.test(CONSUMING))
                .supportSingle(IntFunction.class, () -> (first) -> Function.identity())
                .supportSingle(IntPredicate.class, () -> (first) -> BOOLEAN)
                .supportSingle(IntSupplier.class, () -> () -> NUMERIC)
                .supportSingle(IntToDoubleFunction.class, () -> (first) -> NUMERIC)
                .supportSingle(IntToLongFunction.class, () -> (first) -> NUMERIC)
                .supportSingle(IntUnaryOperator.class, () -> (first) -> NUMERIC)
                .supportSingle(LongBinaryOperator.class, () -> (first, second) -> NUMERIC)
                .supportSingle(LongConsumer.class, () -> (first) -> LOGGER.test(CONSUMING))
                .supportSingle(LongFunction.class, () -> (first) -> NUMERIC)
                .supportSingle(LongPredicate.class, () -> (first) -> BOOLEAN)
                .supportSingle(LongSupplier.class, () -> () -> NUMERIC)
                .supportSingle(LongToDoubleFunction.class, () -> (first) -> NUMERIC)
                .supportSingle(LongToIntFunction.class, () -> (first) -> NUMERIC)
                .supportSingle(LongUnaryOperator.class, () -> (first) -> NUMERIC)
                .supportSingle(ObjDoubleConsumer.class, () -> (first, second) -> LOGGER.test(CONSUMING))
                .supportSingle(ObjIntConsumer.class, () -> (first, second) -> LOGGER.test(CONSUMING))
                .supportSingle(ObjLongConsumer.class, () -> (first, second) -> LOGGER.test(CONSUMING))
                .supportSingle(Predicate.class, () -> (first) -> BOOLEAN)
                .supportSingle(Supplier.class, () -> () -> null)
                .supportSingle(ToDoubleBiFunction.class, () -> (first, second) -> NUMERIC)
                .supportSingle(ToDoubleFunction.class, () -> (first) -> NUMERIC)
                .supportSingle(ToIntBiFunction.class, () -> (first, second) -> NUMERIC)
                .supportSingle(ToIntFunction.class, () -> (first) -> NUMERIC)
                .supportSingle(ToLongBiFunction.class, () -> (first, second) -> NUMERIC)
                .supportSingle(ToLongFunction.class, () -> (first) -> NUMERIC)
                .supportSingle(UnaryOperator.class, () -> (first) -> Function.identity())
                .build();
    }

    /**
     * @return java.util.stream
     */
    static AggregatedValueGenerator getStreamSupport() {
        return SupportBuilder.createSupport()
                .support(BaseStream.class)
                .withImplementation(IntStream.class, () -> IntStream.of(NUMERIC))
                .withImplementation(LongStream.class, () -> LongStream.of(NUMERIC))
                .withImplementation(DoubleStream.class, () -> DoubleStream.of(NUMERIC))
                .withImplementation(Stream.class, Stream::empty)
                .build();
    }

    static AggregatedValueGenerator getConcurrentSupport() {
        return SupportBuilder.createSupport()
                .support(BlockingQueue.class)
                .withImplementation(ArrayBlockingQueue.class, () -> new ArrayBlockingQueue<>(NUMERIC))
                .withImplementation(DelayQueue.class, DelayQueue::new)
                .withImplementation(LinkedTransferQueue.class, LinkedTransferQueue::new)
                .withImplementation(LinkedBlockingDeque.class, () -> new LinkedBlockingDeque<>(NUMERIC))
                .withImplementation(LinkedBlockingQueue.class, () -> new LinkedBlockingQueue<>(NUMERIC))
                .withImplementation(PriorityBlockingQueue.class, () -> new PriorityBlockingQueue<>(NUMERIC))
                .withImplementation(SynchronousQueue.class, SynchronousQueue::new)
                .supportSingle(Callable.class, () -> () -> null)
                .support(Future.class)
                .withImplementation(RunnableScheduledFuture.class, NoOpRunnableScheduledFuture::new)
                .withImplementation(CountedCompleter.class, NoOpCountedCompleter::new)
                .withImplementation(FutureTask.class, () -> new FutureTask<>(() -> null))
                .support(Delayed.class)
                .withImplementation(RunnableScheduledFuture.class, NoOpRunnableScheduledFuture::new)
                .supportSingle(CompletionStage.class, CompletableFuture.class, () -> CompletableFuture.completedFuture(
                        null))
                .supportSingle(ConcurrentLinkedDeque.class, ConcurrentLinkedDeque::new)
                .supportSingle(ConcurrentLinkedQueue.class, ConcurrentLinkedQueue::new)
                .support(ConcurrentMap.class)
                .withImplementation(ConcurrentHashMap.class, ConcurrentHashMap::new)
                .withImplementation(ConcurrentSkipListMap.class, ConcurrentSkipListMap::new)
                .support(Executor.class)
                .withImplementation(ForkJoinPool.class, () -> new ForkJoinPool(NUMERIC))
                .withImplementation(ScheduledThreadPoolExecutor.class, () -> new ScheduledThreadPoolExecutor(NUMERIC))
                .supportSingle(ConcurrentSkipListSet.class, ConcurrentSkipListSet::new)
                .supportSingle(CompletionService.class, ExecutorCompletionService.class,
                        () -> new ExecutorCompletionService<>(new ForkJoinPool(NUMERIC)))
                .supportSingle(CopyOnWriteArrayList.class, CopyOnWriteArrayList::new)
                .supportSingle(CopyOnWriteArraySet.class, CopyOnWriteArraySet::new)
                .supportSingle(CountDownLatch.class, () -> new CountDownLatch(NUMERIC))
                .supportSingle(CyclicBarrier.class, () -> new CyclicBarrier(NUMERIC))
                .supportSingle(Exchanger.class, Exchanger::new)
                .supportSingle(Phaser.class, Phaser::new)
                .supportSingle(Semaphore.class, () -> new Semaphore(NUMERIC))
                .supportSingle(ThreadLocalRandom.class, ThreadLocalRandom::current)
                .supportSingle(TimeUnit.class, () -> TimeUnit.SECONDS)
                .build();
    }

    static AggregatedValueGenerator getConcurrentAtomicSupport() {
        return SupportBuilder.createSupport()
                .supportSingle(AtomicBoolean.class, () -> new AtomicBoolean(true))
                .supportSingle(AtomicInteger.class, () -> new AtomicInteger(1))
                .supportSingle(AtomicIntegerArray.class, () -> new AtomicIntegerArray(1))
                .supportSingle(AtomicLong.class, () -> new AtomicLong(1))
                .supportSingle(AtomicLongArray.class, () -> new AtomicLongArray(1))
                .supportSingle(AtomicReference.class, AtomicReference::new)
                .supportSingle(AtomicReferenceArray.class, () -> new AtomicReferenceArray<>(1))
                .supportSingle(DoubleAccumulator.class, () -> new DoubleAccumulator(Double::sum, 1))
                .supportSingle(DoubleAdder.class, DoubleAdder::new)
                .supportSingle(LongAccumulator.class, () -> new LongAccumulator(Long::sum, 1))
                .supportSingle(LongAdder.class, LongAdder::new)
                .build();
    }

    static AggregatedValueGenerator getConcurrentLocksSupport() {
        return SupportBuilder.createSupport()
                .supportSingle(Lock.class, ReentrantLock.class, ReentrantLock::new)
                .supportSingle(ReadWriteLock.class, ReentrantReadWriteLock.class, ReentrantReadWriteLock::new)
                .supportSingle(StampedLock.class, StampedLock::new)
                .build();
    }

    static class NoOpEnumeration<E> implements Enumeration<E> {
        @Override
        public boolean hasMoreElements() {
            return false;
        }

        @Override
        public E nextElement() {
            return null;
        }
    }

    static class NoOpRunnableScheduledFuture<V> implements RunnableScheduledFuture<V> {
        @Override
        public boolean isPeriodic() {
            return false;
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public long getDelay(TimeUnit unit) {
            return 0;
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public int compareTo(Delayed o) {
            return 0;
        }

        @Override
        public void run() {
            LOGGER.test("Running...");
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public V get() throws InterruptedException, ExecutionException {
            return null;
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }
    }

    static class NoOpCountedCompleter<T> extends CountedCompleter<T> {
        @Override
        public void compute() {
            LOGGER.test("Computing...");
        }

        @Override
        public void onCompletion(CountedCompleter<?> caller) {
            LOGGER.test("Completing...");
        }
    }
}
