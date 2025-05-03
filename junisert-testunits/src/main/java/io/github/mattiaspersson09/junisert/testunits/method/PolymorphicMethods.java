package io.github.mattiaspersson09.junisert.testunits.method;

import io.github.mattiaspersson09.junisert.testunits.polymorphism.Base;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

public class PolymorphicMethods {
    public void publicVoidSuperParameter(Super arg) {
    }

    public static void publicStaticVoidSuperParameter(Super arg) {
    }

    public void publicVoidSuperAndBaseParameters(Super arg, Base arg2) {
    }

    public void publicVoidBaseParameter(Base arg) {
    }

    public Super publicSuperNoParameters() {
        return new Impl();
    }

    public static Super publicStaticSuperNoParameters() {
        return new Impl();
    }

    public Base publicBaseNoParameters() {
        return new Impl();
    }

    public static Base publicStaticBaseNoParameters() {
        return new Impl();
    }

    public Super publicSuperSuperParameter(Super arg) {
        return new Impl();
    }

    public Super publicSuperSuperAndBaseParameters(Super arg, Base arg2) {
        return new Impl();
    }

    public static Super publicStaticSuperSuperParameter(Super arg) {
        return new Impl();
    }

    public Base publicBaseBaseParameter(Base arg) {
        return new Impl();
    }

    public Super publicSuperBaseParameter(Base arg) {
        return new Impl();
    }

    public Base publicBaseSuperParameter(Super arg) {
        return new Impl();
    }
}
