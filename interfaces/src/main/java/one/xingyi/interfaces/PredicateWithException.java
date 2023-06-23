package one.xingyi.interfaces;

@FunctionalInterface
public interface PredicateWithException<T> {
    boolean test(T t) throws Exception;

}
