package io.github.mattiaspersson09.junisert.testunits.equals.broken;

public class OnlyTypeCheckEquals {
    private Object field;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        OnlyTypeCheckEquals that = (OnlyTypeCheckEquals) object;
        return true;
    }
}
