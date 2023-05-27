package one.xingyi.annotations.test.seperationOfStructure.simpler;

import one.xingyi.annotations.test.seperationOfStructure.IMeansOfTransport;
import one.xingyi.optics.ITraversal;

public class BusinessMethodsSimplified {

//    public <T> ITraversal<T, String> getPfIdsFor(ITraversal<T, IMeansOfTransport> traversal) {
//        return ITraversal.of((T t) -> traversal.all(t).map(IMeansOfTransport::getPfId));
//    }

    public <T> void doSomething(ITraversal<T, IMeansOfTransport> traversal, T t) {
        traversal.all(t).forEach(IMeansOfTransport::doSomething);
    }

    public <T> void doSomethingElse(ITraversal<T, IMeansOfTransport> traversal, T t) {
        traversal.forEach(t, IMeansOfTransport::doSomething);
    }

}
        
