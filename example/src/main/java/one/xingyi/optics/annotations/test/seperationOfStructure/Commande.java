package one.xingyi.optics.annotations.test.seperationOfStructure;

import one.xingyi.optics.annotations.Optics;

import java.util.List;

@Optics(debug = false, addListTraversal = true,
        traversals = {"commande2ChassisT:commandeTransportList.tronconTransportList.chassisTronconList"})
public record Commande(List<CommandeTransport> commandeTransportList) {
}
