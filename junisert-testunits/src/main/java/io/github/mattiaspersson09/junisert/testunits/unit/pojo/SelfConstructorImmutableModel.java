package io.github.mattiaspersson09.junisert.testunits.unit.pojo;

import java.util.Objects;

public final class SelfConstructorImmutableModel {
    private final long longField;
    private final String stringField;
    private final boolean booleanField;

    public SelfConstructorImmutableModel(long longField, String stringField, boolean booleanField) {
        this.longField = longField;
        this.stringField = stringField;
        this.booleanField = booleanField;

    }

    @SuppressWarnings("unused")
    public SelfConstructorImmutableModel(SelfConstructorImmutableModel otherModel) {
        this(Objects.requireNonNull(otherModel).getLongField(),
                Objects.requireNonNull(otherModel).getStringField(),
                Objects.requireNonNull(otherModel).isBooleanField());
    }

    public long getLongField() {
        return longField;
    }

    public String getStringField() {
        return stringField;
    }

    public boolean isBooleanField() {
        return booleanField;
    }
}