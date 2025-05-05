package io.github.mattiaspersson09.junisert.testunits.tostring;

import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

import java.util.Arrays;
import java.util.List;

public class NotAllFieldsToString {
    private Integer integerField;
    private int intField;
    private boolean booleanField;
    private String[] stringArrayField;
    private List<? extends Super> listField;

    @Override
    public String toString() {
        return "NotAllFieldsToString{" +
                "integerField=" + integerField +
//                ", intField=" + intField +
                ", booleanField=" + booleanField +
                ", stringArrayField=" + Arrays.toString(stringArrayField) +
                ", listField=" + listField +
                '}';
    }
}
