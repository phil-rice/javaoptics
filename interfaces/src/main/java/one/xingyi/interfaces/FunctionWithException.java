package one.xingyi.interfaces;

@FunctionalInterface
public interface FunctionWithException<From, To> {
    To apply(From from) throws Exception;

}
