package io.github.mattiaspersson09.junisert.testunits.unit.pojo;

import java.util.Objects;

public final class ImmutableModel {
    private final long longField;
    private final String stringField;
    private final boolean booleanField;

    public ImmutableModel(long longField, String stringField, boolean booleanField) {
        this.longField = longField;
        this.stringField = stringField;
        this.booleanField = booleanField;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ImmutableModel that = (ImmutableModel) object;
        return longField == that.longField && booleanField == that.booleanField && Objects.equals(stringField,
                that.stringField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(longField, stringField, booleanField);
    }

    @Override
    public String toString() {
        return "ImmutableModel{" +
                "longField=" + longField +
                ", stringField='" + stringField + '\'' +
                ", booleanField=" + booleanField +
                '}';
    }
}
