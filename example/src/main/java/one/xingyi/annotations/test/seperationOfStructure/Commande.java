package one.xingyi.annotations.test.seperationOfStructure;

import one.xingyi.optics.ITraversal;
import one.xingyi.optics.annotations.Optics;

import java.util.List;

@Optics(traversals = {
        "commandeTransportList.tronconTransportList",
        "commandeTransportList.tronconTransportList.chassisTronconList"})
public record Commande(List<CommandeTransport> commandeTransportList) {
    public static ITraversal<List<Commande>, Commande> listCommandetoCommandeT = ITraversal.listTraversal();

    public static ITraversal<Commande, TronconTransport> commandeToTronconTransportT =
            CommandeOptics.commandeTransportListT.andThen(CommandeTransportOptics.tronconTransportListT);
    public static ITraversal<Commande, ChassisTroncon> commandeToChassisTronconT =
            commandeToTronconTransportT.andThen(TronconTransportOptics.chassisTronconListT);

}
