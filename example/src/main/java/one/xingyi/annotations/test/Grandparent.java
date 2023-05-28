package one.xingyi.annotations.test;

import one.xingyi.annotations.optics.Optics;

@Optics
public record Grandparent(Parent parent) {
}
