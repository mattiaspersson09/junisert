package io.github.mattiaspersson09.junisert.testunits.getter;

public class TwoLettersOrLessBeanStyle {
    private boolean isField;
    private Object mField;

    public Object getmField() {
        return mField;
    }

    public boolean getIsField() {
        return isField;
    }
}
