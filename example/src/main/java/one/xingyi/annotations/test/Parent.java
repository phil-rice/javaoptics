package one.xingyi.annotations.test;

import one.xingyi.optics.annotations.Optics;

import java.util.List;

@Optics
public record Parent(Child child, List<Child> manyChildren) {
}
