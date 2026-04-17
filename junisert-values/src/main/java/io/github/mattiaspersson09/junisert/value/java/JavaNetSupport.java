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

import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.api.internal.support.SupportBuilder;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.DatagramPacket;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
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
import java.net.StandardSocketOptions;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLPermission;
import java.util.jar.JarFile;

final class JavaNetSupport {
    private static final String JUNISERT = "junisert";
    private static final String URI = "http://junisert";

    private JavaNetSupport() {
    }

    static AggregatedValueGenerator getNetSupport() {
        return SupportBuilder.createSupport()
                .supportSingle(CookiePolicy.class, () -> CookiePolicy.ACCEPT_NONE)
                .supportSingle(ProtocolFamily.class, StandardProtocolFamily.class, () -> StandardProtocolFamily.INET)
                .supportSingle(SocketOption.class, () -> StandardSocketOptions.SO_BROADCAST)
                .supportSingle(CookieHandler.class, CookieManager.class, CookieManager::new)
                .supportSingle(DatagramPacket.class, () -> new DatagramPacket("buffer".getBytes(), 1))
                .supportSingle(HttpCookie.class, () -> new HttpCookie("key", "value"))
                .support(URLConnection.class)
                .withImplementation(HttpURLConnection.class, JavaNetSupport::httpURLConnection)
                .withImplementation(JarURLConnection.class, JavaNetSupport::jarURLConnection)
                .supportSingle(NetPermission.class, () -> new NetPermission(JUNISERT))
                .supportSingle(PasswordAuthentication.class, () -> new PasswordAuthentication(JUNISERT, new char[]{}))
                .supportSingle(Proxy.class, () -> new Proxy(Proxy.Type.HTTP, new InetSocketAddress(80)))
                .supportSingle(ServerSocket.class, JavaNetSupport::serverSocket)
                .supportSingle(Socket.class, Socket::new)
                .supportSingle(SocketAddress.class, InetSocketAddress.class, () -> new InetSocketAddress(80))
                .supportSingle(SocketPermission.class, () -> new SocketPermission(JUNISERT, "listen"))
                .supportSingle(URI.class, () -> {
                    try {
                        return new URI(URI);
                    } catch (URISyntaxException e) {
                        throw new UnsupportedTypeError(URI.class);
                    }
                })
                .supportSingle(URL.class, () -> createURLOrElseDontSupport(URI, URL.class))
                .supportSingle(URLClassLoader.class, () -> new URLClassLoader(new URL[]{createURLOrElseDontSupport(URI,
                        URLClassLoader.class)}))
                .supportSingle(URLPermission.class, () -> new URLPermission(URI))
                .supportSingle(Proxy.Type.class, () -> Proxy.Type.HTTP)
                .build();
    }

    private static ServerSocket serverSocket() {
        try {
            return new ServerSocket();
        } catch (IOException e) {
            throw new UnsupportedTypeError(ServerSocket.class);
        }
    }

    private static HttpURLConnection httpURLConnection() {
        return new HttpURLConnection(createURLOrElseDontSupport(URI, HttpURLConnection.class)) {
            @Override
            public void disconnect() {
                // no-op
            }

            @Override
            public boolean usingProxy() {
                return false;
            }

            @Override
            public void connect() {
                // no-op
            }
        };
    }

    private static JarURLConnection jarURLConnection() {
        try {
            return new JarURLConnection(createURLOrElseDontSupport("jar:file:fake-test-jar!/",
                    JarURLConnection.class)) {
                @Override
                public JarFile getJarFile() {
                    return null;
                }

                @Override
                public void connect() {
                    // no-op
                }
            };
        } catch (MalformedURLException e) {
            throw new UnsupportedTypeError(JarURLConnection.class);
        }
    }

    private static URL createURLOrElseDontSupport(String uri, Class<?> maybeSupport) {
        try {
            return new URI(uri).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new UnsupportedTypeError(maybeSupport);
        }
    }
}
