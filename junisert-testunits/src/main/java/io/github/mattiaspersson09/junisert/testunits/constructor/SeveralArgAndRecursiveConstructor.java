package io.github.mattiaspersson09.junisert.testunits.constructor;

public class SeveralArgAndRecursiveConstructor {
    private RecursiveArgConstructor recursed;
    private Object field;

    public SeveralArgAndRecursiveConstructor(RecursiveArgConstructor recursed, Object field) {
        this.recursed = recursed;
        this.field = field;
    }

    public RecursiveArgConstructor getRecursed() {
        return recursed;
    }

    public Object getField() {
        return field;
    }
}
