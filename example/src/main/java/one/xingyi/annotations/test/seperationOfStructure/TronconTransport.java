package one.xingyi.annotations.test.seperationOfStructure;

import one.xingyi.optics.annotations.Optics;

import java.util.List;

@Optics
public record TronconTransport(List<ChassisTroncon> chassisTronconList, List<Ship> ships, List<Train> trains) {
//    public static ITraversal<TronconTransport, ChassisTroncon> tronconTransportToChassisTransport =
//            ITraversal.nullSafe(TronconTransport::getChassisTroncons);
//    public static ITraversal<TronconTransport, Ship> tronconTransportToShipsT =
//            ITraversal.nullSafe(TronconTransport::getShips);
//    public static ITraversal<TronconTransport, Container> tronconTransportToContainersT =
//            tronconTransportToShipsT.andThen(Ship.shipToContainerT);
//    public static ITraversal<TronconTransport, Train> tronconTransportToTrainsT =
//            ITraversal.nullSafe(TronconTransport::getTrains);
//    public static ITraversal<TronconTransport, Wagon> tronconTransportWagonITraversal =
//            tronconTransportToTrainsT.andThen(Train.trainToWagonT);
//    public static ITraversal<TronconTransport, IMeansOfTransport> tronconTransportToMeansOfTransport =
//            ITraversal.<TronconTransport, IMeansOfTransport>concat(
//                    tronconTransportToChassisTransport,
//                    tronconTransportToContainersT,
//                    tronconTransportWagonITraversal);


}
