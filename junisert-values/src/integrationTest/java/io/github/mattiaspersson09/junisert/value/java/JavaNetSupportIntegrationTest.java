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

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.DatagramPacket;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.JarURLConnection;
import java.net.NetPermission;
import java.net.PasswordAuthentication;
import java.net.ProtocolFamily;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.SocketPermission;
import java.net.StandardProtocolFamily;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLPermission;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JavaNetSupportIntegrationTest extends JavaInternalIntegrationTest {
    @ParameterizedTest
    @ValueSource(classes = {
            // Super types
            CookiePolicy.class,
            ProtocolFamily.class,
            SocketOption.class,
            // Implementations
            CookieHandler.class,
            CookieManager.class,
            DatagramPacket.class,
            HttpCookie.class,
            HttpURLConnection.class,
            InetSocketAddress.class,
            JarURLConnection.class,
            NetPermission.class,
            PasswordAuthentication.class,
            Proxy.class,
            ServerSocket.class,
            Socket.class,
            SocketAddress.class,
            SocketPermission.class,
            URI.class,
            URL.class,
            URLClassLoader.class,
            URLConnection.class,
            URLPermission.class,
            Proxy.Type.class,
            StandardProtocolFamily.class
    })
    void javaNet(Class<?> type) {
        assertIsSupported(type);
    }
}
