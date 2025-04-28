package io.github.mattiaspersson09.junisert.testunits.constructor;

public class SeveralParameterConstructors {
    private Object firstConstructorField;
    private Object secondConstructorField;

    public SeveralParameterConstructors(Object firstConstructorField) {
        this.firstConstructorField = firstConstructorField;
        this.secondConstructorField = null;
    }

    public SeveralParameterConstructors(Object firstConstructorField, Object secondConstructorField) {
        this.firstConstructorField = null;
        this.secondConstructorField = secondConstructorField;
    }

    public Object getFirstConstructorField() {
        return firstConstructorField;
    }

    public Object getSecondConstructorField() {
        return secondConstructorField;
    }
}
