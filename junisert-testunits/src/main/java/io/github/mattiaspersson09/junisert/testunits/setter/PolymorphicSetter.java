package io.github.mattiaspersson09.junisert.testunits.setter;

import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

public class PolymorphicSetter {
    private Super field;

    public void setField(Impl field) {
        this.field = field;
    }

    public void setField(Super field) {
        this.field = field;
    }
}
