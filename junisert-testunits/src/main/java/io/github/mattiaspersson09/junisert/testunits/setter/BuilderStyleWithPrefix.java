package io.github.mattiaspersson09.junisert.testunits.setter;

public class BuilderStyleWithPrefix {
    private Object field;

    public BuilderStyleWithPrefix withField(Object field) {
        this.field = field;
        return this;
    }
}
