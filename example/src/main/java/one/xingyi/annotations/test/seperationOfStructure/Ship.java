package one.xingyi.annotations.test.seperationOfStructure;

import one.xingyi.annotations.optics.Optics;

import java.util.List;

@Optics
public record Ship(List<Container> containers) {
}
