package one.focuson.annotations.test;

import one.focuson.optics.annotations.Optics;

@Optics(name = "GrandparentOverrideOptics")
public record Grandparent(Parent parent) {
}
