package io.github.mattiaspersson09.junisert.testunits.unit.enumeration;

public enum EnumUnit {
    CONST("value"),
    OTHER_CONST("other value");

    public static final EnumUnit ENUM_CONSTANT = CONST;
    private final String value;

    EnumUnit(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
