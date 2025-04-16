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
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

final class LogFormatter extends Formatter {
    private static final String COLOR_RESET = "\033[0m";
    private static final String COLOR_WARNING = "\033[31m";
    private static final String COLOR_INFO = COLOR_RESET;
    private static final String COLOR_CONFIG = "\033[33m";

    @Override
    public String format(LogRecord record) {
        return String.format("%1$s[%2$tF %2$tT] [%3$s] [%4$s] %5$s%6$s%n",
                toColor(record.getLevel()),
                new Date(record.getMillis()),
                record.getLevel().getLocalizedName(),
                record.getLoggerName(),
                record.getMessage(),
                COLOR_RESET);
    }

    private String toColor(Level level) {
        if (Level.INFO.equals(level)) {
            return COLOR_INFO;
        } else if (Level.CONFIG.equals(level)) {
            return COLOR_CONFIG;
        } else if (Level.WARNING.equals(level)) {
            return COLOR_WARNING;
        } else {
            return COLOR_RESET;
        }
    }
}
