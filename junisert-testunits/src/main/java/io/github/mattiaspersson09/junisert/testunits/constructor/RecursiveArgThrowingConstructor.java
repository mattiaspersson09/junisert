package io.github.mattiaspersson09.junisert.testunits.constructor;

public class RecursiveArgThrowingConstructor {
    public RecursiveArgThrowingConstructor(RecursiveArgThrowingConstructor arg) {
        throw new RuntimeException();
    }
}
