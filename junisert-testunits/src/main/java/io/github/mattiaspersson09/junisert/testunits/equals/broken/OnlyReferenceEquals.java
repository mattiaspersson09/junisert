package io.github.mattiaspersson09.junisert.testunits.equals.broken;

public class OnlyReferenceEquals {
    private Object field;

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object object) {
        return this == object;
    }
}
