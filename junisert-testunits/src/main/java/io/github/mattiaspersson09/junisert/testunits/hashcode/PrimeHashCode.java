package io.github.mattiaspersson09.junisert.testunits.hashcode;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class PrimeHashCode {
    private String field;
    private int[] intArray;
    private int integer;

    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(intArray);
        result = 31 * result + integer;
        return result;
    }
}
