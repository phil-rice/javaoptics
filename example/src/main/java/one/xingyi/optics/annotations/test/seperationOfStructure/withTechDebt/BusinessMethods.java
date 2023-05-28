package one.xingyi.optics.annotations.test.seperationOfStructure.withTechDebt;

import one.xingyi.optics.annotations.test.seperationOfStructure.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BusinessMethods implements IBusinessMethods {
    public List<String> getPfIdsFor(List<Commande> commandes) {
        var result = new ArrayList<String>();
        for (Commande commande : commandes) {
            for (CommandeTransport commandeTransport : commande.commandeTransportList()) {
                for (TronconTransport tronconTransport : commandeTransport.tronconTransportList()) {
                    for (ChassisTroncon troncon : tronconTransport.chassisTronconList()) {
                        result.add(troncon.pfId());
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<String> getPfIdsForWhere(List<Commande> commandes, Predicate<ChassisTroncon> where) {
        var result = new ArrayList<String>();
        for (Commande commande : commandes) {
            for (CommandeTransport commandeTransport : commande.commandeTransportList()) {
                for (TronconTransport tronconTransport : commandeTransport.tronconTransportList()) {
                    for (ChassisTroncon troncon : tronconTransport.chassisTronconList()) {
                        if (where.test(troncon)) result.add(troncon.pfId());
                    }
                }
            }
        }
        return result;
    }

    public List<String> getPfIdsFor(Commande commande) {
        var result = new ArrayList<String>();
        for (CommandeTransport commandeTransport : commande.commandeTransportList()) {
            for (TronconTransport tronconTransport : commandeTransport.tronconTransportList()) {
                for (ChassisTroncon troncon : tronconTransport.chassisTronconList()) {
                    result.add(troncon.pfId());
                }
            }
        }
        return result;
    }

    @Override
    public void doSomething(Commande commande) {
        for (CommandeTransport commandeTransport : commande.commandeTransportList()) {
            for (TronconTransport tronconTransport : commandeTransport.tronconTransportList()) {
                for (ChassisTroncon troncon : tronconTransport.chassisTronconList()) {
                    troncon.doSomething();
                }
            }
        }
    }

    public void doSomethingForList(List<Commande> commandeList) {
        for (Commande commande : commandeList)
            for (CommandeTransport commandeTransport : commande.commandeTransportList()) {
                for (TronconTransport tronconTransport : commandeTransport.tronconTransportList()) {
                    for (ChassisTroncon troncon : tronconTransport.chassisTronconList()) {
                        troncon.doSomething();
                    }
                }
            }
    }


    @Override
    public void doSomethingWhere(List<Commande> commandeList, Predicate<ChassisTroncon> where) {
        for (Commande commande : commandeList)
            for (CommandeTransport commandeTransport : commande.commandeTransportList()) {
                for (TronconTransport tronconTransport : commandeTransport.tronconTransportList()) {
                    for (ChassisTroncon troncon : tronconTransport.chassisTronconList()) {
                        if (where.test(troncon)) troncon.doSomething();
                    }
                }
            }
    }

    @Override
    public void doSomethingElse(Commande commande) {
        for (CommandeTransport commandeTransport : commande.commandeTransportList()) {
            for (TronconTransport tronconTransport : commandeTransport.tronconTransportList()) {
                for (ChassisTroncon troncon : tronconTransport.chassisTronconList()) {
                    troncon.doSomethingElse();
                }
            }
        }
    }

    @Override
    public void doSomethingElseForList(List<Commande> commande) {
        for (Commande commande1 : commande)
            for (CommandeTransport commandeTransport : commande1.commandeTransportList()) {
                for (TronconTransport tronconTransport : commandeTransport.tronconTransportList()) {
                    for (ChassisTroncon troncon : tronconTransport.chassisTronconList()) {
                        troncon.doSomethingElse();
                    }
                }
            }

    }
}
        
