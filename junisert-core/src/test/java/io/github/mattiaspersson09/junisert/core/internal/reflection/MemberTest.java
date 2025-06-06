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
package io.github.mattiaspersson09.junisert.core.internal.reflection;


import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberTest {
    @Mock
    java.lang.reflect.Member origin;
    private Member member;

    @BeforeEach
    void setUp() {
        member = new UnitMember(origin);
    }

    @Test
    void isInstanceMember_whenModifierIsNotStatic_andNotSynthetic_thenIsTrue() {
        when(origin.isSynthetic()).thenReturn(false);

        assertThat(member.isInstanceMember()).isTrue();
    }

    @Test
    void isInstanceMember_whenModifierIsStatic_andNotSynthetic_thenIsFalse() {
        when(origin.getModifiers()).thenReturn(java.lang.reflect.Modifier.STATIC);

        assertThat(new UnitMember(origin).isInstanceMember()).isFalse();
    }

    @Test
    void isInstanceMember_whenModifierIsNotStatic_butIsSynthetic_thenIsFalse() {
        when(origin.isSynthetic()).thenReturn(true);

        assertThat(member.isInstanceMember()).isFalse();
    }

    @Test
    void getName_whenMemberIsNotOverriding_thenHasDefaultOriginName() {
        when(origin.getName()).thenReturn("name");

        assertThat(member.getName()).isEqualTo("name");
    }

    @Test
    void modifier() {
        when(origin.getModifiers()).thenReturn(100);

        assertThat(new UnitMember(origin).modifier()).isEqualTo(new Modifier(100));
    }

    @Test
    void isSynthetic_whenMemberIsSynthetic_thenIsTrue() {
        when(origin.isSynthetic()).thenReturn(true);

        assertThat(member.isSynthetic()).isEqualTo(true);

        verify(origin, times(1)).isSynthetic();
    }

    @Test
    void isSynthetic_whenMemberIsNotSynthetic_thenIsFalse() {
        when(origin.isSynthetic()).thenReturn(false);

        assertThat(member.isSynthetic()).isEqualTo(false);

        verify(origin, times(1)).isSynthetic();
    }

    @Test
    void getParent() {
        doReturn(Object.class).when(origin).getDeclaringClass();

        assertThat(member.getParent()).isEqualTo(Object.class);
    }

    @Test
    void equalsTest() {
        assertThat(member).isEqualTo(new UnitMember(origin));
        assertThat(member).isEqualTo((Object) member);
        assertThat(member).isNotEqualTo(null);
        assertThat(member).isNotEqualTo(new Object());
    }

    @Test
    void equals_whenHasOtherOrigin_thenIsFalse() {
        assertThat(member).isNotEqualTo(new UnitMember(mock(java.lang.reflect.Member.class)));
    }

    @Test
    void hashCodeTest() {
        assertThat(member.hashCode()).isEqualTo(new UnitMember(origin).hashCode());
        assertThat(member.hashCode()).isEqualTo(((Object) member).hashCode());
        assertThat(member.hashCode()).isNotEqualTo(Objects.hashCode(null));
        assertThat(member.hashCode()).isNotEqualTo(new Object().hashCode());
    }

    @Test
    void hashCode_whenHasOtherOrigin_thenIsFalse() {
        assertThat(member.hashCode()).isNotEqualTo(new UnitMember(mock(java.lang.reflect.Member.class)).hashCode());
    }

    @Test
    void toString_whenNotOverridden_thenShowsUnitAndMemberName() {
        when(origin.getName()).thenReturn("memberName");
        doReturn(UnitMember.class).when(origin).getDeclaringClass();

        assertThat(member.toString()).isEqualTo("UnitMember.memberName");
    }

    private static class UnitMember extends Member {
        private UnitMember(java.lang.reflect.Member origin) {
            super(origin);
        }

        @Override
        public Class<?> getType() {
            return null;
        }

        @Override
        public boolean equals(Object o) {
            return super.equals(o);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
