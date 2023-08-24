package one.xingyi.profile;

import one.xingyi.interfaces.INanoTime;
import one.xingyi.interfaces.RunnableWithExceptionE;
import one.xingyi.interfaces.SupplierWithExceptionE;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public interface IProfile {
    static IProfile makeProfileMap(INanoTime nanoTime) {return new ProfileImpl("", new ConcurrentHashMap<>(), nanoTime);}
    String print();
    IProfile withPrefix(String prefix);

    <T, E extends Exception> T profileE(String name, SupplierWithExceptionE<T, E> fn) throws E;
    <T> T profile(String name, Supplier<T> fn);
    void run(String name, Runnable fn);
    <E extends Exception> void runE(String name, RunnableWithExceptionE<E> fn) throws E;
    void add(String name, long duration);
    Map<String, ProfileBuckets<Long>> getMs();
    Map<String, ProfileBuckets<Integer>> getCounts();
    Map<String, Integer> getTotalCounts();
    Map<String, Long> getTotalAvg();
}
