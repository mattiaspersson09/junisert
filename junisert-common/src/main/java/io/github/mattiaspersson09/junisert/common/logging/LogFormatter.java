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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Stream;

final class LogFormatter extends Formatter {
    static final String COLOR_RESET = "\u001B[0m";
    static final String COLOR_RED = "\u001B[31m";
    static final String COLOR_STANDARD = COLOR_RESET;
    static final String COLOR_BLUE = "\u001B[34m";
    static final String COLOR_YELLOW = "\u001B[33m";
    static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    @Override
    public String format(LogRecord record) {
        LogLevel level = LogLevel.from(record.getLevel());
        String dateTime = DATE_FORMATTER.format(new Date(record.getMillis()));

        return String.format("%1$s[%2$s] [%3$s] [%4$s] %5$s%6$s%n",
                level.toColor(),
                dateTime,
                level,
                record.getLoggerName(),
                record.getMessage(),
                COLOR_RESET);
    }

    private enum LogLevel {
        WARNING(Level.WARNING, COLOR_RED),
        INFO(Level.INFO, COLOR_STANDARD),
        CONFIG(Level.CONFIG, COLOR_BLUE),
        TEST(Level.FINE, COLOR_YELLOW);

        private final Level level;
        private final String color;

        LogLevel(Level level, String color) {
            this.level = level;
            this.color = color;
        }

        private static LogLevel from(Level level) {
            return Stream.of(values())
                    .filter(logLevel -> Objects.equals(logLevel.level, level))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Unsupported log level: " + level.getName()));
        }

        public String toColor() {
            return color;
        }

        @Override
        public String toString() {
            return name().replace("_", " ");
        }
    }
}
