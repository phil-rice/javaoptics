package one.xingyi.interfaces;

@FunctionalInterface
public interface BiFunctionWithException<From1, From2, To> {
    To apply(From1 from1, From2 from2) throws Exception;
}
