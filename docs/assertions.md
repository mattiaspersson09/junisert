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

## Unit assertion

Javadoc: [Javadoc.io > Junisert API > UnitAssertion](https://javadoc.io/doc/io.github.mattiaspersson09/junisert-api/latest/io/github/mattiaspersson09/junisert/api/assertion/UnitAssertion.html)

Is used to assert on any type of unit to verify stucture and expected behavior.

### asPojo

Assumes this unit is a plain object for assertion, which doesn't necessarily follow a naming convention but carries properties.

### isJavaBeanCompliant

Is a convenience assertion to assert that a unit complies with the Java Bean Specification. This should make sure that a unit follows convention, which requires that:
- Unit must have a default constructor (accepting no arguments).
- All instance properties must be private.
- All instance properties must have a working getter.
- All instance properties must have a working setter.

Other than enforcing convention, this assertion might still check for but not enforce:
- Unit should implement Serializable, directly or indirectly
- Unit should override equals and hashCode
- Unit should override toString

Above checks might log a warning but not be enforced. If the user wants to enforce non required checks they should treat the unit as a POJO (Plain Old Java Object) and use asPojo().

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

Test:
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

Test:
```java
@Test
void setters() {
    Junisert.assertThatPojo(PlainObject.class).hasSetters();
}
```

### implementsEqualsAndHashCode

Asserts that unit implements both **equals** and **hashCode** and they work as intended. 
These are asserted together since it's generally necessary to maintain object contract, that units overriding one should also override the other.
If equals pass assertion, so should hashCode also since they harmonize.

**Test:**
```java
@Test
void equalsAndHashCode() {
    Junisert.assertThatPojo(PlainObject.class).implementsEqualsAndHashCode();
}
```

### implementsToString

Asserts that unit implements **toString**, so that it returns a suitable textual representation of the object. 
This assertion will enforce that toString contains the name of the unit and all instance fields with their current value is shown.
For field check this asserts that "&lt;property name&gt;=&lt;property value&gt;" is present for every instance field.

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

Test:
```java
@Test
void toStringMethod() {
    Junisert.assertThatPojo(PlainObject.class).implementsToString();
}
```

### isWellImplemented

This is a convenience asserting operation that performs all assertions in recommended sequential order.
This operation is not suitable for immutable POJO's and performs following assertions:
1. [hasGetters()](#hasgetters)
2. [hasSetters()](#hassetters)
3. [implementsEqualsAndHashCode()](#implementsequalsandhashcode)
4. [implementsToString()](#implementstostring)