package one.xingyi.interfaces;

public interface FunctionWithException<From, To> {
    To apply(From from) throws Exception;

}
