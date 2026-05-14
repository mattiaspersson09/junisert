package io.github.mattiaspersson09.junisert.testunits.method;

public class FakeHashCodeMethods {
    public int hashCode(String string){
        return 0;
    }

    public boolean hashCode(FakeHashCodeMethods object){
        return false;
    }

    public Integer hashCode(Object object){
        return 0;
    }
}
