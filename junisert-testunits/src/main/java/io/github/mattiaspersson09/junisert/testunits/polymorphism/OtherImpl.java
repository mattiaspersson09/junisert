package io.github.mattiaspersson09.junisert.testunits.polymorphism;

import java.util.Objects;

public class OtherImpl extends Base {
    private Object implField;

    public OtherImpl() {
        super(0);
    }

    public OtherImpl(int value) {
        super(value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        OtherImpl other = (OtherImpl) object;
        return Objects.equals(implField, other.implField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), implField);
    }
}
