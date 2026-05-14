package io.github.mattiaspersson09.junisert.testunits.unit;

import java.util.Objects;

public class NeedsExclusion {
    private static final String CONSTANT_STRING = "constant";
    private String fieldMissingFromToString;
    private String stringField;
    private String fieldMissingSetter;
    private String fieldMissingGetter;
    String fieldNotPrivate;

    public String getFieldMissingFromToString() {
        return fieldMissingFromToString;
    }

    public void setFieldMissingFromToString(String fieldMissingFromToString) {
        this.fieldMissingFromToString = fieldMissingFromToString;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public String getFieldMissingSetter() {
        return fieldMissingSetter;
    }

    public void setFieldMissingGetter(String fieldMissingGetter) {
        this.fieldMissingGetter = fieldMissingGetter;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        NeedsExclusion that = (NeedsExclusion) object;
        return Objects.equals(fieldMissingFromToString, that.fieldMissingFromToString)
                && Objects.equals(stringField, that.stringField) && Objects.equals(fieldMissingSetter,
                that.fieldMissingSetter) && Objects.equals(fieldMissingGetter, that.fieldMissingGetter)
                && Objects.equals(fieldNotPrivate, that.fieldNotPrivate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldMissingFromToString, stringField, fieldMissingSetter, fieldMissingGetter,
                fieldNotPrivate);
    }

    @Override
    public String toString() {
        return "NeedsExclusion{" +
                "stringField='" + stringField + '\'' +
                ", fieldMissingSetter='" + fieldMissingSetter + '\'' +
                ", fieldMissingGetter='" + fieldMissingGetter + '\'' +
                ", fieldNotPrivate='" + fieldNotPrivate + '\'' +
                '}';
    }
}
