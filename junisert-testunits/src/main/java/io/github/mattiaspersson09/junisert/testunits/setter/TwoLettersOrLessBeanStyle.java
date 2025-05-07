package io.github.mattiaspersson09.junisert.testunits.setter;

public class TwoLettersOrLessBeanStyle {
    private boolean isField;
    private Object mField;

    public void setField(boolean field) {
        isField = field;
    }

    public void setIsField(boolean field) {
        isField = field;
    }

    public void setmField(Object mField) {
        this.mField = mField;
    }
}
