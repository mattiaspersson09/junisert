package io.github.mattiaspersson09.junisert.testunits.unit.pojo;

import io.github.mattiaspersson09.junisert.testunits.polymorphism.Base;
import io.github.mattiaspersson09.junisert.testunits.polymorphism.Impl;

import java.util.Objects;

public class ModelWithMutableAndImmutableFields {
    private final int constantIntValue = 100;
    private final String constantStringValue = "constant";
    private String mutableString;
    private boolean mutableBoolean;

    public ModelWithMutableAndImmutableFields(String mutableString, boolean mutableBoolean) {
        this.mutableString = mutableString;
        this.mutableBoolean = mutableBoolean;
    }

    public int getConstantIntValue() {
        return constantIntValue;
    }

    public String getConstantStringValue() {
        return constantStringValue;
    }

    public String getMutableString() {
        return mutableString;
    }

    public void setMutableString(String mutableString) {
        this.mutableString = mutableString;
    }

    public boolean isMutableBoolean() {
        return mutableBoolean;
    }

    public void setMutableBoolean(boolean mutableBoolean) {
        this.mutableBoolean = mutableBoolean;
    }

    @SuppressWarnings("ConstantValue")
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ModelWithMutableAndImmutableFields that = (ModelWithMutableAndImmutableFields) object;
        return constantIntValue == that.constantIntValue && mutableBoolean == that.mutableBoolean
                && Objects.equals(constantStringValue, that.constantStringValue) && Objects.equals(
                mutableString, that.mutableString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constantIntValue, constantStringValue, mutableString, mutableBoolean);
    }

    @Override
    public String toString() {
        return "ModelWithMutableAndImmutableFields{" +
                "constantIntValue=" + constantIntValue +
                ", constantStringValue='" + constantStringValue + '\'' +
                ", mutableString='" + mutableString + '\'' +
                ", mutableBoolean=" + mutableBoolean +
                '}';
    }
}
