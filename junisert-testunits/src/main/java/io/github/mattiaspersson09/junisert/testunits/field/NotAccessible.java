package io.github.mattiaspersson09.junisert.testunits.field;

public class NotAccessible {
    private static final Object CONSTANT = null;
    private Object privateField;
    Object packageField = null;
    private final Object immutableField = null;
}
