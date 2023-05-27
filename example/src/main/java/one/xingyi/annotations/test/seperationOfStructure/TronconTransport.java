package one.xingyi.annotations.test.seperationOfStructure;

import one.xingyi.optics.annotations.Optics;

import java.util.List;

@Optics
public record TronconTransport(List<ChassisTroncon> chassisTronconList, List<Ship> ships, List<Train> trains) {
}
