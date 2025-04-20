package io.github.mattiaspersson09.junisert.testunits.equals;

import java.util.Objects;

public class WellImplementedEqualsExtendingBase extends Base {
    private Object field;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        WellImplementedEqualsExtendingBase that = (WellImplementedEqualsExtendingBase) object;
        return Objects.equals(field, that.field);
    }
}
