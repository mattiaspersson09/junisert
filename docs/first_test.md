---
title: Your first test
layout: default
nav_order: 2
---

# Testing
{: .no_toc }

All tests will start from using the base class `Junisert`, to make use of caching and assertion alternatives.
If you are familiar with the **AssertJ** testing library you will be familiar with the fluent testing technique,
chaining methods to set up several tests quickly after each other.

## Table of Contents
{: .no_toc .text-delta }

1. Plain mutable object
2. Plain immutable object
{:toc}


## Plain mutable object

Given mutable class:
```java
public class MutableObject {
    private String sender;
    private String reciever;
    private List<String> labels;
    private byte[] attachment;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MutableObject that = (MutableObject) object;
        return Objects.equals(sender, that.sender) && Objects.equals(reciever, that.reciever)
                && Objects.equals(labels, that.labels) && Arrays.equals(attachment, that.attachment);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(sender, reciever, labels);
        result = 31 * result + Arrays.hashCode(attachment);
        return result;
    }

    @Override
    public String toString() {
        return "MutableObject{" +
                "sender='" + sender + '\'' +
                ", reciever='" + reciever + '\'' +
                ", labels=" + labels +
                ", attachment=" + Arrays.toString(attachment) +
                '}';
    }
}
```

Test that it's well implemented:
```java
class MutableObjectTest {
    @Test
    void isWellImplemented() {
        Junisert.assertThatPojo(MutableObject.class).isWellImplemented();
    }
}
```

Checking that it's well implemented is a convenience way of checking proper structure. It's the same as using the following 
testing technique:
```java
class MutableObjectTest {
    @Test
    void isWellImplemented() {
        Junisert.assertThatPojo(MutableObject.class)
                .hasGetters()
                .hasSetters()
                .implementsEqualsAndHashCode()
                .implementsToString();
    }
}
```

`isWellImplemented()` is not suitable for testing immutable objects at this time, you should instead use the above 
technique but disregarding `hasSetters()`.

## Plain immutable object

Given immutable class:
```java
public class ImmutableObject {
    private final String sender;
    private final String reciever;
    private final List<String> labels;
    private final byte[] attachment;

    public ImmutableObject(String sender, String reciever, List<String> labels, byte[] attachment) {
        this.sender = sender;
        this.reciever = reciever;
        this.labels = labels;
        this.attachment = attachment;
    }

    public String getSender() {
        return sender;
    }

    public String getReciever() {
        return reciever;
    }

    public List<String> getLabels() {
        return labels;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ImmutableObject that = (ImmutableObject) object;
        return Objects.equals(sender, that.sender) && Objects.equals(reciever, that.reciever)
                && Objects.equals(labels, that.labels) && Arrays.equals(attachment, that.attachment);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(sender, reciever, labels);
        result = 31 * result + Arrays.hashCode(attachment);
        return result;
    }

    @Override
    public String toString() {
        return "ImmutableObject{" +
                "sender='" + sender + '\'' +
                ", reciever='" + reciever + '\'' +
                ", labels=" + labels +
                ", attachment=" + Arrays.toString(attachment) +
                '}';
    }
}
```

Test that it's well implemented:
```java
class ImmutableObjectTest {
   @Test
   void isWellImplemented() {
      Junisert.assertThatPojo(ImmutableObject.class)
              .hasGetters()
              .implementsEqualsAndHashCode()
              .implementsToString();
   }
}
```