package one.xingyi.optics.annotations.test.seperationOfStructure;

import one.xingyi.optics.ITraversal;
import one.xingyi.optics.annotations.Optics;

import java.util.List;

@Optics(debug = false, traversals = {
        "commandeTransportList.tronconTransportList",
        "commande2ChassisT:commandeTransportList.tronconTransportList.chassisTronconList"
}, addListTraversal = true)
public record Commande(List<CommandeTransport> commandeTransportList) {


}
