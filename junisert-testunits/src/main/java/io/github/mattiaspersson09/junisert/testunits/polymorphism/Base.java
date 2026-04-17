package io.github.mattiaspersson09.junisert.testunits.polymorphism;

import java.util.Objects;

public abstract class Base implements Super {
    private final int value;

    protected Base(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Base base = (Base) o;
        return value == base.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
