package io.github.mattiaspersson09.junisert.testunits.equals.broken;

public class AlwaysTrueEquals {
    private Object field;

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object object) {
        return true;
    }
}
