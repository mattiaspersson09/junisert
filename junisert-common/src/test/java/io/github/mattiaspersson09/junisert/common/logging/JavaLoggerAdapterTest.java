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
package io.github.mattiaspersson09.junisert.common.logging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class JavaLoggerAdapterTest {
    @Mock
    Handler handler;

    private Logger logger;
    private ByteArrayOutputStream output;

    @BeforeEach
    void setUp() {
        output = new ByteArrayOutputStream();
        logger = new JavaLoggerAdapter(new TestHandler(output), "test");
    }

    @Test
    void logging_whenMessageFormat_thenPlacesArguments() {
        logger.info("{0}", "hello");
        assertThat(output.toString()).isEqualTo("hello");
        output.reset();

        logger.config("hello {0}", "world");
        assertThat(output.toString()).isEqualTo("hello world");
        output.reset();

        logger.warn("hello {0} {1}", "new", "world");
        assertThat(output.toString()).isEqualTo("hello new world");
        output.reset();

        logger.test("hello {0} {1} {2}", "new", "world", "order");
        assertThat(output.toString()).isEqualTo("hello new world order");
    }

    private static class TestHandler extends StreamHandler {
        private final ByteArrayOutputStream outputStream;

        public TestHandler(ByteArrayOutputStream outputStream) {
            super(outputStream, new NoOpFormatter());
            this.outputStream = outputStream;
        }

        @Override
        public void publish(LogRecord record) {
            try {
                outputStream.write(record.getMessage().getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class NoOpFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            return record.getMessage();
        }
    }
}
