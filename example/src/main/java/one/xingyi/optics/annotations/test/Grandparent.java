package one.xingyi.optics.annotations.test;

import one.xingyi.optics.annotations.Optics;

@Optics
public record Grandparent(Parent parent) {
}
