package io.github.mattiaspersson09.junisert.testunits.unit.pojo;

import java.util.Objects;

public class UnknownDependency {
    private final String name;

    public UnknownDependency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UnknownDependency that = (UnknownDependency) object;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "UnknownDependency{" +
                "name='" + name + '\'' +
                '}';
    }
}
