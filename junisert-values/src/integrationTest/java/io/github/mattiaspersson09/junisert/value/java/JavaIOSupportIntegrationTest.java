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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JavaIOSupportIntegrationTest extends JavaInternalIntegrationTest {
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
        assertIsSupported(type);
    }
}
