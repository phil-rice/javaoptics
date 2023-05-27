package one.xingyi.processors;

import java.util.ArrayList;
import java.util.List;

public interface Utils {
    static String lastSegment(String s) {
        int i = s.lastIndexOf('.');
        return i == -1 ? s : s.substring(i + 1);
    }

    static <T> List<T> asList(T[] ts) {
        return ts == null ? List.of() : List.of(ts);
    }

    static<T> List<T> concat(List<T> t1, List<T> t2){
        List<T> result = new ArrayList<>(t1.size() + t2.size());
        result.addAll(t1);
        result.addAll(t2);
        return result;
    }
    static<T> List<T> insert(List<T> ts, T t){
        List<T> result = new ArrayList<>(ts.size() +1);
        result.add(t);
        result.addAll(ts);
        return result;
    }
}
