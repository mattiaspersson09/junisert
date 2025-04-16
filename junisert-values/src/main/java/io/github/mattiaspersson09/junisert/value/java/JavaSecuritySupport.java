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
package io.github.mattiaspersson09.junisert.value.java;

import io.github.mattiaspersson09.junisert.api.internal.support.AggregatedValueGenerator;
import io.github.mattiaspersson09.junisert.api.internal.support.SupportBuilder;
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.AllPermission;
import java.security.Guard;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreSpi;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SecurityPermission;
import java.security.Signature;
import java.security.UnresolvedPermission;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.Enumeration;

final class JavaSecuritySupport {
    private static final String ALGORITHM = "DiffieHellman";
    private static final String NOOP = "no-op";

    private JavaSecuritySupport() {
    }

    static AggregatedValueGenerator getSecuritySupport() {
        return SupportBuilder.createSupport()
                .supportSingle(AlgorithmParameterGenerator.class, () -> {
                    try {
                        return AlgorithmParameterGenerator.getInstance(ALGORITHM);
                    } catch (NoSuchAlgorithmException e) {
                        throw new UnsupportedTypeError(AlgorithmParameterGenerator.class);
                    }
                })
                .supportSingle(AlgorithmParameters.class, () -> {
                    try {
                        return AlgorithmParameters.getInstance(ALGORITHM);
                    } catch (NoSuchAlgorithmException e) {
                        throw new UnsupportedTypeError(AlgorithmParameters.class);
                    }
                })
                .support(Guard.class)
                .withImplementation(AllPermission.class, AllPermission::new)
                .withImplementation(SecurityPermission.class, () -> new SecurityPermission("security"))
                .withImplementation(UnresolvedPermission.class,
                        () -> new UnresolvedPermission(NOOP, NOOP, NOOP, new java.security.cert.Certificate[]{}))
                .support(Key.class)
                .withImplementation(NoOpPublicKey.class, NoOpPublicKey::new)
                .withImplementation(NoOpPrivateKey.class, NoOpPrivateKey::new)
                .supportSingle(KeyFactory.class, () -> {
                    try {
                        return KeyFactory.getInstance(ALGORITHM);
                    } catch (NoSuchAlgorithmException e) {
                        throw new UnsupportedTypeError(KeyFactory.class);
                    }
                })
                .supportSingle(KeyPair.class, () -> new KeyPair(new NoOpPublicKey(), new NoOpPrivateKey()))
                .supportSingle(KeyPairGenerator.class, () -> {
                    try {
                        return KeyPairGenerator.getInstance(ALGORITHM);
                    } catch (NoSuchAlgorithmException e) {
                        throw new UnsupportedTypeError(KeyPairGenerator.class);
                    }
                })
                .supportSingle(KeyStore.class, NoOpKeyStore::new)
                .supportSingle(KeyStoreSpi.class, NoOpKeyStoreSpi::new)
                .supportSingle(MessageDigest.class, () -> {
                    try {
                        return MessageDigest.getInstance("SHA-256");
                    } catch (NoSuchAlgorithmException e) {
                        throw new UnsupportedTypeError(MessageDigest.class);
                    }
                })
                .supportSingle(Principal.class, () -> () -> NOOP + " principal")
                .supportSingle(SecureRandom.class, SecureRandom::new)
                .supportSingle(Signature.class, () -> {
                    try {
                        return Signature.getInstance("SHA256withRSA");
                    } catch (NoSuchAlgorithmException e) {
                        throw new UnsupportedTypeError(Signature.class);
                    }
                })
                .build();
    }

    private static class NoOpKeyStore extends KeyStore {
        public NoOpKeyStore() {
            super(new NoOpKeyStoreSpi(), null, "security");
        }
    }

    private static class NoOpKeyStoreSpi extends KeyStoreSpi {
        @Override
        public Key engineGetKey(String alias, char[] password) {
            return new NoOpPrivateKey();
        }

        @Override
        public Certificate[] engineGetCertificateChain(String alias) {
            return new Certificate[0];
        }

        @Override
        public Certificate engineGetCertificate(String alias) {
            return null;
        }

        @Override
        public Date engineGetCreationDate(String alias) {
            return new Date();
        }

        @Override
        public void engineSetKeyEntry(String alias, Key key, char[] password, Certificate[] chain) {
            // no-op
        }

        @Override
        public void engineSetKeyEntry(String alias, byte[] key, Certificate[] chain) {
            // no-op
        }

        @Override
        public void engineSetCertificateEntry(String alias, Certificate cert) {
            // no-op
        }

        @Override
        public void engineDeleteEntry(String alias) {
            // no-op
        }

        @Override
        public Enumeration<String> engineAliases() {
            return new JavaUtilSupport.EmptyEnumeration<>();
        }

        @Override
        public boolean engineContainsAlias(String alias) {
            return false;
        }

        @Override
        public int engineSize() {
            return 0;
        }

        @Override
        public boolean engineIsKeyEntry(String alias) {
            return false;
        }

        @Override
        public boolean engineIsCertificateEntry(String alias) {
            return false;
        }

        @Override
        public String engineGetCertificateAlias(Certificate cert) {
            return null;
        }

        @Override
        public void engineStore(OutputStream stream, char[] password) {
            // no-op
        }

        @Override
        public void engineLoad(InputStream stream, char[] password) {
            // no-op
        }
    }

    private static class NoOpPublicKey implements PublicKey {
        @Override
        public String getAlgorithm() {
            return ALGORITHM;
        }

        @Override
        public String getFormat() {
            return "X.509";
        }

        @Override
        public byte[] getEncoded() {
            return new byte[0];
        }
    }

    private static class NoOpPrivateKey implements PrivateKey {
        @Override
        public String getAlgorithm() {
            return ALGORITHM;
        }

        @Override
        public String getFormat() {
            return "X.509";
        }

        @Override
        public byte[] getEncoded() {
            return new byte[0];
        }
    }
}
