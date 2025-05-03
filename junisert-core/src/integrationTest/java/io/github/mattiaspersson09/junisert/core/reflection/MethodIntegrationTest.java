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
import io.github.mattiaspersson09.junisert.testunits.method.PolymorphicMethods;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Base;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MethodIntegrationTest {
    @Test
    void givenVoidReturnType_whenNoParameters_thenIsAction_andNotAnyOtherMethodType() throws NoSuchMethodException {
        Method action = Method.of(InstanceMethods.class.getDeclaredMethod("privateVoidNoParameters"));

        assertThat(action.hasReturnType(void.class)).isTrue();
        assertThat(action.hasNoParameters()).isTrue();
        assertThat(action.isAction()).isTrue();

        assertThat(action.isConsumer()).isFalse();
        assertThat(action.isProducer()).isFalse();
        assertThat(action.isFunction()).isFalse();
    }

    @Test
    void givenVoidReturnType_whenHasParameters_thenIsConsumer_andNotAnyOtherMethodType() throws NoSuchMethodException {
        Method consumingSuper = Method.of(PolymorphicMethods.class.getDeclaredMethod("publicVoidSuperParameter",
                Super.class));
        Method staticConsumingSuper = Method.of(PolymorphicMethods.class.getDeclaredMethod(
                "publicStaticVoidSuperParameter",
                Super.class));
        Method consumingSuperAndBase = Method.of(PolymorphicMethods.class.getDeclaredMethod(
                "publicVoidSuperAndBaseParameters",
                Super.class, Base.class));

        assertThat(consumingSuper.hasReturnType(void.class)).isTrue();
        assertThat(staticConsumingSuper.hasReturnType(void.class)).isTrue();
        assertThat(consumingSuperAndBase.hasReturnType(void.class)).isTrue();

        assertThat(consumingSuper.isConsumer()).isTrue();
        assertThat(staticConsumingSuper.isConsumer()).isTrue();
        assertThat(consumingSuperAndBase.isConsumer()).isTrue();

        assertThat(consumingSuper.isAction()).isFalse();
        assertThat(consumingSuper.isProducer()).isFalse();
        assertThat(consumingSuper.isFunction()).isFalse();
    }

    @Test
    void givenConsumer_whenHasSingleParameter_thenConsumesAnyPolymorphic() throws NoSuchMethodException {
        Method consumingSuper = Method.of(PolymorphicMethods.class.getDeclaredMethod("publicVoidSuperParameter",
                Super.class));
        Method notConsumerBase = Method.of(PolymorphicMethods.class.getDeclaredMethod("publicVoidBaseParameter",
                Base.class));

        assertThat(consumingSuper.isConsuming(Super.class)).isTrue();
        assertThat(consumingSuper.isConsuming(Base.class)).isTrue();
        assertThat(consumingSuper.isConsuming(Impl.class)).isTrue();

        assertThat(consumingSuper.isConsumingOnly(Super.class)).isTrue();
        assertThat(consumingSuper.isConsumingOnly(Base.class)).isTrue();
        assertThat(consumingSuper.isConsumingOnly(Impl.class)).isTrue();

        assertThat(notConsumerBase.isConsuming(Super.class)).isFalse();
    }

    @Test
    void givenConsumer_whenHasMoreThanOneParameter_thenCanConsumesAny_butNotConsumingOnlyOne() throws NoSuchMethodException {
        Method consumingSuperAndBase = Method.of(PolymorphicMethods.class.getDeclaredMethod(
                "publicVoidSuperAndBaseParameters",
                Super.class, Base.class));

        assertThat(consumingSuperAndBase.isConsuming(Super.class)).isTrue();
        assertThat(consumingSuperAndBase.isConsuming(Base.class)).isTrue();
        assertThat(consumingSuperAndBase.isConsuming(Impl.class)).isTrue();

        assertThat(consumingSuperAndBase.isConsumingOnly(Super.class)).isFalse();
        assertThat(consumingSuperAndBase.isConsumingOnly(Base.class)).isFalse();
        assertThat(consumingSuperAndBase.isConsumingOnly(Impl.class)).isFalse();
    }

    @Test
    void givenNonVoidReturnType_whenHasNoParameters_thenIsProducer_andNotAnyOtherMethodType() throws NoSuchMethodException {
        Method producingSuper = Method.of(PolymorphicMethods.class.getDeclaredMethod("publicSuperNoParameters"));
        Method staticProducingSuper = Method.of(PolymorphicMethods.class.getDeclaredMethod(
                "publicStaticSuperNoParameters"));

        assertThat(producingSuper.hasReturnType(Super.class)).isTrue();
        assertThat(staticProducingSuper.hasReturnType(Super.class)).isTrue();

        assertThat(producingSuper.hasNoParameters()).isTrue();
        assertThat(staticProducingSuper.hasNoParameters()).isTrue();

        assertThat(producingSuper.isProducer()).isTrue();
        assertThat(staticProducingSuper.isProducer()).isTrue();

        assertThat(producingSuper.isAction()).isFalse();
        assertThat(producingSuper.isConsumer()).isFalse();
        assertThat(producingSuper.isFunction()).isFalse();
        assertThat(staticProducingSuper.isAction()).isFalse();
        assertThat(staticProducingSuper.isConsumer()).isFalse();
        assertThat(staticProducingSuper.isFunction()).isFalse();
    }

    @Test
    void givenProducer_whenProducingSuperType_thenCanProduceSubTypes() throws NoSuchMethodException {
        Method producingBase = Method.of(PolymorphicMethods.class.getDeclaredMethod("publicBaseNoParameters"));

        assertThat(producingBase.hasReturnTypeAssignableTo(Super.class)).isTrue();
        assertThat(producingBase.hasReturnTypeAssignableFrom(Super.class)).isFalse();
        assertThat(producingBase.hasReturnTypeAssignableTo(Impl.class)).isFalse();
        assertThat(producingBase.hasReturnTypeAssignableFrom(Impl.class)).isTrue();

        assertThat(producingBase.isProducing(Super.class)).isFalse();
        assertThat(producingBase.isProducing(Base.class)).isTrue();
        assertThat(producingBase.isProducing(Impl.class)).isTrue();
    }

    @Test
    void givenNonVoidMethod_whenHasParameters_thenIsFunction_andNoOtherMethodType() throws NoSuchMethodException {
        Method resultSuper = Method.of(PolymorphicMethods.class.getDeclaredMethod("publicSuperSuperParameter",
                Super.class));
        Method staticResultSuper = Method.of(PolymorphicMethods.class.getDeclaredMethod(
                "publicStaticSuperSuperParameter",
                Super.class));

        assertThat(resultSuper.hasReturnType(Super.class)).isTrue();
        assertThat(staticResultSuper.hasReturnType(Super.class)).isTrue();

        assertThat(resultSuper.hasParameters()).isTrue();
        assertThat(staticResultSuper.hasParameters()).isTrue();

        assertThat(resultSuper.isFunction()).isTrue();
        assertThat(staticResultSuper.isFunction()).isTrue();

        assertThat(resultSuper.isAction()).isFalse();
        assertThat(resultSuper.isConsumer()).isFalse();
        assertThat(resultSuper.isProducer()).isFalse();

        assertThat(staticResultSuper.isAction()).isFalse();
        assertThat(staticResultSuper.isConsumer()).isFalse();
        assertThat(staticResultSuper.isConsuming(Super.class)).isFalse();
        assertThat(staticResultSuper.isConsumingOnly(Super.class)).isFalse();
        assertThat(staticResultSuper.isProducer()).isFalse();
        assertThat(staticResultSuper.isProducing(Super.class)).isFalse();
    }

    @Test
    void givenFunction_whenHasOnlyOneParameter_thenIsFunctionOfInputAndResult() throws NoSuchMethodException {
        Method inputBaseResultBase = Method.of(PolymorphicMethods.class.getDeclaredMethod("publicBaseBaseParameter",
                Base.class));

        assertThat(inputBaseResultBase.isFunction()).isTrue();
        assertThat(inputBaseResultBase.isFunctionOf(Base.class, Base.class)).isTrue();
        assertThat(inputBaseResultBase.isFunctionOf(Impl.class, Impl.class)).isTrue();
        assertThat(inputBaseResultBase.isFunctionOf(Base.class, Impl.class)).isTrue();
        assertThat(inputBaseResultBase.isFunctionOf(Impl.class, Base.class)).isTrue();

        assertThat(inputBaseResultBase.isFunctionOf(Super.class, Super.class)).isFalse();
        assertThat(inputBaseResultBase.isFunctionOf(Base.class, Super.class)).isFalse();
        assertThat(inputBaseResultBase.isFunctionOf(Super.class, Base.class)).isFalse();
    }

    @Test
    void givenFunction_whenHasMoreThanOneParameter_thenIsNotFunctionOfInputAndResult() throws NoSuchMethodException {
        Method inputSuperAndBaseResultSuper = Method.of(PolymorphicMethods.class.getDeclaredMethod(
                "publicSuperSuperAndBaseParameters", Super.class, Base.class));

        assertThat(inputSuperAndBaseResultSuper.isFunction()).isTrue();

        assertThat(inputSuperAndBaseResultSuper.isFunctionOf(Base.class, Base.class)).isFalse();
        assertThat(inputSuperAndBaseResultSuper.isFunctionOf(Super.class, Super.class)).isFalse();
        assertThat(inputSuperAndBaseResultSuper.isFunctionOf(Super.class, Base.class)).isFalse();
        assertThat(inputSuperAndBaseResultSuper.isFunctionOf(Base.class, Base.class)).isFalse();
    }

    @Test
    void getType_whenHasReturnType_thenReturnsReturnType() throws NoSuchMethodException {
        Method objectReturn = Method.of(InstanceMethods.class.getDeclaredMethod("publicObjectNoParameters"));

        assertThat(objectReturn.getType()).isEqualTo(Object.class);
    }

    @Test
    void getType_whenIsConsumerMethod_thenReturnsVoid() throws NoSuchMethodException {
        Method consumer = Method.of(InstanceMethods.class.getDeclaredMethod("privateVoidNoParameters"));

        assertThat(consumer.getType()).isEqualTo(void.class);
    }

    @Test
    void accepts_whenHasParameters_thenAcceptsParameterTypes() throws NoSuchMethodException {
        Method method = Method.of(InstanceMethods.class
                .getDeclaredMethod("publicVoidOverloadingParameters", Object.class, Object.class));

        assertThat(method.accepts())
                .hasSize(2)
                .allMatch(Object.class::equals);
    }

    @Test
    void invoke_whenReturningObject_thenReturnsResult() throws NoSuchMethodException, InvocationTargetException,
                                                        InstantiationException, IllegalAccessException {
        Object instance = InstanceMethods.class.getDeclaredConstructor().newInstance();
        Method method = Method.of(InstanceMethods.class.getDeclaredMethod("publicObjectNoParameters"));

        assertThat(method.invoke(instance))
                .isNotNull()
                .extracting(Object::getClass)
                .isEqualTo(Object.class);
    }

    @Test
    void invoke_whenNotAcceptedType_thenThrowsUncheckedException() throws NoSuchMethodException,
                                                                   InvocationTargetException,
                                                                   InstantiationException, IllegalAccessException {
        Object instance = InstanceMethods.class.getDeclaredConstructor().newInstance();
        Method method = Method.of(InstanceMethods.class.getDeclaredMethod("publicVoidStringParameter", String.class));

        assertThatThrownBy(() -> method.invoke(instance, new Object())).isInstanceOf(RuntimeException.class);
    }

    @Test
    void invoke_whenUnknownInstance_thenThrowsUncheckedException() throws NoSuchMethodException {
        Method method = Method.of(InstanceMethods.class.getDeclaredMethod("publicVoidStringParameter", String.class));

        assertThatThrownBy(() -> method.invoke(null, "value")).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> method.invoke(new Object(), "value")).isInstanceOf(RuntimeException.class);
    }

    @Test
    void equalsTest() throws NoSuchMethodException {
        Method oneParameterMethod = Method.of(InstanceMethods.class
                .getDeclaredMethod("publicVoidOverloadingParameters", Object.class));
        Method twoParameterOverloadingMethod = Method.of(InstanceMethods.class
                .getDeclaredMethod("publicVoidOverloadingParameters", Object.class, Object.class));

        assertThat(oneParameterMethod).isEqualTo((Object) oneParameterMethod);
        assertThat(oneParameterMethod).isEqualTo(Method.of(InstanceMethods.class
                .getDeclaredMethod("publicVoidOverloadingParameters", Object.class)));

        assertThat(oneParameterMethod).isNotEqualTo(twoParameterOverloadingMethod);
        assertThat(oneParameterMethod).isNotEqualTo(new Object());
        assertThat(oneParameterMethod).isNotEqualTo(null);
    }

    @Test
    void hashCodeTest() throws NoSuchMethodException {
        Method oneParameterMethod = Method.of(InstanceMethods.class
                .getDeclaredMethod("publicVoidOverloadingParameters", Object.class));
        Method twoParameterOverloadingMethod = Method.of(InstanceMethods.class
                .getDeclaredMethod("publicVoidOverloadingParameters", Object.class, Object.class));

        assertThat(oneParameterMethod.hashCode()).isEqualTo(((Object) oneParameterMethod).hashCode());
        assertThat(oneParameterMethod.hashCode()).isEqualTo(Method.of(InstanceMethods.class
                .getDeclaredMethod("publicVoidOverloadingParameters", Object.class)).hashCode());

        assertThat(oneParameterMethod.hashCode()).isNotEqualTo(twoParameterOverloadingMethod.hashCode());
        assertThat(oneParameterMethod.hashCode()).isNotEqualTo(new Object().hashCode());
        assertThat(oneParameterMethod.hashCode()).isNotEqualTo(Objects.hashCode(null));
    }

    @Test
    void toStringTest() throws NoSuchMethodException {
        Method method = Method.of(InstanceMethods.class
                .getDeclaredMethod("publicVoidOverloadingParameters", Object.class));

        assertThat(method.toString()).isEqualTo("publicVoidOverloadingParameters([class java.lang.Object])");
    }
}
