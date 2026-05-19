package io.github.mattiaspersson09.junisert.testunits.constructor.assign;

public class ConstructorAssignsAllProperties {
    private final String stringProperty;
    private final Object objectProperty;

    public ConstructorAssignsAllProperties(String stringProperty, Object objectProperty) {
        this.stringProperty = stringProperty;
        this.objectProperty = objectProperty;
    }
}
