package io.github.mattiaspersson09.junisert.testunits.setter;

public class BooleanCollisionBeanStyle {
    private boolean isField;
    private Object field;

    public void setField(boolean field) {
        isField = field;
    }

    public void setField(Object field) {
        this.field = field;
    }
}
