package one.xingyi.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface ListHelpers {
    static <T> List<T> append(List<T> list, T t) {
        List<T> result = new ArrayList<>(list);
        result.add(t);
        return result;
    }
    static <T, T1> List<T1> map(List<T> list, Function<T, T1> fn) {
        List<T1> result = new ArrayList<>();
        for (T t : list) result.add(fn.apply(t));
        return result;
    }
}
