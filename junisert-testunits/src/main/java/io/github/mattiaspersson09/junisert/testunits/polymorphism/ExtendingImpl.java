package io.github.mattiaspersson09.junisert.testunits.polymorphism;

import java.util.Objects;

public class ExtendingImpl extends Impl {
    private Object extendingImplField;

    public ExtendingImpl() {
        super();
    }

    public ExtendingImpl(int value) {
        super(value);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        ExtendingImpl extending = (ExtendingImpl) object;
        return Objects.equals(extendingImplField, extending.extendingImplField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), extendingImplField);
    }
}
