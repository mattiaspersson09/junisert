---
title: UnitAssertion
layout: default
parent: Assertions
---

# UnitAssertion
{: .no_toc }

## Table of Contents
{: .no_toc .text-delta }
- TOC
{:toc}

Javadoc: [Javadoc.io > Junisert API > UnitAssertion](https://javadoc.io/doc/io.github.mattiaspersson09/junisert-api/latest/io/github/mattiaspersson09/junisert/api/assertion/UnitAssertion.html)

Is used to assert on any type of unit to verify stucture and expected behavior.
To assume the unit under assertion is a specific type of unit, use methods starting with **as** for specific assertions.
Use methods starting with **is** for convenient direct assertion.

## asPojo

Assumes this unit is a plain object for assertion, which doesn't necessarily follow a naming convention but carries
properties.
This method doesn't perform any tests and just switches assertion to [PlainObjectAssertion](#plain-object-assertion).

```java
public record Model(String value) {
}
```
**Example usage:**
```java
@Test
void givenModel_whenIsImmutable_thenIsWellImplemented() {
    Junisert.assertThatUnit(Model.class)
            .isImmutable()
            .asPojo() // Switching to PlainObjectAssertion
            .isWellImplemented();
}
```

## isJavaBeanCompliant

Is a convenience assertion to assert that a unit complies with the Java Bean Specification.

```java
@Test
void givenUnit_whenIsJavaBeanCompliant_thenPassesAssertion() {
    Junisert.assertThatUnit(JavaBeanModel.class).isJavaBeanCompliant();
}
```
**Negative test example:**
```java
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.core.Junisert;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Test
void givenUnit_whenIsNotJavaBeanCompliant_thenFailsAssertion() {
    // Example using AssertJ
   assertThatThrownBy(() -> Junisert.assertThatUnit(NoDefaultConstructorModel.class).isJavaBeanCompliant())
           .isInstanceOf(UnitAssertionError.class)
           .hasMessageContaining("NoDefaultConstructorModel")
           .hasMessageContaining("expected to have a default constructor");

   assertThatThrownBy(() -> Junisert.assertThatUnit(VisiblePropertiesModel.class).isJavaBeanCompliant())
           .isInstanceOf(UnitAssertionError.class)
           .hasMessageContaining("VisiblePropertiesModel")
           .hasMessageContaining("expected to only have private properties");

   assertThatThrownBy(() -> Junisert.assertThatUnit(IsMissingGetterModel.class).isJavaBeanCompliant())
           .isInstanceOf(UnitAssertionError.class)
           .hasMessageContaining("IsMissingGetterModel")
           .hasMessageContaining("expected to have getter for instance field");
}
```

## isImmutable

Asserts that unit is immutable, meaning that all instance fields are read-only and there's a working getter for all
properties.

```java
public record Model(String value) {
}
```
**Example usage:**
```java
@Test
void givenModel_whenIsImmutable_thenFailsAssertion() {
    Junisert.assertThatUnit(Model.class).isImmutable();
}
```
**Negative test with a mutable model:**
```java
import io.github.mattiaspersson09.junisert.api.assertion.UnitAssertionError;
import io.github.mattiaspersson09.junisert.core.Junisert;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Test
void givenModel_whenIsMutable_thenFailsAssertion() {
    // Example using AssertJ
    assertThatThrownBy(() -> Junisert.assertThatUnit(MutableModel.class).isImmutable())
           .isInstanceOf(UnitAssertionError.class)
           .hasMessageContaining("MutableModel")
           .hasMessageContaining("expected to be immutable");
}
```