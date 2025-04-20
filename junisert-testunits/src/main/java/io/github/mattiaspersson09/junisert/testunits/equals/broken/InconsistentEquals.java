package io.github.mattiaspersson09.junisert.testunits.equals.broken;

import java.util.Objects;

public class InconsistentEquals {
    private static int equalsCount = 1;
    private Object field;

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if ((equalsCount++) % 4 == 0) return false;
        InconsistentEquals other = (InconsistentEquals) object;
        return Objects.equals(field, other.field);
    }
}
