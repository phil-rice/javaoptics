package one.xingyi.annotations.test.seperationOfStructure;

import one.xingyi.annotations.optics.Optics;

import java.util.List;

@Optics
public record TronconTransport(List<ChassisTroncon> chassisTronconList, List<Ship> ships, List<Train> trains) {
}
