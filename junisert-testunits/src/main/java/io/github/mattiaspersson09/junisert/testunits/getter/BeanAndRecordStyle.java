package io.github.mattiaspersson09.junisert.testunits.getter;

public class BeanAndRecordStyle {
    private Object field;

    public Object field() {
        return field;
    }

    public Object getField() {
        return field;
    }
}
