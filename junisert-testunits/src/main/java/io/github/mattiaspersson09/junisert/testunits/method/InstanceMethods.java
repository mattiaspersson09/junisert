package io.github.mattiaspersson09.junisert.testunits.method;

public class InstanceMethods {
    public Object publicObjectNoParameters() {
        return new Object();
    }

    public void publicVoidObjectParameter(Object object) {
    }

    public void publicVoidFinalObjectParameter(final Object object) {
    }

    public void publicVoidStringParameter(String string) {
    }

    public void publicVoidOverloadingParameters(Object arg) {
    }

    public void publicVoidOverloadingParameters(Object arg, Object arg2) {
    }

    private void privateVoidNoParameters() {
    }
}
