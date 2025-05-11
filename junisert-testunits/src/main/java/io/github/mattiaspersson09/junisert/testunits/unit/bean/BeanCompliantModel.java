package io.github.mattiaspersson09.junisert.testunits.unit.bean;

import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BeanCompliantModel implements Serializable {
    private Type modelType;
    private String name;
    private int age;
    private List<Type> previousTypes;
    private Set<Type> possibleTypes;
    private Collection<Super> superCollection;
    private boolean active;
    private boolean isPlain;
    private Object plain;
    private BeanCompliantModel[] connectedModels;
    private Object[] modelObjects;

    public BeanCompliantModel() {
        possibleTypes = Collections.singleton(Type.PLAIN);
    }

    public Type getModelType() {
        return modelType;
    }

    public void setModelType(Type modelType) {
        this.modelType = modelType;
    }

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

    public List<Type> getPreviousTypes() {
        return previousTypes;
    }

    public void setPreviousTypes(List<Type> previousTypes) {
        this.previousTypes = previousTypes;
    }

    public Set<Type> getPossibleTypes() {
        return possibleTypes;
    }

    public void setPossibleTypes(Set<Type> possibleTypes) {
        this.possibleTypes = possibleTypes;
    }

    public Collection<Super> getSuperCollection() {
        return superCollection;
    }

    public void setSuperCollection(Collection<Super> superCollection) {
        this.superCollection = superCollection;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getIsPlain() {
        return isPlain;
    }

    public void setIsPlain(boolean isPlain) {
        this.isPlain = isPlain;
    }

    public Object getPlain() {
        return plain;
    }

    public void setPlain(Object plain) {
        this.plain = plain;
    }

    public BeanCompliantModel[] getConnectedModels() {
        return connectedModels;
    }

    public void setConnectedModels(BeanCompliantModel[] connectedModels) {
        this.connectedModels = connectedModels;
    }

    public Object[] getModelObjects() {
        return modelObjects;
    }

    public void setModelObjects(Object[] modelObjects) {
        this.modelObjects = modelObjects;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        BeanCompliantModel that = (BeanCompliantModel) object;
        return age == that.age && active == that.active && isPlain == that.isPlain && modelType == that.modelType
                && Objects.equals(name, that.name) && Objects.equals(previousTypes, that.previousTypes)
                && Objects.equals(possibleTypes, that.possibleTypes) && Objects.equals(superCollection,
                that.superCollection) && Objects.equals(plain, that.plain) && Arrays.equals(
                connectedModels, that.connectedModels) && Arrays.equals(modelObjects, that.modelObjects);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(modelType, name, age, previousTypes, possibleTypes, superCollection, active, isPlain,
                plain);
        result = 31 * result + Arrays.hashCode(connectedModels);
        result = 31 * result + Arrays.hashCode(modelObjects);
        return result;
    }

    @Override
    public String toString() {
        return "BeanCompliantModel{" +
                "modelType=" + modelType +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", previousTypes=" + previousTypes +
                ", possibleTypes=" + possibleTypes +
                ", superCollection=" + superCollection +
                ", active=" + active +
                ", isPlain=" + isPlain +
                ", plain=" + plain +
                ", connectedModels=" + Arrays.toString(connectedModels) +
                ", modelObjects=" + Arrays.toString(modelObjects) +
                '}';
    }

    public enum Type {
        PLAIN,
        SOME_OTHER_TYPE
    }
}
