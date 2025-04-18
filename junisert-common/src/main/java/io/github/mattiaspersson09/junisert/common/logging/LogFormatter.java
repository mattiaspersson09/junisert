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

import java.util.Date;
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

    @Override
    public String format(LogRecord record) {
        LogLevel level = LogLevel.from(record.getLevel());

        return String.format("%1$s[%2$tF %2$tT] [%3$s] [%4$s] %5$s%6$s%n",
                level.toColor(),
                new Date(record.getMillis()),
                level,
                record.getLoggerName(),
                record.getMessage(),
                COLOR_RESET);
    }

    private enum LogLevel {
        TEST(Level.FINE, COLOR_YELLOW),
        CONFIG(Level.CONFIG, COLOR_BLUE),
        INFO(Level.INFO, COLOR_STANDARD),
        WARNING(Level.WARNING, COLOR_RED),
        MISSING(null, COLOR_RESET);

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
                    .orElse(MISSING);
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
