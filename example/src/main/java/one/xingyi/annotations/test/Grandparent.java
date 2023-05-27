package one.xingyi.annotations.test;

import one.xingyi.optics.annotations.Optics;

@Optics
public record Grandparent(Parent parent) {
}
