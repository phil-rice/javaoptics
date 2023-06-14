package one.xingyi.fp;

import lombok.var;
import one.xingyi.interfaces.BiFunctionWithException;
import one.xingyi.interfaces.ConsumerWithException;
import one.xingyi.interfaces.FunctionWithException;

import java.util.*;
import java.util.stream.Stream;

public interface StreamComprehensionsForExceptions {
    static <T, T1> Stream<T1> mapE(Stream<T> stream, FunctionWithException<T, T1> fn) throws Exception {
        List<T1> result = new ArrayList<>();
        for (Iterator<T> iterator = stream.iterator(); iterator.hasNext(); ) {result.add(fn.apply(iterator.next()));}
        return result.stream();
    }
    static <T> List<T> filterE(Stream<T> stream, FunctionWithException<T, Boolean> filter) throws Exception {
        List<T> result = new ArrayList<>();
        for (Iterator<T> iterator = stream.iterator(); iterator.hasNext(); ) {
            var t = iterator.next();
            if (filter.apply(t)) result.add(t);
        }
        return result;
    }
    static <T, T1> List<T1> collectE(Stream<T> stream, FunctionWithException<T, Boolean> filter, FunctionWithException<T, T1> fn) throws Exception {
        List<T1> result = new ArrayList<>();
        for (Iterator<T> iterator = stream.iterator(); iterator.hasNext(); ) {
            var t = iterator.next();
            if (filter.apply(t)) result.add(fn.apply(t));
        }
        return result;
    }
    static <T, T1> List<T1> collectByClassE(Stream<T> stream, Class<T1> theClass, FunctionWithException<T, T1> fn) throws Exception {
        List<T1> result = new ArrayList<>();
        for (Iterator<T> iterator = stream.iterator(); iterator.hasNext(); ) {
            var t = iterator.next();
            if (theClass.isInstance(t)) result.add(fn.apply(t));
        }
        return result;
    }
    static <T> void forEachE(Stream<T> stream, ConsumerWithException<T> fn) throws Exception {
        for (Iterator<T> iterator = stream.iterator(); iterator.hasNext(); ) fn.accept(iterator.next());
    }
    static <T, T1> List<T1> flatMapE(Stream<T> stream, FunctionWithException<T, ? extends Collection<T1>> fn) throws Exception {
        List<T1> result = new ArrayList<>();
        for (Iterator<T> iterator = stream.iterator(); iterator.hasNext(); ) result.addAll(fn.apply(iterator.next()));
        return result;
    }
    static <Id, T> Map<Id, T> toIdMapE(Stream<T> stream, FunctionWithException<T, Id> fn) throws Exception {
        Map<Id, T> result = new HashMap<>();
        for (Iterator<T> iterator = stream.iterator(); iterator.hasNext(); ) {
            var t = iterator.next();
            result.put(fn.apply(t), t);
        }
        return result;
    }
    static <T, K, V> Map<K, V> toMapE(Stream<T> stream, FunctionWithException<T, K> keyFn, FunctionWithException<T, V> vFn) throws Exception {
        Map<K, V> result = new HashMap<>();
        for (Iterator<T> iterator = stream.iterator(); iterator.hasNext(); ) {
            var t = iterator.next();
            result.put(keyFn.apply(t), vFn.apply(t));

        }
        return result;
    }
    static <Acc, T> Acc foldE(Stream<T> stream, BiFunctionWithException<Acc, T, Acc> foldFn, Acc zero) throws Exception {
        var result = zero;
        for (Iterator<T> iterator = stream.iterator(); iterator.hasNext(); )
            result = foldFn.apply(result, iterator.next());
        return result;
    }
    static <T> T reduceE(Stream<T> stream, BiFunctionWithException<T, T, T> foldFn) throws Exception {
        var iterator = stream.iterator();
        if (!iterator.hasNext()) throw new IllegalArgumentException("reduceE has been called with an empty list");
        var result = iterator.next();
        while (iterator.hasNext())
            result = foldFn.apply(result, iterator.next());
        return result;
    }
}
