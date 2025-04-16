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

import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.StreamHandler;

public interface Logger {
    void info(String msg);

    void info(String msg, Object... args);

    void warn(String msg);

    void warn(String msg, Object... args);

    void config(String msg);

    void config(String msg, Object... args);


    static Logger getLogger(Class<?> loggingClass) {
        String loggerName = String.format("%s.%s",
                JavaLoggerAdapter.adaptPackageToLogFormat(loggingClass.getPackage()),
                loggingClass.getSimpleName());

        return getLogger(loggerName);
    }

    static Logger getLogger(String loggingName) {
        Handler handler = new StreamHandler(new PrintStream(System.out), new LogFormatter());

        return new JavaLoggerAdapter(handler, loggingName);
    }
}
