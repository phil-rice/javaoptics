package one.xingyi.interfaces;

@FunctionalInterface
public interface FunctionWithExceptionE<From, To, E extends Exception> {
    To apply(From from) throws E;

}
