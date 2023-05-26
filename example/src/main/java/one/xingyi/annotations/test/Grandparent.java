package one.xingyi.annotations.test;

import one.xingyi.optics.annotations.Optics;

@Optics(name = "GrandparentOverrideOptics")
public record Grandparent(Parent parent) {
}
