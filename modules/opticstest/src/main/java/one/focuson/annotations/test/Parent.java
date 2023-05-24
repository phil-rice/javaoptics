package one.focuson.annotations.test;

import one.focuson.optics.annotations.Optics;

import java.util.Objects;

@Opticsgit
public final class Parent {

    private final Child child;
    private final Child secondChild;

    public Parent(Child child, Child secondChild) {
        this.child = child;
        this.secondChild = secondChild;
    }

    public Child child() {
        return child;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Parent) obj;
        return Objects.equals(this.child, that.child);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child);
    }

    @Override
    public String toString() {
        return "Parent[" +
                "child=" + child + ']';
    }

}
