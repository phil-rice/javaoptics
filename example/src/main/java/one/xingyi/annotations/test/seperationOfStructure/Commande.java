package one.xingyi.annotations.test.seperationOfStructure;

import one.xingyi.optics.ITraversal;
import one.xingyi.optics.annotations.Optics;

import java.util.List;

@Optics(debug = true, traversals = {
}, addListTraversal = true)
public record Commande(List<CommandeTransport> commandeTransportList) {
    public static ITraversal<Commande, ChassisTroncon> commande2chassisTronconT =
            CommandeOptics.commandeTransportListT
                    .andThen(CommandeTransportOptics.tronconTransportListT)
                    .andThen(TronconTransportOptics.chassisTronconListT);

}
