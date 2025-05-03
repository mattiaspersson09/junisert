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
package io.github.mattiaspersson09.junisert.core.reflection;


import java.util.Objects;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ModifierIntegrationTest {

    @Test
    void getsCorrectModifiers() throws NoSuchFieldException, NoSuchMethodException {
        Modifier publicConstant = new Modifier(Modifiers.class.getDeclaredField("PUBLIC_CONSTANT").getModifiers());
        Modifier privateConstant = new Modifier(Modifiers.class.getDeclaredField("PRIVATE_CONSTANT").getModifiers());
        Modifier publicField = new Modifier(Modifiers.class.getDeclaredField("publicField").getModifiers());
        Modifier packageField = new Modifier(Modifiers.class.getDeclaredField("packageField").getModifiers());
        Modifier privateField = new Modifier(Modifiers.class.getDeclaredField("privateField").getModifiers());
        Modifier privateFinalField = new Modifier(Modifiers.class.getDeclaredField("privateFinalField").getModifiers());
        Modifier protectedField = new Modifier(Modifiers.class.getDeclaredField("protectedField").getModifiers());
        Modifier packageTransientField = new Modifier(
                Modifiers.class.getDeclaredField("packageTransientField").getModifiers());
        Modifier packageVolatileField = new Modifier(Modifiers.class
                .getDeclaredField("packageVolatileField")
                .getModifiers());

        Modifier publicSynchronizedMethod = new Modifier(Modifiers.class
                .getDeclaredMethod("publicSynchronizedMethod")
                .getModifiers());
        Modifier privateInterface = new Modifier(PrivateInterface.class.getModifiers());
        Modifier privateStaticAbstract = new Modifier(PrivateStaticAbstractClass.class.getModifiers());

        assertModifiers(publicConstant, Modifier::isPublic, Modifier::isStatic, Modifier::isFinal);
        assertModifiers(privateConstant, Modifier::isPrivate, Modifier::isStatic, Modifier::isFinal);
        assertModifiers(publicField, Modifier::isPublic);
        assertModifiers(packageField, Modifier::isPackagePrivate);
        assertModifiers(privateField, Modifier::isPrivate);
        assertModifiers(privateFinalField, Modifier::isPrivate, Modifier::isFinal);
        assertModifiers(protectedField, Modifier::isProtected);
        assertModifiers(packageTransientField, Modifier::isPackagePrivate, Modifier::isTransient);
        assertModifiers(packageVolatileField, Modifier::isPackagePrivate, Modifier::isVolatile);

        assertModifiers(publicSynchronizedMethod, Modifier::isPublic, Modifier::isSynchronized);
        assertModifiers(privateInterface, Modifier::isPrivate, Modifier::isInterface);
        assertModifiers(privateStaticAbstract, Modifier::isPrivate, Modifier::isStatic, Modifier::isAbstract);
    }

    @Test
    void whenPublicOrPrivateOrProtected_thenIsNotPackagePrivate() throws NoSuchFieldException {
        Modifier publicField = new Modifier(Modifiers.class.getDeclaredField("publicField").getModifiers());
        Modifier privateField = new Modifier(Modifiers.class.getDeclaredField("privateField").getModifiers());
        Modifier protectedField = new Modifier(Modifiers.class.getDeclaredField("protectedField").getModifiers());

        assertThat(publicField.isPackagePrivate()).isFalse();
        assertThat(privateField.isPackagePrivate()).isFalse();
        assertThat(protectedField.isPackagePrivate()).isFalse();
    }

    @Test
    void whenNotStrictFloatingPoint_thenIsNotStrict() {
        Modifier modifiers = new Modifier(Modifiers.class.getModifiers());

        assertThat(modifiers.isStrict()).isFalse();
    }

    @Test
    void whenNotNative_thenIsNotNative() {
        Modifier modifiers = new Modifier(Modifiers.class.getModifiers());

        assertThat(modifiers.isNative()).isFalse();
    }

    @Test
    void equalsTest() throws NoSuchFieldException {
        Modifier publicField = new Modifier(Modifiers.class.getDeclaredField("publicField").getModifiers());
        Modifier privateField = new Modifier(Modifiers.class.getDeclaredField("privateField").getModifiers());

        assertThat(publicField).isEqualTo(new Modifier(Modifiers.class.getDeclaredField("publicField").getModifiers()));
        assertThat(publicField).isEqualTo((Object) publicField);

        assertThat(publicField).isNotEqualTo(new Object());
        assertThat(publicField).isNotEqualTo(privateField);
        assertThat(publicField).isNotEqualTo(null);
    }

    @Test
    void hashCodeTest() throws NoSuchFieldException {
        Modifier publicField = new Modifier(Modifiers.class.getDeclaredField("publicField").getModifiers());
        Modifier privateField = new Modifier(Modifiers.class.getDeclaredField("privateField").getModifiers());

        assertThat(publicField.hashCode())
                .isEqualTo(new Modifier(Modifiers.class.getDeclaredField("publicField").getModifiers()).hashCode());
        assertThat(publicField.hashCode()).isEqualTo(((Object) publicField).hashCode());

        assertThat(publicField.hashCode()).isNotEqualTo(new Object().hashCode());
        assertThat(publicField.hashCode()).isNotEqualTo(privateField.hashCode());
        assertThat(publicField.hashCode()).isNotEqualTo(Objects.hashCode(null));
    }

    @SafeVarargs
    private final void assertModifiers(Modifier modifier, Predicate<Modifier>... modifiers) {
        for (Predicate<Modifier> shouldHaveModifier : modifiers) {
            assertThat(shouldHaveModifier.test(modifier)).isTrue();
        }
    }

    private static class Modifiers {
        public static final Object PUBLIC_CONSTANT = null;
        private static final Object PRIVATE_CONSTANT = null;

        public Object publicField;
        Object packageField;
        private Object privateField;
        protected Object protectedField;
        private final Object privateFinalField = null;
        volatile Object packageVolatileField;
        transient Object packageTransientField;

        public synchronized void publicSynchronizedMethod() {
            // no-op
        }
    }

    private interface PrivateInterface {
    }

    private static abstract class PrivateStaticAbstractClass {
    }
}
