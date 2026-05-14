package io.github.mattiaspersson09.junisert.testunits.method;

public class FakeEqualsMethods {
    public boolean equals(String string){
        return false;
    }

    public boolean equals(FakeEqualsMethods object){
        return false;
    }

    public Object equals(int integer){
        return false;
    }
}
