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

/**
 * Commonly shared logger in the framework.
 */
public interface Logger {
    /**
     * Logs an informational message.
     *
     * @param msg to log
     */
    void info(String msg);

    /**
     * Logs an informational message, using {@link MessageFormat} for replaceable patterns in given {@code msg}.
     *
     * @param msg  to log
     * @param args to replace patterns in {@code msg} with
     */
    default void info(String msg, Object... args) {
        info(MessageFormat.format(msg, args));
    }

    /**
     * Logs a warning.
     *
     * @param msg to log
     */
    void warn(String msg);

    /**
     * Logs a warning message, using {@link MessageFormat} for replaceable patterns in given {@code msg}.
     *
     * @param msg  to log
     * @param args to replace patterns in {@code msg} with
     */
    default void warn(String msg, Object... args) {
        warn(MessageFormat.format(msg, args));
    }

    /**
     * Logs a configuration update.
     *
     * @param msg to log
     */
    void config(String msg);

    /**
     * Logs a configuration update, using {@link MessageFormat} for replaceable patterns in given {@code msg}.
     *
     * @param msg  to log
     * @param args to replace patterns in {@code msg} with
     */
    default void config(String msg, Object... args) {
        config(MessageFormat.format(msg, args));
    }

    /**
     * Logs a test message, used when a test is being performed.
     *
     * @param msg to log
     */
    void test(String msg);

    /**
     * Logs a test message, used when a test is being performed. Using {@link MessageFormat} for replaceable
     * patterns in given {@code msg}.
     *
     * @param msg  to log
     * @param args to replace patterns in {@code msg} with
     */
    default void test(String msg, Object... args) {
        test(MessageFormat.format(msg, args));
    }

    /**
     * Logs a fail message, used when an assertion fails. Using {@link MessageFormat} for replaceable parts and
     * structuring given {@code detail}, {@code expected} and {@code reality} in their suitable place.
     *
     * @param detail   of the failure
     * @param expected before failing
     * @param reality  when failing
     */
    default void fail(String detail, String expected, String reality) {
        warn("{0}{1}{4}Expectation: {2}{1}{4}but{1}{4}Reality: {3}",
                detail, System.lineSeparator(), expected, reality, LogFormatter.COLOR_RED);
    }

    /**
     * Creates a new {@code Logger} for given {@code loggingClass}.
     *
     * @param loggingClass to log for
     * @return a new logger
     */
    static Logger getLogger(Class<?> loggingClass) {
        String loggerName = String.format("%s.%s",
                JavaLoggerAdapter.adaptPackageToLogFormat(loggingClass.getPackage()),
                loggingClass.getSimpleName());

        return getLogger(loggerName);
    }

    /**
     * Creates a new {@code Logger} of given {@code loggingName}.
     *
     * @param loggingName to log for
     * @return a new logger
     */
    static Logger getLogger(String loggingName) {
        return new JavaLoggerAdapter(new LogHandler(new LogFormatter()), loggingName);
    }
}
