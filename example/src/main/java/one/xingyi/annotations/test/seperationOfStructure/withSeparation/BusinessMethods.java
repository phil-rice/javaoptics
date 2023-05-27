package one.xingyi.annotations.test.seperationOfStructure.withSeparation;

import one.xingyi.annotations.test.seperationOfStructure.ChassisTroncon;
import one.xingyi.annotations.test.seperationOfStructure.Commande;
import one.xingyi.annotations.test.seperationOfStructure.IBuisnessMethods;

import java.util.List;
import java.util.function.Predicate;

public class BusinessMethods implements IBuisnessMethods {

    public List<String> getPfIdsFor(List<Commande> commandes) {
        return Commande.listCommandetoCommandeT.andThen(Commande.commandeToChassisTronconT).all(commandes).map(ChassisTroncon::pfId).toList();
    }

    @Override
    public List<String> getPfIdsForWhere(List<Commande> commandes, Predicate<ChassisTroncon> where) {
        return Commande.listCommandetoCommandeT.andThen(Commande.commandeToChassisTronconT).filter(where).all(commandes).map(ChassisTroncon::pfId).toList();
    }

    public List<String> getPfIdsFor(Commande commande) {
        return Commande.commandeToChassisTronconT.all(commande).map(ChassisTroncon::pfId).toList();
    }

    @Override
    public void doSomething(Commande commande) {
        Commande.commandeToChassisTronconT.all(commande).forEach(
                ChassisTroncon::doSomething);

    }

    public void doSomethingForList(List<Commande> commandes) {
        Commande.listCommandetoCommandeT.andThen(Commande.commandeToChassisTronconT).all(commandes).forEach(
                ChassisTroncon::doSomething);

    }

    @Override
    public void doSomethingWhere(List<Commande> commandes, Predicate<ChassisTroncon> where) {
        Commande.listCommandetoCommandeT.andThen(Commande.commandeToChassisTronconT).filter(where).all(commandes).forEach(
                ChassisTroncon::doSomething);
    }

    @Override
    public void doSomethingElse(Commande commande) {
        Commande.commandeToChassisTronconT.all(commande).forEach(
                ChassisTroncon::doSomething);

    }

    @Override
    public void doSomethingElseForList(List<Commande> commandes) {
        Commande.listCommandetoCommandeT.andThen(Commande.commandeToChassisTronconT).all(commandes).forEach(
                ChassisTroncon::doSomethingElse);

    }

}
        
