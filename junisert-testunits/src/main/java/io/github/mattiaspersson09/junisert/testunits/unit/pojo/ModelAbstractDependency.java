package io.github.mattiaspersson09.junisert.testunits.unit.pojo;

import io.github.mattiaspersson09.junisert.testunits.polymorphism.Base;

import java.util.Objects;

public class ModelAbstractDependency {
    private Base base;

    public ModelAbstractDependency(Base base) {
        this.base = base;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ModelAbstractDependency that = (ModelAbstractDependency) object;
        return Objects.equals(base, that.base);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base);
    }

    @Override
    public String toString() {
        return "ModelAbstractDependency{" +
                "base=" + base +
                '}';
    }
}
