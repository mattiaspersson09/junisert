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

import java.util.logging.Handler;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class JavaLoggerAdapter implements Logger {
    private static final int PACKAGE_SHORTENED_CHARS = 3;
    private static final int PACKAGE_DEEP_BREAKPOINT = 3;

    private final java.util.logging.Logger logger;

    JavaLoggerAdapter(Handler handler, String name) {
        logger = java.util.logging.Logger.getLogger(name);
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(String msg, Object... args) {
        info(String.format(msg, args));
    }

    @Override
    public void warn(String msg) {
        logger.warning(msg);
    }

    @Override
    public void warn(String msg, Object... args) {
        warn(String.format(msg, args));
    }

    @Override
    public void config(String msg) {
        logger.config(msg);
    }

    @Override
    public void config(String msg, Object... args) {
        config(String.format(msg, args));
    }

    static String adaptPackageToLogFormat(Package pack) {
        String[] packageNames = pack.getName().split("\\.");

        if (packageNames.length <= PACKAGE_DEEP_BREAKPOINT) {
            return pack.getName();
        }

        return Stream.of(packageNames)
                .map(JavaLoggerAdapter::shortenPackageName)
                .collect(Collectors.joining("."));
    }

    private static String shortenPackageName(String packageName) {
        if (packageName.length() <= PACKAGE_SHORTENED_CHARS) {
            return packageName;
        }

        return packageName.substring(0, PACKAGE_SHORTENED_CHARS);
    }
}
