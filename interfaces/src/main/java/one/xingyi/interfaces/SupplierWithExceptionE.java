package one.xingyi.interfaces;

@FunctionalInterface
public interface SupplierWithExceptionE<T, E extends Exception> {
    public T get() throws E;
}
