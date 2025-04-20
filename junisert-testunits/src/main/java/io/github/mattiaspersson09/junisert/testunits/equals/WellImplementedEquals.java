package io.github.mattiaspersson09.junisert.testunits.equals;

import java.util.Objects;

public class WellImplementedEquals {
    private Object field;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        WellImplementedEquals wellImplementedEquals = (WellImplementedEquals) object;
        return Objects.equals(field, wellImplementedEquals.field);
    }
}
