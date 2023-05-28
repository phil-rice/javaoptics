package one.xingyi.optics.annotations.test.seperationOfStructure;

import java.util.List;
import java.util.function.Predicate;

public interface IBusinessMethods {
    List<String> getPfIdsFor(List<Commande> commandes);

    List<String> getPfIdsForWhere(List<Commande> commandes, Predicate<ChassisTroncon> where);

    List<String> getPfIdsFor(Commande commandes);


    void doSomething(Commande commande);

    void doSomethingForList(List<Commande> commands);

    void doSomethingWhere(List<Commande> commande, Predicate<ChassisTroncon> where);

    void doSomethingElse(Commande commande);

    void doSomethingElseForList(List<Commande> commandes);
}
