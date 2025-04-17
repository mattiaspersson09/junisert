package io.github.mattiaspersson09.junisert.testunits.setter;

public class BuilderStyle {
    private Object field;

    public BuilderStyle field(Object field) {
        this.field = field;
        return this;
    }
}
