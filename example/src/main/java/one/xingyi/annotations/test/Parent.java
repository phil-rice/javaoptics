package one.xingyi.annotations.test;

import one.xingyi.optics.annotations.Optics;
import one.xingyi.optics.annotations.Traversal;

import java.util.List;

@Optics
public record Parent(Child child, @Traversal List<Child> manyChildren) {
}
