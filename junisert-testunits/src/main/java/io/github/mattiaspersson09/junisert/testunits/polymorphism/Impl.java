package io.github.mattiaspersson09.junisert.testunits.polymorphism;

import java.util.Objects;

public class Impl extends Base {
    private Object implField;

    public Impl() {
        super(0);
    }

    public Impl(int value) {
        super(value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        Impl impl = (Impl) object;
        return Objects.equals(implField, impl.implField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), implField);
    }
}
