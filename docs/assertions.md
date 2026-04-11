---
title: Assertions
layout: default
nav_order: 3
---

# Assertions
{: .no_toc }

## Table of Contents
{: .no_toc .text-delta }

1. Unit assertion
2. Plain object assertion
{:toc}

Documentation for version: **0.3.0**

## Unit assertion

Javadoc: [Javadoc.io > Junisert API > UnitAssertion](https://javadoc.io/doc/io.github.mattiaspersson09/junisert-api/latest/io/github/mattiaspersson09/junisert/api/assertion/UnitAssertion.html)

Is used to assert on any type of unit to verify stucture and expected behavior.
To assume the unit under assertion is a specific type of unit, use methods starting with **as** for specific assertions.
Use methods starting with **is** for convenient direct assertion.

### asPojo

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

### isJavaBeanCompliant

Is a convenience assertion to assert that a unit complies with the Java Bean Specification.

```java
@Test
void givenUnit_whenIsJavaBeanCompliant_thenPassesAssertion() {
    Junisert.assertThatUnit(JavaBeanModel.class).isJavaBeanCompliant();
}
```
**Negative test example:**
```java
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

### isImmutable

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

## Plain object assertion

Javadoc: [Javadoc.io > Junisert API > PlainObjectAssertion](https://javadoc.io/doc/io.github.mattiaspersson09/junisert-api/latest/io/github/mattiaspersson09/junisert/api/assertion/PlainObjectAssertion.html)

Is used to assert on a plain object just carrying properties, like POJO, DTO, Value Object, you name it.

### hasGetters

Asserts that the unit has a working getter for all _instance_ fields. This assertion is flexible and accepts both
Java Bean compliant and builder/record styles and does not enforce public visibility.

Example valid getters, assuming the field type is a `boolean`:

```java
// Java Bean style
public boolean getField() {
    return field;
}

// Java Bean style
public boolean isField() {
    return field;
}

// Record style
public boolean field() {
    return field;
}
```

**Test:**

```java

@Test
void getters() {
    Junisert.assertThatPojo(PlainObject.class).hasGetters();
}
```

### hasSetters

Asserts that unit has a working setter for all instance fields. This assertion is flexible and accepts both
Java Bean compliant and builder/record styles and does not enforce public visibility.

Example valid setters, assuming the field type is a `boolean` and class is named `PlainObject`:

```java
// Java Bean style
public void setField(boolean field) {
    this.field = field;
}

// Mix of Java Bean and builder style
public PlainObject setField(boolean field) {
    this.field = field;
    return this;
}

// Record style
public void field(boolean field) {
    this.field = field;
}

// Builder style
public PlainObject field(boolean field) {
    this.field = field;
    return this;
}
```

**Test:**

```java

@Test
void setters() {
    Junisert.assertThatPojo(PlainObject.class).hasSetters();
}
```

### implementsEqualsAndHashCode

Asserts that unit implements both **equals** and **hashCode** and they work as intended.
These are asserted together since it's generally necessary to maintain object contract, that units overriding one should
also override the other.
If equals pass assertion, so should hashCode also since they harmonize.

**Test:**

```java

@Test
void equalsAndHashCode() {
    Junisert.assertThatPojo(PlainObject.class).implementsEqualsAndHashCode();
}
```

### implementsToString

Asserts that unit implements `toString`, so that it returns a suitable textual representation of the object.
This assertion will enforce that `toString` contains the name of the unit and all instance fields with their current
value is shown. For field check this asserts that
`<property name>`, operator `=` or `:` and `<property value>` is present together for every instance field.

Standard typical `toString`:

```java

@Override
public String toString() {
    return "PlainObject{" +
            "booleanField=" + booleanField +
            ", stringField='" + stringField + '\'' +
            ", arrayField=" + Arrays.toString(arrayField) +
            '}';
}
```

Other accepted variant with semicolon:

```java

@Override
public String toString() {
    return "PlainObject{" +
            "booleanField:" + booleanField +
            ", stringField:'" + stringField + '\'' +
            ", arrayField:" + Arrays.toString(arrayField) +
            '}';
}
```

Or like JSON:

```java

@Override
public String toString() {
    return "PlainObject{" +
            "\n  \"booleanField\": " + booleanField +
            ",\n  \"stringField\": \"" + stringField + '\"' +
            ",\n  \"arrayField\": " + Arrays.toString(arrayField) +
            "\n}";
}
```

**Test:**

```java

@Test
void toStringMethod() {
    Junisert.assertThatPojo(PlainObject.class).implementsToString();
}
```

### isWellImplemented

This is a convenience asserting operation that performs all assertions in recommended sequential order.
This operation performs following assertions, skipping hasSetters for immutable units:

1. [hasGetters()](#hasgetters)
2. [hasSetters()](#hassetters)
3. [implementsEqualsAndHashCode()](#implementsequalsandhashcode)
4. [implementsToString()](#implementstostring)