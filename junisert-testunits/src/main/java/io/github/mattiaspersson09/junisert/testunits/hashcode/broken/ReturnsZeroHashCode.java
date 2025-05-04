package io.github.mattiaspersson09.junisert.testunits.hashcode.broken;

public class ReturnsZeroHashCode {
    private Object field;

    @Override
    public int hashCode() {
        return 0;
    }
}
