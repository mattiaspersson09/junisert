package io.github.mattiaspersson09.junisert.testunits.constructor;

import java.util.Objects;

public class DefaultAndArgConstructor {
    private final String field;

    public DefaultAndArgConstructor() {
        field = null;
    }

    public DefaultAndArgConstructor(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DefaultAndArgConstructor that = (DefaultAndArgConstructor) object;
        return Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field);
    }
}
