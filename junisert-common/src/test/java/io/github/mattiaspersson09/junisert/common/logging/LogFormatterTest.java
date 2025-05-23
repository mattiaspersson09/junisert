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

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LogFormatterTest {
    @Mock
    LogRecord logRecord;
    private LogFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new LogFormatter();
    }

    @Test
    void format() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 1, 0, 0, 0);

        when(logRecord.getMillis()).thenReturn(dateTime.toInstant(ZoneOffset.ofHours(1)).toEpochMilli());
        when(logRecord.getLevel()).thenReturn(Level.WARNING);
        when(logRecord.getLoggerName()).thenReturn("logger");
        when(logRecord.getMessage()).thenReturn("message");

        assertThat(formatter.format(logRecord))
                .contains(LogFormatter.COLOR_RED)
                .contains("[WARNING] [logger] message")
                .contains(LogFormatter.COLOR_RESET);
    }

    @Test
    void format_whenUnsupportedLogLevel_thenThrowsIllegalArgumentException() {
        when(logRecord.getLevel()).thenReturn(Level.OFF);

        assertThatThrownBy(() -> formatter.format(logRecord)).isInstanceOf(IllegalArgumentException.class);
    }
}
