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
package io.github.mattiaspersson09.junisert.value.java;

import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.PrimitiveValueGenerator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.AllPermission;
import java.security.BasicPermission;
import java.security.Guard;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreSpi;
import java.security.MessageDigest;
import java.security.Permission;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SecurityPermission;
import java.security.Signature;
import java.security.UnresolvedPermission;
import java.text.Bidi;
import java.text.ChoiceFormat;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaInternalsIntegrationTest {
    private static ValueGenerator<?> generator;
    private static LambdaTester lambdaTester;

    @BeforeAll
    static void setUp() {
        generator = JavaInternals.getSupported();
        lambdaTester = new LambdaTester(Arrays.asList(
                new PrimitiveValueGenerator(),
                new ObjectValueGenerator(),
                JavaUtilSupport.getFunctionalSupport()
        ));
    }

    @ParameterizedTest
    @ValueSource(classes = {
            // Super types
            Appendable.class,
            CharSequence.class,
            Iterable.class,
            Runnable.class,
            Throwable.class,
            // Implementations
            String.class,
            StringBuffer.class,
            StringBuilder.class,
            Exception.class,
            RuntimeException.class,
            Error.class,
            AssertionError.class,
            Thread.class,
            ThreadLocal.class,
    })
    void javaLang(Class<?> type) {
        supportsJavaInternal(type);
        lambdaTester.invoke(type, () -> generator.generate(type).get());
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
        supportsJavaInternal(type);
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
        supportsJavaInternal(type);
        lambdaTester.invoke(type, () -> generator.generate(type).get());
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
        supportsJavaInternal(type);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            // Super types
            Closeable.class,
            DataInput.class,
            DataOutput.class,
            // Implementations
            BufferedInputStream.class,
            BufferedOutputStream.class,
            BufferedReader.class,
            BufferedWriter.class,
            ByteArrayInputStream.class,
            ByteArrayOutputStream.class,
            CharArrayReader.class,
            CharArrayWriter.class,
            DataInputStream.class,
            DataOutputStream.class,
            FilterInputStream.class,
            FilterOutputStream.class,
            InputStream.class,
            InputStreamReader.class,
            LineNumberReader.class,
            OutputStream.class,
            OutputStreamWriter.class,
            PrintStream.class,
            PrintWriter.class,
            Reader.class,
            StreamTokenizer.class,
            StringReader.class,
            StringWriter.class,
            Writer.class,
    })
    void javaIO(Class<?> type) {
        supportsJavaInternal(type);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            // Super types
            DateFormat.class,
            Format.class,
            NumberFormat.class,
            // Implementations
            Bidi.class,
            ChoiceFormat.class,
            DecimalFormat.class,
            MessageFormat.class,
            SimpleDateFormat.class,
    })
    void javaText(Class<?> type) {
        supportsJavaInternal(type);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            // Super types
            Guard.class,
            Key.class,
            Permission.class,
            Principal.class,
            PrivateKey.class,
            PublicKey.class,
            // Implementations
            AlgorithmParameterGenerator.class,
            AlgorithmParameters.class,
            AllPermission.class,
            BasicPermission.class,
            KeyFactory.class,
            KeyPair.class,
            KeyPairGenerator.class,
            KeyStore.class,
            KeyStoreSpi.class,
            MessageDigest.class,
            SecurityPermission.class,
            SecureRandom.class,
            Signature.class,
            UnresolvedPermission.class,
    })
    void javaSecurity(Class<?> type) {
        supportsJavaInternal(type);
    }

    private void supportsJavaInternal(Class<?> javaType) {
        Value<?> value = generator.generate(javaType);

        assertThat(generator.supports(javaType)).isTrue();
        assertThat(value.asEmpty()).isNull();
        assertThat(value.get()).isNotNull();
        assertThat(value.get()).isInstanceOf(javaType);
        assertThat(javaType.cast(value.get())).isInstanceOf(javaType);
    }
}
