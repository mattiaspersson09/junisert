package io.github.mattiaspersson09.junisert.testunits.equals;

import java.util.Objects;

public abstract class Base {
    private Object baseField;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Base base = (Base) object;
        return Objects.equals(baseField, base.baseField);
    }
}
