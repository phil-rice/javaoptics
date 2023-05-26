package one.focuson.annotations.test;

import one.focuson.optics.annotations.Optics;
import one.focuson.optics.annotations.Traversal;

import java.util.List;

@Optics
public record Parent(Child child, @Traversal List<Child> manyChildren) {
}
