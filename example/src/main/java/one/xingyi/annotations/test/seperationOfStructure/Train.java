package one.xingyi.annotations.test.seperationOfStructure;

import one.xingyi.optics.annotations.Optics;

import java.util.List;

@Optics
public record Train(List<Wagon> wagons) {
}