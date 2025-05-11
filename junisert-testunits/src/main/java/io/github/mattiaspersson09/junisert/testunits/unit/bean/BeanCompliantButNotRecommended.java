package io.github.mattiaspersson09.junisert.testunits.unit.bean;

public class BeanCompliantButNotRecommended {
    private String name;
    private int age;
    private boolean active;
    private Object object;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
