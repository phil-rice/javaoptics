package one.xingyi.annotations.test.seperationOfStructure;

import one.xingyi.optics.ITraversal;
import one.xingyi.optics.annotations.Optics;

import java.util.List;

@Optics(traversals = {
        "commande2tronconTransportT:commandeTransportList.tronconTransportList",
        "commande2chassisTronconT:commandeTransportList.tronconTransportList.chassisTronconList"}, addListTraversal = true)
public record Commande(List<CommandeTransport> commandeTransportList) {

}
