package io.github.mattiaspersson09.junisert.testunits.setter;

public class TwoButOnlyOneWorking {
    private Object field;

    public TwoButOnlyOneWorking field(Object field) {
        return this;
    }

    public void setField(Object field) {
        this.field = field;
    }
}
