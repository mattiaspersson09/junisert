package io.github.mattiaspersson09.junisert.testunits.setter;

public class BeanAndBuilderStyle {
    private Object field;

    public BeanAndBuilderStyle field(Object field) {
        this.field = field;
        return this;
    }

    public void setField(Object field) {
        this.field = field;
    }
}
