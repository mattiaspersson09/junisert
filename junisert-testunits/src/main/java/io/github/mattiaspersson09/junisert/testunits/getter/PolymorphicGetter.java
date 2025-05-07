package io.github.mattiaspersson09.junisert.testunits.getter;

import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

public class PolymorphicGetter {
    private Impl field;

    public Super field() {
        return field;
    }
}
