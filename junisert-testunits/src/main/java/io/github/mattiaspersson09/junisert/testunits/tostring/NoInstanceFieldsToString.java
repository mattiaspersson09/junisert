package io.github.mattiaspersson09.junisert.testunits.tostring;

public class NoInstanceFieldsToString {
    private static final String CONSTANT = "123456";

    @Override
    public String toString() {
        return "NoInstanceFieldsToString{CONSTANT=" + CONSTANT + "}";
    }
}
