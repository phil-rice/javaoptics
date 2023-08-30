package one.xingyi.profile;

import lombok.var;
import one.xingyi.interfaces.INanoTime;
import one.xingyi.interfaces.RunnableWithExceptionE;
import one.xingyi.interfaces.SupplierWithExceptionE;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public interface IProfile {
    static IProfileBuilder makeProfileMap(INanoTime nanoTime) {
        return new ProfileImpl("", new ConcurrentHashMap<>(), nanoTime);
    }

    IProfileInfo mainProfileInfo();

    String print();

    <T, E extends Exception> T profileE(String name, SupplierWithExceptionE<T, E> fn) throws E;

    <T> T profile(String name, Supplier<T> fn);

    void run(String name, Runnable fn);


    <E extends Exception> void runE(String name, RunnableWithExceptionE<E> fn) throws E;

    void add(String name, long duration);


}
