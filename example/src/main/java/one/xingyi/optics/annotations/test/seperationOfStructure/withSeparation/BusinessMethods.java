package one.xingyi.optics.annotations.test.seperationOfStructure.withSeparation;

import one.xingyi.optics.ITraversal;
import one.xingyi.optics.annotations.test.seperationOfStructure.ChassisTroncon;
import one.xingyi.optics.annotations.test.seperationOfStructure.Commande;
import one.xingyi.optics.annotations.test.seperationOfStructure.CommandeOptics;
import one.xingyi.optics.annotations.test.seperationOfStructure.IBusinessMethods;

import java.util.List;
import java.util.function.Predicate;

public class BusinessMethods implements IBusinessMethods {

    static ITraversal<List<Commande>, ChassisTroncon> commandeListToChassisTronconT =
            CommandeOptics.listT.andThen(CommandeOptics.commande2ChassisT);

    public List<String> getPfIdsFor(List<Commande> commandes) {
        return commandeListToChassisTronconT.all(commandes).map(ChassisTroncon::pfId).toList();
    }

    @Override
    public List<String> getPfIdsForWhere(List<Commande> commandes, Predicate<ChassisTroncon> where) {
        return commandeListToChassisTronconT.filter(where).all(commandes).map(ChassisTroncon::pfId).toList();
    }

    public List<String> getPfIdsFor(Commande commande) {
        return CommandeOptics.commande2ChassisT.all(commande).map(ChassisTroncon::pfId).toList();
    }

    @Override
    public void doSomething(Commande commande) {
        CommandeOptics.commande2ChassisT.all(commande).forEach(ChassisTroncon::doSomething);

    }

    public void doSomethingForList(List<Commande> commandes) {
        commandeListToChassisTronconT.all(commandes).forEach(ChassisTroncon::doSomething);

    }

    @Override
    public void doSomethingWhere(List<Commande> commandes, Predicate<ChassisTroncon> where) {
        commandeListToChassisTronconT.filter(where).all(commandes).forEach(ChassisTroncon::doSomething);
    }

    @Override
    public void doSomethingElse(Commande commande) {
        CommandeOptics.commande2ChassisT.all(commande).forEach(ChassisTroncon::doSomethingElse);
    }

    @Override
    public void doSomethingElseForList(List<Commande> commandes) {
        commandeListToChassisTronconT.all(commandes).forEach(ChassisTroncon::doSomethingElse);
    }

}
        
