/*
 * Copyright (c) 2025-2025 Mattias Persson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.mattiaspersson09.junisert.core;

import io.github.mattiaspersson09.junisert.testunits.polymorphism.Super;
import io.github.mattiaspersson09.junisert.testunits.unit.bean.BeanCompliantModel;
import io.github.mattiaspersson09.junisert.testunits.unit.pojo.ImmutableModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

class ExpensivePojo {
    private InceptionModel inceptionModel;
    private ImmutableModel immutableModel;
    private BeanCompliantModel beanModel;
    private String name;
    private int age;
    private List<BeanCompliantModel.Type> previousTypes;
    private Set<BeanCompliantModel.Type> possibleTypes;
    private Collection<Super> superCollection;
    private boolean active;
    private boolean isPlain;
    private Object plain;
    private BeanCompliantModel[] connectedModels;
    private Object[] modelObjects;

    public ExpensivePojo(InceptionModel inceptionModel,
                         ImmutableModel immutableModel,
                         BeanCompliantModel beanModel,
                         String name,
                         int age,
                         List<BeanCompliantModel.Type> previousTypes,
                         Set<BeanCompliantModel.Type> possibleTypes,
                         Collection<Super> superCollection,
                         boolean active,
                         boolean isPlain,
                         Object plain,
                         BeanCompliantModel[] connectedModels,
                         Object[] modelObjects) {
        this.inceptionModel = inceptionModel;
        this.immutableModel = immutableModel;
        this.beanModel = beanModel;
        this.name = name;
        this.age = age;
        this.previousTypes = previousTypes;
        this.possibleTypes = possibleTypes;
        this.superCollection = superCollection;
        this.active = active;
        this.isPlain = isPlain;
        this.plain = plain;
        this.connectedModels = connectedModels;
        this.modelObjects = modelObjects;
    }

    public InceptionModel getInceptionModel() {
        return inceptionModel;
    }

    public void setInceptionModel(InceptionModel inceptionModel) {
        this.inceptionModel = inceptionModel;
    }

    public ImmutableModel getImmutableModel() {
        return immutableModel;
    }

    public void setImmutableModel(ImmutableModel immutableModel) {
        this.immutableModel = immutableModel;
    }

    public BeanCompliantModel getBeanModel() {
        return beanModel;
    }

    public void setBeanModel(BeanCompliantModel beanModel) {
        this.beanModel = beanModel;
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

    public List<BeanCompliantModel.Type> getPreviousTypes() {
        return previousTypes;
    }

    public void setPreviousTypes(List<BeanCompliantModel.Type> previousTypes) {
        this.previousTypes = previousTypes;
    }

    public Set<BeanCompliantModel.Type> getPossibleTypes() {
        return possibleTypes;
    }

    public void setPossibleTypes(Set<BeanCompliantModel.Type> possibleTypes) {
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

    public boolean isPlain() {
        return isPlain;
    }

    public void setPlain(boolean plain) {
        isPlain = plain;
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
        ExpensivePojo that = (ExpensivePojo) object;
        return age == that.age && active == that.active && isPlain == that.isPlain && Objects.equals(
                inceptionModel, that.inceptionModel) && Objects.equals(immutableModel, that.immutableModel)
                && Objects.equals(beanModel, that.beanModel) && Objects.equals(name, that.name)
                && Objects.equals(previousTypes, that.previousTypes) && Objects.equals(possibleTypes,
                        that.possibleTypes) && Objects.equals(superCollection, that.superCollection)
                && Objects.equals(plain, that.plain) && Arrays.equals(connectedModels,
                        that.connectedModels) && Arrays.equals(modelObjects, that.modelObjects);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(inceptionModel, immutableModel, beanModel, name, age, previousTypes, possibleTypes,
                superCollection, active, isPlain, plain);
        result = 31 * result + Arrays.hashCode(connectedModels);
        result = 31 * result + Arrays.hashCode(modelObjects);
        return result;
    }

    @Override
    public String toString() {
        return "ExpensivePojo{" +
                "inceptionModel=" + inceptionModel +
                ", immutableModel=" + immutableModel +
                ", beanModel=" + beanModel +
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
}
