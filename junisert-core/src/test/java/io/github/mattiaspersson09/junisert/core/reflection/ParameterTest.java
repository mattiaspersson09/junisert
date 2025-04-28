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

import io.github.mattiaspersson09.junisert.testunits.method.Annotated;
import io.github.mattiaspersson09.junisert.testunits.method.InstanceMethods;
import io.github.mattiaspersson09.junisert.testunits.method.VarArg;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ParameterTest {
    @Test
    void isVarArg_whenParameterIsVarArg_thenIsTrue() throws NoSuchMethodException {
        Parameter varArgParameter = new Parameter(VarArg.class
                .getDeclaredMethod("publicVoidObjectVarArgParameter", Object[].class)
                .getParameters()[0]);

        assertThat(varArgParameter.isVarArgs()).isTrue();
    }

    @Test
    void isVarArg_whenParameterIsArray_thenIsFalse() throws NoSuchMethodException {
        Parameter varArgParameter = new Parameter(VarArg.class
                .getDeclaredMethod("publicVoidObjectArrayParameter", Object[].class)
                .getParameters()[0]);

        assertThat(varArgParameter.isVarArgs()).isFalse();
    }

    @Test
    void isAnnotated_whenParameterHasAnnotation_thenIsTrue() throws NoSuchMethodException {
        Parameter deprecatedParameter = new Parameter(Annotated.class
                .getDeclaredMethod("deprecatedPublicVoidDeprecatedObjectParameter", Object.class)
                .getParameters()[0]);

        assertThat(deprecatedParameter.isAnnotated()).isTrue();
    }

    @Test
    void isAnnotated_whenParameterHasNoAnnotation_thenIsFalse() throws NoSuchMethodException {
        Parameter deprecatedParameter = new Parameter(Annotated.class
                .getDeclaredMethod("deprecatedPublicVoidObjectParameter", Object.class)
                .getParameters()[0]);

        assertThat(deprecatedParameter.isAnnotated()).isFalse();
    }

    @Test
    void getName_hasGenericJavaArgumentName() throws NoSuchMethodException {
        Parameter deprecatedParameter = new Parameter(Annotated.class
                .getDeclaredMethod("deprecatedPublicVoidObjectParameter", Object.class)
                .getParameters()[0]);

        assertThat(deprecatedParameter.getName()).contains("arg");
    }

    @Test
    void getType_isReflectedType() throws NoSuchMethodException {
        Parameter deprecatedParameter = new Parameter(Annotated.class
                .getDeclaredMethod("deprecatedPublicVoidObjectParameter", Object.class)
                .getParameters()[0]);

        assertThat(deprecatedParameter.getType()).isEqualTo(Object.class);
    }

    @Test
    void modifier_whenHasNoModifier_thenIsConsideredPackagePrivate() throws NoSuchMethodException {
        Parameter noModifierParameter = new Parameter(InstanceMethods.class
                .getDeclaredMethod("publicVoidObjectParameter", Object.class)
                .getParameters()[0]);

        assertThat(noModifierParameter.modifier().isPackagePrivate()).isTrue();
    }
}
