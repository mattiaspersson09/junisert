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

import io.github.mattiaspersson09.junisert.testunits.method.InstanceMethods;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MethodTest {
    @Test
    void whenConstructing_thenSetsParameters() throws NoSuchMethodException {
        Method method = new Method(InstanceMethods.class.getDeclaredMethod("publicVoidObjectParameter", Object.class));

        assertThat(method.getParameters())
                .hasSize(1)
                .allMatch(parameter -> parameter.getName().equals("arg0"))
                .allMatch(parameter -> parameter.getType().equals(Object.class));
    }

    @Test
    void getType_whenHasReturnType_thenReturnsReturnType() throws NoSuchMethodException {
        Method objectReturn = new Method(InstanceMethods.class.getDeclaredMethod("publicObjectNoParameters"));

        assertThat(objectReturn.getType()).isEqualTo(Object.class);
    }

    @Test
    void getType_whenIsConsumerMethod_thenReturnsVoid() throws NoSuchMethodException {
        Method consumer = new Method(InstanceMethods.class.getDeclaredMethod("privateVoidNoParameters"));

        assertThat(consumer.getType()).isEqualTo(void.class);
    }

    @Test
    void accepts_whenHasParameters_thenAcceptsParameterTypes() throws NoSuchMethodException {
        Method method = new Method(InstanceMethods.class
                .getDeclaredMethod("publicVoidOverloadingParameters", Object.class, Object.class));

        assertThat(method.accepts())
                .hasSize(2)
                .allMatch(Object.class::equals);
    }

    @Test
    void invoke_whenReturningObject_thenReturnsResult() throws NoSuchMethodException, InvocationTargetException,
                                                        InstantiationException, IllegalAccessException {
        Object instance = InstanceMethods.class.getDeclaredConstructor().newInstance();
        Method method = new Method(InstanceMethods.class.getDeclaredMethod("publicObjectNoParameters"));

        assertThat(method.invoke(instance))
                .isNotNull()
                .extracting(Object::getClass)
                .isEqualTo(Object.class);
    }

    @Test
    void invoke_whenNotAcceptedType_thenThrowsUnchecked() throws NoSuchMethodException, InvocationTargetException,
                                                          InstantiationException, IllegalAccessException {
        Object instance = InstanceMethods.class.getDeclaredConstructor().newInstance();
        Method method = new Method(InstanceMethods.class.getDeclaredMethod("publicVoidStringParameter", String.class));

        assertThatThrownBy(() -> method.invoke(instance, new Object())).isInstanceOf(RuntimeException.class);
    }

    @Test
    void invoke_whenUnknownInstance_thenThrowsUnchecked() throws NoSuchMethodException {
        Method method = new Method(InstanceMethods.class.getDeclaredMethod("publicVoidStringParameter", String.class));

        assertThatThrownBy(() -> method.invoke(null, "value")).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> method.invoke(new Object(), "value")).isInstanceOf(RuntimeException.class);
    }

    @Test
    void equalsTest() throws NoSuchMethodException {
        Method oneParameterMethod = new Method(InstanceMethods.class
                .getDeclaredMethod("publicVoidOverloadingParameters", Object.class));
        Method twoParameterOverloadingMethod = new Method(InstanceMethods.class
                .getDeclaredMethod("publicVoidOverloadingParameters", Object.class, Object.class));

        assertThat(oneParameterMethod).isEqualTo((Object) oneParameterMethod);
        assertThat(oneParameterMethod).isEqualTo(new Method(InstanceMethods.class
                .getDeclaredMethod("publicVoidOverloadingParameters", Object.class)));

        assertThat(oneParameterMethod).isNotEqualTo(twoParameterOverloadingMethod);
        assertThat(oneParameterMethod).isNotEqualTo(new Object());
        assertThat(oneParameterMethod).isNotEqualTo(null);
    }

    @Test
    void hashCodeTest() throws NoSuchMethodException {
        Method oneParameterMethod = new Method(InstanceMethods.class
                .getDeclaredMethod("publicVoidOverloadingParameters", Object.class));
        Method twoParameterOverloadingMethod = new Method(InstanceMethods.class
                .getDeclaredMethod("publicVoidOverloadingParameters", Object.class, Object.class));

        assertThat(oneParameterMethod.hashCode()).isEqualTo(((Object) oneParameterMethod).hashCode());
        assertThat(oneParameterMethod.hashCode()).isEqualTo(new Method(InstanceMethods.class
                .getDeclaredMethod("publicVoidOverloadingParameters", Object.class)).hashCode());

        assertThat(oneParameterMethod.hashCode()).isNotEqualTo(twoParameterOverloadingMethod.hashCode());
        assertThat(oneParameterMethod.hashCode()).isNotEqualTo(new Object().hashCode());
        assertThat(oneParameterMethod.hashCode()).isNotEqualTo(Objects.hashCode(null));
    }
}
