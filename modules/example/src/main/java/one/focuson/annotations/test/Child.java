package one.focuson.annotations.test;

import one.focuson.optics.annotations.Copy;
import one.focuson.optics.annotations.Optics;

@Optics
public record Child(String name, int age) {
    @Copy
    Child copy(String name, int age) {
        return new Child(name, age);
    }
}

