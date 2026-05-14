package io.github.mattiaspersson09.junisert.testunits.setter;

public class BuilderStyleBooleanWithPrefix {
    private boolean isField;

    public BuilderStyleBooleanWithPrefix withField(boolean field) {
        this.isField = field;
        return this;
    }

    public BuilderStyleBooleanWithPrefix withIsField(boolean field) {
        this.isField = field;
        return this;
    }
}
