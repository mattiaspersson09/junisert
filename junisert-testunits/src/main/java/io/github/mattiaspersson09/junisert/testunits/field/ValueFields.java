package io.github.mattiaspersson09.junisert.testunits.field;

public class ValueFields {
    public static final Object PUBLIC_CONSTANT = new Object();
    private static final Object PRIVATE_CONSTANT = new Object();
    private static Object privateStaticObjectField;

    private final String immutableStringValueField = "value";
    private Object privateObjectField;
    private Object[] arrayField;
}
