package io.github.mattiaspersson09.junisert.testunits.constructor;

public class RecursiveArgConstructor {
    private final RecursiveArgConstructor recursed;

    public RecursiveArgConstructor(RecursiveArgConstructor arg) {
        recursed = arg;
    }

    public RecursiveArgConstructor getRecursed() {
        return recursed;
    }
}
