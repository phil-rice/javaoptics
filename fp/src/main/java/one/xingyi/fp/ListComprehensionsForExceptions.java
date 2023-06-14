package one.xingyi.fp;

import lombok.var;
import one.xingyi.interfaces.BiFunctionWithException;
import one.xingyi.interfaces.ConsumerWithException;
import one.xingyi.interfaces.FunctionWithException;

import java.io.IOException;
import java.util.*;

public interface ListComprehensionsForExceptions {
    static <T, T1> List<T1> mapE(Iterable<T> iterable, FunctionWithException<T, T1> fn) throws Exception {
        List<T1> result = new ArrayList<>();
        for (T t : iterable) result.add(fn.apply(t));
        return result;
    }
    static <T> List<T> filterE(Iterable<T> iterable, FunctionWithException<T, Boolean> filter) throws Exception {
        List<T> result = new ArrayList<>();
        for (T t : iterable) if (filter.apply(t)) result.add(t);
        return result;
    }
    static <T, T1> List<T1> collectE(Iterable<T> iterable, FunctionWithException<T, Boolean> filter, FunctionWithException<T, T1> fn) throws Exception {
        List<T1> result = new ArrayList<>();
        for (T t : iterable) if (filter.apply(t)) result.add(fn.apply(t));
        return result;
    }
    static <T, T1> List<T1> collectByClassE(Iterable<T> iterable, Class<T1> theClass, FunctionWithException<T, T1> fn) throws Exception {
        List<T1> result = new ArrayList<>();
        for (T t : iterable) if (theClass.isInstance(t)) result.add(fn.apply(t));
        return result;
    }
    static <T> void foreachE(Iterable<T> iterable, ConsumerWithException<T> fn) throws Exception {
        for (T t : iterable) fn.accept(t);
    }
    static <T, T1> List<T1> flatMapE(Iterable<T> iterable, FunctionWithException<T, ? extends Collection<T1>> fn) throws Exception {
        List<T1> result = new ArrayList<>();
        for (T t : iterable) result.addAll(fn.apply(t));
        return result;
    }
    static <Id, T> Map<Id, T> toIdMapE(Iterable<T> iterable, FunctionWithException<T, Id> fn) throws Exception {
        Map<Id, T> result = new HashMap<>();
        for (T t : iterable) result.put(fn.apply(t), t);
        return result;
    }
    static <T, K, V> Map<K, V> toMapE(Iterable<T> iterable, FunctionWithException<T, K> keyFn, FunctionWithException<T, V> vFn) throws Exception {
        Map<K, V> result = new HashMap<>();
        for (T t : iterable) result.put(keyFn.apply(t), vFn.apply(t));
        return result;
    }
    static <Acc, T> Acc foldE(Iterable<T> iterable, BiFunctionWithException<Acc, T, Acc> foldFn, Acc zero) throws Exception {
        var result = zero;
        for (T t : iterable)
            result = foldFn.apply(result, t);
        return result;
    }
    static <T> T reduceE(Iterable<T> iterable, BiFunctionWithException<T, T, T> foldFn) throws Exception {
        var iterator = iterable.iterator();
        if (!iterator.hasNext()) throw new IllegalArgumentException("reduceE has been called with an empty list");
        var result = iterator.next();
        while (iterator.hasNext())
            result = foldFn.apply(result, iterator.next());
        return result;
    }
}
