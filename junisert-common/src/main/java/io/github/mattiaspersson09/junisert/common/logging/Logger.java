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

import java.text.MessageFormat;

public interface Logger {
    void info(String msg);

    default void info(String msg, Object... args) {
        info(MessageFormat.format(msg, args));
    }

    void warn(String msg);

    default void warn(String msg, Object... args) {
        warn(MessageFormat.format(msg, args));
    }

    void config(String msg);

    default void config(String msg, Object... args) {
        config(MessageFormat.format(msg, args));
    }

    void test(String msg);

    default void test(String msg, Object... args) {
        test(MessageFormat.format(msg, args));
    }

    default void fail(String detail, String expected, String reality) {
        warn("{0}{1}{4}Expectation: {2}{1}{4}but{1}{4}Reality: {3}",
                detail, System.lineSeparator(), expected, reality, LogFormatter.COLOR_RED);
    }


    static Logger getLogger(Class<?> loggingClass) {
        String loggerName = String.format("%s.%s",
                JavaLoggerAdapter.adaptPackageToLogFormat(loggingClass.getPackage()),
                loggingClass.getSimpleName());

        return getLogger(loggerName);
    }

    static Logger getLogger(String loggingName) {
        return new JavaLoggerAdapter(new LogHandler(new LogFormatter()), loggingName);
    }
}
