package one.xingyi.interfaces;

@FunctionalInterface
public interface RunnableWithException {
    void run() throws Exception;
}
