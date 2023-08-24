package one.xingyi.interfaces;

@FunctionalInterface
public interface RunnableWithExceptionE<E extends Exception> {
    void run() throws E;
}
