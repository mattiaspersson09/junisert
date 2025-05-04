package io.github.mattiaspersson09.junisert.testunits.hashcode;

import java.util.Objects;

public class WellImplementedHashCode {
    private Object field;
    private String stringField;
    private int intField;

    @Override
    public int hashCode() {
        return Objects.hash(field, stringField, intField);
    }
}
