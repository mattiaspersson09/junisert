package io.github.mattiaspersson09.junisert.testunits.tostring;

import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

import java.util.Arrays;
import java.util.List;

public class JsonToString {
    private Integer integerField;
    private int intField;
    private boolean booleanField;
    private String[] stringArrayField;
    private List<? extends Super> listField;
    private String stringField;

    @Override
    public String toString() {
        return "JsonToString {\n" +
                "  integerField: " + integerField +
                ",\n  intField: " + intField +
                ",\n  booleanField: " + booleanField +
                ",\n  stringArrayField: " + Arrays.toString(stringArrayField) +
                ",\n  listField: " + listField +
                ",\n  stringField: \"" + stringField + "\"" +
                "\n}";
    }
}
