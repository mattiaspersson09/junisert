package io.github.mattiaspersson09.junisert.testunits.unit.pojo;

import java.util.Objects;

public final class UnknownDependencyImmutable {
    private final long longField;
    private final String stringField;
    private final boolean booleanField;
    private final UnknownDependency dependency;

    public UnknownDependencyImmutable(long longField,
                                      String stringField,
                                      boolean booleanField,
                                      UnknownDependency dependency) {
        this.longField = longField;
        this.stringField = stringField;
        this.booleanField = booleanField;
        this.dependency = dependency;
    }

    public long getLongField() {
        return longField;
    }

    public String getStringField() {
        return stringField;
    }

    public boolean isBooleanField() {
        return booleanField;
    }

    public UnknownDependency getDependency() {
        return dependency;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UnknownDependencyImmutable that = (UnknownDependencyImmutable) object;
        return longField == that.longField && booleanField == that.booleanField && Objects.equals(stringField,
                that.stringField) && Objects.equals(dependency, that.dependency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(longField, stringField, booleanField, dependency);
    }

    @Override
    public String toString() {
        return "UnknownDependencyImmutable{" +
                "longField=" + longField +
                ", stringField='" + stringField + '\'' +
                ", booleanField=" + booleanField +
                ", dependency=" + dependency +
                '}';
    }
}
