package io.github.mattiaspersson09.junisert.testunits.unit.pojo;

import java.util.Objects;

public class DeepDependencyModel {
    private Dependency dependency;

    public DeepDependencyModel(Dependency dependency) {
        this.dependency = dependency;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public void setDependency(Dependency dependency) {
        this.dependency = dependency;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DeepDependencyModel that = (DeepDependencyModel) object;
        return Objects.equals(dependency, that.dependency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dependency);
    }

    @Override
    public String toString() {
        return "DeepDependencyModel{" +
                "dependency=" + dependency +
                '}';
    }

    public static class Dependency {
        private final DeeperDependency dependency;

        public Dependency(DeeperDependency dependency) {
            this.dependency = Objects.requireNonNull(dependency);
        }

        public DeeperDependency getDependency() {
            return dependency;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Dependency that = (Dependency) object;
            return Objects.equals(dependency, that.dependency);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dependency);
        }

        @Override
        public String toString() {
            return "Dependency{" +
                    "dependency=" + dependency +
                    '}';
        }
    }

    public static class DeeperDependency {
        private final DeepestDependency deepestDependency;

        public DeeperDependency(DeepestDependency deepestDependency) {
            this.deepestDependency = deepestDependency;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            DeeperDependency that = (DeeperDependency) object;
            return Objects.equals(deepestDependency, that.deepestDependency);
        }

        @Override
        public int hashCode() {
            return Objects.hash(deepestDependency);
        }

        @Override
        public String toString() {
            return "DeeperDependency{" +
                    "deepestDependency=" + deepestDependency +
                    '}';
        }
    }

    public static class DeepestDependency {
        private final Object object;
        private final String string;
        private final HiddenDependencyModel hiddenModel;

        public DeepestDependency(Object object, String string, HiddenDependencyModel hiddenModel) {
            this.object = Objects.requireNonNull(object);
            this.string = Objects.requireNonNull(string);
            this.hiddenModel = Objects.requireNonNull(hiddenModel);
        }

        public Object getObject() {
            return object;
        }

        public String getString() {
            return string;
        }

        public HiddenDependencyModel getHiddenModel() {
            return hiddenModel;
        }

        @Override
        public boolean equals(Object object1) {
            if (this == object1) return true;
            if (object1 == null || getClass() != object1.getClass()) return false;
            DeepestDependency that = (DeepestDependency) object1;
            return Objects.equals(object, that.object) && Objects.equals(string, that.string)
                    && Objects.equals(hiddenModel, that.hiddenModel);
        }

        @Override
        public int hashCode() {
            return Objects.hash(object, string, hiddenModel);
        }

        @Override
        public String toString() {
            return "DeeperDependency{" +
                    "object=" + object +
                    ", string='" + string + '\'' +
                    ", hiddenModel=" + hiddenModel +
                    '}';
        }
    }

    public static class HiddenDependencyModel {
        private final boolean hidden;

        HiddenDependencyModel(boolean hidden) {
            this.hidden = hidden;
        }

        public boolean isHidden() {
            return hidden;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            HiddenDependencyModel that = (HiddenDependencyModel) object;
            return hidden == that.hidden;
        }

        @Override
        public int hashCode() {
            return Objects.hash(hidden);
        }

        @Override
        public String toString() {
            return "HiddenCreationModel{" +
                    "hidden=" + hidden +
                    '}';
        }
    }
}
