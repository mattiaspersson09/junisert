---
title: Register support
parent: User guide
layout: default
nav_order: 2
---

# Register support
{: .no_toc }

## Table of Contents
{: .no_toc .text-delta }

- TOC
{:toc}

Support can be registered globally, where the support can be used over all tests. And temporary during an assertion,
when the assertion is done the support is no longer used. You can create your own supporting `ValueGenerator` or
supply what values to support and the framework constructs a generator for it during registration.

Support can be custom created implementing [ValueGenerator](https://github.com/mattiaspersson09/junisert/blob/main/junisert-api/src/main/java/io/github/mattiaspersson09/junisert/api/value/ValueGenerator.java)
or let the framework create one from supplied values. It's recommended to register value support that handles
polymorphism if you want to support a hierarchy of types, that way you don't need to implement that check yourself.\
For more information about polymorphic support, see:
- [Value](https://github.com/mattiaspersson09/junisert/blob/main/junisert-api/src/main/java/io/github/mattiaspersson09/junisert/api/value/Value.java)
- [Junisert.registerSupport](https://github.com/mattiaspersson09/junisert/blob/main/junisert-core/src/main/java/io/github/mattiaspersson09/junisert/core/Junisert.java#L109)
  (global support)
- [SupportUser](https://github.com/mattiaspersson09/junisert/blob/main/junisert-api/src/main/java/io/github/mattiaspersson09/junisert/api/internal/support/SupportUser.java)
  (assertion temporary support)

## Global support

**Keynote**: should be registered before ALL tests if it's needed in different test suites, register the support globally
but within a test is not safe. If the support is only needed for a certain test it's better to use temporary support,
to ensure it doesn't disrupt other tests.

### How support should be registered globally
Registration should be done before test suites, hooked into a lifecycle before any test is yet to run.

Example using JUnit Jupiter for a test class:
```java
import io.github.mattiaspersson09.junisert.core.Junisert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ModelTest {
    @BeforeAll
    static void beforeAll() {
        Junisert.registerSupport(/*support*/);
    }

    @Test
    void assertion() {
        Junisert.assertThatPojo(/*unit*/).isWellImplemented();
    }
}
```

### Custom ValueGenerator

#### Example unit needing support

Given unit needing specific `Supplier`:
```java
public class ModelRequiringSpecificSupplier {
    private final String result;
    
    public ModelRequiringSpecificSupplier(Supplier<String> supplier) {
        Objects.requireNonNull(supplier);
        this.result = Objects.requireNonNull(supplier.get());
    
        if (!"required value".equals(this.result) && !"empty".equals(this.result)) {
            throw new IllegalArgumentException("Not the result we want");
        }
    }
    
    public String getResult() {
        return result;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ModelRequiringSpecificSupplier that = (ModelRequiringSpecificSupplier) object;
        return Objects.equals(result, that.result);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(result);
    }
    
    @Override
    public String toString() {
        return String.format("ModelRequiringSpecificSupplier{\"result\": \"%s\"}", result);
    }
}
```
#### Custom created support

```java
import io.github.mattiaspersson09.junisert.api.value.UnsupportedTypeError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.api.value.ValueGenerator;
import java.util.function.Supplier;

public class SupplierSupport implements ValueGenerator<Supplier<String>> {
    @Override
    public Value<? extends Supplier<String>> generate(Class<?> type) throws UnsupportedTypeError {
        if (!supports(type)) {
            throw new UnsupportedTypeError(type);
        }

        return Value.ofEager(() -> "required value", () -> "empty");
    }

    @Override
    public boolean supports(Class<?> type) {
        return Supplier.class.equals(type);
    }
}
```
#### Test with new support

Using AssertJ on negative test for simplicity:
```java
import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.core.Junisert;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ModelTest {
    @Test
    void givenCustomSupplierSupport_whenCustomSupplierIsNeeded_thenPassesAssertion() {
        assertThatThrownBy(() -> Junisert.assertThatPojo(ModelRequiringSpecificSupplier.class).isWellImplemented())
                .isInstanceOf(UnsupportedConstructionError.class)
                .hasMessageContaining("Failed to construct concrete value of")
                .hasMessageContaining(ModelRequiringSpecificSupplier.class.getSimpleName());

        // Only registered within test to showcase assertion result
        Junisert.registerSupport(new SupplierSupport());

        Junisert.assertThatPojo(ModelRequiringSpecificSupplier.class).isWellImplemented();
    }
}
```

### Value to support

#### Example unit needing support

Given unit needing specific `CharSequence` polymorphic with `StringBuilder` implementation:
```java
public class ModelNeedingCharSequence {
    private StringBuilder sequence;

    public ModelNeedingCharSequence(CharSequence sequence) {
        // Cast to prove polymorphic support
        this.sequence = (StringBuilder) sequence;

        if (!"value".contentEquals(this.sequence) && !this.sequence.toString().isEmpty()) {
            throw new IllegalArgumentException("Wrong value");
        }
    }

    public CharSequence getSequence() {
        return sequence;
    }

    public void setSequence(StringBuilder sequence) {
        if (!"value".contentEquals(sequence) && !sequence.toString().isEmpty()) {
            throw new IllegalArgumentException("Wrong value");
        }

        this.sequence = sequence;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ModelNeedingCharSequence that = (ModelNeedingCharSequence) object;
        return Objects.equals(sequence, that.sequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequence);
    }

    @Override
    public String toString() {
        return "ModelNeedingCharSequence{" +
                "sequence=" + sequence +
                '}';
    }
}
```

#### Custom value support

Following support will support any type between `CharSequence` and `StringBuilder`, where `StringBuilder` will be 
the injected value when any of the types is found:
```java
@BeforeAll
static void beforeAll() {
    Junisert.registerSupport(CharSequence.class, StringBuilder.class, Value.of(() -> new StringBuilder("value"), StringBuilder::new));
}
```

#### Test with new support

Using AssertJ on negative test for simplicity:
```java
import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.api.value.Value;
import io.github.mattiaspersson09.junisert.core.Junisert;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ModelTest {
    @Test
    void givenCustomCharSequenceSupport_whenCustomValueIsNeeded_thenPassesAssertion() {
        assertThatThrownBy(() -> Junisert.assertThatPojo(ModelNeedingCharSequence.class).isWellImplemented())
                .isInstanceOf(UnsupportedConstructionError.class)
                .hasMessageContaining("Failed to construct concrete value of")
                .hasMessageContaining(ModelNeedingCharSequence.class.getSimpleName());

        // Only registered within test to showcase assertion result
        Junisert.registerSupport(CharSequence.class, StringBuilder.class,
                Value.of(() -> new StringBuilder("value"), StringBuilder::new));

        Junisert.assertThatPojo(ModelNeedingCharSequence.class).isWellImplemented();
    }
}
```

## Temporary assertion support

**Keynote**: Only supported during an assertion, values from temporary support isn't shared across other
tests to prevent disruption. The support is however active as long as the current assertion is performing operations.

Temporary support is exactly the same as global ones, but only registered for and during an assertion.

### Test with custom support

Following the same unit and support as in [example unit](#example-unit-needing-support) and
[custom support](#custom-created-support):
```java
import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.core.Junisert;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ModelTest {
    @Test
    void givenCustomSupplierSupport_whenCustomSupplierIsNeeded_thenPassesAssertion() {
        assertThatThrownBy(() -> Junisert.assertThatPojo(ModelRequiringSpecificSupplier.class).isWellImplemented())
                .isInstanceOf(UnsupportedConstructionError.class)
                .hasMessageContaining("Failed to construct concrete value of")
                .hasMessageContaining(ModelRequiringSpecificSupplier.class.getSimpleName());

        Junisert.assertThatPojo(ModelRequiringSpecificSupplier.class)
                .withSupport(new SupplierSupport())
                .isWellImplemented();

        // Support isn't active anymore with new assertions and needs to be registered again
        assertThatThrownBy(() -> Junisert.assertThatPojo(ModelRequiringSpecificSupplier.class).isWellImplemented())
                .isInstanceOf(UnsupportedConstructionError.class)
                .hasMessageContaining("Failed to construct concrete value of")
                .hasMessageContaining(ModelRequiringSpecificSupplier.class.getSimpleName());
    }
}
```

### Test with value support

Following the same unit and support as in [example unit value support](#example-unit-needing-support-1) and
[test with new support](#test-with-new-support-1):
```java
import io.github.mattiaspersson09.junisert.api.value.UnsupportedConstructionError;
import io.github.mattiaspersson09.junisert.core.Junisert;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ModelTest {
    @Test
    void givenCustomSupplierSupport_whenCustomSupplierIsNeeded_thenPassesAssertion() {
        assertThatThrownBy(() -> Junisert.assertThatPojo(ModelNeedingCharSequence.class).isWellImplemented())
                .isInstanceOf(UnsupportedConstructionError.class)
                .hasMessageContaining("Failed to construct concrete value of")
                .hasMessageContaining(ModelNeedingCharSequence.class.getSimpleName());

        Junisert.assertThatPojo(ModelNeedingCharSequence.class)
                .withSupport(CharSequence.class, StringBuilder.class,
                        Value.of(() -> new StringBuilder("value"), StringBuilder::new))
                .isWellImplemented();

        // Support isn't active anymore with new assertions and needs to be registered again
        assertThatThrownBy(() -> Junisert.assertThatPojo(ModelNeedingCharSequence.class).isWellImplemented())
                .isInstanceOf(UnsupportedConstructionError.class)
                .hasMessageContaining("Failed to construct concrete value of")
                .hasMessageContaining(ModelNeedingCharSequence.class.getSimpleName());
    }
}
```