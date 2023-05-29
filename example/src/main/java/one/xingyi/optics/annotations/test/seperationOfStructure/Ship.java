package one.xingyi.optics.annotations.test.seperationOfStructure;

import one.xingyi.optics.annotations.Optics;

import java.util.List;

@Optics(addListTraversal = true)
public record Ship(List<Container> containers, List<Wagon> wagons, List<ChassisTroncon> trancons) {
}
