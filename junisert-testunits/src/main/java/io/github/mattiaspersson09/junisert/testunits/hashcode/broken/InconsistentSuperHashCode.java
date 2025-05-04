package io.github.mattiaspersson09.junisert.testunits.hashcode.broken;

public class InconsistentSuperHashCode {
    private Object field;

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
