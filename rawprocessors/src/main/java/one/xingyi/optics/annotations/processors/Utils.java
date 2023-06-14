package one.xingyi.optics.annotations.processors;

import one.xingyi.optics.annotations.interfaces.IBiFunctionWithException;
import one.xingyi.optics.annotations.interfaces.IFunctionWithException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface Utils {
    static String lastSegment(String s) {
        int i = s.lastIndexOf('.');
        return i == -1 ? s : s.substring(i + 1);
    }

    static <T> List<T> asList(T[] ts) {
        return ts == null ? Collections.emptyList() : Arrays.asList(ts);
    }

    static <T> List<T> concat(List<T> t1, List<T> t2) {
        List<T> result = new ArrayList<>(t1.size() + t2.size());
        result.addAll(t1);
        result.addAll(t2);
        return result;
    }

    static <T> List<T> insert(List<T> ts, T t) {
        List<T> result = new ArrayList<>(ts.size() + 1);
        result.add(t);
        result.addAll(ts);
        return result;
    }

    static <T> List<T> append(List<T> ts, T t) {
        List<T> result = new ArrayList<>(ts.size() + 1);
        result.addAll(ts);
        result.add(t);
        return result;
    }

    static <T, T1> List<T1> map(List<T> ts, IFunctionWithException<T, T1> fn) throws IOException {
        List<T1> result = new ArrayList<>(ts.size());
        for (T t : ts) result.add(fn.apply(t));
        return result;
    }

    static String firstPart(String s, String sep, String def) {
        int i = s.indexOf(sep);
        return i == -1 ? def : s.substring(0, i);
    }

    static String lastPart(String s, String sep, String def) {
        int i = s.lastIndexOf(sep);
        return i == -1 ? def : s.substring(i + 1);
    }

    static <Acc, V> Acc foldLeft(List<V> vs, Acc acc, IBiFunctionWithException<Acc, V, Acc> fn) throws IOException {
        for (V v : vs) acc = fn.apply(acc, v);
        return acc;
    }
}
