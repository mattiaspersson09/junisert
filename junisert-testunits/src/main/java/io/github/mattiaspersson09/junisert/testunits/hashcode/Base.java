package io.github.mattiaspersson09.junisert.testunits.hashcode;

import java.util.Objects;

public abstract class Base {
    private Integer baseField;

    @Override
    public int hashCode() {
        return Objects.hash(baseField);
    }
}
