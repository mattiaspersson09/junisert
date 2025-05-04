package io.github.mattiaspersson09.junisert.testunits.hashcode.broken;

import java.util.Random;

public class ReturnsRandomHashCode {
    private Object field;

    @Override
    public int hashCode() {
        return new Random().nextInt(100);
    }
}
