package io.github.mattiaspersson09.junisert.testunits.hashcode.broken;

public class ReturnsConstantPrimeHashCode {
    private Object field;

    @Override
    public int hashCode() {
        return 31;
    }
}
