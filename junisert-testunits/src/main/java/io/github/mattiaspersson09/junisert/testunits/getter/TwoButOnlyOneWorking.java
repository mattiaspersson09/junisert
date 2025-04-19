package io.github.mattiaspersson09.junisert.testunits.getter;

public class TwoButOnlyOneWorking {
    private Object field;

    public Object field() {
        return null;
    }

    public Object getField() {
        return field;
    }
}
