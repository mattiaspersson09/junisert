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
package io.github.mattiaspersson09.junisert.core.units.lombok;

import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class LombokUnit {
    private String stringField;
    private int intField;
    private boolean isField;
    private boolean booleanField;
    private int[] intArray;
    private boolean[] booleanArray;
    private Integer wrapperInteger;
    private Integer[] wrapperIntegerArray;
    private Collection<Super> collection;
}
