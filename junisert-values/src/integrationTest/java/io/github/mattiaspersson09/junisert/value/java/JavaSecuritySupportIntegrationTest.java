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

import io.github.mattiaspersson09.junisert.value.common.ArrayValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.InterfaceValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.ObjectValueGenerator;
import io.github.mattiaspersson09.junisert.value.common.PrimitiveValueGenerator;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.AllPermission;
import java.security.BasicPermission;
import java.security.Guard;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreSpi;
import java.security.MessageDigest;
import java.security.Permission;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SecurityPermission;
import java.security.Signature;
import java.security.UnresolvedPermission;
import java.security.cert.Certificate;
import java.util.Arrays;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JavaSecuritySupportIntegrationTest extends JavaInternalIntegrationTest {
    JavaSecuritySupportIntegrationTest() {
        super(new SupportInvoker(Arrays.asList(
                new PrimitiveValueGenerator(),
                new ArrayValueGenerator(),
                JavaSecuritySupport.getSecuritySupport(),
                JavaIOSupport.getIOSupport(),
                JavaLangSupport.getLangSupport(),
                JavaUtilSupport.getUtilSupport(),
                JavaUtilSupport.getFunctionalSupport(),
                new ObjectValueGenerator(),
                new InterfaceValueGenerator()
        )));
    }

    @ParameterizedTest
    @ValueSource(classes = {
            // Super types
            Certificate.class,
            Guard.class,
            Key.class,
            Permission.class,
            Provider.class,
            Principal.class,
            PrivateKey.class,
            PublicKey.class,
            // Implementations
            AlgorithmParameterGenerator.class,
            AlgorithmParameters.class,
            AllPermission.class,
            BasicPermission.class,
            KeyFactory.class,
            KeyPair.class,
            KeyPairGenerator.class,
            KeyStore.class,
            KeyStoreSpi.class,
            MessageDigest.class,
            SecurityPermission.class,
            SecureRandom.class,
            Signature.class,
            UnresolvedPermission.class,
    })
    void javaSecurity(Class<?> type) {
        assertIsSupported(type);
    }

    @ParameterizedTest
    @ValueSource(classes = {
            Certificate.class,
            PrivateKey.class,
            PublicKey.class,
            Key.class,
            KeyStore.class,
            KeyStoreSpi.class
    })
    void noOpSupport(Class<?> type) {
        assertThatSupportCanBeUsed(type);
    }
}
