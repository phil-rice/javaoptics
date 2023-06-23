package one.xingyi.interfaces;

@FunctionalInterface
public interface SupplierWithException<T> {
    public T get() throws Exception;
}
