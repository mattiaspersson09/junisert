package io.github.mattiaspersson09.junisert.testunits.constructor;

public class SeveralParameterConstructors {
    private Object firstConstuctorField;
    private Object secondConstructorField;

    public SeveralParameterConstructors(Object firstConstuctorField) {
        this.firstConstuctorField = firstConstuctorField;
        this.secondConstructorField = null;
    }

    public SeveralParameterConstructors(Object firstConstuctorField, Object secondConstructorField) {
        this.firstConstuctorField = null;
        this.secondConstructorField = secondConstructorField;
    }

    public Object getFirstConstuctorField() {
        return firstConstuctorField;
    }

    public Object getSecondConstructorField() {
        return secondConstructorField;
    }
}
