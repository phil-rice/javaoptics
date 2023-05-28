package one.xingyi.annotations.test;

import one.xingyi.annotations.optics.Optics;

import java.util.List;

@Optics
public record Parent(Child child, List<Child> manyChildren) {
}
