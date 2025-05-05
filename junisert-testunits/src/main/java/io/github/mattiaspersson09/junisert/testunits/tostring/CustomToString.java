package io.github.mattiaspersson09.junisert.testunits.tostring;

public class CustomToString {
    private String field;

    @Override
    public String toString() {
        return "CustomToString.field(Object) = " + field;
    }
}
