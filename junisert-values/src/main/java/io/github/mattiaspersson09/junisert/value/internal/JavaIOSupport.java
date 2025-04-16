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
package io.github.mattiaspersson09.junisert.value.internal;

import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.api.internal.support.SupportBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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

final class JavaIOSupport {
    static AggregatedValueGenerator getIOSupport() {
        return SupportBuilder.createSupport()
                .supportSingle(Closeable.class, ByteArrayInputStream.class, JavaIOSupport::inputStream)
                .supportSingle(DataInput.class, DataInputStream.class, () -> new DataInputStream(inputStream()))
                .supportSingle(DataOutput.class, DataOutputStream.class, () -> new DataOutputStream(outputStream()))
                .support(InputStream.class)
                .withImplementation(ByteArrayInputStream.class, JavaIOSupport::inputStream)
                .withImplementation(BufferedInputStream.class, () -> new BufferedInputStream(inputStream()))
                .support(OutputStream.class)
                .withImplementation(ByteArrayOutputStream.class, JavaIOSupport::outputStream)
                .withImplementation(BufferedOutputStream.class, () -> new BufferedOutputStream(outputStream()))
                .withImplementation(PrintStream.class, () -> new PrintStream(outputStream()))
                .support(Reader.class)
                .withImplementation(InputStreamReader.class, () -> new InputStreamReader(inputStream()))
                .withImplementation(LineNumberReader.class, () -> new LineNumberReader(reader()))
                .withImplementation(StringReader.class, JavaIOSupport::reader)
                .withImplementation(CharArrayReader.class, () -> new CharArrayReader(new char[]{}))
                .support(Writer.class)
                .withImplementation(OutputStreamWriter.class, () -> new OutputStreamWriter(outputStream()))
                .withImplementation(BufferedWriter.class, () -> new BufferedWriter(writer()))
                .withImplementation(StringWriter.class, StringWriter::new)
                .withImplementation(CharArrayWriter.class, CharArrayWriter::new)
                .withImplementation(PrintWriter.class, () -> new PrintWriter(writer()))
                .supportSingle(StreamTokenizer.class, () -> new StreamTokenizer(reader()))
                .build();
    }

    private static ByteArrayInputStream inputStream() {
        return new ByteArrayInputStream("source".getBytes());
    }

    private static ByteArrayOutputStream outputStream() {
        return new ByteArrayOutputStream();
    }

    private static StringReader reader() {
        return new StringReader("source");
    }

    private static StringWriter writer() {
        return new StringWriter();
    }
}
