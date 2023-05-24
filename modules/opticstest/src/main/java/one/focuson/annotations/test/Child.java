package one.focuson.annotations.test;

import one.focuson.optics.annotations.Optics;

@Optics
public record Child(String name, int age) {
}
