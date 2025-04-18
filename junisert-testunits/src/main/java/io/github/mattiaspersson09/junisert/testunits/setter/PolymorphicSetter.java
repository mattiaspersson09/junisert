package io.github.mattiaspersson09.junisert.testunits.setter;

import io.github.mattiaspersson09.junisert.testunits.polymorphism.ExtendingImpl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;

public class PolymorphicSetter {
    private Impl field;

    public void setField(ExtendingImpl field) {
        this.field = field;
    }
}
